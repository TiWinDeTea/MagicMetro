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
public class PassengerCar {

    private final static int CAPACITY_PASSENGER_CARS_MAX = 6;
	private final int capacityMax;
	List<Passenger> passengers;


	/**
	 * Default constructor
	 */
	public PassengerCar() {
		capacityMax = CAPACITY_PASSENGER_CARS_MAX;
		passengers = new ArrayList<>();
	}

    /**
     * verify if the list is full or not
     * @return true if it's full, false else
     */
	boolean isFull(){
	    return passengers.size() >= capacityMax;
    }

    /**
     * verify if the list is empty or not
     * @return true if it's empty, false else
     */
    boolean isEmpty(){
	    return passengers.isEmpty();
    }

	/**
	 * TODO
	 *
	 * @param passenger TODO
	 */
	public void addPassenger(Passenger passenger) {
		if(!this.isFull()){
		    passengers.add(passenger);
        }
	}

	/**
	 * TODO
	 *
	 * @param passenger TODO
	 */
	public void removePassenger(Passenger passenger) {
		if(this.isEmpty()){
		    passengers.remove(passenger);
        }
	}

}
