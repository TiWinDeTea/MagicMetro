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

import org.tiwindetea.magicmetro.global.TimeManager;
import org.tiwindetea.magicmetro.global.eventdispatcher.EventDispatcher;
import org.tiwindetea.magicmetro.global.eventdispatcher.EventListener;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.GameExitEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.InventoryElementAdditionEvent;
import org.tiwindetea.magicmetro.global.scripts.ElementChoiceScript;
import org.tiwindetea.magicmetro.global.scripts.MapScript;
import org.tiwindetea.magicmetro.global.scripts.StationScript;
import org.tiwindetea.magicmetro.global.util.Utils;
import org.tiwindetea.magicmetro.model.lines.Line;
import org.tiwindetea.magicmetro.view.ViewManager;

import java.time.Duration;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO
 */
public class GameManager implements StationManager, LineManager {

	private static final int LOOP_DELAY_MILLIS = 10; // number of milliseconds (of the TimeManager) between two game tick

	private final ViewManager viewManager;
	private final GameMap gameMap;
	private final Inventory inventory;
	private final MapScript mapScript;
	private final EventListener<InventoryElementAdditionEvent> onInventoryElementAdditionEvent;
	private final EventListener<GameExitEvent> onGameExitEvent;

	private final PriorityQueue<Station> warnedStations = new PriorityQueue<>((o1, o2) ->
	  (int) (o1.getWarnEnd() - o2.getWarnEnd()));

	private final long refreshDelay;
	private boolean gameEnded = false;
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final Runnable gameLoop = new Runnable() {
		@Override
		public void run() {
			long nextLoop = 0;
			int test = 0;
			while(!GameManager.this.gameEnded) {
				long currentTime = TimeManager.getInstance().getTimeAsMillis();
				while(nextLoop < currentTime) {
					// trains move
					for(Train train : GameManager.this.gameMap.getTrainsCopy()) {
						train.live();
					}
					// bonus choice
					ElementChoiceScript elementChoiceScript = GameManager.this.mapScript.elementChoiceScripts.peek();
					if((elementChoiceScript != null) && (elementChoiceScript.apparitionTime.toMillis() < currentTime)) {
						GameManager.this.mapScript.elementChoiceScripts.poll();
						GameManager.this.viewManager.askElementChoice(elementChoiceScript.elementScripts);
					}

					// stations apparition
					StationScript stationScript = GameManager.this.mapScript.stationScripts.peek();
					if((stationScript != null) && (stationScript.apparitionTime.toMillis() < currentTime)) {
						GameManager.this.mapScript.stationScripts.poll();
						Station station = new Station(stationScript.position,
						  stationScript.type,
						  GameManager.this.viewManager.createStationView(stationScript.type),
						  GameManager.this);
						GameManager.this.gameMap.addStation(station);
					}
					// warned stations check
					for(Station station : GameManager.this.warnedStations) {
						station.setWarnValue(Utils.map(
						  currentTime,
						  station.getWarnStart(),
						  station.getWarnEnd(),
						  0,
						  1
						));
					}
					Station station = GameManager.this.warnedStations.peek();
					if((station != null) && station.getWarnEnd() < currentTime) {
						//TODO: loose the game
						System.out.println("Game end");
						GameManager.this.gameEnded = true;
					}
					// passenger apparition
					//FIXME: test
					++test;
					if(test == 200) {
						GameManager.this.gameMap.addPassengerToStation();
						test = 0;
					}

					nextLoop += LOOP_DELAY_MILLIS;
					//TODO
				}

				try {
					Thread.sleep(GameManager.this.refreshDelay);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	/**
	 * Default constructor
	 */
	public GameManager(ViewManager viewManager, MapScript mapScript) {
		this.refreshDelay = TimeManager.getInstance().getRefreshDelay();

		this.mapScript = mapScript;

		this.viewManager = viewManager;
		this.inventory = new Inventory(this.viewManager.getInventoryView());
		this.gameMap = new GameMap(this.inventory);

		this.viewManager.setMapSize(this.mapScript.mapWidth, this.mapScript.mapHeight);
		this.viewManager.setWater(this.mapScript.water);
		StationScript stationScript = this.mapScript.stationScripts.peek();
		while((stationScript != null) && (stationScript.apparitionTime == Duration.ZERO)) {
			this.mapScript.stationScripts.poll();
			Station station = new Station(stationScript.position,
			  stationScript.type,
			  viewManager.createStationView(stationScript.type),
			  this);
			this.gameMap.addStation(station);
			stationScript = this.mapScript.stationScripts.peek();
		}

		for(int i = 0; i < this.mapScript.initialLines; ++i) {
			Line line = new Line(this.viewManager.createLineView(), this);
			this.inventory.addLine(line);
			this.gameMap.addLine(line);
		}

		for(int i = 0; i < this.mapScript.initialTrains; ++i) {
			this.inventory.addTrain(new Train(
			  TrainType.NORMAL.maxSpeed,
			  TrainType.NORMAL.acceleration,
			  viewManager.createTrainView(TrainType.NORMAL
			  )));
		}

		for(int i = 0; i < this.mapScript.initialPassengerCars; ++i) {
			this.inventory.addPassengerCar(new PassengerCar(this.viewManager.createPassengerCarView()));
		}

		for(int i = 0; i < this.mapScript.initialTunnels; ++i) {
			this.inventory.addTunnel();
		}

		for(int i = 0; i < this.mapScript.initialStationUpgrades; ++i) {
			this.inventory.addStationUpgrade(new StationUpgrade());
		}

		this.executorService.submit(this.gameLoop);
		this.executorService.shutdown();
		//TODO

		this.onInventoryElementAdditionEvent = new EventListener<InventoryElementAdditionEvent>() {
			@Override
			public void onEvent(InventoryElementAdditionEvent event) {
				switch(event.elementScript.type) {
				case TRAIN:
					for(int i = 0; i < event.number; ++i) {
						GameManager.this.inventory.addTrain(new Train(
						  TrainType.NORMAL.maxSpeed,
						  TrainType.NORMAL.acceleration,
						  GameManager.this.viewManager.createTrainView(TrainType.NORMAL
						  )));
					}
					break;
				case STATION_UPGRADE:
					for(int i = 0; i < event.number; ++i) {
						GameManager.this.inventory.addStationUpgrade(new StationUpgrade());
					}
					break;
				case TUNNEL:
					for(int i = 0; i < event.number; ++i) {
						GameManager.this.inventory.addTunnel();
					}
					break;
				case LINE:
					for(int i = 0; i < event.number; ++i) {
						Line line = new Line(GameManager.this.viewManager.createLineView(), GameManager.this);
						GameManager.this.inventory.addLine(line);
						GameManager.this.gameMap.addLine(line);
					}
					break;
				case PASSENGER_CAR:
					for(int i = 0; i < event.number; ++i) {
						GameManager.this.inventory.addPassengerCar(new PassengerCar(GameManager.this.viewManager.createPassengerCarView()));
					}
					break;
				}
			}
		};
		EventDispatcher.getInstance().addListener(InventoryElementAdditionEvent.class,
		  this.onInventoryElementAdditionEvent);

		this.onGameExitEvent = new EventListener<GameExitEvent>() {
			@Override
			public void onEvent(GameExitEvent event) {
				GameManager.this.gameEnded = true;
			}
		};
		EventDispatcher.getInstance().addListener(GameExitEvent.class, this.onGameExitEvent);
	}

	@Override
	public void addWarnedStation(Station station) {
		this.warnedStations.add(station);
	}

	@Override
	public void removeWarnedStation(Station station) {
		this.warnedStations.remove(station);
	}

	@Override
	public void lineDeleted(Line deletedLine) {
		for(Train train : this.gameMap.getTrainsCopy()) {
			if(train.getLine() == deletedLine) {
				this.gameMap.removeTrain(train);
				this.inventory.addTrain(train);
				train.stop();
				//TODO: manage passengers
			}
		}
	}
}
