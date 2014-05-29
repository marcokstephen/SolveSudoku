package com.sm.solvesudoku;

import java.util.ArrayList;
import java.util.List;

public class Main {
	static boolean solved = false;
	
	public static void main(String[] args){
		String boardstring = "003020600900305001001806400008102900700000008006708200002609500800203009005010300";
		//String boardstring = "200080300060070084030500209000105408000000000402706000301007040720040060004010003";
		Board gameboard = create_board(boardstring);
		int numberUnsolved = generatePossibleList(gameboard);
		int testResult = 0;
		while (testResult != numberUnsolved){
			//this loop runs as long as the number of unsolved squares keeps decreasing
			//System.out.println("Number unsolved: " + numberUnsolved);
			testResult = numberUnsolved;
			gameboard = new Board(gameboard.getSquarearray());
			numberUnsolved = refreshPossibleList(gameboard);
			if (numberUnsolved == 0){
				printToString(gameboard);
				solved = true;
				break;
			}
		}
		
		if (!solved) System.out.println("Sorry, that one is too hard...");
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
	
	public static int generatePossibleList(Board gameboard){
		Square[] squarearray = gameboard.getSquarearray();
		int count = 0;
		
		for (int i = 0; i < squarearray.length; i++){ //squarearray.length == 81
			Square temp = squarearray[i];
			if (!temp.isAssigned()){
				int groupNumber = Board.getGroupNumber(i);
				int rowNumber = i/9;
				int columnNumber = i%9;
				List<Integer> possibleList = temp.getPossible();
				List<Integer> group = gameboard.getGroups(groupNumber);
				List<Integer> row = gameboard.getRows(rowNumber);
				List<Integer> column = gameboard.getColumns(columnNumber);
				
				for (int m = 1; m < 10; m++){
					if (!(group.contains(m) || row.contains(m) || column.contains(m))){
						possibleList.add(m);
					}
				}
				if (possibleList.size() == 1){
					int value = possibleList.get(0);
					temp.setAssigned(true);
					temp.setValue(value);
					temp.setPossible(null);
				} else {
					count++;
				}
			}
		}
		return count; //this count is used to see if there is an improvement
	}
	
	public static int refreshPossibleList(Board gameboard){
		int count = 0;
		Square[] squarearray = gameboard.getSquarearray();
		
		for (int i = 0; i < squarearray.length; i++){
			Square temp = squarearray[i];
			temp.setPossible(new ArrayList<Integer>());
			if (!temp.isAssigned()){
				int groupNumber = Board.getGroupNumber(i);
				int rowNumber = i/9;
				int columnNumber = i%9;
				List<Integer> possibleList = temp.getPossible();
				List<Integer> group = gameboard.getGroups(groupNumber);
				List<Integer> row = gameboard.getRows(rowNumber);
				List<Integer> column = gameboard.getColumns(columnNumber);
				
				for (int m = 1; m < 10; m++){
					if (!(group.contains(m) || row.contains(m) || column.contains(m))){
						possibleList.add(m);
					}
				}
				if (possibleList.size() == 1){
					int value = possibleList.get(0);
					temp.setAssigned(true);
					temp.setValue(value);
					temp.setPossible(null);
				} else {
					count++;
				}
			}
		}
		return count;
	}
	
	public static void printToString(Board gameboard){
		Square[] squarearray = gameboard.getSquarearray();
		String output = "";
		for (int i = 0; i < squarearray.length; i++){
			output = output + " " + squarearray[i].getValue();
		}
		
		int a = 0;
		int b = 18;
		for (int i = 0; i < 9; i++){
			System.out.println(output.substring(a,b));
			a += 18;
			b += 18;
		}
	}
}