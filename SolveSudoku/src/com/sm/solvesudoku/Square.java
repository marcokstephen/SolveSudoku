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
		
		public Square(){
		
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
	}