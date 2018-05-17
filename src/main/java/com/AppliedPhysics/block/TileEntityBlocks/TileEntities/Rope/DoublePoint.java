package com.AppliedPhysics.block.TileEntityBlocks.TileEntities.Rope;

public class DoublePoint {

	private double x;
	private double y;

	/**
	 * Data class for a coordinate with double precision.
	 *
	 * @param x X position.
	 * @param y Y position.
	 */
	public DoublePoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public DoublePoint copy() {
		return new DoublePoint(this.x, this.y);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	/**
	 * Calculate the shortest distance to another {@link DoublePoint}.
	 * @param otherPoint The other point.
	 * @return The shortest distance to otherPoint.
	 */
	public double distanceTo(DoublePoint otherPoint) {
		return Math.sqrt(Math.pow(otherPoint.x - x, 2) + Math.pow(otherPoint.y - y, 2));
	}

	/**
	 * Calculate the slope towards another {@link DoublePoint}.
	 *
	 * @param otherPoint The other point.
	 * @return The slope towards otherPoint.
	 */
	public double slopeTowards(DoublePoint otherPoint) {
		return (otherPoint.y - y) / (otherPoint.x - x);
	}

	/**
	 * 1: Other point is to the right of this point.<br/>
	 * -1: Other point is to the left of this point.<br/>
	 * 0: Other point is in line with this point.
	 *
	 * @param otherPoint The other point.
	 * @return
	 */
	public int compareX(DoublePoint otherPoint) {
		if (otherPoint.x > x)
			return 1;
		if (otherPoint.x < x)
			return -1;
		return 0;
	}

	/**
	 * 1: Other point is above this point.<br/>
	 * -1: Other point is below this point.<br/>
	 * 0: Other point is in line with this point.
	 *
	 * @param otherPoint The other point.
	 * @return
	 */
	public int compareY(DoublePoint otherPoint) {
		if (otherPoint.y > y)
			return 1;
		if (otherPoint.y < y)
			return -1;
		return 0;
	}
}