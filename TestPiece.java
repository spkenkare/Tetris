package assignment;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.util.*;

import org.junit.Test;

public class TestPiece {

	@Test
	public void testGetWidth() {
		TetrisPiece test1 = new TetrisPiece("0 0  0 1  1 0  1 1");
		assertEquals("test width", 2, test1.getWidth());
		TetrisPiece test2 = new TetrisPiece("0 0  0 1");
		assertEquals("test width", 1, test2.getWidth());
		TetrisPiece test3 = new TetrisPiece("");
		assertEquals("test width", 0, test3.getWidth());
	}
	
	@Test
	public void testGetHeight() {
		TetrisPiece test1 = new TetrisPiece("0 0  0 1  1 0  1 1");
		assertEquals("test height", 2, test1.getHeight());
		TetrisPiece test2 = new TetrisPiece("0 0  1 0");
		assertEquals("test height", 1, test2.getHeight());
		TetrisPiece test3 = new TetrisPiece("");
		assertEquals("test height", 0, test3.getHeight());
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testGetBody() {
		TetrisPiece test1 = new TetrisPiece("0 0  0 1  1 0  1 1");
		Point[] body1 = {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)};
		assertEquals("test body", body1, test1.getBody());
		TetrisPiece test2 = new TetrisPiece("0 0");
		Point[] body2 = {new Point(0, 0)};
		assertEquals("test body", body2, test2.getBody());
		TetrisPiece test3 = new TetrisPiece("");
		Point[] body3 = new Point[0];
		assertEquals("test body", body3, test3.getBody());
	}
	
	@Test
	public void testGetSkirt() {
		TetrisPiece test1 = new TetrisPiece("0 0  0 1  1 0  1 1");
		int[] skirt1 = {0, 0};
		assertArrayEquals("test skirt", skirt1, test1.getSkirt());
		TetrisPiece test2 = new TetrisPiece("0 0");
		int[] skirt2 = {0};
		assertArrayEquals("test skirt", skirt2, test2.getSkirt());
		TetrisPiece test3 = new TetrisPiece("");
		int[] skirt3 = new int[0];
		assertArrayEquals("test skirt", skirt3, test3.getSkirt());
	}
	
	@Test
	public void testGetPiece() {
		TetrisPiece test1 = new TetrisPiece("0 0  0 1  1 0  1 1");
		assertEquals("test piece", test1, test1.getPiece("0 0  0 1  1 0  1 1"));
		TetrisPiece test2 = new TetrisPiece("0 0");
		assertEquals("test piece", test2, test2.getPiece("0 0"));
		TetrisPiece test3 = new TetrisPiece("");
		assertEquals("test piece", test3, test3.getPiece(""));
	}
	
	@Test
	public void testGetNextRotation() {
		TetrisPiece test1 = new TetrisPiece("0 0  0 1  1 0  1 1");
		ArrayList<Point> body1 = new ArrayList<Point>();
		for(int i=0; i<test1.getBody().length; i++) {
			body1.add(test1.getBody()[i]);
		}
		Point[] rotation = test1.nextRotation().getBody();
		ArrayList<Point> rotationBody = new ArrayList<Point>();
		for(int i=0; i<rotation.length; i++) {
			rotationBody.add(rotation[i]);
		}
		for(int i=0; i<test1.getBody().length; i++) {
			assertEquals("test rotation", true, rotationBody.contains(body1.get(i)));
		}
		
		TetrisPiece test2 = new TetrisPiece("0 0  0 1  1 0  0 2");
		ArrayList<Point> body2 = new ArrayList<Point>();
		body2.add(new Point(0, 0));
		body2.add(new Point(0, 1));
		body2.add(new Point(1, 1));
		body2.add(new Point(2, 1));
		Point[] rotation2 = test2.nextRotation().nextRotation().nextRotation().getBody();
		ArrayList<Point> rotationBody2 = new ArrayList<Point>();
		for(int i=0; i<rotation2.length; i++) {
			rotationBody2.add(rotation2[i]);
		}
		for(int i=0; i<test2.getBody().length; i++) {
			assertEquals("test rotation", true, rotationBody2.contains(body2.get(i)));
		}
		
		TetrisPiece test3 = new TetrisPiece("0 0  0 1  1 0  0 2");
		ArrayList<Point> body3 = new ArrayList<Point>();
		body3.add(new Point(0, 0));
		body3.add(new Point(1, 0));
		body3.add(new Point(2, 0));
		body3.add(new Point(2, 1));
		Point[] rotation3 = test3.nextRotation().getBody();
		ArrayList<Point> rotationBody3 = new ArrayList<Point>();
		for(int i=0; i<rotation3.length; i++) {
			rotationBody3.add(rotation3[i]);
		}
		for(int i=0; i<test3.getBody().length; i++) {
			assertEquals("test rotation", true, rotationBody3.contains(body3.get(i)));
		}
		
		TetrisPiece test4 = new TetrisPiece("0 0  0 1  0 2  0 3");
		ArrayList<Point> body4 = new ArrayList<Point>();
		for(int i=0; i<test4.getBody().length; i++) {
			body4.add(test4.getBody()[i]);
		}
		Point[] rotation4 = test4.nextRotation().nextRotation().getBody();
		ArrayList<Point> rotationBody4 = new ArrayList<Point>();
		for(int i=0; i<rotation4.length; i++) {
			rotationBody4.add(rotation4[i]);
		}
		for(int i=0; i<test4.getBody().length; i++) {
			assertEquals("test rotation", true, rotationBody4.contains(body4.get(i)));
		}
		
		TetrisPiece test5 = new TetrisPiece("");
		Point[] rotationBody5 = new Point[0];
		assertArrayEquals("test rotation", rotationBody5, test5.nextRotation().getBody());
		
	}
	
	@Test
	public void testEquals() {
		TetrisPiece test1 = new TetrisPiece("0 0  0 1  1 0  1 1");
		assertEquals("test equals", true, test1.equals(test1));
		
		TetrisPiece test2 = new TetrisPiece("0 0  1 0  1 1  0 1");
		assertEquals("test equals", true, test2.equals(test1));
		
		TetrisPiece test3 = new TetrisPiece("0 0  0 1  1 0  0 2");
		TetrisPiece test4 = new TetrisPiece("0 0  0 1  1 1  2 1");
		TetrisPiece test5 = new TetrisPiece("1 0  1 1  1 2  0 2");
		TetrisPiece test6 = new TetrisPiece("0 0  1 0  2 0  2 1");
		assertEquals("test equals", true, test3.equals(test4));
		assertEquals("test equals", true, test3.equals(test5));
		assertEquals("test equals", true, test3.equals(test6));
		assertEquals("test equals", true, test4.equals(test6));
		assertEquals("test equals", true, test4.equals(test5));
		assertEquals("test equals", true, test4.equals(test3));
		assertEquals("test equals", true, test5.equals(test4));
		assertEquals("test equals", true, test5.equals(test3));
		assertEquals("test equals", true, test5.equals(test6));
		assertEquals("test equals", true, test6.equals(test4));
		assertEquals("test equals", true, test6.equals(test5));
		assertEquals("test equals", true, test6.equals(test3));
		
		assertEquals("test equals", false, test6.equals(0));
	}

}
