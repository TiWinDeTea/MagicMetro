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
import org.tiwindetea.magicmetro.model.lines.Line;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class Train {

    private final static int MAX_SPEED_DEFAULT = 5;
	private int maxSpeed;
	private final TrainView view;
	private TrainState state;
	private Point2d position;
	private Line line;
	private PassengerCar primaryPassengerCar;
	private List<PassengerCar> optionalPassengerCars;

	/**
	 * Default constructor
	 */
	public Train() {
		maxSpeed = MAX_SPEED_DEFAULT;
		line = null;
		primaryPassengerCar = new PassengerCar();
		optionalPassengerCars = new ArrayList<>();
	}

	/**
	 * add a Passenger to the train
	 * try to add a passenger to the primaryPassengerCar and next to the optionalPassengerCars
	 * @param passenger the passenger we want to add to the train
	 */
	private void addPassenger(Passenger passenger) {
		if(!primaryPassengerCar.isFull()){
		    primaryPassengerCar.addPassenger(passenger);
        } else {
		    if(optionalPassengerCars.size() != 0) {
                int i = 0;
                while (optionalPassengerCars.get(i) != null && optionalPassengerCars.get(i).isFull()) {
                    ++i;
                }
                if (optionalPassengerCars.get(i) != null) {
                    optionalPassengerCars.get(i).addPassenger(passenger);
                }
            }
        }
	}

	/**
	 * remove a passager of the train
	 * try to remove the passenger first in the Bonus Passengercars if exist and next the primary passengercar
	 * @param passenger the passenger we want to remove
	 */
	private void removePassenger(Passenger passenger) {
	    int i = optionalPassengerCars.size()-1;
	    if(i != 0){
	        while (optionalPassengerCars.get(i).isEmpty()){
	            --i;
            }
            if(i != -1){
	            optionalPassengerCars.get(i).removePassenger(passenger);
            }
        } else {
	        if(!primaryPassengerCar.isEmpty()){
	            primaryPassengerCar.removePassenger(passenger);
            }
        }
	}

	/**
	 * TODO
	 */
	public void live() {
		state.live();
		switch (state.getStateChangement()){
            case 1 : state = new MovingState();
            break;
            case 2 : state = new AtStationState();
            break;
            default: break;
        }
	}

	/**
	 * TODO
	 */
	public interface TrainState {

		/**
		 * TODO
		 */
		public void live();

        public byte getStateChangement();
	}

	/**
	 * TODO
	 */
	public class MovingState implements TrainState {

	    private byte stateChangement;

		/**
		 * Default constructor
		 */
		public MovingState() {
		}

        @Override
        public void live() {

        }

        @Override
        public byte getStateChangement() {
            return stateChangement;
        }

    }

	/**
	 * TODO
	 */
	public class AtStationState implements TrainState {

	    private byte stateChangement;
		/**
		 * Default constructor
		 */
		public AtStationState() {
		}

        @Override
        public void live() {

        }

        @Override
        public byte getStateChangement() {
            return stateChangement;
        }
    }

}
