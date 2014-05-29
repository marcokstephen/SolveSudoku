package com.sm.solvesudoku;

import java.util.ArrayList;
import java.util.List;

public class Main {
	
	public static void main(String[] args){
		String boardstring = "003020600900305001001806400008102900700000008006708200002609500800203009005010300";
		Board gameboard = create_board(boardstring);
		//set_possible(gameboard);
		//System.out.println(gameboard.getSquarearray()[0].getPossible().size());
	}
	
	public static Board create_board(String boardstring){
		Square[] sqarray = new Square[81];
		for (int i = 0; i < 81; i++){
			int value = Character.getNumericValue(boardstring.charAt(i));
			List<Integer> possibleList = new ArrayList<Integer>();
			if (value == 0){
				sqarray[i] = new Square(false, 0, possibleList);
			} else {
				sqarray[i] = new Square(true, value, possibleList);
			}
		}
		Board board = new Board(sqarray);
		return board;
	}
	
	public static void set_possible(Board gameboard){
		Square[] squarearray = gameboard.getSquarearray();
		for (int i = 0; i < squarearray.length; i++){ //squarearray.length == 81
			Square temp = squarearray[i];
			if (!temp.isAssigned()){
				int groupNumber = Board.getGroupNumber(i);
				int rowNumber = i/9;
				int columnNumber = i%9;
				List<Integer> possibleList = temp.getPossible();
				List<Square> group = gameboard.getGroups()[groupNumber];
				List<Square> row = gameboard.getRows()[rowNumber];
				List<Square> column = gameboard.getColumns()[columnNumber];
				
				for (int m = 1; m < 10; m++){
					if (!(group.contains(m) || row.contains(m) || column.contains(m))){
						possibleList.add(m);
					}
				}
			}
		}
	}
}

//4,5