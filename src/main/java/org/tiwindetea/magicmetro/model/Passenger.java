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

package org.tiwindetea.magicmetro.model;

import java.util.Stack;

/**
 * Passenger, appear in a station and is moved by trains according to his path.
 *
 * @author Julien Barbier
 * @author Maxime PINARD
 * @since 0.1
 */
public class Passenger {

	private Stack<Station> path = new Stack<>();
	private Station station;
	private final StationType stationWanted;

	/**
	 * Instantiates a new Passenger.
	 *
	 * @param station       the station where the passenger is
	 * @param stationWanted the wanted station type
	 */
	public Passenger(Station station, StationType stationWanted) {
		this.station = station;
		this.stationWanted = stationWanted;
	}


	/**
	 * Gets wanted station.
	 *
	 * @return the wanted station
	 */
	public StationType getWantedStation() {
		return this.stationWanted;
	}


	/**
	 * Get station station.
	 *
	 * @return the station
	 */
	public Station getStation(){
		return this.station;
	}


	/**
	 * Sets path.
	 *
	 * @param path the path
	 */
	public void setPath(Stack<Station> path) {
	    this.path = path;
    }

	/**
	 * Gets path.
	 *
	 * @return the path
	 */
	public Stack<Station> getPath() {
		return this.path;
	}
}
