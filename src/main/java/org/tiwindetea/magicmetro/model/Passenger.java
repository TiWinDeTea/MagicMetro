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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.tiwindetea.magicmetro.model.Station.NB_STATION_TYPE;

/**
 * @author Julien Barbier
 * @since  0.1
 */
public class Passenger {

	private List<Connection> path;
	private Station station;
	private StationType stationWanted;

	/**
	 * Default constructor
	 */
	public Passenger() {
	    path = new ArrayList<>();
	    station = null;
        Random random = new Random();
        switch (random.nextInt(NB_STATION_TYPE)){
            case 0 : stationWanted = StationType.CIRCLE;
            break;
            case 1 : stationWanted =StationType.STAR;
            break;
            case 2: stationWanted = StationType.SQUARE;
            break;
            case 3: stationWanted = StationType.DIAMOND;
            break;
            case 4 :
                /*fall through*/
            default: stationWanted = StationType.TRIANGLE;
            break;
        }
	}

    /**
     * Constructor with the list of Station in aim to know where the passenger will spawn
     *
     * @param stations the list of all the station that are present in the map
     */
	public Passenger(List<Station> stations){
        path = new ArrayList<>();
        Random random = new Random();
        switch (random.nextInt(NB_STATION_TYPE)){
            case 0 : stationWanted = StationType.CIRCLE;
                break;
            case 1 : stationWanted =StationType.STAR;
                break;
            case 2: stationWanted = StationType.SQUARE;
                break;
            case 3: stationWanted = StationType.DIAMOND;
                break;
            case 4 :
                /*fall through*/
            default: stationWanted = StationType.TRIANGLE;
                break;
        }
        station = stations.get(random.nextInt(stations.size()));
    }

    /**
     * Getters of the station wanted
     *
     * @return the station wanted by the passenger
     */
	public StationType getWantedStation() {
		return stationWanted;
	}

    /**
     * Getters of the station where the passenger is
     *
     * @return the station where the passenger is
     */
	public Station getStation(){
		return station;
	}

    /**
     * Setter for the path of the Passenger
     *
     * @param path the path we want to set to the the Passenger
     */
	public void setPath(List<Connection> path){
	    this.path = path;
    }

}
