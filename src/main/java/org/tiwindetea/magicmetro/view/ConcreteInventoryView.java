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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.tiwindetea.magicmetro.global.util.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class ConcreteInventoryView extends Parent implements InventoryView {

	private final MapView mapView;

	private HBox mainHBox = new HBox();
	private HBox linesHBox = new HBox();

	private final CircleCounter stationUpgradeCounter = new CircleCounter("Station\nupgrade");
	private final CircleCounter passengerCarCounter = new CircleCounter("Passenger\ncar");
	private final CircleCounter trainCounter = new CircleCounter("Train");
	private final CircleCounter tunnelCounter = new CircleCounter("Tunnel");

	private final List<Pair<ConcreteLineView, LineDisplayer>> lineDisplayers = new ArrayList<>(6);

	//TODO: make this beautiful
	private class CircleCounter extends Parent {

		private int number = 0;
		private final StackPane mainStackPane = new StackPane();
		private final Circle circle = new Circle(30);
		private final Label label;
		private final String text;

		/**
		 * Instantiates a new CircleCounter.
		 *
		 * @param text the text
		 */
		public CircleCounter(String text) {
			this.text = text;
			this.label = new Label(text + ": " + this.number);

			this.getChildren().add(this.mainStackPane);
			this.mainStackPane.getChildren().add(this.circle);
			this.mainStackPane.getChildren().add(this.label);
			Platform.runLater(this::requestLayout); // JavaFx dark magic debug
		}

		/**
		 * Sets number.
		 *
		 * @param number the number
		 */
		public void setNumber(int number) {
			this.number = number;
			this.label.setText(this.text + ": " + this.number);
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

	/**
	 * Instantiates a new ConcreteInventoryView.
	 *
	 * @param mapView the map view
	 */
	public ConcreteInventoryView(@Nonnull MapView mapView) {
		this.mapView = mapView;

		this.mainHBox.getChildren().add(this.stationUpgradeCounter);
		this.mainHBox.getChildren().add(this.passengerCarCounter);
		this.mainHBox.getChildren().add(this.trainCounter);
		this.mainHBox.getChildren().add(this.tunnelCounter);

		this.linesHBox.setAlignment(Pos.CENTER);
		this.mainHBox.getChildren().add(this.linesHBox);

		this.mainHBox.setAlignment(Pos.CENTER);
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

}
