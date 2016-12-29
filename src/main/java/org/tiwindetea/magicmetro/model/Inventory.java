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

import org.tiwindetea.magicmetro.global.util.BooleanWrapper;
import org.tiwindetea.magicmetro.global.util.Pair;
import org.tiwindetea.magicmetro.model.lines.Line;
import org.tiwindetea.magicmetro.view.InventoryView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class Inventory {

	private final InventoryView view;

	private final List<Pair<Line, BooleanWrapper>> lines = new LinkedList<>(); // boolean is true if the line is used on the map

	private final LinkedList<StationUpgrade> stationUpgrades = new LinkedList<>();
	private final LinkedList<Train> trains = new LinkedList<>();
	private final LinkedList<PassengerCar> passengerCars = new LinkedList<>();

	//TODO: documentation

	public Inventory(@Nonnull InventoryView view) {
		this.view = view;
	}

	public void addStationUpgrade(@Nonnull StationUpgrade stationUpgrade) {
		this.stationUpgrades.add(stationUpgrade);
		this.view.setStationUpgrades(this.stationUpgrades.size());
	}

	public void addTrain(@Nonnull Train train) {
		this.trains.add(train);
		this.view.setTrains(this.trains.size());
	}

	public void addPassengerCar(@Nonnull PassengerCar passengerCar) {
		this.passengerCars.add(passengerCar);
		this.view.setPassengerCars(this.passengerCars.size());
	}

	public void addLine(Line line) {
		this.lines.add(new Pair<>(line, new BooleanWrapper(false)));
		this.view.setAvailableLine(line.gameId);
	}

	public boolean setUsed(@Nonnull Line line) {
		for(Pair<Line, BooleanWrapper> pair : this.lines) {
			if(pair.getLeft() == line) {
				pair.getRight().bool = true;
				this.view.setUsed(line.gameId);
				return true;
			}
		}
		return false;
	}

	public boolean setUnused(@Nonnull Line line) {
		for(Pair<Line, BooleanWrapper> pair : this.lines) {
			if(pair.getLeft() == line) {
				pair.getRight().bool = false;
				this.view.setUnused(line.gameId);
				return true;
			}
		}
		return false;
	}

	@Nullable
	public Train takeTrain() {
		Train train = this.trains.poll();
		this.view.setTrains(this.trains.size());
		return train;
	}

	@Nullable
	public PassengerCar takePassengerCar() {
		PassengerCar passengerCar = this.passengerCars.poll();
		this.view.setPassengerCars(this.passengerCars.size());
		return passengerCar;
	}

	@Nullable
	public StationUpgrade takeStationUpgrade() {
		StationUpgrade stationUpgrade = this.stationUpgrades.poll();
		this.view.setStationUpgrades(this.stationUpgrades.size());
		return stationUpgrade;
	}

}
