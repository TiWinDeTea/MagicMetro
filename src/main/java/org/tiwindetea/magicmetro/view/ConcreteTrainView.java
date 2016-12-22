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

import javafx.scene.Parent;
import javafx.scene.shape.Shape;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.tiwindetea.magicmetro.global.util.Pair;
import org.tiwindetea.magicmetro.model.StationType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

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
	private final List<Pair<StationType, Shape>> passengers = new ArrayList<>(MAX_PASSENGERS);
	private final PassengerViewFactory passengerViewFactory;

	/**
	 * Instantiates a new concrete train view.
	 *
	 * @param sprite               the sprite
	 * @param passengersPositions  the passengers positions
	 * @param passengerViewFactory the PassengerView factory
	 */
	public ConcreteTrainView(Shape sprite,
	                         List<Point2d> passengersPositions,
	                         PassengerViewFactory passengerViewFactory) {
		this.sprite = sprite;
		getChildren().add(this.sprite);
		sprite.setTranslateX(0);
		sprite.setTranslateY(0);
		this.passengersPositions = passengersPositions;
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
			Shape passenger = this.passengerViewFactory.newPassengerView(wantedStation);
			Pair<StationType, Shape> pair = new Pair<>(wantedStation, passenger);
			this.passengers.add(pair);
			Point2d position = this.passengersPositions.get(this.passengers.indexOf(pair));
			passenger.setTranslateX(position.getX());
			passenger.setTranslateY(position.getY());
			getChildren().add(passenger);
		}
	}

	@Override
	public void removePassenger(@Nonnull StationType wantedStation) {
		for(Pair<StationType, Shape> passenger : this.passengers) {
			if(passenger.getLeft() == wantedStation) {
				this.passengers.remove(passenger);
				getChildren().remove(passenger.getRight());
				break;
			}
		}
	}

}
