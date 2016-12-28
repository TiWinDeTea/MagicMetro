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

import org.tiwindetea.magicmetro.global.util.SimplePair;
import org.tiwindetea.magicmetro.model.Station;

import java.util.Collection;
import java.util.LinkedList;

/**
 * A line composed of several sections each composed of connections and subsections.
 *
 * @author Maxime PINARD
 * @author Julien Barbier
 * @see Connection
 * @see Section
 * @see SubSection
 * @since 0.1
 */
public class Line {

	public final int gameId;

	private Collection<Station> stations = new LinkedList<>();
	private SimplePair<Connection> lastConnections; //TODO
	private Collection<Section> sections; //TODO

	/**
	 * Default constructor.
	 */
	public Line() {
		this.gameId = 0; //TODO: take the LineView gameId
	}

	/**
	 * Add a station to the line.
	 *
	 * @param station the station to add
	 * @return true if the stations of the line changed
	 */
	public boolean add(Station station){
		return this.stations.add(station);
	}

	/**
	 * Remove a station of a line.
	 *
	 * @param station the station to remove
	 * @return true if the station was removed, false otherwise
	 */
	public boolean remove(Station station){
		return this.stations.remove(station);
	}

	/**
	 * Determine if a line contains a station.
	 *
	 * @param station the station
	 * @return true if the line contains the station, false otherwise
	 */
	public boolean contains(Station station) {
		return this.stations.contains(station);
	}

}
