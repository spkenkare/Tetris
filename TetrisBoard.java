package assignment;

import java.awt.*;
import java.util.*;


/**
 * Represents a Tetris board -- essentially a 2-d grid of
 * booleans. Supports tetris pieces and row clearing.
 * Does not do any drawing or have any idea of pixels. Instead, just
 * represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {

    // JTetris will use this constructor
	private int width;
	private int height;
	private ArrayList<Action> actions;
	private ArrayList<Result> results;
	private boolean[][] board;
	private boolean[][] permanent;
	private TetrisPiece current;
	private Point[] location;
	private Point[] pastLocation;
	private ArrayList<Integer> currentXs;
	private ArrayList<Integer> currentYs;
	private int rowsCleared;
	private int cleared;
	private ArrayList<Integer> columnHeights;
	private ArrayList<Integer> rowWidths;
	private int maxHeight;
	private boolean obstructed;
	private int obstructedX;
	private int obstructedY;
	private boolean isTest;
	
    public TetrisBoard(int width, int height) {
    	this.width=width;
    	this.height=height;
    	actions = new ArrayList<Action>();
    	results = new ArrayList<Result>();
    	board = new boolean[width][height];
    	permanent = new boolean[width][height];
    	currentXs = new ArrayList<Integer>();
    	currentYs = new ArrayList<Integer>();
    	rowsCleared=0;
    	cleared=0;
    	columnHeights = new ArrayList<Integer>();
    	if(height>0) {
    		for(int i=0; i<board.length; i++) {
    			columnHeights.add(0);
    		}
    	}	
    	rowWidths = new ArrayList<Integer>();
    	if(width>0) {
    		for(int i=0; i<board[0].length; i++) {
        		rowWidths.add(0);
        	}
    	}    	
    	maxHeight=0;
    	obstructed=false;
    	obstructedX=0;
    	obstructedY=0;
    	isTest=false;
    }
    
    private void clear() {
    	
    	for(int c=0; c<permanent[0].length; c++) {
    		boolean rowFilled=true;
    		for(int r=0; r<permanent.length; r++) {
    			if(!permanent[r][c]) {
    				rowFilled=false;
    			}
    			
    		}
    		if(rowFilled) {
    			rowsCleared++;
    			cleared=c+1;
    			for(int r=0; r<permanent.length; r++) {
        			permanent[r][c]=false;
    				board[r][c]=false;
        		}
    			shift();
    			c--;   			
    		}
    	}
    	
    }
    
    private void shift() {
    	
    	System.out.println("shift called");
    	boolean[][] temp = new boolean[board.length][board[0].length];
    	for(int r=0; r<board.length; r++) {
    		for(int c=0; c<board[r].length; c++) {
    			temp[r][c]=board[r][c];
    		}
    	}
    	
    	for(int c=cleared-1; c<permanent.length-1; c++) {	
    		for(int r=0; r<permanent.length; r++) {    			
    			board[r][c]=temp[r][c+1];
           		permanent[r][c]=temp[r][c+1];  			
    		}
    	}
    	for(int c=permanent.length-1; c<permanent.length; c++) {	
    		for(int r=0; r<permanent.length; r++) {    			
    			board[r][c]=false;
           		permanent[r][c]=false;			
    		}
    	}
    	
    	
    }
    
    

    public Result move(Action act) {
    	obstructed=false;
    	getColumnHeights();
    	getRowWidths();
    	if(
    		location.length<1) {
    		return Result.NO_PIECE;
    	}
    	for(int i=0; i<location.length; i++) {
			int currentX = (int) location[i].getX();
			int currentY = (int) location[i].getY();
			pastLocation[i] = new Point(currentX, currentY);
			try {
				board[currentX][currentY]=false;
			}
			catch (Exception e) {
				
			}
		}
    	rowsCleared=0;
    	cleared=0;
    	clear();
    	
    	actions.add(act);
    	switch(act) {
    		case LEFT: {
    			return left();
    		}
    		case RIGHT: {   			
    			return right();
    		}
    		case DOWN: {
    			return down();
    		}
    		case DROP: {			
    			return this.drop();
    		}
    		case CLOCKWISE:  {
    			for(int i=0; i<3; i++) {
    				if(canRotate()) {
        				disappear();
            			rotate();
        			}
    				else if(obstructed) {
        				obstructedwallkick();
        			}
    				else {
        				if(!obstructed) {
        					disappear();
        					wallkick();
        				}
        			}
    			}
    			
    			results.add(Result.SUCCESS);
    			return Result.SUCCESS;
    		}
    		case COUNTERCLOCKWISE:  {
    			if(canRotate()) {
    				disappear();
    				rotate();
    			}
    			else if(obstructed) {
    				obstructedwallkick();
    			}
    			else {
    				disappear();
    				wallkick();
    			}
    			results.add(Result.SUCCESS);
    			return Result.SUCCESS;
    		}
    		case NOTHING: {
    			results.add(Result.SUCCESS);
    			return Result.SUCCESS;
    		}
    		default: {
    			results.add(Result.SUCCESS);
    			return Result.NO_PIECE;
    		}
    		
    	}
    }
    
    private void obstructedwallkick() {
    	
    	int maxY=0;
		int maxX=0;
		int minY=Integer.MAX_VALUE;
		int minX=Integer.MAX_VALUE;
	
		for(int i=0; i<location.length; i++) {
			int currentX = (int) pastLocation[i].getX();
			int currentY = (int) pastLocation[i].getY();
			if(currentY>maxY) {
				maxY = currentY;
			}
			if(currentX>maxX) {
				maxX = currentX;
			}
			if(currentY<minY) {
				minY = currentY;
			}
			if(currentX<minX) {
				minX = currentX;
			}
		}
    	
		TetrisPiece p = (TetrisPiece) current.nextRotation();
		
    	if(obstructed) {
    		int y=0;
			boolean canMove=false;
			Point[] rotationBody = p.getBody();
			Point anchor = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
			while(!canMove) {
				canMove=true;
		    	for(int i=0; i<current.getBody().length; i++) {
		    		int currentX = (int) ((int) pastLocation[i].getX()-current.getBody()[i].getX());
					int currentY = (int) ((int) pastLocation[i].getY()-current.getBody()[i].getY());
					if(currentX<anchor.getX()) {
						anchor = new Point(currentX, currentY);
					}
		    	}
		    	
		    	for(int i=0; i<rotationBody.length; i++) {
		    		try {
		    			int newX=(int) (anchor.getX()+rotationBody[i].getX());
		    			int newY=(int) (anchor.getY()+rotationBody[i].getY()-y);
		    			if(permanent[newX][newY]) {
		    				canMove=false;
		    			}
		    		}
		    		catch (Exception e) {
		    		}
		    			    		
		    	}
		    	y++;
		    	

			}
			if(canMove) {
	    		for(int i=0; i<rotationBody.length; i++) {
		    		try {
		    			int newX=(int) (anchor.getX()+rotationBody[i].getX()-y);
		    			int newY=(int) (anchor.getY()+rotationBody[i].getY());
		    			
		    			board[newX][newY]=true;
		    			location[i] = new Point(newX, newY);
		    		}
		    		catch (Exception e) {
		    		}
		    		
		    	}
	    		return;
	    	}
			
			
			int x=0;
			canMove=false;
			rotationBody = p.getBody();
			anchor = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
			while(!canMove) {
				canMove=true;
		    	for(int i=0; i<current.getBody().length; i++) {
		    		int currentX = (int) ((int) pastLocation[i].getX()-current.getBody()[i].getX());
					int currentY = (int) ((int) pastLocation[i].getY()-current.getBody()[i].getY());
					if(currentX<anchor.getX()) {
						anchor = new Point(currentX, currentY);
					}
		    	}
		    	
		    	for(int i=0; i<rotationBody.length; i++) {
		    		try {
		    			int newX=(int) (anchor.getX()+rotationBody[i].getX()-x);
		    			int newY=(int) (anchor.getY()+rotationBody[i].getY());
		    			if(permanent[newX][newY]) {
		    				canMove=false;
		    			}
		    		}
		    		catch (Exception e) {
		    		}
		    		
		    		
		    	}
		    	x++;
		    	
		    	

			}
			if(canMove) {
	    		for(int i=0; i<rotationBody.length; i++) {
		    		try {
		    			int newX=(int) (anchor.getX()+rotationBody[i].getX()-x);
		    			int newY=(int) (anchor.getY()+rotationBody[i].getY());
		    			
		    			board[newX][newY]=true;
		    			location[i] = new Point(newX, newY);
		    		}
		    		catch (Exception e) {
		    			e.getStackTrace();
		    		}
		    		
		    	}
	    		return;
	    	}
			
			y=0;
			canMove=false;
			rotationBody = p.getBody();
			anchor = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
			while(!canMove) {
				canMove=true;
		    	for(int i=0; i<current.getBody().length; i++) {
		    		int currentX = (int) ((int) pastLocation[i].getX()-current.getBody()[i].getX());
					int currentY = (int) ((int) pastLocation[i].getY()-current.getBody()[i].getY());
					if(currentX<anchor.getX()) {
						anchor = new Point(currentX, currentY);
					}
		    	}
		    	
		    	for(int i=0; i<rotationBody.length; i++) {
		    		try {
		    			int newX=(int) (anchor.getX()+rotationBody[i].getX());
		    			int newY=(int) (anchor.getY()+rotationBody[i].getY()+y);
		    			if(permanent[newX][newY]) {
		    				canMove=false;
		    			}
		    		}
		    		catch (Exception e) {
		    		}
		    	}
		    	y++;		    	
			}
			if(canMove) {
	    		for(int i=0; i<rotationBody.length; i++) {
		    		try {
		    			int newX=(int) (anchor.getX()+rotationBody[i].getX()-x);
		    			int newY=(int) (anchor.getY()+rotationBody[i].getY());		    			
		    			board[newX][newY]=true;
		    			location[i] = new Point(newX, newY);
		    		}
		    		catch (Exception e) {
		    			e.getStackTrace();
		    		}
		    		
		    	}
	    		return;
	    	}
			
			x=0;
			canMove=false;
			rotationBody = p.getBody();
			anchor = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
			while(!canMove) {
				canMove=true;
		    	for(int i=0; i<current.getBody().length; i++) {
		    		int currentX = (int) ((int) pastLocation[i].getX()-current.getBody()[i].getX());
					int currentY = (int) ((int) pastLocation[i].getY()-current.getBody()[i].getY());
					if(currentX<anchor.getX()) {
						anchor = new Point(currentX, currentY);
					}
		    	}
		    	
		    	for(int i=0; i<rotationBody.length; i++) {
		    		try {
		    			int newX=(int) (anchor.getX()+rotationBody[i].getX()+x);
		    			int newY=(int) (anchor.getY()+rotationBody[i].getY());
		    			if(permanent[newX][newY]) {
		    				canMove=false;
		    			}
		    		}
		    		catch (Exception e) {
		    		}    				    		
		    	}
		    	x++;
			}
			if(canMove) {
	    		for(int i=0; i<rotationBody.length; i++) {
		    		try {
		    			int newX=(int) (anchor.getX()+rotationBody[i].getX()-x);
		    			int newY=(int) (anchor.getY()+rotationBody[i].getY());
		    			
		    			board[newX][newY]=true;
		    			location[i] = new Point(newX, newY);
		    		}
		    		catch (Exception e) {
		    		}		
		    	}
	    		return;
	    	}
    	}
    	current = (TetrisPiece) p;
    }
    
    private void wallkick() {
    	Piece p = current.nextRotation();
		int width = p.getWidth();
		int height = p.getHeight();
		int maxY=0;
		int maxX=0;
		int minY=Integer.MAX_VALUE;
		int minX=Integer.MAX_VALUE;
	
		for(int i=0; i<location.length; i++) {
			int currentX = (int) pastLocation[i].getX();
			int currentY = (int) pastLocation[i].getY();
			if(currentY>maxY) {
				maxY = currentY;
			}
			if(currentX>maxX) {
				maxX = currentX;
			}
			if(currentY<minY) {
				minY = currentY;
			}
			if(currentX<minX) {
				minX = currentX;
			}
		}
		int y = 0;
		while((height+maxY-y>=getHeight())) {
			Point anchor = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
	    	
	    	for(int i=0; i<current.getBody().length; i++) {
	    		int currentX = (int) ((int) pastLocation[i].getX()-current.getBody()[i].getX());
				int currentY = (int) ((int) pastLocation[i].getY()-current.getBody()[i].getY());
				board[currentX][currentY]=false;
				board[(int) pastLocation[i].getX()][(int) pastLocation[i].getY()]=false;
				board[(int) current.getBody()[i].getX()][(int) current.getBody()[i].getY()]=false;
				if(currentX<anchor.getX()) {
					anchor = new Point(currentX, currentY);
				}
	    	}
	    	Point[] rotationBody = p.getBody();
	    	boolean canMove=true;
	    	for(int i=0; i<rotationBody.length; i++) {
	    		try {
	    			int newX=(int) (anchor.getX()+rotationBody[i].getX());
	    			int newY=(int) (anchor.getY()+rotationBody[i].getY()-y);
	    			if(newX>=getWidth()||newY>=getHeight()) {
	    				canMove=false;
	    			}
	    		}
	    		catch (Exception e) {
	    			canMove=false;
	    		}
	    		
	    	}
	    	int check = 0;
	    	if(canMove) {
	    		
	    		for(int i=0; i<rotationBody.length; i++) {
		    		try {
		    			int newX=(int) (anchor.getX()+rotationBody[i].getX());
		    			int newY=(int) (anchor.getY()+rotationBody[i].getY()-y);
		    			board[newX][newY]=true;
		    			location[i] = new Point(newX, newY);
		    			check++;
		    		}
		    		catch (Exception e) {
		    			e.getStackTrace();
		    		}
		    		
		    	}
	    	}
	    	if(check==rotationBody.length) {
	    		break;
	    	}
	    	y++;

		}
		
		int x=0;
		while(width+minX-x>=getWidth()) {
			Point anchor = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
	    	
			for(int i=0; i<current.getBody().length; i++) {
	    		int currentX = (int) ((int) pastLocation[i].getX()-current.getBody()[i].getX());
				int currentY = (int) ((int) pastLocation[i].getY()-current.getBody()[i].getY());
				board[currentX][currentY]=false;
				board[(int) pastLocation[i].getX()][(int) pastLocation[i].getY()]=false;
				board[(int) current.getBody()[i].getX()][(int) current.getBody()[i].getY()]=false;
				if(currentX<anchor.getX()) {
					anchor = new Point(currentX, currentY);
				}
	    	}
	    	Point[] rotationBody = p.getBody();
	    	boolean canMove=true;
	    	for(int i=0; i<rotationBody.length; i++) {
	    		try {
	    			int newX=(int) (anchor.getX()+rotationBody[i].getX()-x);
	    			int newY=(int) (anchor.getY()+rotationBody[i].getY());
	    			if(newX>=getWidth()||newY>=getHeight()) {
	    				canMove=false;
	    			}
	    		}
	    		catch (Exception e) {
	    			canMove=false;
	    		}
	    		
	    	}
	    	int check = 0;
	    	if(canMove) {
	    		
	    		for(int i=0; i<rotationBody.length; i++) {
		    		try {
		    			int newX=(int) (anchor.getX()+rotationBody[i].getX()-x);
		    			int newY=(int) (anchor.getY()+rotationBody[i].getY());
		    			board[newX][newY]=true;
		    			location[i] = new Point(newX, newY);
		    			check++;
		    		}
		    		catch (Exception e) {
		    			e.getStackTrace();
		    		}
		    		
		    	}
	    	}
	    	if(check==rotationBody.length) {
	    		break;
	    	}
	    	x++;
		}
    	current = (TetrisPiece) p;
    }
    
    private boolean canRotate() {
		Piece p = current.nextRotation();
		int width = p.getWidth();
		int height = p.getHeight();
		int maxY=0;
		int maxX=0;
		int minY=Integer.MAX_VALUE;
		int minX=Integer.MAX_VALUE;
		
		for(int i=0; i<location.length; i++) {
			int currentX = (int) location[i].getX();
			int currentY = (int) location[i].getY();
			if(currentY>maxY) {
				maxY = currentY;
			}
			if(currentX>maxX) {
				maxX = currentX;
			}
			if(currentY<minY) {
				minY = currentY;
			}
			if(currentX<minX) {
				minX = currentX;
			}
		}
		
		
		
		if((height+maxY>=getHeight())) {
			return false;
		}
		if(width+minX>=getWidth()) {
			return false;
		}
		
		for(int i=0; i<current.getBody().length; i++) {
    		int currentX = (int) ((int) pastLocation[i].getX()-current.getBody()[i].getX()+p.getBody()[i].getX());
			int currentY = (int) ((int) pastLocation[i].getY()-current.getBody()[i].getY()+p.getBody()[i].getY());
			try {
				if(permanent[currentX][currentY]) {
					obstructed=true;
					obstructedX=currentX;
					obstructedY=currentY;
				}
				else {
				}
			}
			catch(Exception e) {				
			}   		
    	}
		if(obstructed) {
			return false;
		}
		return true;
    }
    
    private void disappear() {
    	try {
    		for(int i=0; i<location.length; i++) {
    			int currentX = (int) location[i].getX();
    			int currentY = (int) location[i].getY();
    			pastLocation[i] = new Point(currentX, currentY);
    			board[currentX][currentY]=false;
    		}
    	}
    	catch (Exception e) {
    	}
    	location = new Point[location.length];
    	
    }

    private Result down() {
    	
		boolean atBottom=false;
		
		try {
			for(int r=0; r<location.length; r++) {
				int currentX = (int) location[r].getX();
				int currentY = (int) location[r].getY();
				currentXs.add(currentX);
				currentYs.add(currentY);
    	
			}
		}
		catch (Exception e) {
			return Result.SUCCESS;
		}
		
		for(int r=0; r<location.length; r++) {
			int currentX = (int) location[r].getX();
			int currentY = (int) location[r].getY();
			if(currentY==getColumnHeight(currentX)) {
				atBottom=true;    					
			}			
		}
		if(!atBottom) {			
			for(int r=0; r<location.length; r++) {				
				int currentX = (int) location[r].getX();
				int currentY = (int) location[r].getY();			
				try {
					if(board[currentX][currentY-1]&&(!currentXs.contains(currentX)||!currentYs.contains(currentY-1)))  {
						results.add(Result.OBSTRUCTED);
						return Result.OBSTRUCTED;
					}
				}
				catch (Exception e) {					
				}			
			}
			for(int r=0; r<location.length; r++) {
				int currentX = (int) location[r].getX();
				int currentY = (int) location[r].getY();
				location[r] = new Point(currentX, currentY-1);
				board[currentX][currentY]=false;
			}
			try {
				for(int r=0; r<location.length; r++) {
					int currentX = (int) location[r].getX();
					int currentY = (int) location[r].getY();
					board[currentX][currentY]=true;
				}
			} 
			catch (Exception e) {
				
			}
			results.add(Result.SUCCESS);
			return Result.SUCCESS;
		}
		else {
			for(int i=0; i<location.length; i++) {
				int currentX = (int) location[i].getX();
				int currentY = (int) location[i].getY();
				if(permanent[currentX][currentY]) {
					results.add(Result.OBSTRUCTED);
					return Result.OBSTRUCTED;
				}
				board[(int) location[i].getX()][(int) location[i].getY()]=true;
				permanent[(int) location[i].getX()][(int) location[i].getY()]=true;		
			}
			results.add(Result.PLACE);
			return Result.PLACE;
		}
    }
    
    private Result left() {
    	for(int r=0; r<location.length; r++) {
    		int currentX = (int) location[r].getX();
			int currentY = (int) location[r].getY();
			currentXs.add(currentX);
			currentYs.add(currentY);
    	}
		boolean atLeft=false;
		for(int r=0; r<location.length; r++) {
			int currentX = (int) location[r].getX();
			if(currentX==0) {
				atLeft=true;
			}
		}
		if(!atLeft) {
			for(int r=0; r<location.length; r++) {
					int currentX = (int) location[r].getX();
					int currentY = (int) location[r].getY();
					if(board[currentX-1][currentY]&&(!currentXs.contains(currentX-1)||!currentYs.contains(currentY)))  {
						results.add(Result.OBSTRUCTED);
						return Result.OBSTRUCTED;
					}
					if(permanent[currentX-1][currentY]&&(!currentXs.contains(currentX-1)||!currentYs.contains(currentY))) {
						results.add(Result.OBSTRUCTED);
						return Result.OBSTRUCTED;
					}		
			}
			for(int r=0; r<location.length; r++) {
				int currentX = (int) location[r].getX();
				int currentY = (int) location[r].getY();
				location[r] = new Point(currentX-1, currentY);
				board[currentX][currentY]=false;
			}
			for(int r=0; r<location.length; r++) {
				int currentX = (int) location[r].getX();
				int currentY = (int) location[r].getY();
				board[currentX][currentY]=true;
			}
			results.add(Result.SUCCESS);
			return Result.SUCCESS;
		}
		else {
			results.add(Result.OUT_BOUNDS);
			return Result.OUT_BOUNDS;
		}
    }
    
    private Result right() {
    	for(int r=0; r<location.length; r++) {
    		int currentX = (int) location[r].getX();
			int currentY = (int) location[r].getY();
			currentXs.add(currentX);
			currentYs.add(currentY);
    	}
		boolean atRight=false;
		for(int r=0; r<location.length; r++) {
			int currentX = (int) location[r].getX();
			if(currentX==getWidth()-1) {
				atRight=true;
			}
		}
		if(!atRight) {
			for(int r=0; r<location.length; r++) {
				int currentX = (int) location[r].getX();
				int currentY = (int) location[r].getY();			
				if(board[currentX+1][currentY]&&(!currentXs.contains(currentX+1)||!currentYs.contains(currentY)))  {
					results.add(Result.OBSTRUCTED);
					return Result.OBSTRUCTED;
				}
				if(permanent[currentX+1][currentY]&&(!currentXs.contains(currentX+1)||!currentYs.contains(currentY))) {
					results.add(Result.OBSTRUCTED);
					return Result.OBSTRUCTED;
				}		
			}
			for(int r=0; r<location.length; r++) {
				int currentX = (int) location[r].getX();
				int currentY = (int) location[r].getY();
				location[r] = new Point(currentX+1, currentY);
				board[currentX][currentY]=false;
			}
			for(int r=0; r<location.length; r++) {
				int currentX = (int) location[r].getX();
				int currentY = (int) location[r].getY();
				board[currentX][currentY]=true;
			}
			results.add(Result.SUCCESS);
			return Result.SUCCESS;
		}
		else {
			results.add(Result.OUT_BOUNDS);
			return Result.OUT_BOUNDS;
		}
    }

    private Result drop() {
    	
		boolean canMove=true; 
		getColumnHeights();
		for(int i=0; i<location.length; i++) {
			int currentX = (int) location[i].getX();
			int newY = getColumnHeight(currentX);
			try {
				if(permanent[currentX][newY]) {
					canMove=false;
				}
			}
			catch (Exception e) {
				
			}
						
		}
		if(canMove) {
			int max=0;
			ArrayList<Integer> heights = new ArrayList<Integer>();
			for(int i=0; i<location.length; i++) {
				int currentX = (int) location[i].getX();
				heights.add(getColumnHeight(currentX));
				if(max<getColumnHeight(currentX)) {
					max = getColumnHeight(currentX);
				}
			}
			boolean moveDown=true;
			boolean allZeros=true;
			for(int i=0; i<location.length; i++) {
				int currentX = (int) location[i].getX();
				if(getColumnHeight(currentX)!=0) {
					allZeros=false;
				}
				int newY = (int) (max+current.getBody()[i].getY());
				try {
					if(board[currentX][newY-1]) {
						moveDown=false;
					}
				}
				catch (Exception e) {			
				}
			}
			if(moveDown&&!allZeros) {
				for(int i=0; i<location.length; i++) {
					int currentX = (int) location[i].getX();
					int newY = (int) (max+current.getBody()[i].getY());					
					try {
						board[currentX][newY-1]=true;
						permanent[currentX][newY-1]=true;	
					}
					catch (Exception e) {
						
					}
									
				}
			}
			else {
				for(int i=0; i<location.length; i++) {
					int currentX = (int) location[i].getX();
					int newY = (int) (max+current.getBody()[i].getY());
					try {
						board[currentX][newY]=true;
						permanent[currentX][newY]=true;
					}
					catch(Exception e) {
						
					}					
				}
			}
			
		}  	
		results.add(Result.PLACE);
		return Result.PLACE;
    }

    public Board testMove(Action act) {
    	boolean[][] copy = new boolean[board.length][board[0].length];
    	boolean[][] copyPermanent = new boolean[board.length][board[0].length];
    	for(int r=0; r<board.length; r++) {
    		for(int c=0; c<board[r].length; c++) {
    			copy[r][c]=board[r][c];
    			copyPermanent[r][c]=permanent[r][c];
    		}
    	}
    	TetrisBoard otherBoard = new TetrisBoard(board.length, board[0].length);
    	otherBoard.board=copy;
    	otherBoard.permanent=copyPermanent;
    	otherBoard.isTest=true;
    	if(current==null) {
    		return otherBoard;
    	}
    	for(int i=0; i<columnHeights.size(); i++) {
    		columnHeights.set(i, 0);
    	}    	
    	otherBoard.current=current;
    	otherBoard.location=new Point[location.length];
    	otherBoard.pastLocation=new Point[location.length];
    	for(int i=0; i<location.length; i++) {
    		otherBoard.location[i]=location[i];
    		otherBoard.pastLocation[i]=pastLocation[i];
    	}
    	
    	if(act==Board.Action.DROP) {
    		for(int r=0; r<otherBoard.board.length; r++) {
    			for(int c=0; c<otherBoard.board[r].length; c++) {
    			}
    		}
    	}
    	otherBoard.move(act);    
    	
    	if(act==Board.Action.COUNTERCLOCKWISE) {
    		otherBoard.move(Board.Action.DROP);
    	}
    	else if(act==Board.Action.CLOCKWISE) {
    		otherBoard.move(Board.Action.DROP);
    	}
    	
    	otherBoard.clear();
    	otherBoard.getColumnHeights();
    	return otherBoard;
    }

    public void nextPiece(Piece p) {
    	current=(TetrisPiece) p;
    	Point[] points = p.getBody();
    	location = new Point[points.length];
    	pastLocation = new Point[points.length];
    	for(int i=0; i<points.length; i++) {
    		int xVal = (int) (getWidth()/2-1+points[i].getX());
    		int yVal = (int) (getHeight()-current.getHeight()+points[i].getY());
    		board[xVal][yVal]=true;
    		location[i]=new Point(xVal, yVal);
    		pastLocation[i]=new Point(xVal, yVal);
    	}
    }
    
    private void rotate() {
    	Piece p = current.nextRotation();    	
    	Point anchor = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
    	
    	for(int i=0; i<current.getBody().length; i++) {
    		int currentX = (int) ((int) pastLocation[i].getX()-current.getBody()[i].getX());
			int currentY = (int) ((int) pastLocation[i].getY()-current.getBody()[i].getY());
			if(currentX<anchor.getX()) {
				anchor = new Point(currentX, currentY);
			}
    	}
    	Point[] rotationBody = p.getBody();
    	for(int i=0; i<rotationBody.length; i++) {
    		try {
    			int newX=(int) (anchor.getX()+rotationBody[i].getX());
    			int newY=(int) (anchor.getY()+rotationBody[i].getY());
    			
    			board[newX][newY]=true;
    			location[i] = new Point(newX, newY);
    		}
    		catch (Exception e) {
    		}    		
    	}
    	current = (TetrisPiece) p;
    }

    public boolean equals(Object other) { 
    	if(other instanceof TetrisBoard) {
    		boolean equal = true;
    		if(((TetrisBoard) other).getHeight()==getHeight()&&((TetrisBoard) other).getWidth()==getWidth()) {
    			boolean[][] otherBoard = ((TetrisBoard) other).board;
    			for(int r=0; r<board.length; r++) {
    				for(int c=0; c<board[r].length; c++) {
    					if(board[r][c]!=otherBoard[r][c]) {
    						equal = false;
    					}
    				}
    			}
    			return equal;
    		}
    		return false;	
    	}
    	return false; 
    }

    public Result getLastResult() { 
    	try {
    		return results.get(results.size()-1);
    	}
    	catch (Exception e) {
    		return null;
    	}
    }

    public Action getLastAction() { 
    	try {
    		return actions.get(actions.size()-1);
    	}
    	catch (Exception e) {
    		return null;
    	}
    }

    public int getRowsCleared() { 
    	return rowsCleared; 
    }

    public int getWidth() { 
    	return width; 
    }

    public int getHeight() { 
    	return height; 
    }

    public int getMaxHeight() { 	
    	return maxHeight;
    }

    public int dropHeight(Piece piece, int x) { 
    	int[] skirt = piece.getSkirt();
    	return getColumnHeight(x)+skirt[0];
    }

    public int getColumnHeight(int x) { 
    	if(board.length==0||board[0].length==0) {
    		return 0;
    	}
    	return columnHeights.get(x);
    }

    private void getColumnHeights() {
    	int max = 0;
    	for(int r=0; r<board.length; r++) {
    		int numRows = board[r].length;
        	int height = -1;
        	for(int i=0; i<numRows; i++) {
        		
        		if(getPermanentGrid(r, i)) {
        			height=i; 
        		}
        	}
        	if((height+1)>max) {
        		max=height+1;
        	}
        	columnHeights.set(r,  height+1);
    	}
    	maxHeight=max;
    }
    
  
    public int getRowWidth(int y) {
    	if(board.length==0||board[0].length==0) {
    		return 0;
    	}
    	return rowWidths.get(y);
    }
    
    private void getRowWidths() {
    	
    	for(int c=0; c<board[0].length; c++) {
    		int count=0;
        	for(int i=0; i<board.length; i++) {
        		if(getGrid(i, c)) {
        			count++;
        		}
        	}
        	rowWidths.set(c, count);
    	}
    }

    public boolean getGrid(int x, int y) { 
    	if(x>=width||y>=height||x<0||y<0) {
    		return true;
    	}
    	return board[x][y]; 
    }
     
    private boolean getPermanentGrid(int x, int y) { 
    	return permanent[x][y]; 
    }

}
