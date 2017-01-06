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
import org.tiwindetea.magicmetro.view.LineView;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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

	private final LineView view;

	private List<Station> stations = new LinkedList<>();
	private SimplePair<Connection> lastConnections;
	private List<Section> sections;
	private List<Connection> stationConnection = new LinkedList<>();

	/**
	 * Default constructor.
	 * @param view the LineView
	 */
	public Line(LineView view) {
		this.view = view;
		this.gameId = view.getGameId();
		lastConnections = new SimplePair<>(null, null);
		sections = new LinkedList<>();
	}

	/**
	 * verify if the list is empty or not
	 *
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty(){
		return sections.isEmpty();
	}

	/**
	 * add a section to the line
	 * add the two last connections of the section if empty
	 *
	 * @param section the section we want to add
	 */
	public void addSection(Section section){
		if(!stationConnection.contains(section.getLeftConnection())){
			stationConnection.add(section.getLeftConnection());
			stations.add(section.getLeftConnection().getStationRef());
		}
		if(!stationConnection.contains(section.getRightConnection())){
			stationConnection.add(section.getRightConnection());
			stations.add(section.getRightConnection().getStationRef());
		}
		//if the line don't have any section
		if(isEmpty()){
			lastConnections.setLeft(section.getLeftConnection());
			lastConnections.setRight(section.getRightConnection());
		}
		sections.add(section);
	}

	/**
	 * remove a section
	 * remove the station that are in the section
	 * remove the last Connection if the line become empty
	 *
	 * @param section the section we want to remove
	 */
	public void removeSection(Section section){
		sections.remove(section);
		if(isEmpty()){
			lastConnections.setRight(null);
			lastConnections.setLeft(null);
		}
	}

	/**
	 * add a station
	 *
	 * @param station the station to add
	 */
	public void addStation(Station station){
		stations.add(station);
	}

	/**
	 * remove a station
	 *
	 * @param station the station to remove
	 */
	public void removeStation(Station station){
		stations.remove(station);
	}

	/**
	 * add a section with a station (for prolongation)
	 *
	 * @param section the section to add
	 * @param station the station to add
	 */
	public void addSectionStation(Section section, Station station){
		addSection(section);
		if(!stations.contains(station)) {
			addStation(station);
		}
		if(station.containsConnection(section.getRightConnection())) {
			stationConnection.add(section.getRightConnection());
		} else {
			stationConnection.add(section.getLeftConnection());
		}
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

	@Override
	public String toString() {
		return "Line{" +
				"gameId=" + gameId +
				", view=" + view +
				", stations=" + stations +
				", lastConnections=" + lastConnections +
				", sections=" + sections +
				", stationConnection=" + stationConnection +
				'}';
	}
}
