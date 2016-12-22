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
import javafx.scene.Parent;
import javafx.scene.shape.Shape;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.tiwindetea.magicmetro.model.StationType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * A JavaFx implementation of TrainView.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class ConcreteTrainView extends Parent implements TrainView {

	private static final int MAX_PASSENGERS = 6;

	private final Shape sprite;
	private final List<Point2d> passengersPositions;
	private final List<Integer> freePassengersPositionsIndex;
	private final List<PassengerView> passengers = new ArrayList<>(MAX_PASSENGERS);
	private final PassengerViewFactory passengerViewFactory;

	/**
	 * Instantiates a new concrete train view.
	 *
	 * @param sprite               the sprite
	 * @param passengersPositions  the passengers positions
	 * @param passengerViewFactory the PassengerView factory
	 */
	public ConcreteTrainView(Shape sprite,
	                         int spriteWidth,
	                         int spriteHeight,
	                         List<Point2d> passengersPositions,
	                         PassengerViewFactory passengerViewFactory) {
		this.sprite = sprite;
		getChildren().add(this.sprite);
		this.sprite.setTranslateX(0);
		this.sprite.setTranslateY(0);
		setLayoutX(-spriteWidth / 2);
		setLayoutY(-spriteHeight / 2);
		this.passengersPositions = passengersPositions;
		this.freePassengersPositionsIndex = new LinkedList<>();
		for(int i = 0; i < this.passengersPositions.size(); ++i) {
			this.freePassengersPositionsIndex.add(i);
		}
		this.passengerViewFactory = passengerViewFactory;
	}

	@Override
	public void setRotation(double angle) {
		this.setRotate(angle);
	}

	@Override
	public void setPosition(@Nonnull Point2d position) {
		this.setTranslateX(position.getX());
		this.setTranslateY(position.getY());
	}

	@Override
	public void addPassenger(@Nonnull StationType wantedStation) {
		if(this.passengers.size() < MAX_PASSENGERS) {
			Optional<Integer> minIndexOption = this.freePassengersPositionsIndex.parallelStream()
			  .min(Integer::compareTo);
			if(minIndexOption.isPresent()) {
				Integer minIndex = minIndexOption.get();
				this.freePassengersPositionsIndex.removeIf(integer -> integer.equals(minIndex));
				PassengerView passenger = new PassengerView(this.passengersPositions.get(minIndex), wantedStation,
				  this.passengerViewFactory.newPassengerView(wantedStation));
				this.passengers.add(passenger);
				Platform.runLater(() -> getChildren().add(passenger));
			}
		}
	}

	@Override
	public void removePassenger(@Nonnull StationType wantedStation) {
		for(PassengerView passenger : this.passengers) {
			if(passenger.getWantedStation() == wantedStation) {
				this.freePassengersPositionsIndex.add(this.passengersPositions.indexOf(passenger.getPosition()));
				this.passengers.remove(passenger);
				Platform.runLater(() -> getChildren().remove(passenger));
				break;
			}
		}
	}

}
