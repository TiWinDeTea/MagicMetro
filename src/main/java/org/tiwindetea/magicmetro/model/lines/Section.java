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

import org.tiwindetea.magicmetro.global.util.Pair;
import org.tiwindetea.magicmetro.global.util.SimplePair;
import org.tiwindetea.magicmetro.model.Station;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

	private Connection rightConnection;
	private Connection leftConnection;
	private Connection middleConnection;
	private SimplePair<SubSection> subSections;
	private boolean hasTunnel;
	private boolean tunnelUpToDate = false;
	private double length;
	private boolean lengthUpToDate = false;
	private Line lineRef;

	/**
	 * Constructor of a Section with 3 Connections
	 * Set the section ref to the subSection
	 *
	 * @param rightConnection the right connection
	 * @param leftConnection the left connection
	 * @param middleConnection the middle connection
	 */
	public Section(Connection rightConnection, Connection leftConnection, Connection middleConnection, Line line) {
		this.rightConnection = rightConnection;
		this.leftConnection = leftConnection;
		this.middleConnection = middleConnection;
		this.lineRef = line;

		rightConnection.setSubSectionLeft(new SubSection(false, middleConnection, rightConnection));
		leftConnection.setSubSectionRight(new SubSection(false, leftConnection, middleConnection));

		subSections = new SimplePair<>(rightConnection.getLeftSubSection(), leftConnection.getRightSubSection());

		//Setting the reference for the subSections
		if(subSections.getLeft() != null){
			subSections.getLeft().setSectionRef(this);
		}

		if(subSections.getRight() != null){
			subSections.getRight().setSectionRef(this);
		}

		computeLength();
		computeTunnel();
	}

	/**
	 * Compute a update about the tunnel
	 */
	private void computeTunnel() {

		this.hasTunnel = subSections.getLeft().isTunnel() || subSections.getRight().isTunnel();
		this.tunnelUpToDate = true;
	}

	/**
	 * Compute the length with changement
	 */
	private void computeLength() {

		this.length = subSections.getLeft().getLength() + subSections.getLeft().getLength();
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
	 * Gets the line ref
	 * @return the line reference
	 */
	public Line getLineRef() {
		return lineRef;
	}

	/**
	 * Gets the left Connection
	 * @return the left connection
	 */
	public Connection getLeftConnection(){
		return leftConnection;
	}

	/**
	 * Gets the right Connection
	 * @return the right Connection
	 */
	public Connection getRightConnection(){
		return rightConnection;
	}

	/**
	 * Sets the Line Ref
	 *
	 * @param lineRef the line reference
	 */
	protected void setLineRef(Line lineRef) {
		this.lineRef = lineRef;
	}

	@Override
	public String toString() {
		return "Section{" +
				"rightConnection=" + rightConnection +
				", leftConnection=" + leftConnection +
				", middleConnection=" + middleConnection +
				", subSections=" + subSections +
				", hasTunnel=" + hasTunnel +
				", tunnelUpToDate=" + tunnelUpToDate +
				", length=" + length +
				", lengthUpToDate=" + lengthUpToDate +
				", lineRef=" + lineRef +
				'}';
	}
}
