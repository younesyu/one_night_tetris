package tetris;

class TTetromino extends Tetromino {
	
	public TTetromino() {
		this.shapes = new Coord[][] {
			(new Coord[] { new Coord(0, 1), new Coord(1, 0), new Coord(1, 1), new Coord(2, 1) }),
			(new Coord[] { new Coord(0, 0), new Coord(0, 1), new Coord(1, 1), new Coord(0, 2) }),
			(new Coord[] { new Coord(0, 0), new Coord(1, 0), new Coord(1, 1), new Coord(2, 0) }),
			(new Coord[] { new Coord(0, 1), new Coord(1, 0), new Coord(1, 1), new Coord(1, 2) }),
		};
	}

}