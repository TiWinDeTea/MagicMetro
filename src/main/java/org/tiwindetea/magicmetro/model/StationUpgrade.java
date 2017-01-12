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

/**
 * A station upgrade, can be apply to a station.
 */
public class StationUpgrade {

	private final static int CAPACITY_BONUS_DEFAULT = 5;
	private final int capacityBonus;

	/**
	 * Instantiates a new StationUpgrade.
	 */
	public StationUpgrade() {
		this.capacityBonus = CAPACITY_BONUS_DEFAULT;
	}

	/**
	 * Instantiates a new StationUpgrade.
	 *
	 * @param capacityBonus the capacity bonus
	 */
	public StationUpgrade(int capacityBonus) {
		this.capacityBonus = capacityBonus;
	}

	/**
	 * Gets capacity bonus.
	 *
	 * @return the capacity bonus
	 */
	public int getCapacityBonus() {
		return this.capacityBonus;
	}

}
