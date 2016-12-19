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


import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class Station {

	private int maxCapacity;
	private StationView view;
	private List<Passenger> passengers;

	/**
	 * Default constructor
	 */
	public Station() {
		passengers = new ArrayList<>();
		maxCapacity = 6;
	}

	public Station(int maxCapacity){
		this.maxCapacity = maxCapacity;
		passengers = new ArrayList<>(maxCapacity);
	}

	/**
	 * Add a passenger inside the list of passengers
	 *
	 * @param passenger the passenger we want to add
	 */
	public void addPassenger(Passenger passenger) {
		if(passengers.size() < maxCapacity){
			passengers.add(passenger);
		}
	}

	/**
	 * remove passenger of a station
	 *
	 * @param passenger the passenger we want to remove of the station
	 */
	public void removePassenger(Passenger passenger) {
		if(passengers.size() > 0){
			passengers.remove(passenger);
		}
	}

	void setView(StationView stationView){
		this.view = stationView;
	}

}
