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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import org.tiwindetea.magicmetro.global.util.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class ConcreteInventoryView extends Parent implements InventoryView {

	//TODO: externalise in resource bundle
	private static final Image STATION_UPGRADE_IMAGE = new Image(ConcreteInventoryView.class.getResource(
	  "InventoryStationUpgrade.png").toString());
	private static final Image PASSENGER_CAR_IMAGE = new Image(ConcreteInventoryView.class.getResource(
	  "InventoryPassengerCar.png").toString());
	private static final Image TRAIN_IMAGE = new Image(ConcreteInventoryView.class.getResource(
	  "InventoryTrain.png").toString());
	private static final Image TUNNEL_IMAGE = new Image(ConcreteInventoryView.class.getResource(
	  "InventoryTunnel.png").toString());

	private final MapView mapView;
	private final InventoryMouseListener inventoryMouseListener;

	private HBox mainHBox = new HBox();
	private HBox linesHBox = new HBox();

	private final CircleCounter stationUpgradeCounter = new CircleCounter(STATION_UPGRADE_IMAGE);
	private final CircleCounter passengerCarCounter = new CircleCounter(PASSENGER_CAR_IMAGE);
	private final CircleCounter trainCounter = new CircleCounter(TRAIN_IMAGE);
	private final CircleCounter tunnelCounter = new CircleCounter(TUNNEL_IMAGE);

	private final List<Pair<ConcreteLineView, LineDisplayer>> lineDisplayers = new ArrayList<>(6);

	private class CircleCounter extends Parent {

		private int number = 0;
		private final AnchorPane mainAnchorPane = new AnchorPane();
		private final ImageView imageView;
		private final Label label;

		/**
		 * Instantiates a new CircleCounter.
		 *
		 * @param image the image
		 */
		public CircleCounter(Image image) {
			this.imageView = new ImageView(image);
			this.imageView.setFitWidth(60);
			this.imageView.setFitHeight(60);

			this.label = new Label("" + this.number);
			this.label.setTextFill(Color.BLACK);
			this.label.setFont(new Font(20));
			AnchorPane.setRightAnchor(this.label, 0d);
			AnchorPane.setBottomAnchor(this.label, 0d);

			this.mainAnchorPane.setMinWidth(70);
			this.mainAnchorPane.getChildren().add(this.imageView);
			this.mainAnchorPane.getChildren().add(this.label);
			this.getChildren().add(this.mainAnchorPane);

			Platform.runLater(this::requestLayout); // JavaFx dark magic debug
		}

		/**
		 * Sets number.
		 *
		 * @param number the number
		 */
		public void setNumber(int number) {
			this.number = number;
			this.label.setText("" + this.number);
		}

	}

	private class LineDisplayer extends Parent {

		private final Circle circle = new Circle(15);
		private boolean used = false;

		/**
		 * Instantiates a new LineDisplayer.
		 */
		public LineDisplayer() {
			this.circle.setFill(Color.GRAY);
			this.getChildren().add(this.circle);
		}

		/**
		 * Sets available.
		 *
		 * @param color the color
		 */
		public void setAvailable(Color color) {
			this.circle.setFill(color);
		}

		/**
		 * Sets used.
		 */
		public void setUsed() {
			this.used = true;
			this.circle.setRadius(30);
		}

		/**
		 * Sets unused.
		 */
		public void setUnused() {
			this.used = false;
			this.circle.setRadius(15);
		}

		/**
		 * Gets if used or not.
		 *
		 * @return true if used, false otherwise
		 */
		public boolean isUsed() {
			return this.used;
		}

	}

	private Collection<Node> HUD = new LinkedList<>();

	/**
	 * Instantiates a new ConcreteInventoryView.
	 *
	 * @param mapView the map view
	 */
	public ConcreteInventoryView(@Nonnull MapView mapView) {
		this.mapView = mapView;
		this.inventoryMouseListener = mapView;

		this.stationUpgradeCounter.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ConcreteInventoryView.this.inventoryMouseListener.mousePressedOnStationUpgradeCounter();
			}
		});
		this.stationUpgradeCounter.addEventFilter(MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				for(Node node : ConcreteInventoryView.this.HUD) {
					node.setMouseTransparent(true);
				}
				ConcreteInventoryView.this.startFullDrag();
			}
		});
		this.stationUpgradeCounter.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				for(Node node : ConcreteInventoryView.this.HUD) {
					node.setMouseTransparent(false);
				}
			}
		});
		this.mainHBox.getChildren().add(this.stationUpgradeCounter);

		this.passengerCarCounter.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ConcreteInventoryView.this.inventoryMouseListener.mousePressedOnPassengerCarCounter();
			}
		});
		this.passengerCarCounter.addEventFilter(MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				for(Node node : ConcreteInventoryView.this.HUD) {
					node.setMouseTransparent(true);
				}
				ConcreteInventoryView.this.startFullDrag();
			}
		});
		this.passengerCarCounter.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				for(Node node : ConcreteInventoryView.this.HUD) {
					node.setMouseTransparent(false);
				}
			}
		});
		this.mainHBox.getChildren().add(this.passengerCarCounter);

		this.trainCounter.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ConcreteInventoryView.this.inventoryMouseListener.mousePressedOnTrainCounter();
			}
		});
		this.trainCounter.addEventFilter(MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				for(Node node : ConcreteInventoryView.this.HUD) {
					node.setMouseTransparent(true);
				}
				ConcreteInventoryView.this.startFullDrag();
			}
		});
		this.trainCounter.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				for(Node node : ConcreteInventoryView.this.HUD) {
					node.setMouseTransparent(false);
				}
			}
		});
		this.mainHBox.getChildren().add(this.trainCounter);

		this.linesHBox.setAlignment(Pos.CENTER);
		this.linesHBox.setSpacing(20);
		this.mainHBox.getChildren().add(this.linesHBox);

		this.mainHBox.getChildren().add(this.tunnelCounter);

		this.mainHBox.setAlignment(Pos.CENTER);
		this.mainHBox.setSpacing(20);
		this.getChildren().add(this.mainHBox);

	}

	@Override
	public synchronized void setStationUpgrades(int value) {
		this.stationUpgradeCounter.setNumber(value);
	}

	@Override
	public synchronized void setTrains(int value) {
		this.trainCounter.setNumber(value);
	}

	@Override
	public synchronized void setPassengerCars(int value) {
		this.passengerCarCounter.setNumber(value);
	}

	@Override
	public void setTunnels(int value) {
		this.tunnelCounter.setNumber(value);
	}

	@Override
	public synchronized void setAvailableLine(int lineId) {
		ConcreteLineView line = this.mapView.getLineFromId(lineId);
		LineDisplayer lineDisplayer = new LineDisplayer();
		lineDisplayer.setAvailable(line.color);
		this.lineDisplayers.add(new Pair<>(line, lineDisplayer));
		Platform.runLater(() -> this.linesHBox.getChildren().add(lineDisplayer));
	}

	@Override
	public synchronized void setUsed(int lineId) {
		for(Pair<ConcreteLineView, LineDisplayer> pair : this.lineDisplayers) {
			if(pair.getLeft().gameId == lineId) {
				pair.getRight().setUsed();
				break;
			}
		}
	}

	@Override
	public synchronized void setUnused(int lineId) {
		for(Pair<ConcreteLineView, LineDisplayer> pair : this.lineDisplayers) {
			if(pair.getLeft().gameId == lineId) {
				pair.getRight().setUnused();
				break;
			}
		}
	}

	/**
	 * Gets a unused line.
	 *
	 * @return the unused line or null if all lines are used
	 */
	public synchronized ConcreteLineView getUnusedLine() {
		for(Pair<ConcreteLineView, LineDisplayer> pair : this.lineDisplayers) {
			if(!pair.getRight().isUsed()) {
				return pair.getLeft();
			}
		}
		return null;
	}

	public void setHUD(Collection<Node> HUD) {
		this.HUD = HUD;
	}

}
