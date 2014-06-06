package com.sm.solvesudoku;

import java.util.ArrayList;
import java.util.List;

public class Board{
	private Square[] squarearray;
	private List<List<Square>> columns = new ArrayList<List<Square>>();
	private List<List<Square>> groups = new ArrayList<List<Square>>();
	private List<List<Square>> rows = new ArrayList<List<Square>>();

	public Board(Square[] squarearray) {
		super();
		this.squarearray = squarearray;
		List<List<Square>> columns = new ArrayList<List<Square>>();
		List<List<Square>> groups = new ArrayList<List<Square>>();
		List<List<Square>> rows = new ArrayList<List<Square>>();
		
		for (int i = 0; i < squarearray.length; i++){
			Square tempsquare = squarearray[i];
			int rowNumber = i/9;
			if (rows.size() <= rowNumber){
				List<Square> rowList = new ArrayList<Square>();
				rowList.add(tempsquare);
				rows.add(rowList);
			} else {
				List<Square> rowList = rows.get(rowNumber);
				rowList.add(tempsquare);
			}
			
			int columnNumber = i%9;
			if (columns.size() <= columnNumber){
				List<Square> columnList = new ArrayList<Square>();
				columnList.add(tempsquare);
				columns.add(columnList);
			} else {
				List<Square> columnList = columns.get(columnNumber);
				columnList.add(tempsquare);
			}
			
			int groupNumber = getGroupNumber(i);
			if (groups.size() <= groupNumber){
				List<Square> groupList = new ArrayList<Square>();
				groupList.add(tempsquare);
				groups.add(groupList);
			} else {
				List<Square> groupList = groups.get(groupNumber);
				groupList.add(tempsquare);
			}
		}
		
		this.groups = groups;
		this.columns = columns;
		this.rows = rows;
	}
	
	public static int getGroupNumber(int i){
		int rowNumber = i/9;
		int columnNumber = i%9;
		int groupNumber = 0;
		if (columnNumber >= 6){
			if (rowNumber >= 6){
				groupNumber = 8;
			} else if (rowNumber >= 3){
				groupNumber = 5;
			} else {
				groupNumber = 2;
			}
		} else if (columnNumber >= 3){
			if (rowNumber >= 6){
				groupNumber = 7;
			} else if (rowNumber >= 3){
				groupNumber = 4;
			} else {
				groupNumber = 1;
			}
		} else {
			if (rowNumber >= 6){
				groupNumber = 6;
			} else if (rowNumber >= 3){
				groupNumber = 3;
			} else {
				groupNumber = 0;
			}
		}
		return groupNumber;
	}
	
	public static int groupPosnToIndex(int group, int posn){
		int starting = 0;
		if (group == 0){
			
		} else if (group == 1){
			starting = 3;
		} else if (group == 2){
			starting = 6;
		} else if (group == 3){
			starting = 27;
		} else if (group == 4){
			starting = 30;
		} else if (group == 5){
			starting = 33;
		} else if (group == 6){
			starting = 54;
		} else if (group == 7){
			starting = 57;
		} else if (group == 8){
			starting = 60;
		}
		
		int position = starting;
		int column = posn%3;
		int row = posn/3;
		position = position + column + row*9;
		
		return position;
	}


	public List<List<Square>> getColumns() {
		return columns;
	}
	public List<Square> getColumns(int i){
		return columns.get(i);
	}

	public List<List<Square>> getGroups() {
		return groups;
	}
	public List<Square> getGroups(int i){
		return groups.get(i);
	}

	public List<List<Square>> getRows() {
		return rows;
	}
	public List<Square> getRows(int i){
		return rows.get(i);
	}

	public Square[] getSquarearray() {
		return squarearray;
	}
}