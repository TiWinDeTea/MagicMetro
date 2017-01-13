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

package org.tiwindetea.magicmetro.view;

import javax.annotation.Nonnull;

/**
 * Listener of the mouse actions on the sections.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public interface SectionMouseListener {

	/**
	 * Mouse pressed on section.
	 *
	 * @param section the section
	 * @param mouseX  the mouse x
	 * @param mouseY  the mouse y
	 */
	void mousePressedOnSection(@Nonnull SectionView section, double mouseX, double mouseY);

	/**
	 * Mouse pressed on section hook.
	 *
	 * @param section     the section
	 * @param fromStation the from station
	 */
	void mousePressedOnSectionHook(@Nonnull SectionView section, ConcreteStationView fromStation);

	/**
	 * Mouse drag entered on section.
	 *
	 * @param section the section
	 */
	void mouseDragEnteredOnSection(@Nonnull SectionView section);

	/**
	 * Mouse drag exited on section.
	 *
	 * @param section the section
	 */
	void mouseDragExitedOnSection(@Nonnull SectionView section);

}
