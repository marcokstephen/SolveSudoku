package com.sm.solvesudoku;

import java.util.List;

public class Square{
		private boolean assigned;
		private int value;
		private List<Integer> possible;
		public Square(boolean assigned, int value, List<Integer> possible) {
			super();
			this.assigned = assigned;
			this.value = value;
			this.possible = possible;
		}
		
		public boolean isAssigned() {
			return assigned;
		}
		public void setAssigned(boolean assigned) {
			this.assigned = assigned;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		public List<Integer> getPossible() {
			return possible;
		}
		public void setPossible(List<Integer> possible) {
			this.possible = possible;
		}
		
		//this constructor is only used when squarelist.contains(new Square(number)) is used
		//part of custom implementation of .contains method
		public Square(int value){
			this.value = value;
		}
		
		//this override of the equals method allows a custom .contains method in Main.java
		//it compares a Square according to its assigned value
		@Override
		public boolean equals(Object sq){
			boolean retval = false;
			if (sq instanceof Square){
				Square sqr = (Square) sq;
				retval = sqr.getValue() == this.value;
			}
			return retval;
		}
		
		@Override
		public String toString(){
			return "" + this.value;
		}
	}