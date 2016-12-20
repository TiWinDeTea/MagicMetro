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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO
 */
public class PassengerCar {

	private static final int CAPACITY = 6;

	private final List<Passenger> passengers = new ArrayList<>(CAPACITY);
	private int capacity;

	public boolean isFull() {
		return this.passengers.size() == CAPACITY;
	}

	/**
	 * TODO
	 *
	 * @param wantedStation
	 * @return
	 */
	@Nullable
	public Passenger getPassenger(@Nonnull StationType wantedStation) {
		Passenger wantedPassenger = null;
		for(Passenger passenger : this.passengers) {
			if(passenger.getWantedStation() == wantedStation) {
				wantedPassenger = passenger;
				break;
			}
		}
		return wantedPassenger;
	}

	/**
	 * TODO
	 *
	 * @param wantedStation
	 * @return
	 */
	@Nonnull
	public List<Passenger> getPassengers(@Nonnull StationType wantedStation) {
		List<Passenger> wantedPassengers = new LinkedList<>();
		for(Passenger passenger : this.passengers) {
			if(passenger.getWantedStation() == wantedStation) {
				wantedPassengers.add(passenger);
			}
		}
		return wantedPassengers;
	}

	/**
	 * Default constructor
	 */
	public PassengerCar() {

	}

	/**
	 * TODO
	 *
	 * @param passenger TODO
	 */
	public boolean addPassenger(@Nonnull Passenger passenger) {
		// TODO
	}

	/**
	 * TODO
	 *
	 * @param passenger TODO
	 */
	public boolean removePassenger(@Nonnull Passenger passenger) {
		// TODO
	}

}
