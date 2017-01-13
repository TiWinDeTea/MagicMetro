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

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Singleton that dispatch events to the registered listeners.
 *
 * @author Maxime PINARD
 * @see Event
 * @see EventListener
 * @since 0.1
 */
public class EventDispatcher {

	private final Map<Class<? extends Event>, List<WeakReference<EventListener<? extends Event>>>> listenerMap = new HashMap<>(
	  8);
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	private EventDispatcher() {

	}

	private static class EventDispatcherHolder {
		private final static EventDispatcher INSTANCE = new EventDispatcher();
	}

	/**
	 * Gets instance.<p>
	 * The instance is created at the first call of this function.
	 *
	 * @return the instance
	 */
	public static EventDispatcher getInstance() {
		return EventDispatcherHolder.INSTANCE;
	}

	/**
	 * Add a listener related to an event class that will be notified when a event of the specified class is fired.
	 *
	 * @param <EventType> the event type
	 * @param eventClass  the event class
	 * @param listener    the listener
	 * @throws NullPointerException if <i>eventClass</i> or <i>listener</i> is <tt>null</tt>
	 */
	public <EventType extends Event> void addListener(@Nonnull Class<EventType> eventClass,
	                                                  @Nonnull EventListener<EventType> listener) {

		Objects.requireNonNull(eventClass, "eventClass is null");
		Objects.requireNonNull(listener, "listener is null");
		this.readWriteLock.writeLock().lock();
		if(this.listenerMap.containsKey(eventClass)) {
			this.listenerMap.get(eventClass).add(new WeakReference<>(listener));
		}
		else {
			List<WeakReference<EventListener<? extends Event>>> listeners = new ArrayList<>();
			listeners.add(new WeakReference<>(listener));
			this.listenerMap.put(eventClass, listeners);
		}
		this.readWriteLock.writeLock().unlock();
	}

	/**
	 * Remove a listener related to an event class.
	 *
	 * @param <EventType> the event type
	 * @param eventClass  the event class
	 * @param listener    the listener
	 * @throws NullPointerException if <i>eventClass</i> or <i>listener</i> is <tt>null</tt>
	 */
	public <EventType extends Event> void removeListener(@Nonnull Class<EventType> eventClass,
	                                                     @Nonnull EventListener<EventType> listener) {

		Objects.requireNonNull(eventClass, "eventClass is null");
		Objects.requireNonNull(listener, "listener is null");
		this.readWriteLock.writeLock().lock();
		if(this.listenerMap.containsKey(eventClass)) {
			this.listenerMap.get(eventClass).removeIf(listenerWeakReference -> listenerWeakReference.get() == listener);
		}
		this.readWriteLock.writeLock().unlock();
	}

	/**
	 * Fire an event. Notifies all listeners registered related to this event class.<p>
	 * Also remove the listeners that no longer exist from the list of registered listeners related to the event class.
	 *
	 * @param <EventType> the event type
	 * @param event       the event
	 */
	@SuppressWarnings("unchecked")
	public <EventType extends Event> void fire(EventType event) {

		List<WeakReference<EventListener<? extends Event>>> nullWeakReferences = new ArrayList<>();
		this.readWriteLock.readLock().lock();
		List<WeakReference<EventListener<? extends Event>>> listenerWeakReferenceList = this.listenerMap.get(event.getClass());
		List<WeakReference<EventListener<? extends Event>>> listenerWeakReferenceListCopy;
		if(listenerWeakReferenceList != null) {
			listenerWeakReferenceListCopy = new ArrayList<>(listenerWeakReferenceList);
			this.readWriteLock.readLock().unlock();
			for(WeakReference<EventListener<? extends Event>> listenerWeakReference : listenerWeakReferenceListCopy) {
				// noinspection unchecked
				EventListener<EventType> listener = (EventListener<EventType>) listenerWeakReference.get();
				if(listener == null) {
					nullWeakReferences.add(listenerWeakReference);
				}
				else {
					listener.onEvent(event);
				}
			}
			this.readWriteLock.writeLock().lock();
			listenerWeakReferenceList.removeAll(nullWeakReferences);
			this.readWriteLock.writeLock().unlock();
		}
		else {
			this.readWriteLock.readLock().unlock();
		}
	}

}
