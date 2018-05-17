package com.AppliedPhysics.block.TileEntityBlocks.TileEntities.Rope;

import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.Rope.formulas.CatenaryFormula;
import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.Rope.formulas.SlopeLineFormula;

public class Catenary {

	public static final double STRAIGHT_THRESHOLD = 0.01;

	private final DoublePoint pointA;
	private final DoublePoint pointB;
	private final double ropeLength;
	private final boolean isStraight;
	private final double slopeAToB;

	private SlopeLineFormula slopeLineFormula;
	private CatenaryFormula catenaryFormula;

	/**
	 * Create a new instance of Catenary.
	 * Based on the distance between pointA and pointB and the ropeLength,
	 * this class makes use of the {@link SlopeLineFormula} or {@link CatenaryFormula}.
	 *
	 * <p>
	 * Note: Point A must be to the left of Point B.
	 *
	 * @param pointA     Most left point on the catenary line.
	 * @param pointB     Most right point on the catenary line.
	 * @param ropeLength Length of the line between pointA and pointB.
	 */
	public Catenary(DoublePoint pointA, DoublePoint pointB, double ropeLength) {

		if(pointA.getX() > pointB.getX()) {
			System.out.println("going to switch points A and B for point B is left of point A");
			DoublePoint pointACopy = pointA.copy();
			pointA = pointB.copy();
			pointB = pointACopy;
		} else if(pointA.getX() == pointB.getX()) {
			throw new IllegalArgumentException("invalid argument: point A must be left to point B");
		}

		this.pointA = pointA;
		this.pointB = pointB;
		this.ropeLength = ropeLength;
		this.isStraight = Math.abs(pointA.distanceTo(pointB) - ropeLength) < STRAIGHT_THRESHOLD;
		this.slopeAToB = pointA.slopeTowards(pointB);
//
//		Stage stage = new Stage();
//		try {
//			Parent root = FXMLLoader.load(getClass().getResource("/ChartDisplay.fxml"));
//		} catch(IOException e) {
//
//		}
//
//		Scene scene = new Scene(root);
//
//		stage.setTitle("Catenary Chart Test");
//		stage.setScene(scene);
//		stage.show();
	}

	public DoublePoint getPointA() {
		return pointA;
	}

	public DoublePoint getPointB() {
		return pointB;
	}

	public double getRopeLength() {
		return ropeLength;
	}

	public double getSlopeAToB() {
		return slopeAToB;
	}

	public double calculateY(double x) {
		if (isStraight) {
			if (slopeLineFormula == null)
				slopeLineFormula = new SlopeLineFormula(slopeAToB, pointA);

			return slopeLineFormula.calculate(x);
		}

		if (catenaryFormula == null) {
			long start = System.currentTimeMillis();
			catenaryFormula = new CatenaryFormula(pointA, pointB, ropeLength);
			long end = System.currentTimeMillis();

			System.out.printf("Constructing the catenary took %d ms\n", end - start);
		}

		return catenaryFormula.calculate(x);
	}

}