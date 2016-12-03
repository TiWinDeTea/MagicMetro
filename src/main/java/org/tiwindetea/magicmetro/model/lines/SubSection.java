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

import javax.annotation.Nonnull;

/**
 * A subsection between two connections.
 *
 * @author Maxime PINARD
 * @see Connection
 * @since 0.1
 */
public class SubSection {

	private boolean isTunnel;
	private Pair<Connection, Connection> connections;

	/**
	 * Instantiates a new SubSection.
	 *
	 * @param isTunnel    true if tunnel, false otherwise
	 * @param connections the connections
	 */
	public SubSection(boolean isTunnel, @Nonnull Pair<Connection, Connection> connections) {

		this.isTunnel = isTunnel;
		this.connections = new Pair<>(connections);
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
		this.connections = new Pair<>(leftConnection, rightConnection);
	}

	/**
	 * Gets if tunnel or not.
	 *
	 * @return true if tunnel, false otherwise
	 */
	public boolean isTunnel() {
		return this.isTunnel;
	}

}
