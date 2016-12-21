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

import org.tiwindetea.magicmetro.model.lines.Line;
import org.tiwindetea.magicmetro.model.lines.Section;

import javax.annotation.Nullable;
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

    /**
     * try to find the optimal path between a station A and a type of station
     * @param stationA the station where we begin
     * @param stationWanted the type of station we want
     * @return the optimal path (it is a list of section)
     */
    public List<Section> findOptimalPath(Station stationA, StationType stationWanted){
        List<List<Section>> resultPath = new ArrayList<>();
        resultPath = findAllPath(stationA, stationWanted, null);
        double distanceMem = Double.MAX_VALUE, distanceTmp;
        List<Section> optimalPath = null;
        if(resultPath != null) {
            for (List<Section> sectionList : resultPath) {
                distanceTmp = 0;
                for(Section section : sectionList){
                    distanceTmp += section.getLength();
                }
                if(distanceTmp < distanceMem){
                    optimalPath = sectionList;
                }
            }
        }
        return optimalPath;
    }

    /**
     * try to find all the pathes between a station A and a type of station
     * low probability to work
     * @param stationA the station where we begin
     * @param stationWanted the type of station we want
     * @param sections the section we have before
     * @return all the pathes we have between the station A and the type of station wanted (it is a list of list of Section)
     */
    private List<List<Section>> findAllPath(Station stationA, StationType stationWanted, @Nullable List<Section> sections) {
        List<List<Section>> lists = new ArrayList<>();
        if(stationA.getType() == stationWanted){
            return null;
        } else {
            for(int i = 0; i < stationA.getConnections().size(); ++i){
                if(stationA.getConnections().get(i).getSectionRef() == sections.get(sections.size()-1));
                lists.add(new ArrayList<>());
                sections.add(stationA.getConnections().get(i).getSectionRef());
                lists.addAll(findAllPath(stationA.getConnections().get(i).getSectionRef().getStations().getRight(), stationWanted, sections));
                lists.get(i).add(stationA.getConnections().get(i).getSectionRef());
            }
            return lists;
        }
    }
}
