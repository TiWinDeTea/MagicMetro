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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * A section of a line composed of several connections and subsections.
 * The right and left connections are part of different stations linked by the section.
 *
 * @author Julien Barbier
 * @author Maxime PINARD
 * @see SubSection
 * @see Connection
 * @see Line
 * @since 0.1
 */
public class Section {

	/**
	 * The Game id.
	 */
	public final int gameId;

	private Line line;

	private Connection leftConnection;
	private Connection rightConnection;
	private Connection middleConnection;
	private SubSection leftSubSection;
	private SubSection rightSubSection;

	/**
	 * Instantiates a new Section.
	 *
	 * @param gameId           the game id
	 * @param line             the line
	 * @param leftConnection   the left connection
	 * @param rightConnection  the right connection
	 * @param middleConnection the middle connection
	 * @param leftSubSection   the left sub section
	 * @param rightSubSection  the right sub section
	 */
	public Section(int gameId,
	               @Nonnull Line line,
	               @Nonnull Connection leftConnection,
	               @Nonnull Connection rightConnection,
	               @Nonnull Connection middleConnection,
	               @Nonnull SubSection leftSubSection,
	               @Nonnull SubSection rightSubSection) {

		Objects.requireNonNull(leftConnection.getStation(), "Left connection must be in a station");
		Objects.requireNonNull(rightConnection.getStation(), "Left connection must be in a station");

		this.gameId = gameId;
		this.line = line;
		this.leftConnection = leftConnection;
		this.rightConnection = rightConnection;
		this.middleConnection = middleConnection;
		this.leftSubSection = leftSubSection;
		this.rightSubSection = rightSubSection;

		this.rightSubSection.setSection(this);
		this.leftSubSection.setSection(this);
	}


	/**
	 * Gets if has tunnel.
	 *
	 * @return true if has tunnel, false otherwise
	 */
	public boolean hasTunnel() {
		return this.leftSubSection.isTunnel() || this.rightSubSection.isTunnel();
	}

	/**
	 * Gets length.
	 *
	 * @return the length
	 */
	public double getLength() {
		return this.leftSubSection.getLength() + this.rightSubSection.getLength();
	}

	/**
	 * Gets line.
	 *
	 * @return the line
	 */
	@Nullable
	public Line getLine() {
		return this.line;
	}

	/**
	 * Sets line.
	 *
	 * @param line the line
	 */
	public void setLine(@Nullable Line line) {
		this.line = line;
	}

	/**
	 * Gets left connection.
	 *
	 * @return the left connection
	 */
	@Nonnull
	public Connection getLeftConnection() {
		return this.leftConnection;
	}

	/**
	 * Sets left connection.
	 *
	 * @param leftConnection the left connection
	 */
	public void setLeftConnection(@Nonnull Connection leftConnection) {
		this.leftConnection = leftConnection;
	}

	/**
	 * Gets right connection.
	 *
	 * @return the right connection
	 */
	@Nonnull
	public Connection getRightConnection() {
		return this.rightConnection;
	}

	/**
	 * Sets right connection.
	 *
	 * @param rightConnection the right connection
	 */
	public void setRightConnection(@Nonnull Connection rightConnection) {
		this.rightConnection = rightConnection;
	}

	/**
	 * Gets middle connection.
	 *
	 * @return the middle connection
	 */
	@Nonnull
	public Connection getMiddleConnection() {
		return this.middleConnection;
	}

	/**
	 * Sets middle connection.
	 *
	 * @param middleConnection the middle connection
	 */
	public void setMiddleConnection(@Nonnull Connection middleConnection) {
		this.middleConnection = middleConnection;
	}

	/**
	 * get the other Station Connection
	 *
	 * @param connection the station Connection
	 * @return the other station Connection or left if it's not a station Connection
	 */
	public Connection getOtherStationConnection(@Nonnull Connection connection){
		if(connection == leftConnection){
			return rightConnection;
		} else {
			return leftConnection;
		}
	}

	/**
	 * Gets left sub section.
	 *
	 * @return the left sub section
	 */
	@Nonnull
	public SubSection getLeftSubSection() {
		return this.leftSubSection;
	}

	/**
	 * Sets left sub section.
	 *
	 * @param leftSubSection the left sub section
	 */
	public void setLeftSubSection(@Nonnull SubSection leftSubSection) {
		this.leftSubSection = leftSubSection;
	}

	/**
	 * Gets right sub section.
	 *
	 * @return the right sub section
	 */
	@Nonnull
	public SubSection getRightSubSection() {
		return this.rightSubSection;
	}

	/**
	 * Sets right sub section.
	 *
	 * @param rightSubSection the right sub section
	 */
	public void setRightSubSection(@Nonnull SubSection rightSubSection) {
		this.rightSubSection = rightSubSection;
	}

}
