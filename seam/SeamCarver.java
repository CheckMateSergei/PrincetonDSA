import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
	private int[][] pixels;
	private int h;
	private int w;
	private boolean transposed;
	private boolean calledFromH;
	private double[][] e;

	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture) {
		if (picture == null) throw new IllegalArgumentException();
		Picture pic = new Picture(picture);
		w = picture.width();
		h = picture.height();
		pixels = new int[h][w];
		transposed = false;
		calledFromH = false;
		e = new double[h][w];
		// fill the pixels matrix
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				pixels[i][j] = pic.getRGB(j, i);
			}
		}
		// fill the energy matrix AFTER PIXELS
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				e[i][j] = energy(j, i);
			}
		}
	}
	
	private void transpose(int[][] p) {
		// System.out.printf("ABOUT TO TRANSPOSE\n\n");
		int[][] transpose = new int[w][h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				transpose[i][j] = p[j][i];
			}
		}
		pixels = transpose;
		e = transpose(e); // transpose the energy array
		// swap height and width
		h = w + h;
		w = h - w;
		h = h - w;

		if (transposed) {
			transposed = false;
			return;
		}
		transposed = true;
	}

	private double[][] transpose(double[][] energy) {
		double[][] eTranspose = new double[w][h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				eTranspose[i][j] = energy[j][i];
			}
		}
		return eTranspose;
	}

	// current picture
	public Picture picture() {
		boolean t = transposed;
		if (transposed) transpose(pixels);
		Picture p = new Picture(w, h);
		// set the pixels
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				p.setRGB(j, i, pixels[i][j]);
			}
		}
		if (t) transpose(pixels);
		return p;
	}

	// width of current picture
	public int width() {
		if (transposed) return h;
		return w;
	}

	// height of current picture
	public int height() {
		if (transposed) return w;
		return h;
	}

	private double energyX(int x, int y) {
		// get the RGB values
		int xLeft = x - 1;
		int xRight = x + 1;

		int rL, gL, bL, rR, gR, bR;
		int rgbL = pixels[y][xLeft];
		rL = (rgbL >> 16) & 0xFF; // extract red int value
		gL = (rgbL >> 8) & 0xFF; // extract green int value
		bL = (rgbL >> 0) & 0xFF; // extract blue int value
		int rgbR = pixels[y][xRight];
		rR = (rgbR >> 16) & 0xFF; // extract red int value
		gR = (rgbR >> 8) & 0xFF; // extract green int value
		bR = (rgbR >> 0) & 0xFF; // extract blue int value

		int r = rL - rR;
		int g = gL - gR;
		int b = bL - bR;
		return r * r + g * g + b * b;
	}

	private double energyY(int x, int y) {
		// get the RGB values
		int yLeft = y - 1;
		int yRight = y + 1;

		int rL, gL, bL, rR, gR, bR;
		int rgbL = pixels[yLeft][x];
		rL = (rgbL >> 16) & 0xFF; // shift least significant bits out
		gL = (rgbL >> 8) & 0xFF; // shift most sigbits out then least sigbits out
		bL = (rgbL >> 0) & 0xFF; // same as above 
		int rgbR = pixels[yRight][x];
		rR = (rgbR >> 16) & 0xFF; // shift least significant bits out
		gR = (rgbR >> 8) & 0xFF; // shift most sigbits out then least sigbits out
		bR = (rgbR >> 0) & 0xFF; // same as above 

		int r = rL - rR;
		int g = gL - gR;
		int b = bL - bR;
		return r * r + g * g + b * b;
	}

	private void checkParams(int x, int y) {
		if (x < 0 || x > w - 1) throw new IllegalArgumentException();
		if (y < 0 || y > h - 1) throw new IllegalArgumentException();
	}

	// energy of pixel at column x and row y
	public double energy(int x, int y) {
		checkParams(x, y);
		// border pixel cases
		if (x == 0 || x == w - 1) return 1000;
		if (y == 0 || y == h - 1) return 1000;

		return Math.sqrt(energyX(x, y) + energyY(x, y));
	}

	public int[] findHorizontalSeam() {
		// transpose the current picture if not already
		if (!transposed) transpose(pixels);
		// call find vertical seam 
		calledFromH = true;
		int[] path = findVerticalSeam();
		calledFromH = false;
		return path;
	}

	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		// check if method was called from findHorizontalSeam
		if (transposed && !calledFromH) transpose(pixels);

		double[][] distTo = new double[h][w];
		for (int x = 0; x < h; x++) {
			for (int y = 0; y < w; y++) {
				distTo[x][y] = Double.POSITIVE_INFINITY;
			}
		}
		int[][] edgeTo = new int[h][w]; // column coordinate
		int[] path = new int[h];

		for (int y = 0; y < w; y++) {
			distTo[0][y] = 0;
			edgeTo[0][y] = y;
		}
		for (int x = 0; x < h - 1; x++) {
			for (int y = 0; y < w; y++) {
				// relax edges
				if (y == 0) {
					double dist = distTo[x][y] + e[x + 1][y];
					if (dist < distTo[x + 1][y]) {
						distTo[x + 1][y] = dist;
						edgeTo[x + 1][y] = y;
					}
					if (w == 1) continue;

					dist = distTo[x][y] + e[x + 1][y + 1];
					if (dist < distTo[x + 1][y + 1]) {
						distTo[x + 1][y + 1] = dist;
						edgeTo[x + 1][y + 1] = y;
					}
				}
				else if (y == w - 1) {
					double dist = distTo[x][y] + e[x + 1][y];
					if (dist < distTo[x + 1][y]) {
						distTo[x + 1][y] = dist;
						edgeTo[x + 1][y] = y;
					}
					dist = distTo[x][y] + e[x + 1][y - 1];
					if (dist < distTo[x + 1][y - 1]) {
						distTo[x + 1][y - 1] = dist;
						edgeTo[x + 1][y - 1] = y;
					}
				}
				else {
					double dist = distTo[x][y] + e[x + 1][y];
					if (dist < distTo[x + 1][y]) {
						distTo[x + 1][y] = dist;
						edgeTo[x + 1][y] = y;
					}
					dist = distTo[x][y] + e[x + 1][y + 1];
					if (dist < distTo[x + 1][y + 1]) {
						distTo[x + 1][y + 1] = dist;
						edgeTo[x + 1][y + 1] = y;
					}
					dist = distTo[x][y] + e[x + 1][y - 1];
					if (dist < distTo[x + 1][y - 1]) {
						distTo[x + 1][y - 1] = dist;
						edgeTo[x + 1][y - 1] = y;
					}
				}
			}
		}

		// find min distance on bottom row
		double minDist = Double.POSITIVE_INFINITY;
		int index = 0;
		for (int y = 0; y < w; y++) {
			if (h == 1) {
				if (distTo[h - 1][y] < minDist) {
					index = y;
					minDist = distTo[h - 1][y];
				}
				continue;
			}
			if (distTo[h - 2][y] < minDist) {
				index = y;
				minDist = distTo[h - 2][y];
			}
		}

		if (h == 1) {
			path[0] = index;
			return path;
		}

		// add entries to the path;
		path[h - 1] = index;
		path[h - 2] = index;
		int col = path[h - 2];
		// stores the column index of the seam to remove
		// one column index for each level in the grid
		for (int x = h - 2; x >= 1; x--) {
			path[x - 1] = edgeTo[x][col];
			col = path[x - 1];
		}
	
		return path;
	}

	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		if (seam == null) throw new IllegalArgumentException();

		if (!transposed) transpose(pixels);
		calledFromH = true;
		removeVerticalSeam(seam);
		calledFromH = false;
	}

	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		if (seam == null) throw new IllegalArgumentException();
		if (transposed && !calledFromH) transpose(pixels);
		if (seam.length != h) throw new IllegalArgumentException();
		for (int i = 0; i < seam.length; i++) {
			if (seam[i] < 0 || seam[i] > w - 1) throw new IllegalArgumentException();
		}
		if (w <= 1) throw new IllegalArgumentException();
		for (int i = 0; i < seam.length - 1; i++) {
			int x = seam[i] - seam[i + 1];
			x = x * x;
			if (x > 1) throw new IllegalArgumentException();
		}

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w - seam[i] - 1; j++) {
				// shift everything over to the left
				pixels[i][seam[i] + j] = pixels[i][seam[i] + j + 1];
			}
		}
		// recalculate the energy pixels that were removed
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w - seam[i] - 1; j++) {
				e[i][seam[i] + j] = e[i][seam[i] + j + 1];
			}
		}
		for (int i = 0; i < h; i++) {
			e[i][seam[i]] = energy(seam[i], i);
		}
		// decrement width
		w--;
	}

	// unit testing 
	public static void main(String[] args) {

		Picture picture = new Picture(args[0]);
		SeamCarver carver = new SeamCarver(picture);
		int[] pp = {3, 4, 4, 3, 4, 3};
		carver.removeVerticalSeam(pp);
		System.out.println("Removed 1");
		System.out.printf("Height is now: %d\n", carver.height());
		System.out.printf("Width is now: %d\n", carver.width());
		int [] ppp = {2, 3, 2, 1, 1, 0};
	  	carver.removeVerticalSeam(ppp);
		System.out.println("Removed 2");
		System.out.printf("Height is now: %d\n", carver.height());
		System.out.printf("Width is now: %d\n", carver.width());
		int [] pppp = {1, 2, 1};
		carver.removeHorizontalSeam(pppp);
		System.out.println("Removed 3");
		System.out.printf("Height is now: %d\n", carver.height());
		System.out.printf("Width is now: %d\n", carver.width());
		int[] ppppp = {1, 2, 1, 2, 1};
		carver.removeVerticalSeam(ppppp);
		System.out.println("Removed 4");
		System.out.printf("Height is now: %d\n", carver.height());
		System.out.printf("Width is now: %d\n", carver.width());
		System.out.printf("CARVER WIDTH %d\n", carver.width());

	}
}
