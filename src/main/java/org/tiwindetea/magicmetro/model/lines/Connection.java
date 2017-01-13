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

package org.tiwindetea.magicmetro.model.lines;


import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.tiwindetea.magicmetro.global.util.Pair;
import org.tiwindetea.magicmetro.global.util.SimplePair;
import org.tiwindetea.magicmetro.model.Station;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
	private Station station;

	/**
	 * Instantiates a new Connection.
	 *
	 * @param position    the position
	 * @param subSections the subsections
	 */
	public Connection(@Nonnull Point2d position, @Nonnull Pair<SubSection, SubSection> subSections) {

		this.position = new Point2d(position);
		this.subSections = new SimplePair<>(subSections);
	}

	/**
	 * Instantiates a new Connection.
	 *
	 * @param position the position of the connection
	 */
	public Connection(@Nonnull Point2d position){
		this.position = new Point2d(position);
		this.subSections = new SimplePair<>(null, null);
	}

	/**
	 * Instantiates a new Connection.<p>
	 * Take the position of the station.
	 *
	 * @param station the station of the connection
	 */
	public Connection(@Nonnull Station station) {
		this.station = station;
		this.position = this.station.getPosition();

		this.subSections = new SimplePair<>(null, null);
	}

	/**
	 * Instantiates a new Connection.
	 *
	 * @param position        the position
	 * @param leftSubSection  the left subsection
	 * @param rightSubSection the right subsection
	 */
	public Connection(@Nonnull Point2d position,
	                  @Nullable SubSection leftSubSection,
	                  @Nullable SubSection rightSubSection) {

		this.position = new Point2d(position);
		this.subSections = new SimplePair<>(leftSubSection, rightSubSection);
	}

	public Connection(Point2d position, SubSection leftSubSection, SubSection rightSubSection, Station stationRef) {
		this.position = (position == null) ? new Point2d() : new Point2d(position);
		this.subSections = new SimplePair<>(leftSubSection, rightSubSection);
		this.station = stationRef;
	}

	/**
	 * Instantiates a new Connection.
	 *
	 * @param position   the position
	 * @param subSection the subsection (right and left)
	 */
	public Connection(@Nonnull Point2d position, @Nonnull SubSection subSection) {

		this.position = new Point2d(position);
		this.subSections = new SimplePair<>(subSection, subSection);
	}

	/**
	 * Gets position.
	 *
	 * @return the position
	 */
	@Nonnull
	public Point2d getPosition() {
		return new Point2d(this.position);
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
	@Nullable
	public SubSection getLeftSubSection() {
		return this.subSections.getLeft();
	}

	/**
	 * Gets right subsection.
	 *
	 * @return the right subsection
	 */
	@Nullable
	public SubSection getRightSubSection() {
		return this.subSections.getRight();
	}

	/**
	 * Gets if in station.
	 *
	 * @return true if in station, false otherwise
	 */
	public boolean isInStation() {
		return (this.station != null);
	}

	/**
	 * Gets station.
	 *
	 * @return the station if in station, null otherwise
	 */
	@Nullable
	public Station getStation() {

		return this.station;
	}

	/**
	 * Sets station.
	 *
	 * @param station the station
	 */
	public void setStation(@Nullable Station station) {
		this.station = station;
	}

	/**
	 * Sets the left and right subsection of the connection.
	 *
	 * @param subSectionLeft the left subsection
	 * @param subSectionRight the right subsection
	 */
	public void setSubSections(@Nullable SubSection subSectionLeft, @Nullable SubSection subSectionRight) {
		this.subSections.setLeft(subSectionLeft);
		this.subSections.setRight(subSectionRight);
	}

	/**
	 * Sets the right sub section.
	 *
	 * @param subSectionRight the right sub section
	 */
	public void setSubSectionRight(@Nullable SubSection subSectionRight) {
		this.subSections.setRight(subSectionRight);
	}

	/**
	 * Sets the left sub section.
	 *
	 * @param subSectionLeft the left sub section
	 */
	public void setSubSectionLeft(@Nullable SubSection subSectionLeft) {
		this.subSections.setLeft(subSectionLeft);
	}

}
