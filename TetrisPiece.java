package assignment;

import java.awt.*;
import java.util.*;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * Each piece is defined by the blocks that make up its body.
 */
public final class TetrisPiece extends Piece {
    /**
     * Return a Piece class from a String representation of the points
     * in the piece's body
     */
	private String pieceString;
	private Point[] body;
	private int height;
	private int width;
	private int[] skirt;
	
	private TetrisPiece(String pieces) {
		pieceString=pieces;
		body=getBody();
		getPieceInfo();
		width=getWidth();
		height=getHeight();
		skirt=getSkirt();
	}
	
	public Piece nextRotation() {
		int max = 0;
		max = Math.max(width, height);
		double cartesianCenterX = 0;
		double cartesianCenterY = 0;
		for(int i=0; i<body.length; i++) {
			int currentX = (int) body[i].getX();
			int currentY = (int) body[i].getY();
			cartesianCenterX += currentX + 0.5;
			cartesianCenterY += (currentY+0.5);
		}
		cartesianCenterX/=body.length;
		cartesianCenterY/=body.length;
		double boxCenterX=max/2.0;
		double boxCenterY=max/2.0;
		
		ArrayList<boolean[][]> list = new ArrayList<boolean[][]>();
		ArrayList<double[]> centers = new ArrayList<double[]>();
		for(int r=0; r<max; r++) {
			for(int c=0; c<max; c++) {
				try {
					boolean[][] boundingSquare = new boolean[max][max];
					for(int i=0; i<body.length; i++) {
						int x = (int)body[i].getX();
						int y = (int)body[i].getY();
						boundingSquare[c+y][r+x]=true;
					}
					double[] array = new double[2];
					array[0]=cartesianCenterX+r;
					array[1]=max-(cartesianCenterY+c);
					list.add(boundingSquare);
					centers.add(array);
				}
				catch (Exception e) {
					
				}
				
			}
		}
		double minDistance = Double.MAX_VALUE;
		ArrayList<Double> distances = new ArrayList<Double>();
		for(int i=0; i<list.size(); i++) {
			int count=0;
			boolean[][] square = list.get(i);
			for(int r=0; r<square.length; r++) {
				for(int c=0; c<square[r].length; c++) {
					if(square[r][c]) {
						count++;
					}
				}
			}
			if(count!=body.length) {
				list.remove(i);
				centers.remove(i);
			}
			else {
				double xDiff = boxCenterX - centers.get(i)[0];
				double yDiff = boxCenterY - centers.get(i)[1];
				double distance = Math.sqrt(xDiff*xDiff+yDiff*yDiff);
				if(distance<minDistance) {
					minDistance=distance;
				}
				distances.add(distance);
			}
		}
		for(int i=0; i<distances.size(); i++) {
			if(distances.get(i)>minDistance) {
				distances.remove(i);
				list.remove(i);
				centers.remove(i);
				i--;
			}
		}
		
		double minX = Double.MAX_VALUE;
		ArrayList<Double> Xs = new ArrayList<Double>();
		for(int i=0; i<list.size(); i++) {
			double x = centers.get(i)[0];
			if(x<minX) {
				minX=x;
			}
			Xs.add(x);
		}
		
		for(int i=0; i<Xs.size(); i++) {
			if(Xs.get(i)>minX) {
				Xs.remove(i);
				list.remove(i);
				centers.remove(i);
				i--;
			}
		}
		
		if(list.size()!=1) {
			double minY = Double.MAX_VALUE;
			ArrayList<Double> Ys = new ArrayList<Double>();
			for(int i=0; i<list.size(); i++) {
				double y = centers.get(i)[1];
				if(y<minY) {
					minY=y;
				}
				Ys.add(y);
			}
			
			for(int i=0; i<Ys.size(); i++) {
				if(Ys.get(i)>minY) {
					Ys.remove(i);
					list.remove(i);
					centers.remove(i);
					i--;
				}
			}
		}
		
		boolean[][] boundingSquare = list.get(0);
		boolean[][] rotation = new boolean[boundingSquare.length][boundingSquare[0].length];
		
		for(int r=0; r<boundingSquare.length; r++) {
			for(int c=0; c<boundingSquare[r].length; c++) {
				rotation[r][c]=boundingSquare[c][r];
			}
		}
		
		for(int r=0; r<boundingSquare.length; r++) {
			for(int c=0; c<boundingSquare[r].length/2; c++) {
				boolean temp=rotation[r][c];
				rotation[r][c]=rotation[r][boundingSquare.length-c-1];
				rotation[r][boundingSquare.length-c-1]=temp;
			}
		}
		
		
		
		int leftmost = Integer.MAX_VALUE;
		for(int r=0; r<boundingSquare.length; r++) {
			for(int c=0; c<boundingSquare[r].length; c++) {
				if(c<leftmost&&rotation[r][c]) {
					leftmost=c;
				}
			}
		}
		boolean[][] rotationShiftedLeft = new boolean[rotation.length][rotation[0].length];
		if(leftmost!=0) {
			for(int r=0; r<boundingSquare.length; r++) {
				for(int c=0; c<boundingSquare[r].length; c++) {
					if(rotation[r][c]) {
						rotationShiftedLeft[r][c-leftmost]=true;
					}
				}
			}
			
		}
		else {
			rotationShiftedLeft = rotation;
		}
		
		int downmost = Integer.MAX_VALUE;
		for(int r=0; r<boundingSquare.length; r++) {
			for(int c=0; c<boundingSquare[r].length; c++) {
				if(r<downmost&&rotationShiftedLeft[r][c]) {
					downmost=r;
				}
			}
		}
		boolean[][] rotationShifted = new boolean[rotation.length][rotation[0].length];
		if(downmost!=0) {
			for(int r=0; r<boundingSquare.length; r++) {
				for(int c=0; c<boundingSquare[r].length; c++) {
					if(rotation[r][c]) {
						rotationShifted[r-downmost][c]=true;
					}
				}
			}
			
		}
		else {
			rotationShifted = rotationShiftedLeft;
		}
		
		String rotationString="";
		for(int r=0; r<boundingSquare.length; r++) {
			for(int c=0; c<boundingSquare[r].length; c++) {
				if(rotationShifted[r][c]) {
					rotationString+=(c+" "+r+"  ");
				}
			}
		}
		
		
		next=new TetrisPiece(rotationString);
		return next;
	}


	
    public static Piece getPiece(String pieceString) { 

    	return new TetrisPiece(pieceString);
    }

    private void getPieceInfo() {
    	this.body=parsePoints(pieceString);
    	double maxX=0;
		if(body.length==0) {
			width = 0;
		}
		else {
			for(int i=0; i<body.length; i++)
			{
				if(body[i].getX()>maxX)
					maxX=body[i].getX();
			}
			width = (int)(maxX+1);
		}
		
		double maxY=0;
    	if(body.length==0) {
			height = 0;
		}
    	else {
    		for(int i=0; i<body.length; i++)
    		{
    			if(body[i].getY()>maxY)
    				maxY=body[i].getY();
    		}
    		height = (int)(maxY+1);
    	}
    	
    	int[] skirt = new int[width];
    	HashMap<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
    	for(int i=0; i<width; i++)
    	{
    		ArrayList<Integer> list = new ArrayList<Integer>();
    		list.add(Integer.MAX_VALUE);
    		map.put(i,list);
    	}
    	
    	for(int i=0; i<body.length; i++)
    	{
    		ArrayList<Integer> update = map.get((int)(body[i].getX()));
    		update.add((int)(body[i].getY()));
    		map.put((int)(body[i].getX()), update);
    	}
    	
    	for (Map.Entry<Integer, ArrayList<Integer>> entry : map.entrySet()) {
    		int min = Integer.MAX_VALUE;
    		for(int i=0; i<entry.getValue().size(); i++) {
    			if(entry.getValue().get(i)<min)
    				min=entry.getValue().get(i);
    		}
    		skirt[entry.getKey()]=min;
    	}
    	this.skirt=skirt;
		
    }

    public int getWidth() {
    	return width;
    }

    public int getHeight() { 
    	return height;
    }

    public Point[] getBody() { 
    	return body; 
    }

    public int[] getSkirt() {
    	
    	return skirt;
    }

    //writing this assuming that other is a Piece object	
    public boolean equals(Object other) { 
    	if(other instanceof TetrisPiece) {
    		Point[] otherPoints = ((TetrisPiece) other).getBody();
    		if(otherPoints.length!=body.length) {
    			return false;
    		}
    		ArrayList<Point> list1 = new ArrayList<Point>();
    		ArrayList<Point> list2 = new ArrayList<Point>();
    		ArrayList<Point> list3 = new ArrayList<Point>();
    		ArrayList<Point> list4 = new ArrayList<Point>();
    		Point[] ray2 = nextRotation().getBody();
    		Point[] ray3 = nextRotation().nextRotation().getBody();
    		Point[] ray4 = nextRotation().nextRotation().nextRotation().getBody();
    		boolean isFirst=true;
    		boolean isSecond=true;
    		boolean isThird=true;
    		boolean isFourth=true;
    		ArrayList<Point> otherList = new ArrayList<Point>();
    		for(int i=0; i<body.length; i++) {
    			list1.add(body[i]);
    			list2.add(ray2[i]);
    			list3.add(ray3[i]);
    			list4.add(ray4[i]);
    			otherList.add(otherPoints[i]);
    		}
    		for(int j=0; j<list1.size(); j++) {
    			if(!otherList.contains(list1.get(j))) {
    				isFirst=false;
    			}
    		}
    		
    		
    		for(int j=0; j<list2.size(); j++) {
    			if(!otherList.contains(list2.get(j))) {
    				isSecond= false;
    			}
    		}
    		
    		
    		for(int j=0; j<list1.size(); j++) {
    			if(!otherList.contains(list3.get(j))) {
    				isThird= false;
    			}
    		}
    		
    		
    		for(int j=0; j<list1.size(); j++) {
    			if(!otherList.contains(list4.get(j))) {
    				isFourth= false;
    			}
    		}
    		    		
    		return (isFirst||isSecond||isThird||isFourth);
    		
    	}
    	return false;
    }
}
