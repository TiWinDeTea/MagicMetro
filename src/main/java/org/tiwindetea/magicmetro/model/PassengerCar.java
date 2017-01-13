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

import org.tiwindetea.magicmetro.view.PassengerCarView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A passenger car, contains passengers and is moved by a train.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class PassengerCar {

	public final int gameId;

	private static final int CAPACITY = 6;

	private final PassengerCarView view;
	private final List<Passenger> passengers = new ArrayList<>(CAPACITY);
	private int capacity;

	/**
	 * Instantiates a new PassengerCar.
	 *
	 * @param view the passenger view
	 */
	public PassengerCar(PassengerCarView view) {
		this.gameId = view.getGameId();
		this.view = view;
	}

	/**
	 * Determine if the passenger car is full.
	 *
	 * @return true if full, false otherwise
	 */
	public boolean isFull() {
		return this.passengers.size() == CAPACITY;
	}

	/**
	 * Gets a passenger with a given wanted station.
	 *
	 * @param wantedStation the station wanted by the passenger
	 * @return the passenger if it exist, null otherwise
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
	 * Gets all passenger with a given wanted station.
	 *
	 * @param wantedStation the station wanted by the passengers
	 * @return the passengers
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
	 * Add a passenger.
	 *
	 * @param passenger the passenger
	 * @return true if the passenger was added, false if the passenger car is full
	 */
	public boolean addPassenger(@Nonnull Passenger passenger) {
		if(this.passengers.size() < CAPACITY) {
			this.passengers.add(passenger);
			this.view.addPassenger(passenger.getWantedStation());
			return true;
		}
		return false;
	}

	/**
	 * Remove a passenger.
	 *
	 * @param passenger the passenger
	 * @return true if the passenger was removed, false if the passenger is not in the passenger car
	 */
	public boolean removePassenger(@Nonnull Passenger passenger) {
		if(this.passengers.remove(passenger)) {
			this.view.removePassenger(passenger.getWantedStation());
			return true;
		}
		return false;
	}

}
