package com.sm.solvesudoku;

import java.util.ArrayList;
import java.util.List;

public class Main {
	final static int BOARDSIZE = 81;
	static boolean solved = false;

	public static void main(String[] args){
		//TESTCASES:
		//String boardstring = "003020600900305001001806400008102900700000008006708200002609500800203009005010300"; //easy, can solve
		//String boardstring = "200080300060070084030500209000105408000000000402706000301007040720040060004010003"; //cannot solve
		String boardstring = "001640803704580010000000500065217900002000400009354120007000000040025701108096300"; //can solve http://www.websudoku.com/?level=1&set_id=1684315439
		Board gameboard = create_board(boardstring);
		int numberUnsolved = generatePossibleList(gameboard);
		int testResult = 0;
		while (testResult != numberUnsolved){
			//this loop runs as long as the number of unsolved squares keeps decreasing
			testResult = numberUnsolved;
			gameboard = new Board(gameboard.getSquarearray());
			numberUnsolved = refreshPossibleList(gameboard);
			if (numberUnsolved == 0){
				printToString(gameboard);
				solved = true;
				break;
			}
		} //end while loop

		checkUniqueRowNumbers(gameboard);
		gameboard = refreshBoard(gameboard);
		checkUniqueRowNumbers(gameboard);

		if (!solved){
			System.out.println("Cant do it...");
			printToString(gameboard);
		} else {
			printToString(gameboard);
		}
	}

	public static Board refreshBoard(Board board){
		int numberRemaining = 1;
		int temp = 0;
		board = new Board(board.getSquarearray());
		while (numberRemaining != temp){
			temp = numberRemaining;
			numberRemaining = refreshPossibleList(board);
			board = new Board(board.getSquarearray());
		}
		if (numberRemaining == 0) solved = true;
		return board;
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

		for (int i = 0; i < BOARDSIZE; i++){
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

		for (int i = 0; i < BOARDSIZE; i++){
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

	public static int checkUniqueRowNumbers(Board gameboard){
		Square[] squarearray = gameboard.getSquarearray();
		int numberSolvedThisRound = 0;

		for (int i = 0; i < BOARDSIZE; i++){
			Square temp = squarearray[i];
			if (!temp.isAssigned()){
				int rowNumber = i/9;
				List<Integer> rowList = gameboard.getRows(rowNumber);
				List<Integer> possibleList = temp.getPossible();

				List<Integer> joinedPossibleList = new ArrayList<Integer>();
				for (int r = 0; r < 9; r++){
					if (rowList.get(r) == 0 && (i != rowNumber*9+r)){
						joinedPossibleList.addAll(squarearray[rowNumber*9+r].getPossible());
					}
				}
				for (int m = 0; m < possibleList.size(); m++){
					if (!joinedPossibleList.contains(possibleList.get(m))){
						//there is a unique value in the row!
						int value = possibleList.get(m);
						temp.setAssigned(true);
						temp.setValue(value);
						List<Integer> nullList = new ArrayList<Integer>();
						temp.setPossible(nullList);
						numberSolvedThisRound++;
					}
				}
			} //end if stating that we only check non-assigned squares
		} //end for loop
		return numberSolvedThisRound;
	}

	public static int checkUniqueColumnNumbers(Board gameboard){
		Square[] squarearray = gameboard.getSquarearray();
		int numberSolvedThisRound = 0;

		for (int i = 0; i < BOARDSIZE; i++){
			Square temp = squarearray[i];
			if (!temp.isAssigned()){
				int columnNumber = i%9;
				List<Integer> columnList = gameboard.getColumns(columnNumber);
				List<Integer> possibleList = temp.getPossible();

				List<Integer> joinedPossibleList = new ArrayList<Integer>();
				for (int c = 0; c < 9; c++){
					if (columnList.get(c) == 0 && (i != c*9+columnNumber)){
						joinedPossibleList.addAll(squarearray[c*9+columnNumber].getPossible());
					}
				}
				for (int m = 0; m < possibleList.size(); m++){
					if (!joinedPossibleList.contains(possibleList.get(m))){
						//there is a unique value in the column!
						int value = possibleList.get(m);
						temp.setAssigned(true);
						temp.setValue(value);
						List<Integer> nullList = new ArrayList<Integer>();
						temp.setPossible(nullList);
						numberSolvedThisRound++;
					}
				}
			}//end if loop
		}//end for loop
		return numberSolvedThisRound;
	}

	public static void printToString(Board gameboard){
		Square[] squarearray = gameboard.getSquarearray();
		String output = "";
		for (int i = 0; i < BOARDSIZE; i++){
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