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

import org.tiwindetea.magicmetro.global.util.Pair;
import org.tiwindetea.magicmetro.global.util.SimplePair;
import org.tiwindetea.magicmetro.model.Station;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A section of a line composed of several connections and subsections.
 * The two last connections are part of different stations linked by the section.
 *
 * @author Maxime PINARD
 * @see SubSection
 * @see Connection
 * @see Line
 * @since 0.1
 */
public class Section {

	private SimplePair<Connection> lastConnections;
	private Collection<Connection> connections;
	private Collection<SubSection> subSections;
	private boolean hasTunnel;
	private boolean tunnelUpToDate = false;
	private int length;
	private boolean lengthUpToDate = false;

	/**
	 * Instantiates a new Section.
	 *
	 * @param connections     the connections
	 * @param lastConnections the last connections
	 */
	public Section(@Nonnull Collection<Connection> connections, @Nonnull Pair<Connection, Connection> lastConnections) {

		this.connections = new ArrayList<>(connections);
		this.lastConnections = new SimplePair<>(lastConnections);
	}

	private void computeTunnel() {

		this.hasTunnel = false;
		for(SubSection subSection : this.subSections) {
			if(subSection.isTunnel()) {
				this.hasTunnel = true;
				break;
			}
		}
		this.tunnelUpToDate = true;
	}

	private void computeLength() {

		this.length = 0;
		for(SubSection subSection : this.subSections) {
			this.length += subSection.getLength();
		}
		this.lengthUpToDate = true;
	}

	/**
	 * Gets if has tunnel.
	 *
	 * @return true if has tunnel, false otherwise
	 */
	public boolean hasTunnel() {

		if(!this.tunnelUpToDate) {
			computeTunnel();
		}
		return this.hasTunnel;
	}

	/**
	 * Gets length.
	 *
	 * @return the length
	 */
	public double getLength() {

		if(!this.lengthUpToDate) {
			computeLength();
		}
		return this.length;
	}

	/**
	 * Gets stations linked by this section.
	 *
	 * @return the stations
	 */
	@Nonnull
	public Pair<Station, Station> getStations() {

		return new Pair<>(this.lastConnections.getLeft().getStationRef(),
		  this.lastConnections.getRight().getStationRef());
	}

}
