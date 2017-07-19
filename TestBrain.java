package assignment;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.util.*;

import org.junit.Test;

import assignment.Board.Action;

public class TestBrain {

	@Test
	public void testProperMove() {
		NewBrain test1 = new NewBrain();
		Board b = new TetrisBoard(5, 5);
		b.nextPiece(new TetrisPiece("0 0  0 1  1 0  1 1"));
		Action r = test1.nextMove(b);
		assertEquals("test move", Action.DROP, r);
		
		NewBrain test2 = new NewBrain();
		Board b1 = new TetrisBoard(5, 5);
		b.nextPiece(new TetrisPiece("0 0  0 1  0 2  0 3"));
		Action a = test2.nextMove(b1);
		assertEquals("test move", Action.CLOCKWISE, a);
		
		NewBrain test3 = new NewBrain();
		Board b2 = new TetrisBoard(5, 5);
		b.nextPiece(new TetrisPiece("0 0  1 0  2 0  3 0"));
		Action r3 = test3.nextMove(b);
		assertEquals("test move", Action.DROP, r3);
		
	}
	
	

}
