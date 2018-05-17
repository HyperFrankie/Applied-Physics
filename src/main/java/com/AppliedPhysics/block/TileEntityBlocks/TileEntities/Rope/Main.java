package com.AppliedPhysics.block.TileEntityBlocks.TileEntities.Rope;

public class Main {
	public static void main(String... args) {
		DoublePoint A = new DoublePoint(0, 0);
		DoublePoint B = new DoublePoint(5, 3);
		double ropeLength = 20;

		Catenary line = new Catenary(A, B, ropeLength);

		System.out.println("f(5) = " + line.calculateY(5));
	}
}