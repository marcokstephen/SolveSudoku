package com.sm.solvesudoku;

import java.util.ArrayList;
import java.util.List;

public class Main {
	
	public static void main(String[] args){
		String boardstring = "003020600900305001001806400008102900700000008006708200002609500800203009005010300";
		Board gameboard = create_board(boardstring);
		set_possible(gameboard);
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
		
	}
}