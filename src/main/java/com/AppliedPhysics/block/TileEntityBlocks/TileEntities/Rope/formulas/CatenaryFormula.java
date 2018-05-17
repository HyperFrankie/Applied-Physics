package com.AppliedPhysics.block.TileEntityBlocks.TileEntities.Rope.formulas;


import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.Rope.DoublePoint;

public class CatenaryFormula extends Formula {

	/**
	 * How many approximation cycles should be done.
	 */
	private static long APPROX__CYCLES = 5000;
	private static double APPROX__INCREMENT_START = .1;
	private static double APPROX__INCREMENT_MIN = 1e-20;
	private static double APPROX__INCREMENT_MULTIPLIER = 0.8;

	private final double curveRadius;
	/**
	 * How far the catenary is shifted to the right on the X-axis.
	 */
	private final double offsetX;
	/**
	 * How far the catenary is shifted upwards on the Y-axis.
	 */
	private final double offsetY;

	public double asinh(double x) {
		return Math.log(x + Math.sqrt(1 + x * x));
	}

	/**
	 * Construct a new instance of the {@link CatenaryFormula}.
	 * Upon construction, the value of the catenary constant is approximated.
	 *
	 * @param pointA     A point on the line.
	 * @param pointB     Another point on the line.
	 * @param ropeLength How long the line between the two points.
	 */
	public CatenaryFormula(DoublePoint pointA, DoublePoint pointB, double ropeLength) {

		// Approximate the curve radius
		double c = 0.007;
		double increment = APPROX__INCREMENT_START;
		boolean lastTooLow = false;

		// Store the X-coordinates to improve the performance slightly
		double Xa = pointA.getX();
		double Xb = pointB.getX();

		for (long i = 0; i < APPROX__CYCLES; i++) {
			double guessedLength = length(Xa, Xb, c);

			boolean tooLow = guessedLength < ropeLength;
			double howFarOff = Math.abs(ropeLength - guessedLength);

			if (howFarOff < 1 || lastTooLow != tooLow)
				increment *= APPROX__INCREMENT_MULTIPLIER;

			if (increment < APPROX__INCREMENT_MIN)
				increment = APPROX__INCREMENT_MIN;

			c += tooLow
					? increment
					: -increment;

			lastTooLow = tooLow;
		}

		// Store the approximated curve radius
		curveRadius = c;

		// Adjust the X and Y offsets
		offsetX = ((pointB.getX() + pointA.getX()) / 2.0) - (curveRadius * asinh((pointA.getY() - pointB.getY()) / (2.0 * curveRadius * Math.sinh((pointA.getX() - pointB.getX()) / (2.0 * curveRadius))))); // TODO: Calculate the correct X-offset
		offsetY = pointA.getY() - calculate(pointA.getX() - offsetX, curveRadius);
		System.out.println("offsetY: " + offsetY);
	}

	/**
	 * Calculate the value of the primitive formula of the arc length.
	 * How the formula has been made: <code>∫ √(1 + ([a*cosh(x/a)]')^2) dx</code> &rarr; <code>a*sinh(x/a)</code>.
	 *
	 * @param x           X position to be used.
	 * @param curveRadius Catenary specific constant.
	 * @return The calculated value.
	 */
	public static double calculatePrimitiveLineLength(double x, double curveRadius) {
		return curveRadius * Math.sinh(x / curveRadius);
	}

	/**
	 * Calculate the line length between two X values.
	 *
	 * @param x1          Most left X.
	 * @param x2          Most right X.
	 * @param curveRadius Catenary specific constant.
	 * @return The length of the line between x1 and x2.
	 */
	public static double length(double x1, double x2, double curveRadius) {
		return calculatePrimitiveLineLength(x2, curveRadius) - calculatePrimitiveLineLength(x1, curveRadius);
	}

	/**
	 * Calculate the Y position of the catenary line.
	 *
	 * @param x           The X position to be used to calculate the appropriate Y value.
	 * @param curveRadius Catenary specific constant.
	 * @return The Y position for the given X position.
	 */
	public static double calculate(double x, double curveRadius) {
		return curveRadius * Math.cosh(x / curveRadius);
	}

	/**
	 * Calculate the Y position of the catenary line.
	 * The required catenary constant is used and necessary X- and Y-offsets are added.
	 *
	 * @param x The X position to be used to calculate the appropriate Y value.
	 * @return The Y position for the given X position.
	 */
	@Override
	public double calculate(double x) {
		return calculate(x - offsetX, curveRadius) + offsetY;
	}
}