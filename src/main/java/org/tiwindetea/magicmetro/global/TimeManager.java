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

package org.tiwindetea.magicmetro.global;

import org.tiwindetea.magicmetro.global.eventdispatcher.EventDispatcher;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.timeevents.TimePauseEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.timeevents.TimeResetEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.timeevents.TimeSpeedChangeEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.timeevents.TimeStartEvent;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.timeevents.TimeStopEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Singleton that manage a time counter in his own thread.
 * Time minimum speed is 0.1
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class TimeManager {

	private final AtomicLong actualTime = new AtomicLong(0); // millis
	private final AtomicLong added = new AtomicLong(10);
	private final AtomicBoolean paused = new AtomicBoolean(true);
	private final AtomicBoolean stopped = new AtomicBoolean(false);
	private final long delayMillis = 10;
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final Runnable timeLoop = new Runnable() {
		@Override
		public void run() {
			while(!TimeManager.this.stopped.get()) {
				if(!TimeManager.this.paused.get()) {
					TimeManager.this.actualTime.addAndGet(TimeManager.this.added.get());
				}
				try {
					Thread.sleep(TimeManager.this.delayMillis);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	private TimeManager() {
		this.executorService.submit(this.timeLoop);
	}

	private static class TimeManagerHolder {
		private final static TimeManager INSTANCE = new TimeManager();
	}

	/**
	 * Gets instance.<p>
	 * The instance is created at the first call of this function.
	 *
	 * @return the instance
	 */
	public static TimeManager getInstance() {
		return TimeManager.TimeManagerHolder.INSTANCE;
	}

	/**
	 * Start or restart the time.
	 */
	public void start() {
		boolean started = false;
		if(this.stopped.get()) {
			started = true;
			this.stopped.set(false);
			this.executorService.submit(this.timeLoop);
		}
		if(this.paused.get()) {
			started = true;
			this.paused.set(false);
		}
		if(started) {
			EventDispatcher.getInstance().fire(new TimeStartEvent());
		}
	}

	/**
	 * Pause the time.<p>
	 * The time thread keep running.
	 */
	public void pause() {
		if(!this.paused.get()) {
			this.paused.set(true);
			EventDispatcher.getInstance().fire(new TimePauseEvent());
		}
	}

	/**
	 * Reset the time.
	 */
	public void reset() {
		this.actualTime.set(0);
		EventDispatcher.getInstance().fire(new TimeResetEvent());
	}

	/**
	 * Sets time speed.
	 *
	 * @param speed the speed
	 */
	public void setSpeed(double speed) {
		long oldSpeed = this.added.get() / 10;
		long newSpeed = Math.max((long) (10 * speed), (long) 1);
		if(newSpeed != oldSpeed) {
			this.added.set(newSpeed);
			EventDispatcher.getInstance().fire(new TimeSpeedChangeEvent(oldSpeed, newSpeed));
		}
	}

	/**
	 * Gets time as millis.
	 *
	 * @return the time as millis
	 */
	public long getTimeAsMillis() {
		return this.actualTime.get();
	}

	/**
	 * Gets time as seconds.
	 *
	 * @return the time as seconds
	 */
	public long getTimeAsSeconds() {
		return (Math.round(this.actualTime.get() / 1000.0));
	}

	/**
	 * Stop the time.<p>
	 * The time thread stop running.
	 */
	public void stop() {
		if(!this.stopped.get()) {
			this.stopped.set(true);
			EventDispatcher.getInstance().fire(new TimeStopEvent());
		}
	}

	/**
	 * End the TimeManager, cannot be reuse after this function call.
	 */
	public void end() {
		stop();
		this.executorService.shutdown();
	}

	/**
	 * Gets the refresh delay as milliseconds
	 *
	 * @return the refresh delay
	 */
	public long getRefreshDelay() {
		return this.delayMillis;
	}

}
