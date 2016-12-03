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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * TODO
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class Connection {

	private Point2d position;
	private Pair<SubSection, SubSection> subSections;

	public Connection(@Nullable Point2d position, @Nonnull Pair<SubSection, SubSection> subSections) {

		this.position = (position == null) ? new Point2d() : new Point2d(position);
		this.subSections = new Pair<>(subSections);
	}

	public Connection(@Nullable Point2d position,
	                  @Nonnull SubSection leftSubSection,
	                  @Nonnull SubSection rightSubSection) {

		this.position = (position == null) ? new Point2d() : new Point2d(position);
		this.subSections = new Pair<>(leftSubSection, rightSubSection);
	}

	public Connection(@Nullable Point2d position, @Nonnull SubSection subSection) {

		this.position = (position == null) ? new Point2d() : new Point2d(position);
		this.subSections = new Pair<>(subSection, subSection);
	}

	public Point2d getPosition() {
		return this.position;
	}

	public Pair<SubSection, SubSection> getSubSections() {
		return this.subSections;
	}

	public SubSection getLefttSubSection() {
		return this.subSections.getLeft();
	}

	public SubSection getRightSubSection() {
		return this.subSections.getRight();
	}
}
