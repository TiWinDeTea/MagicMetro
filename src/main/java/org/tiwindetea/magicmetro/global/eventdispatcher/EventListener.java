/*
 * MIT License
 *
 * Copyright (c) 2016 Maxime Pinard
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

package org.tiwindetea.magicmetro.global.eventdispatcher;

/**
 * The EventListener interface. Classes that implement this interface can be added as listeners in the eventdispatcher
 * to be notified via the onEvent method when an event of the related class is fired.
 *
 * @param <EventType> the type of the events related to this listener
 * @author Maxime PINARD
 * @see Event
 * @see EventDispatcher
 * @since 0.1
 */
public interface EventListener<EventType extends Event> {

	/**
	 * Notifie the listener with an Event.<p>
	 * Method called by the eventdispatcher to notifie the listener when an Event is fired.
	 *
	 * @param event the event
	 */
	void onEvent(EventType event);

}
