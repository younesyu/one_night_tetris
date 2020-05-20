package tetris;

class ITetromino extends Tetromino {
	
	public ITetromino() {
		this.shapes = new Coord[][] {
			(new Coord[] { new Coord(0, 1), new Coord(1, 1), new Coord(2, 1), new Coord(3, 1) }),
			(new Coord[] { new Coord(0, 0), new Coord(0, 1), new Coord(0, 2), new Coord(0, 3) }),
		};
		this.color = Color.AQUA;
	}

}