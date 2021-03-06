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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A subsection between two connections.
 *
 * @author Maxime PINARD
 * @see Connection
 * @see Section
 * @since 0.1
 */
public class SubSection {

	private boolean isTunnel;
	private SimplePair<Connection> connections;
	private double length;
	private boolean lengthUpToDate = false;
	private Section section;

	/**
	 * Instantiates a new SubSection.
	 *
	 * @param isTunnel    true if tunnel, false otherwise
	 * @param connections the connections
	 */
	public SubSection(boolean isTunnel, @Nonnull Pair<Connection, Connection> connections) {

		this.isTunnel = isTunnel;
		this.connections = new SimplePair<>(connections);
	}

	/**
	 * Instantiates a new SubSection.
	 *
	 * @param isTunnel        true if tunnel, false otherwise
	 * @param leftConnection  the left connection
	 * @param rightConnection the right connection
	 */
	public SubSection(boolean isTunnel, @Nonnull Connection leftConnection, @Nonnull Connection rightConnection) {

		this.isTunnel = isTunnel;
		this.connections = new SimplePair<>(leftConnection, rightConnection);
		computeLength();
	}

	/**
	 * Gets if tunnel or not.
	 *
	 * @return true if tunnel, false otherwise
	 */
	public boolean isTunnel() {
		return this.isTunnel;
	}

	private void computeLength() {

		this.length = this.connections.getLeft().getPosition().getDistance(this.connections.getRight().getPosition());
		this.lengthUpToDate = true;
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
	 * Gets section.
	 *
	 * @return the section
	 */
	@Nullable
	public Section getSection() {

		return this.section;
	}

	/**
	 * Sets section.
	 *
	 * @param section the section
	 */
	public void setSection(@Nullable Section section) {

		this.section = section;
	}

	/**
	 * Determine if the subsection contains an connection.
	 *
	 * @param connection The connection
	 * @return true if the subsection contains the connection, false otherwise
	 */
	public boolean contains(@Nullable Connection connection) {
		return this.connections.contains(connection);
	}

	/**
	 * Gets the other connection.
	 *
	 * @param connection the connection
	 * @return the other connection
	 */
	@Nullable
	public Connection getOther(Connection connection) {
		return this.connections.getOther(connection);
	}

}
