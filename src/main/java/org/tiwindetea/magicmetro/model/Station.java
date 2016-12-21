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

import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.tiwindetea.magicmetro.model.lines.Connection;
import org.tiwindetea.magicmetro.view.StationView;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

/**
 * A station, contains connections and passengers.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class Station {

	private static final int STATION_DEFAULT_CAPACITY = 10; // TODO: real value
	private static final int STATION_FULL_DELAY = 10; // TODO: real value
	private static final int STATION_MAX_CONNECTIONS = 10; // TODO: real value

	public static final int NB_STATION_TYPE = 5;

	private final Point2d position;
	private final StationType type;
	private final StationView view;
	private int maxCapacity = STATION_DEFAULT_CAPACITY;

	private final List<Passenger> passengers = new LinkedList<>();
	private final List<Connection> connections = new LinkedList<>();

	/**
	 * Instantiates a new Station.
	 *
	 * @param position the position
	 * @param type     the type
	 * @param view     the view
	 */
	public Station(@Nonnull Point2d position, @Nonnull StationType type, @Nonnull StationView view) {
		this.position = position;
		this.type = type;
		this.view = view;
	}

	/**
	 * Add a passenger.
	 *
	 * @param passenger the passenger
	 */
	public void addPassenger(@Nonnull Passenger passenger) {
		this.passengers.add(passenger);
		this.view.addPassenger(passenger.getWantedStation());
		if(this.passengers.size() > this.maxCapacity) {
			this.view.warn(STATION_FULL_DELAY);
			//TODO: add station to a list in the game loop to check if the game is lost. (with starting time of the delay)
		}
	}


	/**
	 * Remove a passenger.
	 *
	 * @param passenger the passenger
	 * @return true if the passenger was removed, false if the passenger is not in the station
	 */
	public boolean removePassenger(@Nonnull Passenger passenger) {
		if(this.passengers.remove(passenger)) {
			this.view.removePassenger(passenger.getWantedStation());
			// if station was more than full before
			if(this.passengers.size() == this.maxCapacity) {
				this.view.unWard();
				//TODO: remove station from game loop check list.
			}
		}
		return false;
	}

	/**
	 * Gets the station type.
	 *
	 * @return the station type
	 */
	@Nonnull
	public StationType getType() {
		return this.type;
	}

	/**
	 * Add a connection.
	 *
	 * @param connection the connection
	 * @return true if the connection was added, false if the station already has the maximum connections
	 */
	public boolean addConnection(@Nonnull Connection connection) {
		if(this.connections.size() < STATION_MAX_CONNECTIONS) {
			this.connections.add(connection);
			return true;
		}
		return false;
	}

	/**
	 * Remove a connection.
	 *
	 * @param connection the connection
	 * @return true if the connection was removed, false if the connection is not in the station
	 */
	public boolean removeConnection(@Nonnull Connection connection) {
		return this.connections.remove(connection);
	}

    /**
     * getters of the connections inside the station
     *
     * @return the list of connections present inside the station
     */
	public List<Connection> getConnections(){
		return connections;
	}

}
