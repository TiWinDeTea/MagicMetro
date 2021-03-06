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
 * Event for the inner extension of a Line (addition of a station not at the end or the beginning of the line).
 *
 * @author Maxime PINARD
 * @author Julien Barbier
 * @since 0.1
 */
public class LineInnerExtensionEvent implements Event {

	public final int lineId;
	public final int oldSectionId;
	public final int newLeftSectionId;
	public final int newRightSectionId;
	public final int fromStationId;
	public final int addedStationId;
	public final int toStationId;
	public final Point2d middleLeftConnectionPosition;
	public final Point2d middleRightConnectionPosition;

	/**
	 * Instantiates a new LineExtensionEvent.
	 *
	 * @param lineId                        the line id
	 * @param oldSectionId                  the old section id
	 * @param newLeftSectionId              the new left section id
	 * @param newRightSectionId             the new right section id
	 * @param fromStationId                 the from station id
	 * @param addedStationId                the added station id
	 * @param toStationId                   the to station id
	 * @param middleLeftConnectionPosition  the middle left connection position
	 * @param middleRightConnectionPosition the middle right connection position
	 */
	public LineInnerExtensionEvent(int lineId,
	                               int oldSectionId,
	                               int newLeftSectionId,
	                               int newRightSectionId,
	                               int fromStationId,
	                               int addedStationId,
	                               int toStationId,
	                               @Nonnull Point2d middleLeftConnectionPosition,
	                               @Nonnull Point2d middleRightConnectionPosition) {
		this.lineId = lineId;
		this.oldSectionId = oldSectionId;
		this.newLeftSectionId = newLeftSectionId;
		this.newRightSectionId = newRightSectionId;
		this.fromStationId = fromStationId;
		this.addedStationId = addedStationId;
		this.toStationId = toStationId;
		this.middleLeftConnectionPosition = middleLeftConnectionPosition;
		this.middleRightConnectionPosition = middleRightConnectionPosition;
	}

}
