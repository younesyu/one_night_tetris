package tetris;

abstract class Tetromino {
	int x, y; // position of first cell in the grid
	Coord[][] shapes;
	Color color;
	int currentShape = 0;

	void rotate() {
		currentShape = (currentShape + 1) % shapes.length;
	}
	
	public Coord[] currentShape() {
		return shapes[currentShape];
	}
	
	public Coord[] nextShape() {
		return shapes[(currentShape + 1) % shapes.length];
	}
	
	public int height() {
		int max = 0;
		for (Coord coord : shapes[currentShape]) {
			if (coord.y > max) max = coord.y; 
		}
		
		return max + 1;
	}

	public int width() {
		int max = 0;
		for (Coord coord : shapes[currentShape]) {
			if (coord.x > max) max = coord.x; 
		}
		
		return max + 1;
	}
}