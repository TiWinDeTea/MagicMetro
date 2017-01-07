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
import org.tiwindetea.magicmetro.global.eventdispatcher.events.lineevents.LineExtensionEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.lineevents.LineInnerExtensionEvent;
import org.tiwindetea.magicmetro.model.lines.Connection;
import org.tiwindetea.magicmetro.model.lines.Line;
import org.tiwindetea.magicmetro.model.lines.Section;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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

	private final EventListener<LineCreationEvent> onLineCreationEvent = event -> {
		for(Line line : this.lines) {
			if(line.gameId == event.lineId) {
				line.manage(event, this.stations);
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
	};

	private final EventListener<LineInnerExtensionEvent> onLineInnerExtensionEvent = event -> {
		for(Line line : this.lines) {
			if(line.gameId == event.lineId) {
				line.manage(event, this.stations);
				break;
			}
		}
	};

	private double[][] stationHeuristics;

    /**
     * Default Constructor.
     */
    public GameMap(Inventory inventory){
        this.inventory = inventory;
	    EventDispatcher.getInstance().addListener(LineCreationEvent.class, this.onLineCreationEvent);
	    EventDispatcher.getInstance().addListener(LineExtensionEvent.class, this.onLineExtensionEvent);
	    EventDispatcher.getInstance().addListener(LineInnerExtensionEvent.class, this.onLineInnerExtensionEvent);
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
		station.addPassenger(passenger);
		return passenger;
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
	    for(Station station : this.stations) {
		    if(station.gameId == id) {
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
	    for(Line line : this.lines) {
		    if(line.gameId == id) {
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
        } else {
            return null;
        }
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
	//TODO: documentation

    /**
     * find the shortest path between a station and a type of station
     *
     * @param stationA      the station where we begin
     * @param stationWanted the type of station we want
     * @return the shortest path between the station and the type of station wanted, null if the station doesn't have connection or we don't have the type of station
     */
    public List<Station> findShortestPath(Station stationA, StationType stationWanted){
        List<Station> predecessor = dijkstra(stationA);
        List<Station> stationsSearching = null;
	    this.stationHeuristics = new double[this.stations.size()][];
	    Station stationTmp = findTheNearestStationWithType(stationA, stationWanted);
	    if(predecessor != null) {
		    stationsSearching = new ArrayList<>();
		    while(predecessor.size() != 0) {
			    stationsSearching.add(stationTmp);
			    stationTmp = predecessor.get(this.stations.indexOf(stationTmp));
			    predecessor.remove(stationTmp);
		    }
        }
        return stationsSearching;
    }

    /**
     * initialize the heuristique value for path finding
     *
     * @param stationA the station wher we begin
     */
    private void init_Dijkstra(Station stationA){
	    int index = this.stations.indexOf(stationA);
	    this.stationHeuristics[index] = new double[this.stations.size()];
	    for(int i = 0; i < this.stations.size(); ++i) {
		    if(stationA != this.stations.get(i)) {
			    this.stationHeuristics[index][i] = Double.MAX_VALUE;
		    }
		    else {
			    this.stationHeuristics[index][i] = 0;
		    }
        }
    }

    /**
     * search the nearest Station near the station sent
     * use for dijkstra Algorithm
     *
     * @param station the station where we are
     * @return the nearest station of the station sent
     */
    private Station searchNearestStation(Station station, List<Station> stationsNotVisited){
        double mini = Double.MAX_VALUE;
        Station result = null;
        //we verify that the station owns connections
        if(station.getConnections()!=null) {
            for (Connection connection : station.getConnections()) {
                //we get the section which link the station and the temporary station
	            Section section = connection.getSubSections().getRight().getSection();
	            //we verify section isn't null
	            if(section != null) {
		            double tmp = section.getLength();
		            Station station1 = section.getRightConnection().getStation();
		            //we verify if tmp is different from 0 and we are not passed by this station before
		            if(tmp != 0 && !stationsNotVisited.contains(section.getRightConnection().getStation())) {
			            int index = this.stations.indexOf(station);
			            int index2 = this.stations.indexOf(station1);
			            //if mini is superior als the distance between station A and the temporary station
			            if(mini > this.stationHeuristics[index][index2]) {
				            //we save the temporary station inside result
				            result = section.getRightConnection().getStation();
				            //we affect mini with the good value
				            mini = this.stationHeuristics[index][index2];
			            }
                    }
                }
            }
        }
        return result;
    }

    /**
     * update the distance inside the stationsHeuristique between two stations (MUST BE CONNECTED)
     *
     * @param stationA the station A
     * @param stationB the station B
     * @param beginStation the station where we begin the dijkstra algorithm
     * @param predecessor the list of all station we pass
     */
    private void updateDistance(Station stationA, Station stationB, Station beginStation, List<Station> predecessor){
        double i = distanceBetweenTwoStationConnected(stationA, stationB);
	    int index = this.stations.indexOf(stationA);
	    int index1 = this.stations.indexOf(stationB);
	    int index2 = this.stations.indexOf(beginStation);
	    if(i >= 0) {
		    if(this.stationHeuristics[index2][index1] > this.stationHeuristics[index2][index] + i) {
			    this.stationHeuristics[index2][index1] = this.stationHeuristics[index2][index] + i;
			    predecessor.add(index1, stationA);
		    }
        }
    }

    /**
     * compute all the distance between all the station and the station where we are
     *
     * @param stationA the station where we are
     * @return the list of station corresponding of the predecessor of all the station
     */
    private List<Station> dijkstra(Station stationA){

        //initialisation of all the variables needed
	    List<Station> stationsSearching = new ArrayList<>(this.stations);
	    List<Station> predecessor = new ArrayList<>(stationsSearching.size());
	    Station stationTmp = stationA;
	    Station stationPredecessor = null;

        //initialisation of dijkstra
        init_Dijkstra(stationA);

        //verification that stationA owns connections
        if(stationA.getConnections() != null) {

            //while the list of station isn't empty
            while (!stationsSearching.isEmpty()) {

                //find the nearest station
                stationPredecessor = stationTmp;
                stationTmp = searchNearestStation(stationTmp, stationsSearching);

                //verify that we will not enter in a infinity loop
                if (stationTmp == null && stationPredecessor != stationA) {
                    //return to the beginning
                    stationTmp = stationA;
                } else if (stationTmp == null) {
                    //no more stations connected so break
                    break;
                } else {
                    //we remove the station finded and we update the distance for all the station neared
                    stationsSearching.remove(stationTmp);
                    for (Connection connection : stationTmp.getConnections()) {
                        //we verify if we have a section and so a station
	                    if(connection.getRightSubSection().getSection() != null) {
		                    updateDistance(stationTmp,
		                      connection.getRightSubSection().getSection().getRightConnection().getStation(),
		                      stationA,
		                      predecessor);
	                    }
                    }
                }
            }
        }
        return predecessor;
    }

    /**
     * find all the station with the type wanted
     *
     * @param stationType the type of station we want
     * @return the list of station with the good type
     */
    private List<Station> findAllStationWithType(StationType stationType){
        List<Station> stationList = new ArrayList<>();
	    for(Station station : this.stations) {
		    if(station.getType() == stationType) {
			    stationList.add(station);
		    }
        }
        return stationList;
    }

    /**
     * find the nearest station with the types wanted
     * must be create after the initialisation of stationHeuristics
     *
     * @param stationA the station where we are (just for verification)
     * @param stationType the type of station we want
     * @return the nearest station
     */
    private Station findTheNearestStationWithType(Station stationA, StationType stationType){
        double distanceTmp = Double.MAX_VALUE;
	    int index = this.stations.indexOf(stationA);
	    Station stationTmp = stationA;
	    //we initialize the list which will contain all station with the good type
	    List<Station> stationsWithGoodType = findAllStationWithType(stationType);
	    //we verify the station where we are isn't a station with the good type
	    if(stationA.getType() != stationType) {
		    for(Station elem : stationsWithGoodType) {
		        //we get the index of the station  inside the list of all stations
			    int index1 = this.stations.indexOf(elem);
			    //if the distance between the station of beginning and the temporary station is smaller als the distance we have
			    if(this.stationHeuristics[index][index1] < distanceTmp) {
				    stationTmp = elem;
				    distanceTmp = this.stationHeuristics[index][index1];
			    }
            }
        }
        return stationTmp;
    }

    /**
     * compute the distance between two station connected
     *
     * @param stationA the first station
     * @param stationB the second station
     * @return the distance between the two station, -1 if the two station isn't connected
     */
    private double distanceBetweenTwoStationConnected(Station stationA, Station stationB){
        int i = 0;
	    while(i < stationA.getConnections().size() && stationA.getConnections()
	      .get(i)
	      .getRightSubSection()
	      .getSection()
	      .getRightConnection() != null
	      && stationA.getConnections()
	      .get(i)
	      .getRightSubSection()
	      .getSection()
	      .getRightConnection()
	      .getStation() != stationB) {
		    ++i;
	    }
        if(i < stationA.getConnections().size()) {
	        return stationA.getConnections().get(i).getRightSubSection().getSection().getLength();
        } else {
            return -1;
        }
    }

}
