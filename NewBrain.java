package assignment;

import java.util.*;

/**
 * A Lame Brain implementation for JTetris
 */

public class NewBrain implements Brain {

    private ArrayList<Board> options;
    private ArrayList<Board.Action> firstMoves;
    private ArrayList<ArrayList<Board.Action>> moves;

    /**
     * Decide what the next move should be based on the state of the board.
     */
    public Board.Action nextMove(Board currentBoard) {
        // Fill the our options array with versions of the new Board
        options = new ArrayList<>();
        firstMoves = new ArrayList<>();
        moves = new ArrayList<>();
        enumerateOptions(currentBoard);

        int best = 0;
        int bestIndex = 0;
        // Check all of the options and get the one with the highest score
        for (int i = 0; i < options.size(); i++) {
            int score = scoreBoard(options.get(i));
            //System.out.println(score);
            if (score > best) {
                best = score;
                bestIndex = i;
            }
        }

        // We want to return the first move on the way to the best Board
        System.out.println("BEST: "+best);
        return firstMoves.get(bestIndex);
    }

    /**
     * Test all of the places we can put the current Piece.
     * Since this is just a Lame Brain, we aren't going to do smart
     * things like rotating pieces.
     */
    private void enumerateOptions(Board currentBoard) {
    	// We can always drop our current Piece
        options.add(currentBoard.testMove(Board.Action.DROP));
        firstMoves.add(Board.Action.DROP);
        
        Board counterclockwise = currentBoard.testMove(Board.Action.COUNTERCLOCKWISE); 
        options.add(counterclockwise);
        firstMoves.add(Board.Action.COUNTERCLOCKWISE);
        
        Board clockwise = currentBoard.testMove(Board.Action.CLOCKWISE); 
        options.add(clockwise);
        firstMoves.add(Board.Action.CLOCKWISE);

        // Now we'll add all the places to the left we can DROP
        Board left = currentBoard.testMove(Board.Action.LEFT);
        while (left.getLastResult() == Board.Result.SUCCESS) {
            options.add(left.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.LEFT);
            left.move(Board.Action.LEFT);
        }

        // And then the same thing to the right
        Board right = currentBoard.testMove(Board.Action.RIGHT);
        while (right.getLastResult() == Board.Result.SUCCESS) {
            options.add(right.testMove(Board.Action.DROP));
            firstMoves.add(Board.Action.RIGHT);
            right.move(Board.Action.RIGHT);
        }
    }

    /**
     * Since we're trying to avoid building too high,
     * we're going to give higher scores to Boards with
     * MaxHeights close to 0.
     */
    private int scoreBoard(Board newBoard) {
    	int score = 100;
    	score-=1*newBoard.getMaxHeight();
    	for(int i=0; i<newBoard.getWidth(); i++) {
    		score-=newBoard.getColumnHeight(i);
    		//System.out.println(newBoard.getColumnHeight(i));
    	}
    	score+=10*newBoard.getRowsCleared();
    	//System.out.println(score);
        return score;
    }

}
