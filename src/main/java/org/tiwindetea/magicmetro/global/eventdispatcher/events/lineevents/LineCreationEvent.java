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

package org.tiwindetea.magicmetro.global.eventdispatcher.events.lineevents;

import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.tiwindetea.magicmetro.global.eventdispatcher.Event;

import javax.annotation.Nonnull;

/**
 * Event for the creation of a Line (addition of a section to an empty line).
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class LineCreationEvent implements Event {

	public final int lineId;
	public final int newSectionId;
	public final int fromStationId;
	public final int toStationId;
	public final Point2d middleConnectionPosition;

	/**
	 * Instantiates a new LineCreationEvent.
	 *
	 * @param lineId                   the line id
	 * @param newSectionId             the new section id
	 * @param fromStationId            the from station id
	 * @param toStationId              the to station id
	 * @param middleConnectionPosition the middle connection position
	 */
	public LineCreationEvent(int lineId,
	                         int newSectionId,
	                         int fromStationId,
	                         int toStationId,
	                         @Nonnull Point2d middleConnectionPosition) {
		this.lineId = lineId;
		this.newSectionId = newSectionId;
		this.fromStationId = fromStationId;
		this.toStationId = toStationId;
		this.middleConnectionPosition = middleConnectionPosition;
	}

}
