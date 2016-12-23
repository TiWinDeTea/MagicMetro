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

import org.tiwindetea.magicmetro.model.lines.Connection;
import org.tiwindetea.magicmetro.model.lines.Line;
import org.tiwindetea.magicmetro.model.lines.Section;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julien Barbier on 21/12/2016.
 *
 * @author Julien Barbier
 * @since  0.1
 */
public class Map {

    private List<Train> trains;
    private List<Station> stations;
    private List<Line> lines;

    private double[][] stationHeuristique;

    /**
     * Default Constructor
     */
    public Map(){
        trains = new ArrayList<>();
        stations = new ArrayList<>();
        lines = new ArrayList<>();
    }

    /**
     * allow to add a train in the map
     * @param train the train we want to add
     * @return an boolean to verify if the train is added in the map
     */
    public boolean addTrains(Train train){
        return trains.add(train);
    }

    /**
     * allow to remove a train of the map
     * @param train the train we want to remove
     * @return a boolean to verify if the train is removed of the map
     */
    public boolean removeTrains(Train train){
        return trains.remove(train);
    }

    /**
     * allow to add a station in the map
     * @param station the station we want to add
     * @return a boolean to verify if the station is added in the map
     */
    public boolean addStation(Station station){
        return stations.add(station);
    }

    /**
     * allow to remove a station of the map
     * @param station the station we want to remove
     * @return a boolean to verify if the station is removed of the map
     */
    public boolean removeStation(Station station){
        return stations.remove(station);
    }

    /**
     * allow to add lines in the map
     * @param line the line we want to add
     * @return a boolean to verify if the line is added
     */
    public boolean addLine(Line line){
        return lines.add(line);
    }

    /**
     * allow to remove a line of the map
     * @param line the line we want to remove
     * @return a boolean to verify if the line is removed
     */
    public boolean removeLine(Line line){
        return lines.remove(line);
    }

    //all function for path finding (not Optimized)

    /**
     * find the shortest path between a station and a type of station
     *
     * @param stationA the station where we begin
     * @param stationWanted the type of station we want
     * @return the shortest path between the station and the type of station wanted, null if the station doesn't have connection
     */
    public List<Station> findShortestPath(Station stationA, StationType stationWanted){
        List<Station> predecessor = dijkstra(stationA);
        List<Station> stationsSearching = null;
        stationHeuristique = new double[stations.size()][];
        Station stationTmp = findTheNearestStationWithType(stationA, stationWanted);
        if(predecessor != null) {
            stationsSearching = new ArrayList<>();
            while (predecessor.size() != 0) {
                stationsSearching.add(stationTmp);
                stationTmp = predecessor.get(stations.indexOf(stationTmp));
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
        int index = stations.indexOf(stationA);
        stationHeuristique[index] = new double[stations.size()];
        for(int i = 0; i < stations.size(); ++i){
            if(stationA != stations.get(i)){
                stationHeuristique[index][i] = Double.MAX_VALUE;
            } else {
                stationHeuristique[index][i] = 0;
            }
        }
    }

    /**
     * search the nearest Station near the station sended
     *
     * @param station the station where we are
     * @return the nearest station of the station sended
     */
    private Station searchNearestStation(Station station, List<Station> stationsNotVisited){
        double mini = Double.MAX_VALUE;
        Station result = null;
        if(station.getConnections()!=null) {
            for (Connection connection : station.getConnections()) {
                Section section = connection.getSubSections().getRight().getSectionRef();
                double tmp = section.getLength();
                Station station1 = section.getStations().getRight();
                if (tmp != 0 && stationsNotVisited.contains(section.getStations().getRight())) {
                    int index = stations.indexOf(station);
                    int index2 = stations.indexOf(station1);
                    if (mini > stationHeuristique[index][index2]) {
                        result = section.getStations().getRight();
                        mini = stationHeuristique[index][index2];
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
        int index = stations.indexOf(stationA);
        int index1 = stations.indexOf(stationB);
        int index2 = stations.indexOf(beginStation);
        if(i >=0) {
            if (stationHeuristique[index2][index1] > stationHeuristique[index2][index] + i) {
                stationHeuristique[index2][index1] = stationHeuristique[index2][index] + i;
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
        List<Station> stationsSearching = new ArrayList<>(stations);
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
                        if(connection.getRightSubSection().getSectionRef() != null) {
                            updateDistance(stationTmp, connection.getRightSubSection().getSectionRef().getStations().getRight(), stationA, predecessor);
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
        for(Station station : stations){
            if(station.getType() == stationType){
                stationList.add(station);
            }
        }
        return stationList;
    }

    /**
     * find the nearest station with the types wanted
     * must be create after the initialisation of stationHeuristique
     *
     * @param stationA the station where we are (just for verification)
     * @param stationType the type of station we want
     * @return the nearest station
     */
    private Station findTheNearestStationWithType(Station stationA, StationType stationType){
        double distanceTmp = Double.MAX_VALUE;
        int index = stations.indexOf(stationA);
        Station stationTmp = stationA;
        List<Station> stationsWithGoodType = findAllStationWithType(stationType);
        if(stationA.getType() != stationType) {
            for (Station elem : stationsWithGoodType) {
                int index1 = stations.indexOf(elem);
                if (stationHeuristique[index][index1] < distanceTmp){
                    stationTmp = elem;
                    distanceTmp = stationHeuristique[index][index1];
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
        while (i < stationA.getConnections().size() && stationA.getConnections().get(i).getRightSubSection().getSectionRef().getStations().getRight() != stationB){
            ++i;
        }
        if(i < stationA.getConnections().size()) {
            return stationA.getConnections().get(i).getRightSubSection().getSectionRef().getLength();
        } else {
            return -1;
        }
    }

}
