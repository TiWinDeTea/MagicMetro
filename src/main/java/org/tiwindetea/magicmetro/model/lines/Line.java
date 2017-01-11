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
import org.tiwindetea.magicmetro.global.eventdispatcher.events.lineevents.LineDecreaseEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.lineevents.LineExtensionEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.lineevents.LineInnerExtensionEvent;
import org.tiwindetea.magicmetro.global.util.SimplePair;
import org.tiwindetea.magicmetro.model.LineManager;
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

	private final LineManager lineManager;

	/**
	 * Instantiates a new Line.
	 *
	 * @param view        the view
	 * @param lineManager the line manager
	 */
	public Line(LineView view, LineManager lineManager) {
		this.view = view;
		this.gameId = view.getGameId();
		this.lineManager = lineManager;
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

		if(toStation == null) {
			throw new IllegalStateException("toStation is not on the map");
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

		Section oldSection = null;
		for(Section section : this.sections) {
			if(section.gameId == event.oldSectionId) {
				oldSection = section;
				break;
			}
		}

		if(oldSection == null) {
			throw new IllegalStateException("oldSection is not part of the line");
		}

		Connection leftConnection = oldSection.getLeftConnection();
		Station leftStation = leftConnection.getStation();
		Connection rightConnection = oldSection.getRightConnection();
		Station rightStation = rightConnection.getStation();
		Connection oldConnection = oldSection.getMiddleConnection();

		if(leftConnection == rightConnection ||
		  leftConnection == oldConnection ||
		  rightConnection == oldConnection) {
			throw new IllegalStateException("oldSection connections are not valid");
		}
		if(leftStation == null || rightStation == null) {
			throw new IllegalStateException("oldSection stations are not valid");
		}

		Station middleStation = null;
		for(Station station : stations) {
			if(station.gameId == event.addedStationId) {
				middleStation = station;
				break;
			}
		}

		if(middleStation == null) {
			throw new IllegalStateException("middleStation is not on the map");
		}

		Connection middleLeftConnection = new Connection(event.middleLeftConnectionPosition);
		Connection middleConnection = new Connection(middleStation);
		Connection middleRightConnection = new Connection(event.middleRightConnectionPosition);

		middleStation.addConnection(middleConnection);

		SubSection leftSubsection = new SubSection(false, //TODO: tunnel management
		  leftConnection,
		  middleLeftConnection);
		SubSection middleLeftSubsection = new SubSection(false, //TODO: tunnel management
		  middleLeftConnection,
		  middleConnection);
		SubSection middleRightSubsection = new SubSection(false, //TODO: tunnel management
		  middleConnection,
		  middleRightConnection);
		SubSection rightSubsection = new SubSection(false, //TODO: tunnel management
		  middleRightConnection,
		  rightConnection);

		//left connection
		if(leftConnection.getRightSubSection() == oldConnection.getLeftSubSection() ||
		  leftConnection.getRightSubSection() == oldConnection.getRightSubSection()) {
			leftConnection.setSubSectionRight(leftSubsection);
		}
		if(leftConnection.getLeftSubSection() == oldConnection.getLeftSubSection() ||
		  leftConnection.getLeftSubSection() == oldConnection.getRightSubSection()) {
			leftConnection.setSubSectionLeft(leftSubsection);
		}

		//middle left connection
		middleLeftConnection.setSubSectionLeft(leftSubsection);
		middleLeftConnection.setSubSectionRight(middleLeftSubsection);

		//middle connection
		middleConnection.setSubSectionLeft(middleLeftSubsection);
		middleConnection.setSubSectionRight(middleRightSubsection);

		//middle right connection
		middleRightConnection.setSubSectionLeft(middleRightSubsection);
		middleRightConnection.setSubSectionRight(rightSubsection);

		//right subsection
		if(rightConnection.getRightSubSection() == oldConnection.getLeftSubSection() ||
		  rightConnection.getRightSubSection() == oldConnection.getRightSubSection()) {
			rightConnection.setSubSectionRight(rightSubsection);
		}
		if(rightConnection.getLeftSubSection() == oldConnection.getLeftSubSection() ||
		  rightConnection.getLeftSubSection() == oldConnection.getRightSubSection()) {
			rightConnection.setSubSectionLeft(rightSubsection);
		}

		Section leftSection = new Section(
		  event.newLeftSectionId,
		  this,
		  leftConnection,
		  middleConnection,
		  middleLeftConnection,
		  leftSubsection,
		  middleLeftSubsection
		);

		Section rightSection = new Section(
		  event.newRightSectionId,
		  this,
		  middleConnection,
		  rightConnection,
		  middleRightConnection,
		  middleRightSubsection,
		  rightSubsection
		);

		this.sections.remove(oldSection);
		this.view.deleteSection(oldSection.gameId);

		this.stations.add(middleStation);
		this.sections.add(leftSection);
		this.sections.add(rightSection);
	}

	public void manage(LineDecreaseEvent event) {

		Station oldStation = null;
		for(Station station : this.stations) {
			if(station.gameId == event.oldStationId) {
				oldStation = station;
				break;
			}
		}

		if(oldStation == null) {
			throw new IllegalStateException("oldStation is not part of the line");
		}

		Connection oldConnection = null;
		if(this.lastConnections.getLeft().getStation() == oldStation) {
			oldConnection = this.lastConnections.getLeft();
		}
		if(this.lastConnections.getRight().getStation() == oldStation) {
			oldConnection = this.lastConnections.getRight();
		}

		if(oldConnection == null) {
			throw new IllegalStateException("oldStation doesn't contain one of the last connections of the line");
		}

		Section oldSection = null;
		for(Section section : this.sections) {
			if(section.gameId == event.oldSectionId) {
				oldSection = section;
			}
		}

		if(oldSection == null) {
			throw new IllegalStateException("oldSection is not part of the line");
		}

		Connection lastConnection = null;
		if(oldSection.getLeftConnection() == oldConnection) {
			lastConnection = oldSection.getRightConnection();
		}
		if(oldSection.getRightConnection() == oldConnection) {
			lastConnection = oldSection.getLeftConnection();
		}

		if(oldSection.getLeftSubSection() == lastConnection.getLeftSubSection() ||
		  oldSection.getRightSubSection() == lastConnection.getLeftSubSection()) {
			lastConnection.setSubSectionLeft(lastConnection.getRightSubSection());
			this.lastConnections.setLeft(lastConnection);
		}

		if(oldSection.getLeftSubSection() == lastConnection.getRightSubSection() ||
		  oldSection.getRightSubSection() == lastConnection.getRightSubSection()) {
			lastConnection.setSubSectionRight(lastConnection.getLeftSubSection());
			this.lastConnections.setRight(lastConnection);
		}

		//TODO: remove old section from view

		this.stations.remove(oldStation);
		this.sections.remove(oldSection);
		this.view.deleteSection(oldSection.gameId);

		if(this.sections.isEmpty()) {
			lastConnection.setSubSectionLeft(null);
			lastConnection.setSubSectionRight(null);
			this.lineManager.lineDeleted(this);
		}
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
