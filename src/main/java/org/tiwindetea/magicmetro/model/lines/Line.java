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

package org.tiwindetea.magicmetro.model.lines;

import org.tiwindetea.magicmetro.model.Station;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julien Barbier
 * @since 0.1
 */
public class Line {

	private List<Station> stationInTheLine;

	/**
	 * Default constructor
	 */
	public Line() {
		stationInTheLine = new ArrayList<>();
	}

	/**
	 * Allow to add a station inside the line
	 *
	 * @param station the station we want to add
	 * @return true if the station is added, false if error
	 */
	public boolean add(Station station){
		return stationInTheLine.add(station);
	}

	/**
	 * allow to remove a station of a line
	 *
	 * @param station the station we want to remove
	 * @return true if the station is removed, false if error (don't contain...)
	 */
	public boolean remove(Station station){
		return stationInTheLine.remove(station);
	}

	/**
	 * verify if a station is inside the line
	 *
	 * @param station the station we want to verify if it's inside the line
	 * @return true if contains, false else
	 */
	public boolean isStationInside(Station station){
		return stationInTheLine.contains(station);
	}

}
