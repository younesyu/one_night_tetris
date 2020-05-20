package tetris;

class OTetromino extends Tetromino {

	public OTetromino() {
		this.shapes = new Coord[][] {
			(new Coord[] { new Coord(0, 0), new Coord(0, 1), new Coord(1, 0), new Coord(1, 1) }),
		};
		
		this.color = Color.VIOLET;
	}

}