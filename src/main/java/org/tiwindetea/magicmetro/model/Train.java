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
import org.tiwindetea.magicmetro.model.lines.Connection;
import org.tiwindetea.magicmetro.model.lines.Line;
import org.tiwindetea.magicmetro.model.lines.Section;
import org.tiwindetea.magicmetro.view.TrainView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * A train, move by following connections and subsections of a line.<p>
 * Exchange passenger with stations depending on the passenger path.
 *
 * @author Julien Barbier
 * @since 0.2
 * @author Maxime PINARD
 * @since 0.1
 */
public class Train {

	public final int gameId;

	private static final int CAPACITY = 6;
	private static final int PASSENGER_MOVE_DELAY = 20; // TODO: choose a real value
	private static final double STATION_SLOW_DOWN_DISTANCE = 100000; // TODO: choose a real value

	private final double maxSpeed;
	private final double acceleration;
	private double speed;
	private Point2d position;
	private double rotation;

	private List<PassengerCar> passengerCars = new ArrayList<>();
	private final TrainView view;
	private final List<Passenger> passengers = new ArrayList<>(CAPACITY);

	private final TrainState movingState = new MovingState();
	private final TrainState atStationState = new AtStationState();
	private TrainState currentState = this.movingState;

	private Line line;

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
		//we verify if the train isFull to avoid loop
		if(isFull()){
			while(!passengerCar.getPassengers().isEmpty()){
				Passenger passenger = passengerCar.getPassengers().get(0);
				passenger.getStation().addPassenger(passenger);
				//we set the initial path to the passenger
				passengerCar.removePassenger(passenger);
			}
		} else {
			//we add the passenger in the other PassengerCar
			for (PassengerCar passengerCar1 : this.passengerCars){
				if(passengerCar1 != passengerCar){
					while (!passengerCar1.isFull() && !passengerCar.isEmpty()){
						//we send the other Passenger in the other PassengerCars
						passengerCar1.addPassenger(passengerCar.getPassengers().get(0));
						passengerCar.removePassenger(passengerCar.getPassengers().get(0));
					}
					if(passengerCar.isEmpty()){
						break;
					}
				}
			}
			//we verify if it's empty or not.
			if(!passengerCar.isEmpty()){
				//we send the passenger to the station of beginning
				for(Passenger passenger : passengerCar.getPassengers()){
					passengerCar.removePassenger(passenger);
					passenger.getStation().addPassenger(passenger);
				}
			}
		}
		this.passengerCars.remove(passengerCar);
	}

	/**
	 * Removed the last Passenger Car
	 *
	 * @return the PassengerCar removed
	 */
	public synchronized PassengerCar removePassengerCar(){
		PassengerCar tmp = passengerCars.get(passengerCars.size()-1);
		if(tmp != null) {
			removePassengerCar(tmp);
		}
		return tmp;
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

	private void setPosition(Point2d position) {
		this.position = position;
		this.view.setPosition(this.position);
	}

	public synchronized void toEmpty(){
		if(this.line != null) {
			for (PassengerCar passengerCar : passengerCars) {
				removePassengerCar(passengerCar);
			}
			while (!passengers.isEmpty()){
				Passenger passenger = passengers.get(0);
				removePassenger(passenger);
			}
		}
	}

	public void start(@Nonnull Line line) {
		this.line = line;
		this.view.setLine(this.line.gameId);
		Connection firstConnection = line.getLastConnections().getLeft();
		this.lastConnection = firstConnection;
		this.nextConnection = firstConnection.getRightSubSection().getOther(firstConnection);
		this.setPosition(firstConnection.getPosition());
		this.currentState = this.movingState;
		this.currentState.init();
		this.view.setVisible(true);
	}

	public void start(@Nonnull Section section) {
		if(section.getLine() == null) {
			throw new IllegalStateException("section is not in a line");
		}
		this.line = section.getLine();
		this.view.setLine(this.line.gameId);
		this.lastConnection = section.getMiddleConnection();
		this.nextConnection = section.getRightConnection();
		this.setPosition(section.getMiddleConnection().getPosition());
		this.currentState = this.movingState;
		this.currentState.init();
		this.view.setVisible(true);
	}

	public void stop() {
		this.line = null;
		this.view.setVisible(false);
	}

	@Nullable
	public Line getLine() {
		return this.line;
	}

	/**
	 * Add a passenger
	 *
	 * @param passenger the passenger to add
	 * @return true if the passenger added, false otherwise
	 */
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

	/**
	 * Remove passenger
	 *
	 * @param passenger the passenger to remove
	 * @return true if the passenger is removed, false otherwise
	 */
	private synchronized boolean removePassenger(Passenger passenger) {
		if(this.passengers.remove(passenger)) {
			this.view.removePassenger(passenger.getWantedStation());
			passenger.getStation().addPassenger(passenger);
			return true;
		}
		for(PassengerCar passengerCar : this.passengerCars) {
			if(passengerCar.removePassenger(passenger)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * make the train living
	 *
	 * @param lineRef the line where the train is
	 * @param connectionA the fromConnection
	 * @param connectionB toConnection
	 */
	public void makeItLivable(Line lineRef, Connection connectionA, Connection connectionB){
		setLastConnection(connectionA);
		setNextConnection(connectionB);
		setPosition(new Point2d(connectionA.getPosition().getX(), connectionB.getPosition().getY()));
		setLineRef(lineRef);
		if(position != null) {
			makeItVisible();
			currentState = movingState;
			currentState.init();
			//System.out.println(toString());
		}
	}

	/**
	 * set the Line reference
	 *
	 * @param lineRef the line
	 */
	private void setLineRef(Line lineRef) {
		this.line = lineRef;
	}

	/**
	 * get the Line
	 *
	 * @return the line where the train is
	 */
	protected Line getLineRef() {
		return line;
	}

	/**
	 * gets the next Connection
	 *
	 * @return the next Connection
	 */
	protected Connection getNextConnection() {
		return nextConnection;
	}

	/**
	 * set the last Connection
	 *
	 * @param lastConnection the connection
	 */
	public void setLastConnection(Connection lastConnection) {
		this.lastConnection = lastConnection;
	}

	/**
	 * sets the next Connection
	 *
	 * @param nextConnection the connection
	 */
	public void setNextConnection(Connection nextConnection) {
		this.nextConnection = nextConnection;
	}

	/**
	 * make the train visible
	 */
	public void makeItVisible(){
		this.view.setPosition(position);
		this.view.setVisible(true);
	}

	/**
	 * Moving the train
	 *
	 * @param move the vector of moving
	 */
	private void move(Vector2d move) {
		this.position.add(move);
		this.view.setPosition(this.position);
	}

	/**
	 * sets the rotation
	 *
	 * @param rotation the rotation
	 */
	private void setRotation(double rotation) {
		this.rotation = rotation;
		this.view.setRotation(this.rotation);
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
		public synchronized void init() {
			this.angleToNextConnection = Math.atan2(
			  Train.this.nextConnection.getPosition().getY() - Train.this.position.getY(),
			  Train.this.nextConnection.getPosition().getX() - Train.this.position.getX()
			);
			setRotation(Math.toDegrees(this.angleToNextConnection));

			Train.this.speed = 0;
		}

		@Override
		public synchronized void live() {
			Train.this.speed = Math.min(Train.this.speed += Train.this.acceleration, Train.this.maxSpeed);

			double dx = Train.this.speed * Math.cos(this.angleToNextConnection);
			double dy = Train.this.speed * Math.sin(this.angleToNextConnection);
			Point2d oldPosition = new Point2d(Train.this.position);
			move(new Vector2d(dx, dy));

			// if train was before connection and is now at or after connection
			if(((Math.min(oldPosition.getX(), Train.this.position.getX())
			  < Train.this.nextConnection.getPosition().getX())
			  && (Math.max(oldPosition.getX(), Train.this.position.getX())
			  >= Train.this.nextConnection.getPosition().getX()))
			  || ((Math.min(oldPosition.getY(), Train.this.position.getY())
			  < Train.this.nextConnection.getPosition().getY())
			  && (Math.max(oldPosition.getY(), Train.this.position.getY())
			  >= Train.this.nextConnection.getPosition().getY()))
			  ) {

				// put next connection in nextConnection (and nextConnection old value in lastConnection)
				Connection leftConnection = Train.this.nextConnection.getLeftSubSection()
				  .getOther(Train.this.nextConnection);
				Connection rightConnection = Train.this.nextConnection.getRightSubSection()
				  .getOther(Train.this.nextConnection);

				Connection tmpConnection = Train.this.nextConnection;
				if(leftConnection != Train.this.lastConnection) {
					Train.this.nextConnection = leftConnection;
				}
				else {
					Train.this.nextConnection = rightConnection;
				}
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

		@Override
		public synchronized void init() {
			this.delayCounter = 0;
			this.actualStation = Train.this.lastConnection.getStation();
			this.finishedOut = false;
			this.finishedIn = false;
		}

		@Override
		public synchronized void live() {
			if(this.delayCounter == 0) {
				if(!this.finishedOut) {
					this.finishedOut = true;
					for(Passenger passenger : Train.this.passengers) {
						Station station = null;
						Stack<Station> passengerPath = passenger.getPath();
						if(passengerPath != null) {
							if(!passengerPath.isEmpty()) { // for stack dark magic purpose
								station = passengerPath.peek();
							}
							if(station == this.actualStation) {
								passenger.getPath().pop();
								if(!passengerPath.isEmpty()) { // for stack dark magic purpose
									station = passengerPath.peek();
								}
								if(!Train.this.line.contains(station) || station == this.actualStation) {
									removePassenger(passenger);
									this.actualStation.addPassenger(passenger);
									this.finishedOut = false;
									break;
								}
							}
						}
					}
				}
				else {
					if(!this.finishedIn) {
						this.finishedIn = true;
						for(Passenger passenger : this.actualStation.getPassengers()) {
							Station station = null;
							Stack<Station> passengerPath = passenger.getPath();
							if(passengerPath != null) {
								if(!passengerPath.isEmpty()) { // for stack dark magic purpose
									station = passengerPath.peek();
								}
								if(Train.this.line.contains(station)) {
									if(Train.this.addPassenger(passenger)) {
										this.actualStation.removePassenger(passenger);
										this.finishedIn = false;
										break;
									}
								}
							}
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
