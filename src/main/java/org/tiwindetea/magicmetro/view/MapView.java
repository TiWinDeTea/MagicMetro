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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.arakhne.afc.math.geometry.d2.d.Circle2d;
import org.arakhne.afc.math.geometry.d2.d.MultiShape2d;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.dfx.MultiShape2dfx;
import org.arakhne.afc.math.geometry.d2.dfx.Point2dfx;
import org.arakhne.afc.math.geometry.d2.dfx.Rectangle2dfx;

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
public class MapView extends DraggableZoomableParent implements StationMouseListener, SectionMouseListener {

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

		public LineModificationState(ConcreteStationView fromStation) {

			this.concreteLineView = MapView.this.inventoryView.getUnusedLine();
			this.sectionView = new SectionView(this.concreteLineView, fromStation.getTranslateX(), fromStation.getTranslateY());
			if(this.concreteLineView != null) {
				this.sectionView.setSectionMouseListener(MapView.this);
				this.sectionView.setFromStation(fromStation);
				this.sectionView.setWater(MapView.this.water);
				MapView.this.lineGroup.getChildren().add(this.sectionView);
			}
			else {
				MapView.this.modificationState = MapView.this.voidModificationState;
			}
		}

		@Override
		public synchronized void init(double x, double y) {

		}

		@Override
		public synchronized void update(double x, double y) {
			this.sectionView.setTo(x, y);
		}

		@Override
		public synchronized void apply(double x, double y) {
			MapView.this.lineGroup.getChildren().remove(this.sectionView);
			ConcreteStationView toStation = null;
			for(ConcreteStationView station : MapView.this.stations) {
				if(station != this.sectionView.getFromStation()) {
					Circle2d circle2d = new Circle2d(new Point2d(station.getTranslateX(), station.getTranslateY()),
					  STATION_BOUNDS_RADIUS);
					if(circle2d.contains(this.sectionView.getTo())) {
						toStation = station;
						break;
					}
				}
			}

			if(toStation != null) {
				this.sectionView.setToStation(toStation);
				this.sectionView.setTo(toStation.getTranslateX(), toStation.getTranslateY());
				this.concreteLineView.addSection(this.sectionView);
				MapView.this.inventoryView.setUsed(this.concreteLineView.gameId);
			}

			//TODO: event to model...
		}
	}

	private class LineDoubleModificationState implements ModificationState {

		ConcreteLineView concreteLineView;
		SectionView oldSectionView;
		SectionView fromSectionView;
		SectionView toSectionView;

		public LineDoubleModificationState(SectionView modifiedSection, double x, double y) {
			this.oldSectionView = modifiedSection;
			this.concreteLineView = modifiedSection.getLine();
			this.fromSectionView = new SectionView(this.concreteLineView, this.oldSectionView.getFrom(), new Point2dfx(x, y));
			this.fromSectionView.setSectionMouseListener(MapView.this);
			this.fromSectionView.setFromStation(this.oldSectionView.getFromStation());
			this.fromSectionView.setWater(MapView.this.water);
			this.toSectionView = new SectionView(this.concreteLineView, this.oldSectionView.getTo(), new Point2dfx(x, y));
			this.toSectionView.setSectionMouseListener(MapView.this);
			this.toSectionView.setFromStation(this.oldSectionView.getToStation());
			this.toSectionView.setWater(MapView.this.water);

			MapView.this.lineGroup.getChildren().add(this.fromSectionView);
			MapView.this.lineGroup.getChildren().add(this.toSectionView);
		}

		@Override
		public void init(double x, double y) {

		}

		@Override
		public void update(double x, double y) {
			this.fromSectionView.setTo(x, y);
			this.toSectionView.setTo(x, y);
		}

		@Override
		public void apply(double x, double y) {
			MapView.this.lineGroup.getChildren().remove(this.toSectionView);
			MapView.this.lineGroup.getChildren().remove(this.fromSectionView);
			ConcreteStationView toStation = null;
			for(ConcreteStationView station : MapView.this.stations) {
				if(station != this.fromSectionView.getFromStation() && station != this.toSectionView.getFromStation()) {
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
				this.toSectionView.setTo(toStation.getTranslateX(), toStation.getTranslateY());
				this.fromSectionView.setToStation(toStation);
				this.toSectionView.setToStation(toStation);
				this.concreteLineView.addSection(this.fromSectionView);
				this.concreteLineView.addSection(this.toSectionView);
			}

			//TODO: event to model...
		}
	}

	private final VoidModificationState voidModificationState = new VoidModificationState();
	private ModificationState modificationState = this.voidModificationState;
	private Lock modificationStateLock = new ReentrantLock();

	public MapView() {
		super();
		this.setWidth(DEFAULT_WIDTH);
		this.setHeight(DEFAULT_HEIGHT);
		this.getChildren().add(this.mainPane);
		this.mainPane.getChildren().add(this.waterGroup);
		this.mainPane.getChildren().add(this.lineGroup);
		this.mainPane.getChildren().add(this.stationsGroup);
		this.mainPane.getChildren().add(this.trainGroup);

		addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				MapView.this.modificationStateLock.lock();
				MapView.this.modificationState.update(event.getX(), event.getY());
				MapView.this.modificationStateLock.unlock();
			}
		});
		addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
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
		if(MapView.this.inventoryView.getUnusedLine() != null) {
			this.modificationState = new LineModificationState(station);
		}
		this.modificationStateLock.unlock();
		//no init
	}

	@Override
	public void mousePressedOnSection(SectionView section, double mouseX, double mouseY) {
		this.modificationStateLock.lock();
		this.modificationState = new LineDoubleModificationState(section, mouseX, mouseY);
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

}
