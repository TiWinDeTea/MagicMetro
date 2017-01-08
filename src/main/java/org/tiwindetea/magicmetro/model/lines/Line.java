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

import org.tiwindetea.magicmetro.global.eventdispatcher.events.lineevents.LineCreationEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.lineevents.LineExtensionEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.lineevents.LineInnerExtensionEvent;
import org.tiwindetea.magicmetro.global.util.SimplePair;
import org.tiwindetea.magicmetro.model.Station;
import org.tiwindetea.magicmetro.view.LineView;

import javax.annotation.Nonnull;
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
	private List<Section> sections = new LinkedList<>();
	private SimplePair<Connection> lastConnections;
	private List<Connection> stationConnection = new LinkedList<>();

	/**
	 * Default constructor.
	 * @param view the LineView
	 */
	public Line(LineView view) {
		this.view = view;
		this.gameId = view.getGameId();
		this.lastConnections = new SimplePair<>(null, null);
	}

	public void manage(LineCreationEvent event, Collection<Station> stations) {
		if(!this.sections.isEmpty()) {
			throw new IllegalStateException("Line creation event on a non empty line");
		}

		Station fromStation = null;
		Station toStation = null;
		for(Station station : stations) {
			if(station.gameId == event.fromStationId) {
				fromStation = station;
			}
			if(station.gameId == event.toStationId) {
				toStation = station;
			}
		}

		Connection fromConnection = new Connection(fromStation);
		Connection middleConnection = new Connection(event.middleConnectionPosition);
		Connection toConnection = new Connection(toStation);

		fromStation.addConnection(fromConnection);
		toStation.addConnection(toConnection);

		SubSection leftSubSection = new SubSection(false, fromConnection, middleConnection); //TODO: tunnel management
		SubSection rightSubSection = new SubSection(false, middleConnection, toConnection); //TODO: tunnel management

		fromConnection.setSubSections(leftSubSection, leftSubSection);
		middleConnection.setSubSections(leftSubSection, rightSubSection);
		toConnection.setSubSections(rightSubSection, rightSubSection);

		this.lastConnections.setLeft(fromConnection);
		this.lastConnections.setRight(toConnection);
		this.stationConnection.add(fromConnection);
		this.stationConnection.add(toConnection);

		Section section = new Section(
		  event.newSectionId,
		  this,
		  fromConnection,
		  toConnection,
		  middleConnection,
		  leftSubSection,
		  rightSubSection
		);

		this.stations.add(fromStation);
		this.stations.add(toStation);
		this.sections.add(section);
	}

	public void manage(LineExtensionEvent event, Collection<Station> stations) {
		boolean left = true;

		Connection fromConnection = null;
		if(this.lastConnections.getLeft().getStation().gameId == event.fromStationId) {
			fromConnection = this.lastConnections.getLeft();
		}
		if(this.lastConnections.getRight().getStation().gameId == event.fromStationId) {
			fromConnection = this.lastConnections.getRight();
			left = false;
		}
		Station toStation = null;
		for(Station station : stations) {
			if(station.gameId == event.toStationId) {
				toStation = station;
				break;
			}
		}

		Connection middleConnection = new Connection(event.middleConnectionPosition);
		Connection toConnection = new Connection(toStation);

		toStation.addConnection(toConnection);

		SubSection leftSubSection = new SubSection(false, fromConnection, middleConnection); //TODO: tunnel management
		SubSection rightSubSection = new SubSection(false, middleConnection, toConnection); //TODO: tunnel management

		if(left) {
			fromConnection.setSubSectionLeft(leftSubSection);
			this.lastConnections.setLeft(toConnection);
		}
		else {
			fromConnection.setSubSectionRight(leftSubSection);
			this.lastConnections.setRight(toConnection);
		}
		middleConnection.setSubSections(leftSubSection, rightSubSection);
		toConnection.setSubSections(rightSubSection, rightSubSection);

		Section section = new Section(
		  event.newSectionId,
		  this,
		  fromConnection,
		  toConnection,
		  middleConnection,
		  leftSubSection,
		  rightSubSection
		);

		this.stations.add(toStation);
		this.sections.add(section);
	}

	public void manage(LineInnerExtensionEvent event, Collection<Station> stations) {
		System.out.println("Line: manage LineInnerExtensionEvent"); //FIXME: test output
		//TODO
	}

	/**
	 * verify if the list is empty or not
	 *
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty(){
		return this.sections.isEmpty();
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

	/**
	 * Gets last connections.
	 *
	 * @return the last connections
	 */
	@Nonnull
	public SimplePair<Connection> getLastConnections() {
		return this.lastConnections;
	}

}
