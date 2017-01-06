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
	private Station stationRef;

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
	 * Instantiates a new Connections with the position
	 *
	 * @param position the position of the connection
	 */
	public Connection(@Nonnull Point2d position){
		this.position = position;
		subSections = new SimplePair<>(null, null);
	}

	/**
	 * Instantiates a new Connection that are on a station
	 *
	 * @param position the position of this connection
	 * @param stationRef the station of the connection
	 */
	public Connection(Point2d position, Station stationRef) {
		this.position = position;
		setStationRef(stationRef);
		this.subSections = new SimplePair<>(null, null);
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
	public Pair<SubSection, SubSection> getSubSections() {
		return this.subSections;
	}

	/**
	 * Gets left subsection.
	 *
	 * @return the left subsection
	 */
	public SubSection getLeftSubSection() {
		return this.subSections.getLeft();
	}

	/**
	 * Gets right subsection.
	 *
	 * @return the right subsection
	 */
	public SubSection getRightSubSection() {
		return this.subSections.getRight();
	}

	/**
	 * Gets if in station.
	 *
	 * @return true if in station, false otherwise
	 */
	public boolean isInStation() {

		return (this.stationRef != null);
	}

	/**
	 * Gets station reference if in station.
	 *
	 * @return the station reference if in station, null otherwise
	 */
	@Nullable
	public Station getStationRef() {

		return this.stationRef;
	}

	/**
	 * Sets station reference, the connection will consider it is in the station.
	 *
	 * @param stationRef the station reference
	 */
	public void setStationRef(@Nullable Station stationRef) {
		stationRef.addConnection(this);
		this.stationRef = stationRef;
	}

	/**
	 * Sets the left and right subsection to the connection.
	 *
	 * @param subSectionLeft the left subsection
	 * @param subSectionRight the right subsection
	 */
	public void setSubSections(SubSection subSectionLeft, SubSection subSectionRight){
		this.subSections.setLeft(subSectionLeft);
		this.subSections.setRight(subSectionRight);
	}

	/**
	 * Sets the right subsection
	 *
	 * @param subSectionRight the right subSection
	 */
	public void setSubSectionRight(SubSection subSectionRight){
		this.subSections.setRight(subSectionRight);
	}

	/**
	 * Sets the left subSection
	 *
	 * @param subSectionLeft the left subSection
	 */
	public void setSubSectionLeft(SubSection subSectionLeft){
		this.subSections.setLeft(subSectionLeft);
	}

}
