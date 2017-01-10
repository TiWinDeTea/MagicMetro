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

import org.tiwindetea.magicmetro.global.eventdispatcher.EventDispatcher;
import org.tiwindetea.magicmetro.global.eventdispatcher.EventListener;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.lineevents.LineCreationEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.lineevents.LineDecreaseEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.lineevents.LineExtensionEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.lineevents.LineInnerExtensionEvent;
import org.tiwindetea.magicmetro.global.util.BooleanWrapper;
import org.tiwindetea.magicmetro.model.lines.Connection;
import org.tiwindetea.magicmetro.model.lines.Line;
import org.tiwindetea.magicmetro.model.lines.Section;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Map of the game contains trains, stations and lines that are active in the game.<p>
 * Find the best path between two stations for the passengers.
 *
 * @author Maxime PINARD
 * @author Julien Barbier
 * @since 0.1
 */
public class GameMap {

	private List<Train> trains = new LinkedList<>();
	private Train[] trainsCopy = new Train[0]; // to avoid concurrent access
	private List<Station> stations = new LinkedList<>();
	private List<StationType> stationsTypes = new LinkedList<>();
	private List<Line> lines = new LinkedList<>();
	private Inventory inventory;
	private boolean haveChanged;

	private final EventListener<LineCreationEvent> onLineCreationEvent = event -> {
		for(Line line : this.lines) {
			if(line.gameId == event.lineId) {
				line.manage(event, this.stations);
				GameMap.this.initLine(line);
				break;
			}
		}
	};

	private final EventListener<LineExtensionEvent> onLineExtensionEvent = event -> {
		for(Line line : this.lines) {
			if(line.gameId == event.lineId) {
				line.manage(event, this.stations);
				break;
			}
		}
		haveChanged = true;
		calculateChangementPath();
	};

	private final EventListener<LineInnerExtensionEvent> onLineInnerExtensionEvent = event -> {
		for(Line line : this.lines) {
			if(line.gameId == event.lineId) {
				line.manage(event, this.stations);
				break;
			}
		}
		haveChanged = true;
		calculateChangementPath();
	};

	private final EventListener<LineDecreaseEvent> onLineDecreaseEvent = event -> {
		for(Line line : this.lines) {
			if(line.gameId == event.lineId) {
				line.manage(event);
				break;
			}
		}
	};

	private double[][] stationHeuristics;

    /**
     * Default Constructor
     */
    public GameMap(Inventory inventory){
        this.inventory = inventory;
	    EventDispatcher.getInstance().addListener(LineCreationEvent.class, this.onLineCreationEvent);
	    EventDispatcher.getInstance().addListener(LineExtensionEvent.class, this.onLineExtensionEvent);
	    EventDispatcher.getInstance().addListener(LineInnerExtensionEvent.class, this.onLineInnerExtensionEvent);
	    EventDispatcher.getInstance().addListener(LineDecreaseEvent.class, this.onLineDecreaseEvent);
	    haveChanged = false;
    }

	Passenger addPassengerToStation() {
		//TODO: create passenger with a wantedStation present on the map and add it to a station
		//FIXME: test
		Random random = new Random();
		Station station = this.stations.get(random.nextInt(this.stations.size()));
		StationType wantedStationType = this.stationsTypes.get(random.nextInt(this.stationsTypes.size()));
		while(wantedStationType == station.getType()) {
			wantedStationType = this.stationsTypes.get(random.nextInt(this.stationsTypes.size()));
		}
		Passenger passenger = new Passenger(station, wantedStationType);
		passenger.setPath(pathFinding(station, wantedStationType));

		//passenger.setPath();
		station.addPassenger(passenger);
		//passenger.setPath(findShortestPath(station, wantedStationType)); //TODO
		return passenger;
	}

	private void initLine(Line line) {
    	haveChanged = true;
		Train train = this.inventory.takeTrain();
		if(train != null) {
			train.start(line);
			this.addTrain(train);
		}
		calculateChangementPath();
	}

	private void calculateChangementPath(){
		for(Station station : stations){
			for(Passenger passenger : station.getPassengers()){
				passenger.setPath(pathFinding(station, passenger.getWantedStation()));
			}
		}
	}

    /**
     * Add a trains.
     *
     * @param train the train
     * @return true if the trains in the map changed, false otherwise
     */
    public synchronized boolean addTrain(Train train) {
	    boolean result = this.trains.add(train);
	    this.trainsCopy = this.trains.toArray(new Train[this.trains.size()]);
	    return result;
    }

    /**
     * Remove a trains.
     *
     * @param train the train
     * @return true if the trains was removed, false otherwise
     */
    public synchronized boolean removeTrain(Train train) {
	    boolean result = this.trains.remove(train);
	    this.trainsCopy = this.trains.toArray(new Train[this.trains.size()]);
	    return result;
    }

	/**
	 * Get a copy of the trains.
	 *
	 * @return the trains
	 */
	public synchronized Train[] getTrainsCopy() {
		return this.trainsCopy;
	}

    /**
     * Add a station.
     *
     * @param station the station
     * @return true if the stations in the map changed, false otherwise
     */
    public boolean addStation(Station station){
	    this.stationsTypes.add(station.getType());
	    return this.stations.add(station);
    }

    /**
     * Add a line.
     *
     * @param line the line
     * @return true if the lines in the map changed, false otherwise
     */
    public boolean addLine(Line line){
	    return this.lines.add(line);
    }

    /**
     * Remove a line.
     *
     * @param line the line
     * @return true if the line was removed, false otherwise
     */
    public boolean removeLine(Line line){
	    return this.lines.remove(line);
    }

    /**
     * Determine if a line contains a station..
     *
     * @param line    the line
     * @param station the station
     * @return true if the line contains the station, false otherwise
     */
    public boolean isInLine(Line line, Station station){
	    return line.contains(station);
    }

    /**
     * get the station with the id
     *
     * @param id the id of the station
     * @return the station with the good id, null if the station with this id isn't in the map
     */
    @Nullable
    private Station getStationWithId(int id){
        for(Station station : stations){
            if(station.gameId == id){
                return station;
            }
        }
        return null;
    }

    /**
     * get line with the id
     *
     * @param id the id of the line
     * @return the line with the id, null if the line with this id isn't in the map
     */
    private Line getLineWithId(int id){
        for(Line line : lines){
            if(line.gameId == id){
                return line;
            }
        }
        return null;
    }

    private Connection getConnectionInStationWithLine(Station station, Line line){
        int i = 0;
        if(line.contains(station)){
            while (i < station.getConnections().size() && (station.getConnections().get(i).getRightSubSection() != null &&
              station.getConnections().get(i).getRightSubSection().getSection().getLine() != line
                    || station.getConnections().get(i).getLeftSubSection() != null &&
              station.getConnections()
	            .get(i)
	            .getLeftSubSection()
	            .getSection()
	            .getLine() != line)) { //TODO Make the left section
	            ++i;
            }
            if(i < station.getConnections().size()){
                return station.getConnections().get(i);
            } else {
                return null;
            }
        }
        return null;
    }

    private Section getSectionWithConnections(Connection connectionA, Connection connectionB){
        if(connectionA.getLeftSubSection() != null && connectionB.getRightSubSection() != null &&
          connectionA.getLeftSubSection().getSection() != null && connectionB.getRightSubSection()
          .getSection() != null &&
          connectionA.getLeftSubSection().getSection() == connectionB.getRightSubSection().getSection()) {
	        return connectionA.getLeftSubSection().getSection();
        } else {
            if(connectionA.getRightSubSection() != null && connectionB.getLeftSubSection() != null &&
              connectionA.getRightSubSection().getSection() != null && connectionB.getLeftSubSection()
              .getSection() != null &&
              connectionA.getRightSubSection().getSection() == connectionB.getLeftSubSection().getSection()) {
	            return connectionA.getRightSubSection().getSection();
            } else {
                return null;
            }
        }
    }

    private boolean isTrainInSection(Section section, Line line){
        int i = 0;
        return true;
    }

    //all function for path finding (not Optimized)

    /**
     * compute the distance between two station connected
	 * Recursive
     *
     * @param stationA the first station
     * @param stationB the second station
     * @return the distance between the two station, -1 if the two station isn't connected
     */
    private synchronized double distanceBetweenTwoStationConnected(Station stationA, Station stationB){
    	if(stationA != stationB) {
			for (Connection connection : stationA.getConnections()) {
				if (connection.getLeftSubSection() != null && connection.getLeftSubSection().getSection().getOtherStationConnection(connection).getStation() == stationB) {
					return connection.getLeftSubSection().getSection().getLength();
				}
				if (connection.getRightSubSection() != null && connection.getRightSubSection().getSection().getOtherStationConnection(connection).getStation() == stationB) {
					return connection.getRightSubSection().getSection().getLength();
				}
			}
			return -1;
		}
		return 0;
    }

    private synchronized void initHeuristics(Station station){
    	int index = stations.indexOf(station);
    	int i = 0;
    	stationHeuristics = new double[stations.size()][stations.size()];
		for (Station station1 : stations) {
			if (station1 == station) {
				stationHeuristics[index][i] = 0;
			} else {
				stationHeuristics[index][i] = Double.MAX_VALUE;
			}
			++i;
		}
	}

	/**
	 * update the distance inside the stationsHeuristique between two stations (MUST BE CONNECTED)
	 *
	 * @param stationA the station A
	 * @param stationB the station B
	 * @param beginStation the station where we begin
	 */
	private void updateDistance(Station stationA, Station stationB, Station beginStation, Station[] predecessor){
		double i = distanceBetweenTwoStationConnected(stationA, stationB);
		int index = this.stations.indexOf(stationA);
		int index1 = this.stations.indexOf(stationB);
		int index2 = this.stations.indexOf(beginStation);
		if(i >= 0) {
			if(this.stationHeuristics[index2][index1] > this.stationHeuristics[index2][index] + i) {
				this.stationHeuristics[index2][index1] = this.stationHeuristics[index2][index] + i;
				predecessor[index1] = stationA;
			}
		}
	}

	/**
	 * path finding for the passenger
	 * Not Optimized
	 *
	 * @param station the station of the passenger
	 * @param stationType the type of station wanted
	 * @return the path
	 */
    public synchronized Stack<Station> pathFinding(Station station, StationType stationType){
		System.out.println("Wanted type = " + stationType + " and station is " + station.toString());
		Stack<Station> result = new Stack<>();
		Station[] predecessor = new Station[stations.size()];
		Station current = station;

		initHeuristics(station);
		if(station.getType() != stationType) {
			PriorityQueue<Station> path = new PriorityQueue<>(new Comparator<Station>() {
				@Override
				public int compare(Station o1, Station o2) {
					return (int) (stationHeuristics[stations.indexOf(station)][stations.indexOf(o1)] -
							stationHeuristics[stations.indexOf(station)][stations.indexOf(o2)]);
				}
			});
			path.add(station);
			Stack<Station> finished = new Stack<>();
			predecessor[stations.indexOf(station)] = current;
			while (!path.isEmpty()){
				System.out.println(path.toString());
				current = path.poll();
				if(current.getType() == stationType){
					System.out.println("Waiting Result");
					while (current != station){
						int index = stations.indexOf(current);
						result.push(current);
						current = predecessor[index];
					}
					System.out.println("Path finded : " + result.toString());
					return result;
				}
				for(Connection connection : current.getConnections()){
					if(connection.getRightSubSection() != null &&
							!finished.contains(connection.getRightSubSection().getSection().getOtherStationConnection(connection).getStation())){
						updateDistance(connection.getStation(), connection.getRightSubSection().getSection().getOtherStationConnection(connection).getStation(),station, predecessor);
						path.add(connection.getRightSubSection().getSection().getOtherStationConnection(connection).getStation());
					}
					if(connection.getLeftSubSection() != null &&
							!finished.contains(connection.getLeftSubSection().getSection().getOtherStationConnection(connection).getStation())){
						updateDistance(connection.getStation(), connection.getLeftSubSection().getSection().getOtherStationConnection(connection).getStation(), station, predecessor);
						path.add(connection.getLeftSubSection().getSection().getOtherStationConnection(connection).getStation());
					}
				}
				finished.add(current);
			}
		} else{
			System.out.println("The station is of type wanted");
		}
		System.out.println("No path finded");
		return null;
	}
}
