package tetris;

abstract class Tetromino {
	Coord[][] shapes;
	int r, g, b;
	int currentShape = 0;

	void rotate() {
		currentShape = (currentShape + 1) % shapes.length;
	}
	
	public Coord[] currentCoords() {
		return shapes[currentShape];
	}
	
	public int leftmost() {
		int leftmost = 10;
		for (Coord coord : shapes[currentShape]) {
			if (leftmost > coord.x) leftmost = coord.x; 
		}
		
		return leftmost;
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