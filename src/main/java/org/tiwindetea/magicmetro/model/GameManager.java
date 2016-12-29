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
import org.tiwindetea.magicmetro.global.eventdispatcher.events.TimePauseEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.TimeSpeedChangeEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.TimeStartEvent;
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
public class GameManager implements StationManager {

	private static final int LOOP_DELAY_MILLIS = 10; // number of milliseconds (of the TimeManager) between two game tick

	private final ViewManager viewManager;
	private final GameMap gameMap;
	private final Inventory inventory;
	private final MapScript mapScript;
	private final EventListener<TimeStartEvent> onTimeStartEvent = event -> {
		//TODO
	};
	private final EventListener<TimePauseEvent> onTimePauseEvent = event -> {
		//TODO
	};
	private final EventListener<TimeSpeedChangeEvent> onTimeSpeedChangeEvent = event -> {
		//TODO
	};

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

		EventDispatcher.getInstance().addListener(TimeStartEvent.class, this.onTimeStartEvent);
		EventDispatcher.getInstance().addListener(TimePauseEvent.class, this.onTimePauseEvent);
		EventDispatcher.getInstance().addListener(TimeSpeedChangeEvent.class, this.onTimeSpeedChangeEvent);

		this.viewManager = viewManager;
		this.gameMap = new GameMap();

		this.viewManager.setMapSize(mapScript.mapWidth, mapScript.mapHeight);
		this.viewManager.setWater(this.mapScript.water);
		StationScript stationScript = this.mapScript.stationScripts.peek();
		while((stationScript != null) && (stationScript.apparitionTime == Duration.ZERO)) {
			mapScript.stationScripts.poll();
			Station station = new Station(stationScript.position,
			  stationScript.type,
			  viewManager.createStationView(stationScript.type),
			  this);
			this.gameMap.addStation(station);
			stationScript = this.mapScript.stationScripts.peek();
		}

		//TODO: initial lines
		this.inventory = new Inventory(this.viewManager.getInventoryView());
		for(int i = 0; i < mapScript.initialLines; ++i) {
			Line line = new Line();
			this.inventory.addLine(line);
		}

		this.executorService.submit(this.gameLoop);
		this.executorService.shutdown();
		//TODO
	}

	@Override
	public void addWarnedStation(Station station) {
		this.warnedStations.add(station);
	}

	@Override
	public void removeWarnedStation(Station station) {
		this.warnedStations.remove(station);
	}

}
