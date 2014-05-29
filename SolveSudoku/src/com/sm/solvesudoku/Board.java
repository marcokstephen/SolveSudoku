package com.sm.solvesudoku;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class Board{
	private Square[] squarearray;
	private List<List<Integer>> columns = new ArrayList<List<Integer>>();
	private List<List<Integer>> groups = new ArrayList<List<Integer>>();
	private List<List<Integer>> rows = new ArrayList<List<Integer>>();

	public Board(Square[] squarearray) {
		super();
		this.squarearray = squarearray;
		List<List<Integer>> columns = new ArrayList<List<Integer>>();
		List<List<Integer>> groups = new ArrayList<List<Integer>>();
		List<List<Integer>> rows = new ArrayList<List<Integer>>();
		
		for (int i = 0; i < squarearray.length; i++){
			Square tempsquare = squarearray[i];
			int sqvalue = tempsquare.getValue();
			int rowNumber = i/9;
			if (rows.size() <= rowNumber){
				List<Integer> rowList = new ArrayList<Integer>();
				rowList.add(sqvalue);
				rows.add(rowList);
			} else {
				List<Integer> rowList = rows.get(rowNumber);
				rowList.add(sqvalue);
			}
			
			int columnNumber = i%9;
			if (columns.size() <= columnNumber){
				List<Integer> columnList = new ArrayList<Integer>();
				columnList.add(sqvalue);
				columns.add(columnList);
			} else {
				List<Integer> columnList = columns.get(columnNumber);
				columnList.add(sqvalue);
			}
			
			int groupNumber = getGroupNumber(i);
			if (groups.size() <= groupNumber){
				List<Integer> groupList = new ArrayList<Integer>();
				groupList.add(sqvalue);
				groups.add(groupList);
			} else {
				List<Integer> groupList = groups.get(groupNumber);
				groupList.add(sqvalue);
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


	public List<List<Integer>> getColumns() {
		return columns;
	}
	public List<Integer> getColumns(int i){
		return columns.get(i);
	}

	public List<List<Integer>> getGroups() {
		return groups;
	}
	public List<Integer> getGroups(int i){
		return groups.get(i);
	}

	public List<List<Integer>> getRows() {
		return rows;
	}
	public List<Integer> getRows(int i){
		return rows.get(i);
	}

	public Square[] getSquarearray() {
		return squarearray;
	}
}