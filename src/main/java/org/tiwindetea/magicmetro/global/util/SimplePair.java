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

package org.tiwindetea.magicmetro.global.util;

import javax.annotation.Nullable;

/**
 * SimplePair type, store a pair of objects of the same type.
 *
 * @author Maxime PINARD
 * @see Pair
 * @since 0.1
 */
public class SimplePair<T> extends Pair<T, T> {

	public SimplePair() {

	}

	public SimplePair(Pair<T, T> pair) {
		super(pair);
	}

	public SimplePair(T left, T right) {
		super(left, right);
	}

	@Nullable
	public T getOther(T value) {

		if(this.left == value) {
			return this.right;
		}
		if(this.right == value) {
			return this.left;
		}
		return null;
	}

	/**
	 * Determine if the pair contains an value.
	 *
	 * @param value The value
	 * @return true if the pair contains the value, false otherwise
	 */
	public boolean contains(@Nullable T value) {
		return ((this.left == value) || (this.right == value));
	}

}
