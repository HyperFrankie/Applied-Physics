package com.AppliedPhysics.block.TileEntityBlocks.TileEntities.Rope.formulas;

import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.Rope.DoublePoint;

public class SlopeLineFormula extends Formula {

	private final double slope;
	private final double offsetY;

	/**
	 * Construct a new instance of the {@link SlopeLineFormula}.
	 * This is the formula for a simple, straight line.
	 * The direction of the line is determined based on the slope.
	 * The Y-offset is calculated using the given point that should be on the line.
	 *
	 * @param slope     The slope of the line.
	 * @param somePoint A point on the line.
	 */
	public SlopeLineFormula(double slope, DoublePoint somePoint) {
		this.slope = slope;

		// Calculate the Y-offset (y = ax + b) where `b` is the Y-offset
		this.offsetY = somePoint.getY() - this.slope * somePoint.getX();
	}

	public double getSlope() {
		return slope;
	}

	public double getOffsetY() {
		return offsetY;
	}

	@Override
	public double calculate(double x) {
		return slope * x + offsetY;
	}
}