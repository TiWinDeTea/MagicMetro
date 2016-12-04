package org.tiwindetea.magicmetro.global.util;

import javax.annotation.Nullable;

/**
 * SimplePair type, store a pair of objects of the same type.
 *
 * @author Maxime PINARD
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

}
