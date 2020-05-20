package tetris;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;

public class Tetris extends PApplet {
	int rowCount, columnCount, cellSize;
	int gridX, gridY;
	Tetromino block;
	Tetromino nextBlock;

	Color[][] gridState;

	public Tetris(int gridX, int gridY, int cellSize, int columnCount, int rowCount) {
		this.gridX = gridX;
		this.gridY = gridY;
		this.cellSize = cellSize;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.gridState = new Color[columnCount][rowCount];
		this.block = newBlock();
		this.block.x = columnCount / 2 - 1; // do something with the width
		this.block.y = 0;
		this.nextBlock = newBlock();

	}

	private Tetromino newBlock() {
		switch (new Random().nextInt(7)) {
		case 0:
			return new OTetromino();
		case 1:
			return new STetromino();
		case 2:
			return new ZTetromino();
		case 3:
			return new ITetromino();
		case 4:
			return new LTetromino();
		case 5:
			return new JTetromino();
		default:
			return new TTetromino();
		}
	}

	private void showGrid() {
		// Filled cells
		noStroke();
		for (int j = 0; j < columnCount; j++) {
			for (int i = 0; i < rowCount; i++) {
				fill(gridState[j][i].r, gridState[j][i].g, gridState[j][i].b);
				rect(gridX + cellSize * j, gridY + cellSize * i, cellSize, cellSize);
			}
		}

		// Outside rectangle
		stroke(255);
		strokeWeight(5);
		noFill();
		rect(gridX - 5, gridY - 5, cellSize * columnCount + 10, cellSize * rowCount + 10);
	}

	private void showBlock() {
		Coord[] coords = block.currentCoords();

		fill(block.color.r, block.color.g, block.color.b);
		noStroke();

		for (int bcell = 0; bcell < coords.length; bcell++) {
			int x = block.x + coords[bcell].x;
			int y = block.y + coords[bcell].y;
			rect(gridX + x * cellSize, gridY + y * cellSize, cellSize, cellSize);
		}

	}

	public void show() {
		showGrid();
		showBlock();
	}

	void shiftDownAllAbove(int row) {
		if (row == 0)
			return;

		for (int j = row - 1; j > 0; j--) {
			for (int i = 0; i < columnCount; i++) {
				this.gridState[i][j + 1] = this.gridState[i][j];
			}
		}
	}

	List<Integer> fullLines() {
		List<Integer> fullLines = new ArrayList<Integer>();

		for (int j = 0; j < rowCount; j++) {
			boolean full = true;
			for (int i = 0; i < columnCount; i++) {
				if (gridState[i][j] == Color.EMPTY) {
					full = false;
					break;
				}
			}
			if (full)
				fullLines.add(j);
		}
		return fullLines;
	}

	void saveBlock() {
		for (Coord cell : this.block.currentCoords()) {
			gridState[block.x + cell.x][block.y + cell.y] = this.block.color;
		}
	}

	void moveDown() {
		if (block.y + this.block.height() < rowCount) {
			block.y++;
		}
	}

	void moveLeft() {
		boolean leftIsEmpty = block.x > 0;

		if (leftIsEmpty) {
			for (Coord cell : this.block.currentCoords()) {
				if (gridState[block.x + cell.x - 1][block.y + cell.y] != Color.EMPTY) {
					leftIsEmpty = false;
					break;
				}
			}
		}

		if (leftIsEmpty) {
			block.x--;
		}
	}

	void moveRight() {
		boolean rightIsEmpty = block.x + this.block.width() < columnCount;

		if (rightIsEmpty) {
			for (Coord cell : this.block.currentCoords()) {
				if (gridState[block.x + cell.x + 1][block.y + cell.y] != Color.EMPTY) {
					rightIsEmpty = false;
					break;
				}
			}
		}

		if (rightIsEmpty) {
			block.x++;
		}
	}

	void rotate() {
		block.rotate();
		shiftIfNeeded(block);
	}

	void shiftIfNeeded(Tetromino block) {
		if (block.x + block.width() > columnCount) {
			block.x = columnCount - block.width();
		}

		if (block.y + block.height() > rowCount) {
			block.y = rowCount - block.height();
		}
	}

	boolean touched() {
		if (block.y + block.height() == rowCount)
			return true;

		for (Coord cell : this.block.currentCoords()) {
			if (gridState[block.x + cell.x][block.y + cell.y + 1] != Color.EMPTY) {
				return true;
			}
		}
		return false;
	}

	public void settings() {
		size(600, 800);
		cellSize = 30;
		rowCount = 20;
		columnCount = 10;

		for (int j = 0; j < columnCount; j++) {
			for (int i = 0; i < rowCount; i++) {
				gridState[j][i] = Color.EMPTY;
			}
		}
	}

	public void keyPressed() {
		if (keyCode == UP) {
			rotate();
		}
		if (keyCode == DOWN) {
			moveDown();
			time = System.nanoTime();
		}
		if (keyCode == LEFT) {
			moveLeft();
		}
		if (keyCode == RIGHT) {
			moveRight();
		}
	}

	long time = System.nanoTime();
	long speed = 1000000000L;

	void motion() {
		long now = System.nanoTime();
		if (now - time > speed) {
			time = now;
			moveDown();
		}

		if (touched()) {
			saveBlock();
			block = nextBlock;
			nextBlock = newBlock();
			block.x = columnCount / 2 - 1; // do something to really center
			block.y = 0;
			show();
			if (touched()) {
				gameOver();
			}

			int scored = 0;
			for (Integer fullRow : fullLines()) {
				for (int i = 0; i < columnCount; i++) {
					gridState[i][fullRow] = Color.EMPTY;
				}
				shiftDownAllAbove(fullRow);
				if (scored == 0)
					scored++;
				else
					scored *= 5;
			}

			score += scored;
		}

	}

	private void gameOver() {
		fill(0, 0, 0, 200);
		rect(gridX, gridY, columnCount * cellSize, rowCount * cellSize);
		noLoop();
	}

	private void showNext() {

		int nextBoxX = width / 8 + columnCount * cellSize + 20;
		int nextBoxY = 80;
		
		fill(255);
		textSize(20);
		textAlign(LEFT);
		text("Next block :", nextBoxX, nextBoxY);

		fill(nextBlock.color.r, nextBlock.color.g, nextBlock.color.b);
		for (Coord cell : nextBlock.currentCoords()) {
			rect(nextBoxX + cell.x * cellSize + cellSize, cellSize + nextBoxY + cell.y * cellSize, cellSize, cellSize);
		}
	}

	int score = 0;

	private void showScore() {

		int scoreBoxX = width / 8 + columnCount * cellSize + 20;
		int scoreBoxY = 180;

		fill(255);
		textSize(20);
		textAlign(LEFT);
		text("Score : " + score, scoreBoxX, scoreBoxY + 110);
	}

	public void draw() {
		background(0);
		show();
		showNext();
		showScore();

		motion();
	}

	public static void main(String[] args) {
		String[] processingArgs = { "Tetris in Processing" };
		Tetris mySketch = new Tetris(20, 20, 30, 10, 20);
		PApplet.runSketch(processingArgs, mySketch);
	}
}
