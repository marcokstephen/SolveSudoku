package com.sm.solvesudoku;

import java.util.ArrayList;
import java.util.List;

public class Main {
	final static int BOARDSIZE = 81;
	static boolean solved = false;

	public static void main(String[] args){
		//String boardstring = "060090341090060000002701080610000200000000000009000014030207400000080050481050060"; //hard puzzle, can solve (needs advanced row tactics)
		//String boardstring = "100920000524010000000000070050008102000000000402700090060000000000030945000071006"; //euler, too hrad
		String boardstring = "000500080051000026000091000004600008009070400800005100000280000780000240030007000";
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
				System.exit(0);
				break;
			}
		} //end while loop
		
		boolean changeOccurred = true;
		while (changeOccurred){
			changeOccurred = false;
			int numChangedRowNumbers = -1;
			while (numChangedRowNumbers != 0){
				if (numChangedRowNumbers > 0) changeOccurred = true;
				numChangedRowNumbers = checkUniqueRowNumbers(gameboard);
				gameboard = refreshBoard(gameboard);
			}
			if (solved) {
				printToString(gameboard);
				System.exit(0);
			}
			
			int numChangedColumnNumbers = -1;
			while (numChangedColumnNumbers != 0){
				if (numChangedColumnNumbers > 0) changeOccurred = true;
				numChangedColumnNumbers = checkUniqueColumnNumbers(gameboard);
				gameboard = refreshBoard(gameboard);
			}
			if (solved) {
				printToString(gameboard);
				System.exit(0);
			}
			
			int numChangedGroupNumbers = -1;
			while (numChangedGroupNumbers != 0){
				if (numChangedGroupNumbers > 0) changeOccurred = true;
				numChangedGroupNumbers = checkUniqueGroupNumbers(gameboard);
				gameboard = refreshBoard(gameboard);
			}
			if (solved) {
				printToString(gameboard);
				System.exit(0);
			}
			checkNakedPairs(gameboard);
		}//end while
		
		if (!solved){
			System.out.println("Cant do it...");
			printToString(gameboard);
			System.out.println("--------");
			for (int i = 0; i < BOARDSIZE; i++){
				if (!gameboard.getSquarearray()[i].isAssigned()){
					System.out.println(i+": " + gameboard.getSquarearray()[i].getPossible());
				}
			}
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
				List<Square> group = gameboard.getGroups(groupNumber);
				List<Square> row = gameboard.getRows(rowNumber);
				List<Square> column = gameboard.getColumns(columnNumber);

				for (int m = 1; m < 10; m++){
					if (!(group.contains(new Square(m)) || row.contains(new Square(m)) || column.contains(new Square(m)))){
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
			if (!temp.isAssigned()){
				int groupNumber = Board.getGroupNumber(i);
				int rowNumber = i/9;
				int columnNumber = i%9;
				List<Integer> possibleList = temp.getPossible();
				List<Square> group = gameboard.getGroups(groupNumber);
				List<Square> row = gameboard.getRows(rowNumber);
				List<Square> column = gameboard.getColumns(columnNumber);
				
				for (int m = 0; m < possibleList.size(); m++){
					int possValue = possibleList.get(m);
					if (group.contains(new Square(possValue)) || row.contains(new Square(possValue)) || column.contains(new Square(possValue))){
						possibleList.remove(new Integer(possValue));
						m--;
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
				List<Square> rowList = gameboard.getRows(rowNumber);
				List<Integer> possibleList = temp.getPossible();

				List<Integer> joinedPossibleList = new ArrayList<Integer>();
				for (int r = 0; r < 9; r++){
					if (rowList.get(r).getValue() == 0 && (i != rowNumber*9+r)){
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
				List<Square> columnList = gameboard.getColumns(columnNumber);
				List<Integer> possibleList = temp.getPossible();

				List<Integer> joinedPossibleList = new ArrayList<Integer>();
				for (int c = 0; c < 9; c++){
					if (columnList.get(c).getValue() == 0 && (i != c*9+columnNumber)){
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
	
	public static int checkUniqueGroupNumbers(Board gameboard){
		Square[] squarearray = gameboard.getSquarearray();
		int numberSolvedThisRound = 0;
		
		for (int i = 0; i < BOARDSIZE; i++){
			Square temp = squarearray[i];
			if (!temp.isAssigned()){
				int groupNumber = Board.getGroupNumber(i);
				List<Square> groupList = gameboard.getGroups(groupNumber);
				List<Integer> possibleList = temp.getPossible();
				
				List<Integer> joinedPossibleList = new ArrayList<Integer>();
				for (int g = 0; g < 9; g++){
					int index = Board.groupPosnToIndex(groupNumber, g);
					if (groupList.get(g).getValue() == 0 && (i != index)){
						joinedPossibleList.addAll(squarearray[index].getPossible());
					}
				}
				for (int m = 0; m < possibleList.size(); m++){
					if (!joinedPossibleList.contains(possibleList.get(m))){
						//there is a unique value in the group!
						int value = possibleList.get(m);
						temp.setAssigned(true);
						temp.setValue(value);
						List<Integer> nullList = new ArrayList<Integer>();
						temp.setPossible(nullList);
						numberSolvedThisRound++;
					}
				}
			}
		}//end for loop
		return numberSolvedThisRound;
	}
	
	public static void checkNakedPairs(Board gameboard){
		//checking rows
		for (int p = 0; p < BOARDSIZE; p++){
			Square tempsquare = gameboard.getSquarearray()[p];
			if (!tempsquare.isAssigned()){
				List<Integer> possibleList = tempsquare.getPossible();
				int sizePossibleList = possibleList.size();
				int rowNumber = p/9;
				int columnNumber = p%9;
				List<Square> rowSquares = gameboard.getRows(rowNumber);
				int tempSimilarCount = 1;
				List<Integer> numsToIgnore = new ArrayList<Integer>();
				numsToIgnore.add(columnNumber);
				for (int r = 0; r < rowSquares.size(); r++){
					if (!rowSquares.get(r).isAssigned() && r != columnNumber){
						List<Integer> tempPossible = rowSquares.get(r).getPossible();
						if (possibleList.equals(tempPossible)){
							tempSimilarCount++;
							numsToIgnore.add(r);
						}
					}
					if (tempSimilarCount == sizePossibleList){
						int output = 0;
						do {
							output = filterRows(possibleList, rowSquares, numsToIgnore);
							gameboard = refreshBoard(gameboard);
						} while (output != 0);
						break;
					}
				}
			}
		}//end for loop
		if (solved) {
			printToString(gameboard);
			System.exit(0);
		}
		
		//checking columns
		for (int p = 0; p < BOARDSIZE; p++){
			Square tempsquare = gameboard.getSquarearray()[p];
			if (!tempsquare.isAssigned()){
				List<Integer> possibleList = tempsquare.getPossible();
				int sizePossibleList = possibleList.size();
				int rowNumber = p/9;
				int columnNumber = p%9;
				List<Square> columnSquares = gameboard.getColumns(columnNumber);
				int tempSimilarCount = 1;
				List<Integer> numsToIgnore = new ArrayList<Integer>();
				numsToIgnore.add(rowNumber);
				for (int c = 0; c < columnSquares.size(); c++){
					if (!columnSquares.get(c).isAssigned() && c != rowNumber){
						List<Integer> tempPossible = columnSquares.get(c).getPossible();
						if (possibleList.equals(tempPossible)){
							tempSimilarCount++;
							numsToIgnore.add(c);
						}
					}
					if (tempSimilarCount == sizePossibleList){
						int output = 0;
						do {
							output = filterRows(possibleList, columnSquares, numsToIgnore);
							//we can use filterrows for the columns as well because the structure of
							//a row and a column is identical and does not matter for that function
							gameboard = refreshBoard(gameboard);
						} while (output != 0);
						break;
					}
				}
			}
		}
		if (solved) {
			printToString(gameboard);
			System.exit(0);
		}
		
		//checking groups
		for (int p = 0; p < BOARDSIZE; p++){
			Square tempsquare = gameboard.getSquarearray()[p];
			if (!tempsquare.isAssigned()){
				List<Integer> possibleList = tempsquare.getPossible();
				int sizePossibleList = possibleList.size();
				int groupNumber = Board.getGroupNumber(p);
				List<Square> groupSquares = gameboard.getGroups(groupNumber);
				int tempSimilarCount = 1;
				List<Integer> numsToIgnore = new ArrayList<Integer>();
				int groupPosn = Board.indexToGroupPosn(p);
				numsToIgnore.add(groupPosn);
				for (int g = 0; g < groupSquares.size(); g++){
					if (!groupSquares.get(g).isAssigned() && g != groupPosn){
						List<Integer> tempPossible = groupSquares.get(g).getPossible();
						if (possibleList.equals(tempPossible)){
							tempSimilarCount++;
							numsToIgnore.add(g);
						}
					}
					if (tempSimilarCount == sizePossibleList){
						int output = 0;
						do {
							output = filterRows(possibleList, groupSquares, numsToIgnore);
							gameboard = refreshBoard(gameboard);
						} while (output != 0);
						break;
					}
				}
			}
		}
		if (solved) {
			printToString(gameboard);
			System.exit(0);
		}
	}
	
	public static int filterRows(List<Integer> possibleList, List<Square> rowSquares, List<Integer> numsToIgnore){
		for (int i = 0; i < rowSquares.size(); i++){
			if (!numsToIgnore.contains(i) && !rowSquares.get(i).isAssigned()){
				List<Integer> tempPossible = rowSquares.get(i).getPossible();
				for (int p = 0; p < possibleList.size(); p++){
					int numToRemove = possibleList.get(p);
					tempPossible.remove(new Integer(numToRemove));
				}
			}
		}
		int count = 0;
		for (int i = 0; i < rowSquares.size(); i++){
			Square tempSquare = rowSquares.get(i);
			if (!tempSquare.isAssigned()){
				List<Integer> tempPossible = tempSquare.getPossible();
				if (tempPossible.size() == 1){
					tempSquare.setAssigned(true);
					tempSquare.setValue(tempPossible.get(0));
					List<Integer> nullList = new ArrayList<Integer>();
					tempSquare.setPossible(nullList);
					count++;
				}
			}
		}
		return count;
	}

	//prints the final board to console after all attempts have been
	//made to solve the board
	public static void printToString(Board gameboard){
		Square[] squarearray = gameboard.getSquarearray();
		String output = "";
		for (int i = 0; i < BOARDSIZE; i++){
			output = output + " " + squarearray[i].getValue();
		}

		int a = 0;
		int b = 18;
		for (int i = 0; i < 9; i++){
			//splits the board up into 9 different lines of 9 numbers each
			System.out.println(output.substring(a,b));
			a += 18;
			b += 18;
		}
	}
}