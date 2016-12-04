package org.tiwindetea.magicmetro.global.util;

import java.io.Serializable;

/**
 * Pair type, store a pair of objects.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class Pair<L, R> implements Serializable {

	protected L left;
	protected R right;

	/**
	 * Default constructor.
	 */
	public Pair() {

	}

	/**
	 * Copy constructor.
	 *
	 * @param pair the pair to copy
	 */
	public Pair(Pair<L, R> pair) {
		this.left = pair.left;
		this.right = pair.right;
	}

	/**
	 * Creates a new pair.
	 *
	 * @param left  The left value of the pair
	 * @param right The right value of the pair
	 */
	public Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * Sets the left value of the pair.
	 *
	 * @param left the left value of the pair
	 */
	public void setLeft(L left) {
		this.left = left;
	}

	/**
	 * Gets the left value of the pair.
	 *
	 * @return left value of the pair
	 */
	public L getLeft() {
		return this.left;
	}

	/**
	 * Sets the right value of the pair.
	 *
	 * @param right the right value of the pair
	 */
	public void setRight(R right) {
		this.right = right;
	}

	/**
	 * Gets the right value of the pair.
	 *
	 * @return right value of the pair
	 */
	public R getRight() {
		return this.right;
	}

	@Override
	public String toString() {
		return "( " + this.left + " , " + this.right + " )";
	}

	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o instanceof Pair) {
			Pair pair = (Pair) o;
			if(this.left != null ? !this.left.equals(pair.left) : pair.left != null)
				return false;
			if(this.right != null ? !this.right.equals(pair.right) : pair.right != null)
				return false;
			return true;
		}
		return false;
	}
}
