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

import org.tiwindetea.magicmetro.global.eventdispatcher.EventDispatcher;
import org.tiwindetea.magicmetro.global.eventdispatcher.EventListener;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.TimePauseEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.TimeSpeedChangeEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.TimeStartEvent;
import org.tiwindetea.magicmetro.global.scripts.MapScript;
import org.tiwindetea.magicmetro.view.ViewManager;

/**
 * TODO
 */
public class GameManager {

	private final ViewManager viewManager;
	private final GameMap gameMap;
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

	/**
	 * Default constructor
	 */
	public GameManager(ViewManager viewManager, MapScript mapScript) {
		this.mapScript = mapScript;
		EventDispatcher.getInstance().addListener(TimeStartEvent.class, this.onTimeStartEvent);
		EventDispatcher.getInstance().addListener(TimePauseEvent.class, this.onTimePauseEvent);
		EventDispatcher.getInstance().addListener(TimeSpeedChangeEvent.class, this.onTimeSpeedChangeEvent);

		this.viewManager = viewManager;
		this.gameMap = new GameMap();

		this.viewManager.setWater(this.mapScript.getWater());
		//TODO
	}

}
