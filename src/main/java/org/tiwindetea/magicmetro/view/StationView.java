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

import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.tiwindetea.magicmetro.model.Station;
import org.tiwindetea.magicmetro.model.StationType;

import javax.annotation.Nonnull;

/**
 * View of a Station, control a graphical representation of a Station.
 *
 * @author Maxime PINARD
 * @see Station
 * @since 0.1
 */
public interface StationView {

	/**
	 * Gets game id.
	 *
	 * @return the game id
	 */
	int getGameId();

	/**
	 * Sets position.
	 *
	 * @param position the position
	 */
	void setPosition(@Nonnull Point2d position);

	/**
	 * Add passenger.
	 *
	 * @param wantedStation the wanted station
	 */
	void addPassenger(@Nonnull StationType wantedStation);

	/**
	 * Remove passenger.
	 *
	 * @param wantedStation the wanted station
	 */
	void removePassenger(@Nonnull StationType wantedStation);

	/**
	 * Make bigger.
	 */
	void makeBigger();

	/**
	 * Warn.
	 */
	void warn();

	/**
	 * Un ward.
	 */
	void unWard();

	/**
	 * Sets warn value.
	 *
	 * @param percentage the percentage
	 */
	void setWarnValue(double percentage);
}
