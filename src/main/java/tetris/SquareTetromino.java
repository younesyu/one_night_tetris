package tetris;

class SquareTetromino extends Tetromino {

	public SquareTetromino() {
		this.shapes = new Coord[][] {
			(new Coord[] { new Coord(0, 0), new Coord(0, 1), new Coord(1, 0), new Coord(1, 1) }),
		};
	}

}