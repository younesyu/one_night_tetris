package tetris;

public enum Color {
	EMPTY(0, 0, 0), RED(249, 65, 68), ORANGE(243, 114, 44), EGGYOLK(248, 150, 30), YELLOW(249, 199, 79),
	GREEN(144, 190, 109), AQUA(67, 170, 139), VIOLET(87, 117, 144);

	public final int r;
	public final int g;
	public final int b;

	Color(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
}