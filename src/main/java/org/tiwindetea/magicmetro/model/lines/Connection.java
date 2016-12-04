/*
 * MIT License
 *
 * Copyright (c) 2016 Time Winder Dev Team (tiwindetea.org)
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

package org.tiwindetea.magicmetro.model.lines;

import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.tiwindetea.magicmetro.global.util.Pair;
import org.tiwindetea.magicmetro.global.util.SimplePair;
import org.tiwindetea.magicmetro.model.Station;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;

/**
 * A connection between two subsections.
 *
 * @author Maxime PINARD
 * @see SubSection
 * @see Section
 * @since 0.1
 */
public class Connection {

	private Point2d position;
	private SimplePair<SubSection> subSections;
	private WeakReference<Station> stationRef = new WeakReference<>(null);
	private WeakReference<Section> sectionRef = new WeakReference<>(null);

	/**
	 * Instantiates a new Connection.
	 *
	 * @param position    the position
	 * @param subSections the subsections
	 */
	public Connection(@Nullable Point2d position, @Nonnull Pair<SubSection, SubSection> subSections) {

		this.position = (position == null) ? new Point2d() : new Point2d(position);
		this.subSections = new SimplePair<>(subSections);
	}

	/**
	 * Instantiates a new Connection.
	 *
	 * @param position        the position
	 * @param leftSubSection  the left subsection
	 * @param rightSubSection the right subsection
	 */
	public Connection(@Nullable Point2d position,
	                  @Nonnull SubSection leftSubSection,
	                  @Nonnull SubSection rightSubSection) {

		this.position = (position == null) ? new Point2d() : new Point2d(position);
		this.subSections = new SimplePair<>(leftSubSection, rightSubSection);
	}

	/**
	 * Instantiates a new Connection.
	 *
	 * @param position   the position
	 * @param subSection the subsection (right and left)
	 */
	public Connection(@Nullable Point2d position, @Nonnull SubSection subSection) {

		this.position = (position == null) ? new Point2d() : new Point2d(position);
		this.subSections = new SimplePair<>(subSection, subSection);
	}

	/**
	 * Gets position.
	 *
	 * @return the position
	 */
	@Nonnull
	public Point2d getPosition() {
		return this.position;
	}

	/**
	 * Gets sub sections.
	 *
	 * @return the subsections
	 */
	@Nonnull
	public Pair<SubSection, SubSection> getSubSections() {
		return this.subSections;
	}

	/**
	 * Gets left subsection.
	 *
	 * @return the left subsection
	 */
	@Nonnull
	public SubSection getLeftSubSection() {
		return this.subSections.getLeft();
	}

	/**
	 * Gets right subsection.
	 *
	 * @return the right subsection
	 */
	@Nonnull
	public SubSection getRightSubSection() {
		return this.subSections.getRight();
	}

	/**
	 * Gets if in station.
	 *
	 * @return true if in station, false otherwise
	 */
	public boolean isInStation() {

		return (this.stationRef.get() != null);
	}

	/**
	 * Gets station reference if in station.
	 *
	 * @return the station reference if in station, null otherwise
	 */
	@Nullable
	public Station getStationRef() {

		return this.stationRef.get();
	}

	/**
	 * Sets station reference, the connection will consider it is in the station.
	 *
	 * @param stationRef the station reference
	 */
	public void setStationRef(@Nullable Station stationRef) {

		this.stationRef = new WeakReference<>(stationRef);
	}

	/**
	 * Gets section reference.
	 *
	 * @return the section reference
	 */
	@Nullable
	public Section getSectionRef() {

		return this.sectionRef.get();
	}

	/**
	 * Sets section reference.
	 *
	 * @param sectionRef the section reference
	 */
	public void setSectionRef(Section sectionRef) {

		this.sectionRef = new WeakReference<>(sectionRef);
	}

}
