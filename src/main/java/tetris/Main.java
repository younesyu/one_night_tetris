package tetris;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;

public class Main extends PApplet {
	int cellSize;
	int rowCount;
	int columnCount;
	Grid grid;

	class Grid {
		int rowCount, columnCount, cellSize;
		int grid_x, grid_y;
		Tetromino block;
		Tetromino next_block;
		int block_x;
		int block_y;
		
		boolean[][] gridState;
		
		public Grid(int grid_x, int grid_y, int cellSize, int columnCount, int rowCount) {
			this.grid_x = grid_x;
			this.grid_y = grid_y;
			this.cellSize = cellSize;
			this.rowCount = rowCount;
			this.columnCount = columnCount;
			this.gridState = new boolean[columnCount][rowCount];
			this.block_x = columnCount / 2 - 1; // do something with the width
			this.block_y = 0;
			this.block = newBlock();
			this.next_block = newBlock();
		
		}
		
		private Tetromino newBlock() {
			
			switch (new Random().nextInt(7)) {
			case 0 :
				return new SquareTetromino();
			case 1 :
				return new SRTetromino();
			case 2 :
				return new SLTetromino();
			case 3 :
				return new ITetromino();
			case 4 :
				return new LLTetromino();
			case 5 :
				return new RLTetromino();
			default :
				return new TTetromino();
			}
		}
		
		private void showGrid() {
			stroke(0);
			strokeWeight(1);
			fill(255);
			int x = width / 8;
			int y = 20;

			for (int j = 0; j < columnCount; j++) {
				for (int i = 0; i < rowCount; i++) {
					rect(x + cellSize * j, y + cellSize * i, cellSize, cellSize);
				}
			}
			
			// Outside rectangle
			stroke(255, 0, 0);
			strokeWeight(5);
			noFill();
			rect(x, y, cellSize * columnCount, cellSize * rowCount);
			
			// Filled cells
			fill(0);
			for (int j = 0; j < columnCount; j++) {
				for (int i = 0; i < rowCount; i++) {
					if (gridState[j][i]) {
						rect(x + cellSize * j, y + cellSize * i, cellSize, cellSize);
					}
				}
			}
		}
		
		private void showBlock() {
			Coord[] coords = block.currentCoords();

			fill(0);
			for (int bcell = 0; bcell < coords.length; bcell++) {
				int x = block_x + coords[bcell].x;
				int y = block_y + coords[bcell].y;
				rect(grid_x + x * cellSize, grid_y + y * cellSize, cellSize, cellSize);
			}
			
		}
		
		public void show() {
			showGrid();
			showBlock();
		}
		
		void shiftDownAllAbove(int row) {
			if (row == 0) return;
			
			for(int j = row - 1; j > 0; j--) {
				for(int i = 0; i < columnCount; i++) {
					this.gridState[i][j + 1] = this.gridState[i][j];
				}
			}
		}
		
		List<Integer> fullLines() {
			List<Integer> fullLines = new ArrayList<Integer>();

			for (int j = 0; j < rowCount; j++) {
				boolean full = true;
				for (int i = 0; i < columnCount; i++) {
					if (!gridState[i][j]) {
						full = false;
						break;
					}
				}
				if(full) fullLines.add(j);
			}
			return fullLines;
		}
		
		void saveBlock() {
			for (Coord cell : this.block.currentCoords()) {
				gridState[block_x + cell.x][block_y + cell.y] = true; 
			}
		}
		
		void block_down() {
			if(block_y + this.block.height() < rowCount) {
				block_y++;
			}
		}
		
		void block_left() {
			boolean leftIsEmpty = block_x > 0;
			
			if (leftIsEmpty) {
				for (Coord cell : this.block.currentCoords()) {
					if (gridState[block_x + cell.x - 1][block_y + cell.y]) {
						leftIsEmpty = false;
						break;
					}
				}
			}
		
			if(leftIsEmpty) {
				block_x--;
			}
		}
		
		void block_right() {
			boolean rightIsEmpty = block_x + this.block.width() < columnCount;
			
			if(rightIsEmpty) {
				for (Coord cell : this.block.currentCoords()) {
					if (gridState[block_x + cell.x + 1][block_y + cell.y]) {
						rightIsEmpty = false;
						break;
					}
				}
			}
			
			if(rightIsEmpty) {
				block_x++;
			}
		}
		
		void rotate() {
			block.rotate();
			shiftIfNeeded(block);
		}
		
		void shiftIfNeeded(Tetromino block) {
			if (block_x + block.width() > columnCount) {
				block_x = columnCount - block.width();
			}
			
			if (block_y + block.height() > rowCount) {
				block_y = rowCount - block.height();
			}
		}
		
		boolean touched() {
			if  (block_y + block.height() == rowCount) return true;
			
			for (Coord cell : this.block.currentCoords()) {
				if (gridState[block_x + cell.x][block_y + cell.y + 1]) {
					return true;
				}
			}
			return false;
		}

	}

	public void settings() {
		size(600, 800);
		cellSize = 30;
		rowCount = 20;
		columnCount = 10;
		this.grid = new Grid(width/8, 20, cellSize, columnCount, rowCount);
	}
	
	public void keyPressed() {
	  if (keyCode == UP)    { grid.rotate();      }
	  if (keyCode == DOWN)  { grid.block_down(); time = System.nanoTime(); }
	  if (keyCode == LEFT)  { grid.block_left();  }
	  if (keyCode == RIGHT) { grid.block_right(); }
	}

	
	long time = System.nanoTime();
	long speed = 1000000000L;
	void motion() {
		long now = System.nanoTime();
		if(now - time > speed) {
			time = now;
			grid.block_down();
		}
		
		if (grid.touched()) {
			grid.saveBlock();
			grid.block = grid.next_block;
			grid.next_block = grid.newBlock();
			grid.block_x = columnCount / 2 - 1; // do something to really center
			grid.block_y = 0;
			grid.show();
			if(grid.touched()) {
				gameOver();
			}
			
			int scored = 0;
			for (Integer fullRow : grid.fullLines()) {
				for(int i = 0; i < columnCount; i++) {
					grid.gridState[i][fullRow] = false;
				}
				grid.shiftDownAllAbove(fullRow);
				if(scored == 0) scored++;
				else scored *= 5;
			}
			
			score += scored;
		}

	}
	
	private void gameOver() {
		fill(0, 0, 0, 200);
		rect(grid.grid_x, grid.grid_y, columnCount * cellSize, rowCount * cellSize);
		noLoop();
	}
	
	private void showNext() {

		int nextBoxX = width / 8 + columnCount * cellSize + 20;
		int nextBoxY = 20;

		fill(255);
		
		rect(nextBoxX, nextBoxY, cellSize * 5, cellSize * 5);
		fill(0);
		for (Coord cell : grid.next_block.currentCoords()) {
			rect(nextBoxX + cell.x * cellSize + cellSize, 
					cellSize + nextBoxY + cell.y * cellSize, 
					cellSize, cellSize);
		}
	}
	
	int score = 0;
	private void showScore() {

		int scoreBoxX = width / 8 + columnCount * cellSize + 20;
		int scoreBoxY = 180;

		fill(255);
		
		rect(scoreBoxX, scoreBoxY, cellSize * 5, cellSize * 5);
		fill(0);
		textSize(100);
		text(score, scoreBoxX + 20, scoreBoxY + 110);
	}
	
	public void draw() {
		background(0);
		grid.show();
		showNext();
		showScore();
		
		motion();
	}

	public static void main(String[] args) {
		String[] processingArgs = { "Tetris in Processing" };
		Main mySketch = new Main();
		PApplet.runSketch(processingArgs, mySketch);
	}
}
