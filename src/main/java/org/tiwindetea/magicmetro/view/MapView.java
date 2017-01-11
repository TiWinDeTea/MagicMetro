/*
 * MIT License
 *
 * Copyright (c) 2016 TiWinDeTea - contact@tiwindetea.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.tiwindetea.magicmetro.view;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.arakhne.afc.math.geometry.d2.d.Circle2d;
import org.arakhne.afc.math.geometry.d2.d.MultiShape2d;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.dfx.MultiShape2dfx;
import org.arakhne.afc.math.geometry.d2.dfx.Point2dfx;
import org.arakhne.afc.math.geometry.d2.dfx.Rectangle2dfx;
import org.tiwindetea.magicmetro.global.eventdispatcher.EventDispatcher;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.lineevents.LineCreationEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.lineevents.LineDecreaseEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.lineevents.LineExtensionEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.lineevents.LineInnerExtensionEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.moveevents.TrainInventoryMoveEvent;
import org.tiwindetea.magicmetro.model.TrainType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Representation of a passenger used by ConcreteTrainView and ConcreteStationView.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class MapView extends DraggableZoomableParent implements StationMouseListener, SectionMouseListener, InventoryMouseListener {

	private ConcreteInventoryView inventoryView;

	private static final Color WATER_COLOR = Color.AQUA;
	private static final double DEFAULT_WIDTH = 800;
	private static final double DEFAULT_HEIGHT = 600;
	private static final double STATION_BOUNDS_RADIUS = 25;

	private final Pane mainPane = new Pane();
	private final MultiShape2dfx<Rectangle2dfx> water = new MultiShape2dfx<>(); // for collisions
	private final Collection<Rectangle> waterRectangles = new LinkedList<>();
	private final Group trainGroup = new Group();
	private final Group stationsGroup = new Group();
	private final Group lineGroup = new Group();
	private final Group waterGroup = new Group();

	private final List<ConcreteStationView> stations = new LinkedList<>();
	private final List<ConcreteTrainView> trains = new LinkedList<>();
	private final List<ConcreteLineView> lines = new LinkedList<>();

	private SectionView dragOverSection; // The section with the mouse is dragging over or null
	private ConcreteStationView dragOverStation; // The station with the mouse is dragging over or null
	private Lock dragOverLock = new ReentrantLock();

	private interface ModificationState {

		void init(double x, double y);

		void update(double x, double y);

		void apply(double x, double y);

	}

	private class VoidModificationState implements ModificationState {

		@Override
		public void init(double x, double y) {
			//void
		}

		@Override
		public void update(double x, double y) {
			//void
		}

		@Override
		public void apply(double x, double y) {
			//void
		}
	}

	private class LineModificationState implements ModificationState {

		private ConcreteLineView concreteLineView;
		private SectionView sectionView;
		private ConcreteStationView oldToStation = null;

		private ConcreteStationView toStation = null;

		private final MultiShape2d<Circle2d> oldToStationBounds = new MultiShape2d<>();
		private boolean wasInOldToStationBounds = true;

		private final MultiShape2d<Circle2d> stationsBounds;

		public LineModificationState(@Nonnull ConcreteLineView unusedLine,
		                             @Nonnull ConcreteStationView fromStation,
		                             @Nullable ConcreteStationView oldToStation) {

			this.oldToStation = oldToStation;

			this.concreteLineView = unusedLine;
			this.sectionView = new SectionView(
			  this.concreteLineView,
			  fromStation.getTranslateX(),
			  fromStation.getTranslateY());
			if(this.concreteLineView != null) {
				this.sectionView.setSectionMouseListener(MapView.this);
				this.sectionView.setFromStation(fromStation);
				this.sectionView.setWater(MapView.this.water);
				MapView.this.lineGroup.getChildren().add(this.sectionView);
			}
			else {
				MapView.this.modificationState = MapView.this.voidModificationState;
			}

			this.stationsBounds = new MultiShape2d<>();
			for(ConcreteStationView station : MapView.this.stations) {
				if(station != this.sectionView.getFromStation() && station != this.oldToStation) {
					Circle2d circle2d = new Circle2d(new Point2d(station.getTranslateX(), station.getTranslateY()),
					  STATION_BOUNDS_RADIUS);
					this.stationsBounds.add(circle2d);
				}
			}
			if(this.oldToStation != null) {
				this.oldToStationBounds.add(new Circle2d(new Point2d(this.oldToStation.getTranslateX(),
				  this.oldToStation.getTranslateY()), STATION_BOUNDS_RADIUS));
			}
		}

		@Override
		public synchronized void init(double x, double y) {

		}

		@Override
		public synchronized void update(double x, double y) {
			this.sectionView.setTo(x, y);

			if(this.stationsBounds.contains(x, y)) {
				this.apply(x, y);
				if(this.toStation != null) {
					MapView.this.modificationState = new LineExtensionState(this.sectionView, this.toStation, null);
				}
			}
			if(!this.wasInOldToStationBounds) {
				if(this.oldToStationBounds.contains(x, y)) {
					this.stationsBounds.addAll(this.oldToStationBounds);
					this.apply(x, y);
					if(this.toStation != null) {
						MapView.this.modificationState = new LineExtensionState(this.sectionView, this.toStation, null);
					}
				}
			}
			if(this.oldToStation != null) {
				this.wasInOldToStationBounds = this.oldToStationBounds.contains(x, y);
			}
		}

		@Override
		public synchronized void apply(double x, double y) {
			MapView.this.lineGroup.getChildren().remove(this.sectionView);
			for(ConcreteStationView station : MapView.this.stations) {
				if(station != this.sectionView.getFromStation()) {
					Circle2d circle2d = new Circle2d(new Point2d(station.getTranslateX(), station.getTranslateY()),
					  STATION_BOUNDS_RADIUS);
					if(circle2d.contains(this.sectionView.getTo())) {
						this.toStation = station;
						break;
					}
				}
			}

			if(this.toStation != null) {
				this.sectionView.setToStation(this.toStation);
				this.sectionView.setTo(this.toStation.getTranslateX(), this.toStation.getTranslateY());
				this.sectionView.setFromHookVisible(true);
				this.sectionView.setToHookVisible(true);
				this.concreteLineView.addSection(this.sectionView);
				MapView.this.inventoryView.setUsed(this.concreteLineView.gameId);

				EventDispatcher.getInstance().fire(new LineCreationEvent(
				  this.concreteLineView.gameId,
				  this.sectionView.gameId,
				  this.sectionView.getFromStation().gameId,
				  this.sectionView.getToStation().gameId,
				  this.sectionView.getMiddleForModel()
				));
			}
		}
	}

	private class LineDoubleModificationState implements ModificationState {

		private ConcreteLineView concreteLineView;
		private SectionView oldSectionView;
		private SectionView fromSectionView;
		private SectionView toSectionView;

		private final MultiShape2d<Circle2d> stationsBounds;
		//private final Circle2d oldFromStationBounds;
		//private final Circle2d oldToStationBounds;

		public LineDoubleModificationState(SectionView modifiedSection, double x, double y) {
			this.oldSectionView = modifiedSection;
			this.concreteLineView = modifiedSection.getLine();
			this.fromSectionView = new SectionView(this.concreteLineView,
			  this.oldSectionView.getFrom(),
			  new Point2dfx(x, y));
			this.fromSectionView.setSectionMouseListener(MapView.this);
			this.fromSectionView.setFromStation(this.oldSectionView.getFromStation());
			this.fromSectionView.setWater(MapView.this.water);
			this.toSectionView = new SectionView(this.concreteLineView,
			  new Point2dfx(x, y),
			  this.oldSectionView.getTo());
			this.toSectionView.setSectionMouseListener(MapView.this);
			this.toSectionView.setToStation(this.oldSectionView.getToStation());
			this.toSectionView.setWater(MapView.this.water);

			MapView.this.lineGroup.getChildren().add(this.fromSectionView);
			MapView.this.lineGroup.getChildren().add(this.toSectionView);

			this.stationsBounds = new MultiShape2d<>();
			for(ConcreteStationView station : MapView.this.stations) {
				if(station != this.oldSectionView.getFromStation() && station != this.oldSectionView.getToStation()) {
					Circle2d circle2d = new Circle2d(new Point2d(station.getTranslateX(), station.getTranslateY()),
					  STATION_BOUNDS_RADIUS);
					this.stationsBounds.add(circle2d);
				}
			}

			/*this.oldFromStationBounds = new Circle2d(new Point2d(
			  this.oldSectionView.getFromStation().getTranslateX(),
			  this.oldSectionView.getFromStation().getTranslateY()),
			  STATION_BOUNDS_RADIUS);
			this.oldToStationBounds = new Circle2d(new Point2d(
			  this.oldSectionView.getToStation().getTranslateX(),
			  this.oldSectionView.getToStation().getTranslateY()),
			  STATION_BOUNDS_RADIUS);*/
		}

		@Override
		public void init(double x, double y) {

		}

		@Override
		public void update(double x, double y) {
			this.fromSectionView.setTo(x, y);
			this.toSectionView.setFrom(x, y);

			if(this.stationsBounds.contains(x, y)) {
				apply(x, y);
				MapView.this.modificationState = new VoidModificationState();
			}
		}

		@Override
		public void apply(double x, double y) {
			MapView.this.lineGroup.getChildren().remove(this.toSectionView);
			MapView.this.lineGroup.getChildren().remove(this.fromSectionView);
			ConcreteStationView toStation = null;
			for(ConcreteStationView station : MapView.this.stations) {
				if(station != this.fromSectionView.getFromStation() && station != this.toSectionView.getToStation()) {
					Circle2d circle2d = new Circle2d(new Point2d(station.getTranslateX(), station.getTranslateY()),
					  STATION_BOUNDS_RADIUS);
					if(circle2d.contains(this.fromSectionView.getTo())) {
						toStation = station;
						break;
					}
				}
			}

			if(toStation != null) {
				this.fromSectionView.setTo(toStation.getTranslateX(), toStation.getTranslateY());
				this.toSectionView.setFrom(toStation.getTranslateX(), toStation.getTranslateY());
				this.fromSectionView.setToStation(toStation);
				this.toSectionView.setFromStation(toStation);

				this.fromSectionView.setPrevSection(this.oldSectionView.getPrevSection());
				this.fromSectionView.setNextSection(this.toSectionView);
				this.toSectionView.setPrevSection(this.fromSectionView);
				this.toSectionView.setNextSection(this.oldSectionView.getNextSection());

				if(this.fromSectionView.getPrevSection() == null) {
					this.fromSectionView.setFromHookVisible(true);
				}

				if(this.toSectionView.getNextSection() == null) {
					this.toSectionView.setToHookVisible(true);
				}

				this.concreteLineView.addSection(this.fromSectionView);
				this.concreteLineView.addSection(this.toSectionView);

				this.oldSectionView.setSectionMouseListener(null);
				this.oldSectionView.setFromHookVisible(false);
				this.oldSectionView.setToHookVisible(false);

				EventDispatcher.getInstance().fire(new LineInnerExtensionEvent(
				  this.concreteLineView.gameId,
				  this.oldSectionView.gameId,
				  this.fromSectionView.gameId,
				  this.toSectionView.gameId,
				  this.fromSectionView.getFromStation().gameId,
				  this.fromSectionView.getToStation().gameId,
				  this.toSectionView.getToStation().gameId,
				  this.fromSectionView.getMiddleForModel(),
				  this.toSectionView.getMiddleForModel()
				));
			}
		}
	}

	private class LineExtensionState implements ModificationState {

		private final boolean from;
		private final SectionView srcSectionView;
		private SectionView sectionView;
		private final ConcreteStationView fromStation;
		private ConcreteStationView oldToStation = null;

		private ConcreteStationView toStation = null;

		private final MultiShape2d<Circle2d> stationsBounds;
		private final Circle2d fromStationBounds;
		private boolean wasInFromStationBounds = true;
		private final MultiShape2d<Circle2d> oldToStationBounds;
		private boolean wasInToStationBounds = true;

		public LineExtensionState(@Nonnull SectionView srcSectionView,
		                          @Nonnull ConcreteStationView fromStation,
		                          @Nullable ConcreteStationView oldToStation) {
			this.srcSectionView = srcSectionView;
			this.fromStation = fromStation;
			this.oldToStation = oldToStation;

			this.sectionView = new SectionView(this.srcSectionView.getLine(),
			  this.fromStation.getTranslateX(),
			  this.fromStation.getTranslateY());
			this.sectionView.setSectionMouseListener(MapView.this);
			this.sectionView.setFromStation(this.fromStation);
			this.sectionView.setWater(MapView.this.water);
			MapView.this.lineGroup.getChildren().add(this.sectionView);

			if(this.srcSectionView.getFromStation() == this.fromStation) {
				this.srcSectionView.setFromHookVisible(false);
				this.from = true;
			}
			else {
				this.srcSectionView.setToHookVisible(false);
				this.from = false;
			}

			this.fromStationBounds = new Circle2d(new Point2d(fromStation.getTranslateX(), fromStation.getTranslateY()),
			  STATION_BOUNDS_RADIUS);
			this.stationsBounds = new MultiShape2d<>();
			for(ConcreteStationView station : MapView.this.stations) {
				if(station != this.sectionView.getFromStation()) {
					Circle2d circle2d = new Circle2d(new Point2d(station.getTranslateX(), station.getTranslateY()),
					  STATION_BOUNDS_RADIUS);
					this.stationsBounds.add(circle2d);
				}
			}

			this.oldToStationBounds = new MultiShape2d<>();
			if(this.oldToStation != null) {
				this.oldToStationBounds.add(new Circle2d(new Point2d(this.oldToStation.getTranslateX(),
				  this.oldToStation.getTranslateY()),
				  STATION_BOUNDS_RADIUS));
				this.wasInToStationBounds = true;
			}
			else {
				this.wasInToStationBounds = false;
			}
		}

		@Override
		public void init(double x, double y) {
			update(x, y);
		}

		@Override
		public void update(double x, double y) {
			this.sectionView.setTo(x, y);

			if(!this.wasInFromStationBounds && !this.wasInToStationBounds) {
				if(this.stationsBounds.contains(x, y)) {
					this.apply(x, y);
					if(this.toStation != null) {
						MapView.this.modificationState = new LineExtensionState(this.sectionView, this.toStation, null);
					}
				}
				else {
					if(this.fromStationBounds.contains(x, y)) {
						MapView.this.lineGroup.getChildren().remove(this.sectionView);
						boolean left = this.srcSectionView.getPrevSection() == null;
						if(this.srcSectionView.getPrevSection() != null || this.srcSectionView.getNextSection() != null) {
							this.srcSectionView.setMouseTransparent(true);
							MapView.this.modificationState = new LineExtensionState(
							  left ? this.srcSectionView.getNextSection() : this.srcSectionView.getPrevSection(),
							  left ? this.srcSectionView.getToStation() : this.srcSectionView.getFromStation(),
							  this.fromStation);
						}
						else { // only one section left
							this.srcSectionView.setMouseTransparent(true);
							MapView.this.inventoryView.setUnused(this.sectionView.getLine().gameId);
							MapView.this.modificationState = new LineModificationState(this.sectionView.getLine(),
							  this.srcSectionView.getFromStation(),
							  this.fromStation);
						}
						EventDispatcher.getInstance().fire(new LineDecreaseEvent(
						  this.srcSectionView.getLine().gameId,
						  this.srcSectionView.gameId,
						  this.fromStation.gameId
						));
					}
				}
			}
			this.wasInFromStationBounds = this.fromStationBounds.contains(x, y);
			this.wasInToStationBounds = this.oldToStationBounds.contains(x, y);
		}

		@Override
		public void apply(double x, double y) {
			MapView.this.lineGroup.getChildren().remove(this.sectionView);
			for(ConcreteStationView station : MapView.this.stations) {
				if(station != this.sectionView.getFromStation()) {
					Circle2d circle2d = new Circle2d(new Point2d(station.getTranslateX(), station.getTranslateY()),
					  STATION_BOUNDS_RADIUS);
					if(circle2d.contains(this.sectionView.getTo())) {
						this.toStation = station;
						break;
					}
				}
			}

			if(this.toStation != null) {
				this.sectionView.setToStation(this.toStation);
				this.sectionView.setTo(this.toStation.getTranslateX(), this.toStation.getTranslateY());
				this.sectionView.setToHookVisible(true);
				this.sectionView.setPrevSection(this.srcSectionView);

				if(this.from) {
					this.srcSectionView.setPrevSection(this.sectionView);
				}
				else {
					this.srcSectionView.setNextSection(this.sectionView);
				}

				this.srcSectionView.getLine().addSection(this.sectionView);

				EventDispatcher.getInstance().fire(new LineExtensionEvent(
				  this.srcSectionView.getLine().gameId,
				  this.sectionView.gameId,
				  this.sectionView.getFromStation().gameId,
				  this.sectionView.getToStation().gameId,
				  this.sectionView.getMiddleForModel()
				));
			}
			else {
				if(this.from) {
					this.srcSectionView.setFromHookVisible(true);
				}
				else {
					this.srcSectionView.setToHookVisible(true);
				}
			}

		}
	}

	private class StationUpgradeMoveState implements ModificationState {

		private final Shape stationUpgradeShape;

		public StationUpgradeMoveState(Shape StationUpgradeShape) {
			this.stationUpgradeShape = StationUpgradeShape;
			// Circles already have a good layout
			//this.stationUpgradeShape.setLayoutX(-Skin.STATION_UPGRADE_VIEW_WIDTH / 2);
			//this.stationUpgradeShape.setLayoutY(-Skin.STATION_UPGRADE_VIEW_HEIGHT / 2);

			this.stationUpgradeShape.setMouseTransparent(true);
			MapView.this.trainGroup.getChildren().add(this.stationUpgradeShape);
			this.stationUpgradeShape.setVisible(false);
		}

		@Override
		public void init(double x, double y) {
			update(x, y);
		}

		@Override
		public void update(double x, double y) {
			this.stationUpgradeShape.setTranslateX(x);
			this.stationUpgradeShape.setTranslateY(y);
			this.stationUpgradeShape.setVisible(true);
		}

		@Override
		public void apply(double x, double y) {
			MapView.this.trainGroup.getChildren().remove(this.stationUpgradeShape);
			MapView.this.dragOverLock.lock();
			if(MapView.this.dragOverStation != null) {
				//TODO: event to model... (upgrade station, model check if a station upgrade is available)
			}
			MapView.this.dragOverLock.unlock();
		}
	}

	private class PassengerCarMoveState implements ModificationState {

		private final Shape passengerCarShape;

		public PassengerCarMoveState(Shape passengerCarShape) {
			this.passengerCarShape = passengerCarShape;
			this.passengerCarShape.setLayoutX(-Skin.PASSENGERCAR_VIEW_WIDTH / 2);
			this.passengerCarShape.setLayoutY(-Skin.PASSENGERCAR_VIEW_HEIGHT / 2);

			this.passengerCarShape.setMouseTransparent(true);
			MapView.this.trainGroup.getChildren().add(this.passengerCarShape);
			this.passengerCarShape.setVisible(false);
		}

		@Override
		public void init(double x, double y) {
			update(x, y);
		}

		@Override
		public void update(double x, double y) {
			this.passengerCarShape.setTranslateX(x);
			this.passengerCarShape.setTranslateY(y);
			this.passengerCarShape.setVisible(true);
		}

		@Override
		public void apply(double x, double y) {
			MapView.this.trainGroup.getChildren().remove(this.passengerCarShape);
			MapView.this.dragOverLock.lock();
			if(MapView.this.dragOverSection != null) {
				//TODO: event to model... (add passenger car to a train in the line, model check if a passenger car is available)
			}
			MapView.this.dragOverLock.unlock();
		}
	}

	private class TrainMoveState implements ModificationState {

		private final Shape trainShape;

		public TrainMoveState(Shape trainShape) {
			this.trainShape = trainShape;
			this.trainShape.setLayoutX(-Skin.TRAIN_VIEW_WIDTH / 2);
			this.trainShape.setLayoutY(-Skin.TRAIN_VIEW_HEIGHT / 2);

			this.trainShape.setMouseTransparent(true);
			MapView.this.trainGroup.getChildren().add(this.trainShape);
			this.trainShape.setVisible(false);
		}

		@Override
		public void init(double x, double y) {
			update(x, y);
		}

		@Override
		public void update(double x, double y) {
			this.trainShape.setTranslateX(x);
			this.trainShape.setTranslateY(y);
			this.trainShape.setVisible(true);
		}

		@Override
		public void apply(double x, double y) {
			MapView.this.trainGroup.getChildren().remove(this.trainShape);
			MapView.this.dragOverLock.lock();
			if(MapView.this.dragOverSection != null) {
				EventDispatcher.getInstance().fire(new TrainInventoryMoveEvent(
				  MapView.this.dragOverSection.getLine().gameId,
				  MapView.this.dragOverSection.gameId,
				  new Point2d(x, y)
				));
			}
			MapView.this.dragOverLock.unlock();
		}
	}

	private final VoidModificationState voidModificationState = new VoidModificationState();
	private ModificationState modificationState = this.voidModificationState;
	private Lock modificationStateLock = new ReentrantLock();

	private final Skin skin;
	private Collection<Node> HUD = new LinkedList<>();

	public MapView(Skin skin) {
		super();
		this.skin = skin;

		this.setWidth(DEFAULT_WIDTH);
		this.setHeight(DEFAULT_HEIGHT);
		this.getChildren().add(this.mainPane);
		this.mainPane.getChildren().add(this.waterGroup);
		this.mainPane.getChildren().add(this.lineGroup);
		this.mainPane.getChildren().add(this.stationsGroup);
		this.mainPane.getChildren().add(this.trainGroup);

		addEventFilter(MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				for(Node node : MapView.this.HUD) {
					node.setMouseTransparent(true);
				}
				MapView.this.startFullDrag();
			}
		});

		addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				for(Node node : MapView.this.HUD) {
					node.setMouseTransparent(false);
				}
			}
		});

		this.addEventFilter(MouseDragEvent.MOUSE_DRAG_OVER, new EventHandler<MouseDragEvent>() {
			@Override
			public void handle(MouseDragEvent event) {
				MapView.this.modificationStateLock.lock();
				MapView.this.modificationState.update(event.getX(), event.getY());
				MapView.this.modificationStateLock.unlock();
			}
		});

		addEventFilter(MouseDragEvent.MOUSE_DRAG_RELEASED, new EventHandler<MouseDragEvent>() {
			@Override
			public void handle(MouseDragEvent event) {
				MapView.this.modificationStateLock.lock();
				MapView.this.modificationState.apply(event.getX(), event.getY());
				MapView.this.modificationState = MapView.this.voidModificationState;
				MapView.this.modificationStateLock.unlock();
			}
		});

	}

	public void setInventoryView(ConcreteInventoryView inventoryView) {
		this.inventoryView = inventoryView;
	}

	@Override
	public void mousePressedOnStation(@Nonnull ConcreteStationView station) {
		this.modificationStateLock.lock();
		ConcreteLineView concreteLineView = MapView.this.inventoryView.getUnusedLine();
		if(concreteLineView != null) {
			this.modificationState = new LineModificationState(concreteLineView, station, null);
		}
		this.modificationStateLock.unlock();
		//no init
	}

	@Override
	public void mouseDragEnteredOnStation(@Nonnull ConcreteStationView station) {
		this.dragOverLock.lock();
		this.dragOverStation = station;
		this.dragOverLock.unlock();
	}

	@Override
	public void mouseDragExitedOnStation(@Nonnull ConcreteStationView station) {
		this.dragOverLock.lock();
		this.dragOverStation = null;
		this.dragOverLock.unlock();
	}

	@Override
	public void mousePressedOnSection(@Nonnull SectionView section, double mouseX, double mouseY) {
		this.modificationStateLock.lock();
		this.modificationState = new LineDoubleModificationState(section, mouseX, mouseY);
		this.modificationStateLock.unlock();
	}

	@Override
	public void mousePressedOnSectionHook(@Nonnull SectionView section, ConcreteStationView fromStation) {
		this.modificationStateLock.lock();
		this.modificationState = new LineExtensionState(section, fromStation, null);
		this.modificationStateLock.unlock();
	}

	@Override
	public void mouseDragEnteredOnSection(@Nonnull SectionView section) {
		this.dragOverLock.lock();
		this.dragOverSection = section;
		this.dragOverLock.unlock();
	}

	@Override
	public void mouseDragExitedOnSection(@Nonnull SectionView section) {
		this.dragOverLock.lock();
		this.dragOverSection = null;
		this.dragOverLock.unlock();
	}

	@Override
	public void mousePressedOnStationUpgradeCounter() {
		this.modificationStateLock.lock();
		this.modificationState = new StationUpgradeMoveState(this.skin.newStationUpgradeView());
		this.modificationStateLock.unlock();
	}

	@Override
	public void mousePressedOnPassengerCarCounter() {
		this.modificationStateLock.lock();
		this.modificationState = new PassengerCarMoveState(this.skin.newPassengerCarView(TrainType.NORMAL)); //FIXME: temporary TrainType.NORMAL for tests
		this.modificationStateLock.unlock();
	}

	@Override
	public void mousePressedOnTrainCounter() {
		this.modificationStateLock.lock();
		this.modificationState = new TrainMoveState(this.skin.newTrainView(TrainType.NORMAL)); //FIXME: temporary TrainType.NORMAL for tests
		this.modificationStateLock.unlock();
	}

	@Nullable
	public ConcreteLineView getLineFromId(int gameId) {
		for(ConcreteLineView line : this.lines) {
			if(line.gameId == gameId) {
				return line;
			}
		}
		return null;
	}

	public void setWidth(double width) {
		this.mainPane.setMinWidth(width);
		this.mainPane.setMaxWidth(width);
		this.mainPane.setPrefWidth(width);
	}

	public void setHeight(double height) {
		this.mainPane.setMinHeight(height);
		this.mainPane.setMaxHeight(height);
		this.mainPane.setPrefHeight(height);
	}

	public void setWater(@Nonnull MultiShape2d<Rectangle2d> water) {
		this.waterGroup.getChildren().clear();
		this.waterRectangles.clear();
		this.water.clear();
		for(Rectangle2d rectangle2d : water.getBackendDataList()) {
			Rectangle2dfx rectangle2dfx = new Rectangle2dfx();
			Rectangle rectangle = new Rectangle();
			rectangle.setFill(WATER_COLOR);
			rectangle.widthProperty().bind(rectangle2dfx.widthProperty());
			rectangle.heightProperty().bind(rectangle2dfx.heightProperty());
			rectangle.translateXProperty().bind(rectangle2dfx.minXProperty());
			rectangle.translateYProperty().bind(rectangle2dfx.minYProperty());
			rectangle2dfx.setMinX(rectangle2d.getMinX());
			rectangle2dfx.setMaxX(rectangle2d.getMaxX());
			rectangle2dfx.setMinY(rectangle2d.getMinY());
			rectangle2dfx.setMaxY(rectangle2d.getMaxY());
			this.water.add(rectangle2dfx);
			this.waterRectangles.add(rectangle);
			this.waterGroup.getChildren().add(rectangle);
		}
	}

	public void addStation(@Nonnull ConcreteStationView station) {
		this.stations.add(station);
		this.stationsGroup.getChildren().add(station);
		station.setStationMouseListener(this);
	}

	public void addTrain(@Nonnull ConcreteTrainView train) {
		this.trains.add(train);
		this.trainGroup.getChildren().add(train);
	}

	public void addLine(@Nonnull ConcreteLineView line) {
		this.lines.add(line);
		Platform.runLater(() -> this.lineGroup.getChildren().add(line));
	}

	public void setBackgroundColor(@Nonnull Color backgroundColor) {
		this.mainPane.setBackground(new Background(new BackgroundFill(
		  backgroundColor,
		  CornerRadii.EMPTY,
		  Insets.EMPTY)));
	}

	public void setHUD(Collection<Node> HUD) {
		this.HUD = HUD;
	}
}
