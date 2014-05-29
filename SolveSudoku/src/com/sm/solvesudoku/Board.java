package com.sm.solvesudoku;

import java.util.List;

@SuppressWarnings("unchecked")
public class Board{
	private Square[] squarearray;
	private List<Square>[] columns = new List[9];
	private List<Square>[] groups = new List[9];
	private List<Square>[] rows = new List[9];

	public Board(Square[] squarearray) {
		super();
		this.squarearray = squarearray;
		List<Square>[] rows = new List[9];
		List<Square>[] groups = new List[9];
		List<Square>[] columns = new List[9];
		
		for (int i = 0; i < rows.length; i++){
			Square tempsquare = squarearray[i];
			int rowNumber = i/9;
			rows[rowNumber].add(tempsquare);
			
			int columnNumber = i%9;
			columns[columnNumber].add(tempsquare);
			
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
			groups[groupNumber].add(tempsquare);
		}
		
		this.groups = groups;
		this.columns = columns;
		this.rows = rows;
	}


	public List<Square>[] getColumns() {
		return columns;
	}


	public List<Square>[] getGroups() {
		return groups;
	}


	public List<Square>[] getRows() {
		return rows;
	}


	public Square[] getSquarearray() {
		return squarearray;
	}
}