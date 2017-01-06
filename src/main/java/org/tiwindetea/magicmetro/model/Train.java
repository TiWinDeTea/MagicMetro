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
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.tiwindetea.magicmetro.global.util.Utils;
import org.tiwindetea.magicmetro.model.lines.Connection;
import org.tiwindetea.magicmetro.model.lines.Line;
import org.tiwindetea.magicmetro.model.lines.SubSection;
import org.tiwindetea.magicmetro.view.TrainView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * A train, move by following connections and subsections of a line.<p>
 * Exchange passenger with stations depending on the passenger path.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class Train {

	public final int gameId;

	private static final int CAPACITY = 6;
	private static final double STATION_SLOW_DOWN_DISTANCE = 15; // TODO: choose a real value
	private static final int PASSENGER_MOVE_DELAY = 7; // TODO: choose a real value

	private final double maxSpeed;
	private final double acceleration;
	private double speed;
	private Point2d position;
	private double rotation;
	private Line lineRef;

	private List<PassengerCar> passengerCars = new ArrayList<>();
	private final TrainView view;
	private final List<Passenger> passengers = new ArrayList<>(CAPACITY);

	private final TrainState movingState = new MovingState();
	private final TrainState atStationState = new MovingState();
	private TrainState currentState = this.movingState;

	private Connection lastConnection;
	private Connection nextConnection;

	/**
	 * Instantiates a new Train.
	 *
	 * @param maxSpeed     the max speed
	 * @param acceleration the acceleration
	 * @param view         the view
	 */
	public Train(double maxSpeed, double acceleration, TrainView view) {
		this.gameId = view.getGameId();
		this.maxSpeed = maxSpeed;
		this.acceleration = acceleration;
		this.view = view;
		this.view.setVisible(false);
	}

	/**
	 * Add a passenger car.
	 *
	 * @param passengerCar the passenger car
	 */
	public synchronized void addPassengerCar(PassengerCar passengerCar) {
		this.passengerCars.add(passengerCar);
	}

	/**
	 * Remove a passenger car.
	 *
	 * @param passengerCar the passenger car
	 */
	public synchronized void removePassengerCar(PassengerCar passengerCar) {
		//TODO: move passengers in other passengerCars or send them back to their initial station
		this.passengerCars.remove(passengerCar);
	}

	/**
	 * Determine if the train (and the passenger cars) is full.
	 *
	 * @return true if full, false otherwise
	 */
	public synchronized boolean isFull() {
		boolean full = this.passengers.size() == CAPACITY;
		if(full) {
			for(PassengerCar passengerCar : this.passengerCars) {
				full = passengerCar.isFull();
				if(!full) {
					break;
				}
			}
		}
		return full;
	}

	private synchronized boolean addPassenger(Passenger passenger) {
		if(this.passengers.size() < CAPACITY) {
			this.passengers.add(passenger);
			this.view.addPassenger(passenger.getWantedStation());
			return true;
		}
		for(PassengerCar passengerCar : this.passengerCars) {
			if(!passengerCar.isFull()) {
				passengerCar.addPassenger(passenger);
				return true;
			}
		}
		return false;
	}

	private synchronized boolean removePassenger(Passenger passenger) {
		if(this.passengers.remove(passenger)) {
			this.view.removePassenger(passenger.getWantedStation());
			return true;
		}
		for(PassengerCar passengerCar : this.passengerCars) {
			if(passengerCar.removePassenger(passenger)) {
				return true;
			}
		}
		return false;
	}

	public void makeItLivable(Line lineRef, Connection connectionA, Connection connectionB){
		setLastConnection(connectionA);
		setNextConnection(connectionB);
		setPosition(connectionA.getPosition());
		setLineRef(lineRef);
		if(position != null) {
			makeItVisible();
			currentState = movingState;
			currentState.init();
			//System.out.println(toString());
		}
	}

	public void setLineRef(Line lineRef) {
		this.lineRef = lineRef;
	}

	public void setLastConnection(Connection lastConnection) {
		this.lastConnection = lastConnection;
	}

	public void setNextConnection(Connection nextConnection) {
		this.nextConnection = nextConnection;
	}

	public void makeItVisible(){
		this.view.setPosition(position);
		this.view.setVisible(true);
	}

	public void setPosition(Point2d position) {
		this.position = position;
		this.view.setPosition(this.position);
	}

	private void move(Vector2d move) {
		this.position.add(move);
		this.view.setPosition(this.position);
	}

	private void setRotation(double rotation) {
		this.rotation = rotation;
		this.view.setRotation(this.rotation);
	}

	@Override
	public String toString() {
		return "Train{" +
				"gameId=" + gameId +
				", maxSpeed=" + maxSpeed +
				", acceleration=" + acceleration +
				", speed=" + speed +
				", position=" + position +
				", rotation=" + rotation +
				", lineRef=" + lineRef +
				", passengerCars=" + passengerCars +
				", view=" + view +
				", passengers=" + passengers +
				", movingState=" + movingState +
				", atStationState=" + atStationState +
				", currentState=" + currentState +
				", lastConnection=" + lastConnection +
				", nextConnection=" + nextConnection +
				'}';
	}

	/**
	 * Function called by the game loop to make the train live.<p>
	 * The train will move and exchange passengers with station on it way.
	 */
	public synchronized void live() {
		this.currentState.live();
	}

	private interface TrainState {

		/**
		 * Init the state.
		 */
		void init();

		/**
		 * Apply actions depending on the train state.
		 */
		void live();

	}

	private class MovingState implements TrainState {

		private double angleToNextConnection;

		@Override
		public void init() {
			this.angleToNextConnection = Math.atan2(
			  Train.this.nextConnection.getPosition().getY() - Train.this.position.getY(),
			  Train.this.nextConnection.getPosition().getX() - Train.this.position.getX()
			);
			setRotation(Math.toDegrees(this.angleToNextConnection));
		}

		@Override
		public void live() {
			Train.this.speed = Math.max(Train.this.speed += Train.this.acceleration, Train.this.maxSpeed);
			double dx = Train.this.speed * Math.cos(this.angleToNextConnection);
			double dy = Train.this.speed * Math.sin(this.angleToNextConnection);

			// if next connection is in station
			if(Train.this.nextConnection.isInStation()) {

				// if closer to slow down distance
				if(Train.this.position.getDistance(Train.this.nextConnection.getPosition()) > STATION_SLOW_DOWN_DISTANCE) {
					dx = Utils.map(dx, 0, STATION_SLOW_DOWN_DISTANCE, 0, dx);
					dy = Utils.map(dy, 0, STATION_SLOW_DOWN_DISTANCE, 0, dy);
				}
			}
			Point2d oldPosition = new Point2d(Train.this.position);
			move(new Vector2d(dx, dy));

			// if train was before connection and is now at or after connection
			if(Math.min(oldPosition.getX(), Train.this.position.getX()) < Train.this.nextConnection.getPosition()
			  .getX() && Math.max(oldPosition.getX(),
			  Train.this.position.getX()) >= Train.this.nextConnection.getPosition().getX()) {

				// put next connection in nextConnection (and nextConnection old value in lastConnection)
				SubSection nextSubsection = Train.this.nextConnection.getLeftSubSection()
				  .contains(Train.this.nextConnection) ? Train.this.nextConnection
				  .getLeftSubSection() : Train.this.nextConnection.getRightSubSection();
				Connection tmpConnection = Train.this.nextConnection;
				Train.this.nextConnection = nextSubsection.getOther(Train.this.nextConnection);
				Train.this.lastConnection = tmpConnection;

				Train.this.currentState.init();

				// if at station, change state
				if(Train.this.lastConnection.isInStation()) {
					Train.this.currentState = Train.this.atStationState;
					Train.this.currentState.init();
				}
			}
		}
	}

	private class AtStationState implements TrainState {

		private int delayCounter;
		private Station actualStation;
		private boolean finishedOut; // from train to station
		private boolean finishedIn; // from station to train
		private Queue<Passenger> passengersOut;
		private Queue<Passenger> passengersIn;

		@Override
		public void init() {
			this.delayCounter = 0;
			this.actualStation = Train.this.lastConnection.getStationRef();
			this.finishedOut = false;
			this.finishedIn = false;

			this.passengersOut = new LinkedList<>();
			//TODO: problem: only find passenger for which the final destination is StationType
			//TODO: list of passenger that want to go to the Station -> depend on path-finding
			for(Passenger passenger : Train.this.passengers) {
				if(passenger.getWantedStation() == this.actualStation.getType()) {
					this.passengersOut.add(passenger);
				}
			}
			for(PassengerCar passengerCar : Train.this.passengerCars) {
				this.passengersOut.addAll(passengerCar.getPassengers(this.actualStation.getType()));
			}

			this.passengersIn = new LinkedList<>();
			//TODO: list of passenger that want to take the train -> depend on path-finding
		}

		@Override
		public void live() {

			if(this.delayCounter == 0) {
				if(!this.finishedOut) {
					Passenger passenger = this.passengersOut.peek();
					if(passenger != null) {
						removePassenger(passenger); // boolean check ?
						this.actualStation.addPassenger(passenger);
					}
					else {
						this.finishedOut = true;
					}
				}
				else {
					if(!this.finishedIn) {
						Passenger passenger = this.passengersIn.peek();
						if(passenger != null) {
							if(addPassenger(passenger)) {
								this.actualStation.removePassenger(passenger);
							}
							else {
								this.finishedIn = true;
							}
						}
						else {
							this.finishedIn = true;
						}
					}
				}
			}

			++this.delayCounter;
			this.delayCounter = this.delayCounter % PASSENGER_MOVE_DELAY;

			// if finishedOut to move passengers, change state
			if(this.finishedOut && this.finishedIn) {
				Train.this.currentState = Train.this.movingState;
				Train.this.currentState.init();
			}
		}
	}

}
