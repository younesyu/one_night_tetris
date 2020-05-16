package tetris;

class SRTetromino extends Tetromino {

	public SRTetromino() {
		this.shapes = new Coord[][] {
			(new Coord[] { new Coord(0, 0), new Coord(0, 1), new Coord(1, 1), new Coord(1, 2) }),
			(new Coord[] { new Coord(0, 1), new Coord(1, 0), new Coord(1, 1), new Coord(2, 0) }),
		};
	}

}