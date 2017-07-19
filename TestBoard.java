package assignment;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import assignment.Board.Action;
import assignment.Board.Result;

public class TestBoard {
	
	@Test
	public void testGetWidth() {
		TetrisBoard test1 = new TetrisBoard(5, 5);
		assertEquals("test width", 5, test1.getWidth());
		TetrisBoard test2 = new TetrisBoard(10, 24);
		assertEquals("test width", 10, test2.getWidth());
		TetrisBoard test3 = new TetrisBoard(0, 0);
		assertEquals("test width", 0, test3.getWidth());
	}
	
	@Test
	public void testGetHeight() {
		TetrisBoard test1 = new TetrisBoard(5, 5);
		assertEquals("test height", 5, test1.getHeight());
		TetrisBoard test2 = new TetrisBoard(10, 24);
		assertEquals("test height", 24, test2.getHeight());
		TetrisBoard test3 = new TetrisBoard(0, 0);
		assertEquals("test height", 0, test3.getHeight());
	}
	
	
	@Test
	public void testEquals() {
		TetrisBoard test1 = new TetrisBoard(5, 5);
		TetrisBoard test2 = new TetrisBoard(10, 24);
		TetrisBoard test3 = new TetrisBoard(0, 0);
		TetrisBoard test4 = new TetrisBoard(5, 5);
		TetrisBoard test5 = new TetrisBoard(10, 24);
		boolean[][] board = new boolean[10][24];
		board[1][2]=true;
		test2.board=board;
		test5.board=board;
		assertEquals("test equals", true, test1.equals(test4));
		assertEquals("test equals", false, test1.equals(test2));
		assertEquals("test equals", true, test3.equals(test3));
		assertEquals("test equals", true, test2.equals(test5));
	}
	
	
	@Test
	public void testRowWidth() {
		TetrisBoard test1 = new TetrisBoard(0, 0);
		TetrisBoard test2 = new TetrisBoard(5, 5);
		TetrisBoard test3 = new TetrisBoard(5, 5);
		TetrisBoard test4 = new TetrisBoard(5, 5);
		boolean[][] board2 = new boolean[5][5];
		boolean[][] board3 = new boolean[5][5];
		board2[0][0]=true;
		board2[4][0]=true;
		test2.board=board2;
		board3[0][0]=true;
		board3[1][0]=true;
		board3[2][0]=true;
		board3[3][0]=true;
		board3[4][0]=true;
		test3.board=board3;
		test2.getRowWidths();
		test3.getRowWidths();
		test4.getRowWidths();
		assertEquals("test row width", 0, test1.getRowWidth(0));
		assertEquals("test row width", 2, test2.getRowWidth(0));
		assertEquals("test row width", 5, test3.getRowWidth(0));
		assertEquals("test row width", 0, test4.getRowWidth(0));
	}
	
	@Test
	public void testColumnHeight() {
		TetrisBoard test1 = new TetrisBoard(0, 0);
		TetrisBoard test2 = new TetrisBoard(5, 5);
		TetrisBoard test3 = new TetrisBoard(5, 5);
		TetrisBoard test4 = new TetrisBoard(5, 5);
		boolean[][] board2 = new boolean[5][5];
		boolean[][] board3 = new boolean[5][5];
		board2[0][0]=true;
		board2[0][4]=true;
		test2.board=board2;
		test2.permanent=board2;
		board3[0][0]=true;
		board3[0][1]=true;
		board3[0][2]=true;
		board3[0][3]=true;
		board3[0][4]=true;
		test3.board=board3;
		test3.permanent=board3;
		test2.getColumnHeights();
		test3.getColumnHeights();
		test4.getColumnHeights();
		assertEquals("test column height", 0, test1.getColumnHeight(0));
		assertEquals("test column height", 5, test2.getColumnHeight(0));
		assertEquals("test column height", 5, test3.getColumnHeight(0));
		assertEquals("test column height", 0, test4.getColumnHeight(0));
	}
	
	@Test
	public void testMaxHeight() {
		TetrisBoard test1 = new TetrisBoard(0, 0);
		TetrisBoard test2 = new TetrisBoard(5, 5);
		TetrisBoard test3 = new TetrisBoard(5, 5);
		boolean[][] board2 = new boolean[5][5];
		boolean[][] board3 = new boolean[5][5];
		board2[1][3]=true;
		board2[0][4]=true;
		test2.board=board2;
		test2.permanent=board2;
		board3[0][1]=true;
		board3[3][1]=true;
		board3[4][3]=true;
		board3[2][2]=true;
		board3[0][0]=true;
		test3.board=board3;
		test3.permanent=board3;
		test2.getColumnHeights();
		test3.getColumnHeights();
		assertEquals("test column height", 0, test1.getMaxHeight());
		assertEquals("test column height", 5, test2.getMaxHeight());
		assertEquals("test column height", 4, test3.getMaxHeight());
	}
	
	@Test
	public void testClearAndShiftAndRowsCleared() {
		TetrisBoard test1 = new TetrisBoard(5, 5);
		TetrisBoard test2 = new TetrisBoard(5, 5);
		TetrisBoard test3 = new TetrisBoard(5, 5);
		TetrisBoard test4 = new TetrisBoard(5, 5);
		TetrisBoard test5 = new TetrisBoard(5, 5);
		TetrisBoard test6 = new TetrisBoard(5, 5);
		boolean[][] board2 = new boolean[5][5];
		boolean[][] board3 = new boolean[5][5];
		boolean[][] board4 = new boolean[5][5];
		boolean[][] board5 = new boolean[5][5];
		boolean[][] board6 = new boolean[5][5];
		board2[0][0]=true;
		board2[1][0]=true;
		board2[2][0]=true;
		board2[3][0]=true;
		board2[4][0]=true;
		test2.board=board2;
		test2.permanent=board2;
		board3[0][0]=true;
		board3[1][0]=true;
		board3[2][0]=true;
		board3[3][0]=true;
		board3[4][0]=true;
		board3[1][1]=true;
		board3[4][1]=true;
		test3.board=board3;
		test3.permanent=board3;
		board4[1][0]=true;
		board4[4][0]=true;
		test4.board=board4;
		test4.permanent=board4;
		board5[0][0]=true;
		board5[4][0]=true;
		board5[0][1]=true;
		board5[1][1]=true;
		board5[2][1]=true;
		board5[3][1]=true;
		board5[4][1]=true;
		board5[0][2]=true;
		board5[1][2]=true;
		board5[2][2]=true;
		board5[3][2]=true;
		board5[4][2]=true;
		board5[1][3]=true;
		board5[3][3]=true;
		test5.board=board5;
		test5.permanent=board5;
		board6[0][0]=true;
		board6[4][0]=true;
		board6[1][1]=true;
		board6[3][1]=true;
		test6.board=board6;
		test6.permanent=board6;
		test2.clear();
		test3.clear();
		
		test5.clear();
		
		assertArrayEquals("test clear and shift", test1.board, test2.board);		
		assertArrayEquals("test clear and shift", test4.board, test3.board);
		assertArrayEquals("test clear and shift", test5.board, test6.board);
		
		assertEquals("test rows cleared", 1, test2.getRowsCleared());		
		assertEquals("test rows cleared", 1, test3.getRowsCleared());
		assertEquals("test rows cleared", 2, test5.getRowsCleared());
	}
	
	@Test
	public void testTestMove() {
		TetrisBoard test1 = new TetrisBoard(5, 5);
		TetrisBoard test2 = new TetrisBoard(5, 5);
		TetrisBoard test3 = new TetrisBoard(5, 5);
		
		boolean[][] board1 = new boolean[5][5];
		boolean[][] board2 = new boolean[5][5];
		boolean[][] board3 = new boolean[5][5];
		
		TetrisPiece piece1 = new TetrisPiece("0 0  0 1  1 0  0 2");
		test1.nextPiece(piece1);
		test1.move(Action.DROP);
		test3.nextPiece(piece1);
		test3.move(Action.DROP);
		test3.testMove(Action.DROP);
		//TetrisBoard test4 = (TetrisBoard) test3.testMove(Action.DROP);
		board1[1][0]=true;
		board1[1][1]=true;
		board1[2][0]=true;
		board1[1][2]=true;
		
		board2[1][0]=true;
		board2[1][1]=true;
		board2[2][0]=true;
		board2[1][2]=true;
		board2[1][3]=true;
		board2[1][4]=true;
		board2[2][3]=true;
		board2[2][4]=true;
		
		TetrisPiece piece2 = new TetrisPiece("0 0  0 1  1 0  1 1");
		test2.nextPiece(piece1);
		test2.move(Action.DROP);
		test2.nextPiece(piece2);
		test2.move(Action.DROP);
		
		board3[0][0]=true;
		board3[1][0]=true;
		board3[2][0]=true;
		board3[3][0]=true;
		board3[4][0]=true;
		board3[1][1]=true;
		board3[4][1]=true;
		test3.board=board3;
		test3.permanent=board3;
		
		//assertArrayEquals("test drop", test4.board, test1.board);		
		assertArrayEquals("test drop", board2, test2.board);
		
	}
	
	public void testTestPermanent() {
		TetrisBoard test1 = new TetrisBoard(5, 5);
		TetrisBoard test2 = new TetrisBoard(5, 5);
		TetrisBoard test3 = new TetrisBoard(5, 5);
		
		boolean[][] board1 = new boolean[5][5];
		boolean[][] board2 = new boolean[5][5];
		boolean[][] board3 = new boolean[5][5];
		
		TetrisPiece piece1 = new TetrisPiece("0 0  0 1  1 0  0 2");
		test1.nextPiece(piece1);
		test1.move(Action.DROP);
		test3.nextPiece(piece1);
		test3.move(Action.DROP);
		test3.testMove(Action.DROP);
		//TetrisBoard test4 = (TetrisBoard) test3.testMove(Action.DROP);
		board1[1][0]=true;
		board1[1][1]=true;
		board1[2][0]=true;
		board1[1][2]=true;
		
		board2[1][0]=true;
		board2[1][1]=true;
		board2[2][0]=true;
		board2[1][2]=true;
		board2[1][3]=true;
		board2[1][4]=true;
		board2[2][3]=true;
		board2[2][4]=true;
		
		TetrisPiece piece2 = new TetrisPiece("0 0  0 1  1 0  1 1");
		test2.nextPiece(piece1);
		test2.move(Action.DROP);
		test2.nextPiece(piece2);
		test2.move(Action.DROP);
		
		board3[0][0]=true;
		board3[1][0]=true;
		board3[2][0]=true;
		board3[3][0]=true;
		board3[4][0]=true;
		board3[1][1]=true;
		board3[4][1]=true;
		test3.board=board3;
		test3.permanent=board3;
		
		//assertArrayEquals("test drop", test4.board, test1.board);		
		assertArrayEquals("test drop", board2, test2.board);
		
	}
	
	@Test
	public void testDown() {
		TetrisBoard test1 = new TetrisBoard(5, 5);
		TetrisBoard test2 = new TetrisBoard(5, 5);
		TetrisBoard test3 = new TetrisBoard(5, 5);
		
		boolean[][] board1 = new boolean[5][5];
		boolean[][] board2 = new boolean[5][5];
		boolean[][] board3 = new boolean[5][5];
		
		TetrisPiece piece1 = new TetrisPiece("0 0  0 1  1 0  0 2");
		test1.nextPiece(piece1);
		test1.move(Action.DOWN);
		
		
		board1[1][1]=true;
		board1[1][2]=true;
		board1[2][1]=true;
		board1[1][3]=true;
		
		
		TetrisPiece piece2 = new TetrisPiece("0 0  0 1  1 0  1 1");
		test2.nextPiece(piece2);
		test2.move(Action.DOWN);
		test2.move(Action.DOWN);
		
		board2[1][1]=true;
		board2[1][2]=true;
		board2[2][1]=true;
		board2[2][2]=true;
		
		TetrisPiece piece3 = new TetrisPiece("0 0  0 1  0 2  0 3");
		test3.nextPiece(piece3);
		test3.move(Action.DOWN);
		test3.move(Action.DOWN);
		
		board3[1][0]=true;
		board3[1][1]=true;
		board3[1][2]=true;
		board3[1][3]=true;
		
		assertArrayEquals("test down", board1, test1.board);		
		assertArrayEquals("test down", board2, test2.board);
		assertArrayEquals("test down", board3, test3.board);
		assertEquals("test action", Action.DOWN, test1.getLastAction());
		assertEquals("test action", Action.DOWN, test2.getLastAction());
		assertEquals("test action", Action.DOWN, test3.getLastAction());
		assertEquals("test result", Result.SUCCESS, test1.getLastResult());
		assertEquals("test result", Result.SUCCESS, test2.getLastResult());
		assertEquals("test result", Result.PLACE, test3.getLastResult());
	}
	
	@Test
	public void testLeft() {
		TetrisBoard test1 = new TetrisBoard(5, 5);
		TetrisBoard test2 = new TetrisBoard(5, 5);
		
		boolean[][] board1 = new boolean[5][5];
		boolean[][] board2 = new boolean[5][5];
		
		TetrisPiece piece1 = new TetrisPiece("0 0  0 1  1 0  0 2");
		test1.nextPiece(piece1);
		test1.move(Action.LEFT);
		
		
		board1[0][2]=true;
		board1[0][3]=true;
		board1[1][2]=true;
		board1[0][4]=true;
		
		
		test2.nextPiece(piece1);
		test2.move(Action.LEFT);
		test2.move(Action.LEFT);
		
		
		/*for(int i=0; i<test2.board.length; i++) {
			for(int j=0; j<test2.board[i].length; j++) {
				System.out.print(test2.board[i][j]+" ");
			}
			System.out.println();
		}*/
		assertArrayEquals("test left", board1, test1.board);		
		assertArrayEquals("test left", board2, test2.board);
		
		assertEquals("test action", Action.LEFT, test1.getLastAction());
		assertEquals("test action", Action.LEFT, test2.getLastAction());
		assertEquals("test result", Result.SUCCESS, test1.getLastResult());
		assertEquals("test result", Result.OUT_BOUNDS, test2.getLastResult());
	}
	
	@Test
	public void testRight() {
		TetrisBoard test1 = new TetrisBoard(5, 5);
		TetrisBoard test2 = new TetrisBoard(5, 5);
		
		boolean[][] board1 = new boolean[5][5];
		boolean[][] board2 = new boolean[5][5];
		
		TetrisPiece piece1 = new TetrisPiece("0 0  0 1  1 0  0 2");
		test1.nextPiece(piece1);
		test1.move(Action.RIGHT);
		
		
		board1[2][2]=true;
		board1[2][3]=true;
		board1[3][2]=true;
		board1[2][4]=true;
		
		
		test2.nextPiece(piece1);
		test2.move(Action.RIGHT);
		test2.move(Action.RIGHT);
		test2.move(Action.RIGHT);
		
		
		/*for(int i=0; i<test2.board.length; i++) {
			for(int j=0; j<test2.board[i].length; j++) {
				System.out.print(test2.board[i][j]+" ");
			}
			System.out.println();
		}*/
		assertArrayEquals("test right", board1, test1.board);		
		assertArrayEquals("test right", board2, test2.board);
		
		assertEquals("test action", Action.RIGHT, test1.getLastAction());
		assertEquals("test action", Action.RIGHT, test2.getLastAction());
		assertEquals("test result", Result.SUCCESS, test1.getLastResult());
		assertEquals("test result", Result.OUT_BOUNDS, test2.getLastResult());
	}
	
	@Test
	public void testDrop() {

		TetrisBoard test1 = new TetrisBoard(5, 5);
		TetrisBoard test2 = new TetrisBoard(5, 5);
		
		boolean[][] board1 = new boolean[5][5];
		boolean[][] board2 = new boolean[5][5];
		
		TetrisPiece piece1 = new TetrisPiece("0 0  0 1  1 0  0 2");
		test1.nextPiece(piece1);
		test1.move(Action.DROP);
		
		board1[1][0]=true;
		board1[1][1]=true;
		board1[2][0]=true;
		board1[1][2]=true;
		
		board2[1][0]=true;
		board2[1][1]=true;
		board2[2][0]=true;
		board2[1][2]=true;
		board2[1][3]=true;
		board2[1][4]=true;
		board2[2][3]=true;
		board2[2][4]=true;
		
		TetrisPiece piece2 = new TetrisPiece("0 0  0 1  1 0  1 1");
		test2.nextPiece(piece1);
		test2.move(Action.DROP);
		test2.nextPiece(piece2);
		test2.move(Action.DROP);
		
		assertArrayEquals("test drop", board1, test1.board);		
		assertArrayEquals("test drop", board2, test2.board);
		assertEquals("test action", Action.DROP, test1.getLastAction());
		assertEquals("test action", Action.DROP, test2.getLastAction());
		assertEquals("test result", Result.PLACE, test1.getLastResult());
		assertEquals("test result", Result.PLACE, test2.getLastResult());
	}

	@Test
	public void testGetGrid() {
		TetrisBoard test1 = new TetrisBoard(5, 5);
		
		boolean[][] board1 = new boolean[5][5];
		
		board1[0][0]=true;
		board1[4][4]=true;
		test1.board=board1;
		
		assertEquals("test get grid", true, test1.getGrid(0, 0));
		assertEquals("test get grid", true, test1.getGrid(4, 4));
		assertEquals("test get grid", false, test1.getGrid(3, 3));
		assertEquals("test get grid", true, test1.getGrid(-1, -1));
		assertEquals("test get grid", true, test1.getGrid(0, 5));
	}
	
}
