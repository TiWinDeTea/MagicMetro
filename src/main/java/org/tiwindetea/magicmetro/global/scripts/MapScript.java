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
 * TODO
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class MapScript {

	private final String mapName;

	private PriorityQueue<ElementChoiceScript> elementChoiceScripts = new PriorityQueue<>((o1, o2) ->
	  (int) (o1.apparitionTime.toMillis() - o2.apparitionTime.toMillis()));
	private PriorityQueue<StationScript> stationScripts = new PriorityQueue<>((o1, o2) ->
	  (int) (o1.apparitionTime.toMillis() - o2.apparitionTime.toMillis()));

	private MultiShape2d<Rectangle2d> water;
	//TODO: map description: water...

	/**
	 * Default constructor.
	 */
	public MapScript(String mapName, MultiShape2d<Rectangle2d> water) {
		this.mapName = mapName;
		this.water = water;
	}

	public String getMapName() {
		return this.mapName;
	}

	public MultiShape2d<Rectangle2d> getWater() {
		return this.water;
	}

	public void addElementChoiceScript(ElementChoiceScript elementChoiceScript) {
		this.elementChoiceScripts.add(elementChoiceScript);
	}

	public void addStationScript(StationScript stationScript) {
		this.stationScripts.add(stationScript);
	}

}
