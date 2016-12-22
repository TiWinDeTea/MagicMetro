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
import org.tiwindetea.magicmetro.model.StationType;

/**
 * Representation of a passenger used by ConcreteTrainView and ConcreteStationView.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class PassengerView extends Parent {

	private Point2d position;
	private StationType wantedStation;
	private Shape sprite;

	/**
	 * Instantiates a new passenger view.
	 *
	 * @param wantedStation the wanted station
	 * @param sprite        the sprite
	 */
	public PassengerView(StationType wantedStation, Shape sprite) {
		this(new Point2d(), wantedStation, sprite);
	}

	/**
	 * Instantiates a new passenger view.
	 *
	 * @param position      the position
	 * @param wantedStation the wanted station
	 * @param sprite        the sprite
	 */
	public PassengerView(Point2d position, StationType wantedStation, Shape sprite) {
		setPosition(position);
		this.wantedStation = wantedStation;
		this.sprite = sprite;
		getChildren().add(sprite);
	}

	/**
	 * Sets position.
	 *
	 * @param position the position
	 */
	public void setPosition(Point2d position) {
		this.position = position;
		setTranslateX(position.getX());
		setTranslateY(position.getY());
	}

	/**
	 * Gets position.
	 *
	 * @return the position
	 */
	public Point2d getPosition() {
		return this.position;
	}

	/**
	 * Gets wanted station.
	 *
	 * @return the wanted station
	 */
	public StationType getWantedStation() {
		return this.wantedStation;
	}

}
