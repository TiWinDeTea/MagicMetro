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

package org.tiwindetea.magicmetro.global.scripts;

import org.arakhne.afc.math.geometry.d2.d.MultiShape2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

import java.util.PriorityQueue;

/**
 * Describe the map ans all the events that happens during a game on this map.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class MapScript {

	public final String mapName;
	public final double mapWidth;
	public final double mapHeight;

	public final PriorityQueue<ElementChoiceScript> elementChoiceScripts = new PriorityQueue<>((o1, o2) ->
	  (int) (o1.apparitionTime.toMillis() - o2.apparitionTime.toMillis()));
	public final PriorityQueue<StationScript> stationScripts = new PriorityQueue<>((o1, o2) ->
	  (int) (o1.apparitionTime.toMillis() - o2.apparitionTime.toMillis()));

	public final MultiShape2d<Rectangle2d> water;

	public int initialLines = 0;
	public int initialTrains = 0;
	public int initialPassengerCars = 0;
	public int initialStationUpgrades = 0;
	public int initialTunnels = 0;

	/**
	 * Instantiates a new MapScript.
	 *
	 * @param mapName   the map name
	 * @param mapWidth  the map width
	 * @param mapHeight the map height
	 * @param water     the water
	 */
	public MapScript(String mapName, double mapWidth, double mapHeight, MultiShape2d<Rectangle2d> water) {
		this.mapName = mapName;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.water = water;
	}

	/**
	 * Instantiates a new MapScript, copy constructor.
	 *
	 * @param mapScript the map script to copy
	 */
	public MapScript(MapScript mapScript) {
		this.mapName = mapScript.mapName;
		this.mapWidth = mapScript.mapWidth;
		this.mapHeight = mapScript.mapHeight;
		this.elementChoiceScripts.addAll(mapScript.elementChoiceScripts);
		this.stationScripts.addAll(mapScript.stationScripts);
		this.water = mapScript.water.clone();
		this.initialLines = mapScript.initialLines;
		this.initialTrains = mapScript.initialTrains;
		this.initialPassengerCars = mapScript.initialPassengerCars;
		this.initialStationUpgrades = mapScript.initialStationUpgrades;
		this.initialTunnels = mapScript.initialTunnels;
	}

}
