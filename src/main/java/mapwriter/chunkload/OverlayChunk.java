package mapwriter.chunkload;

class OverlayChunk {
	int x, z;

	long loadTime, unloadTime;
	public OverlayChunk(int x, int z) {
		this.x = x;
		this.z = z;
		this.loadTime = System.currentTimeMillis();
		this.unloadTime = -1;
	}

	public String getKey() {
		return getKey(x, z);
	}

	public static String getKey(int x, int z) {
		return x + "," + z;
//		return (long) x << 32 | (long) z;
	}

	public void unload() {
		this.unloadTime = System.currentTimeMillis();
	}

	public int getMinX() {
		return x * 16;
	}

	public int getMinZ() {
		return z * 16;
	}

	public int getMaxX() {
		return (x + 1) * 16;
	}

	public int getMaxZ() {
		return (z + 1) * 16;
	}
}
