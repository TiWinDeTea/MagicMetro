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

package org.tiwindetea.magicmetro.view;

import org.tiwindetea.magicmetro.model.StationType;
import org.tiwindetea.magicmetro.model.TrainType;

/**
 * TODO
 */
public class ViewManager {

	private final Skin skin = new Skin();

	public ViewManager() {
	}

	public TrainView createTrainView(TrainType type) {
		return new ConcreteTrainView(this.skin.newTrainView(type),
		  Skin.TRAIN_VIEW_WIDTH, Skin.TRAIN_VIEW_HEIGHT, this.skin.getTrainPassengerPositions(), this.skin);
	}

	public StationView createStationView(StationType type) {
		return new ConcreteStationView(this.skin.newStationView(type),
		  Skin.STATION_VIEW_WIDTH,
		  Skin.STATION_VIEW_HEIGHT,
		  this.skin);
	}

	public void createLineView() {
		//TODO
	}

}
