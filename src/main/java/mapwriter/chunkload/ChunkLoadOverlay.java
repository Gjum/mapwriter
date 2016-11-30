package mapwriter.chunkload;

import mapwriter.api.*;
import mapwriter.util.Render;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ChunkLoadOverlay implements IMwDataProvider {

	private final ChunkLoadEventHandler chunkLoadEventHandler;

	public ChunkLoadOverlay(ChunkLoadEventHandler chunkLoadEventHandler) {
		this.chunkLoadEventHandler = chunkLoadEventHandler;
	}

	@Override
	public void onDraw(IMapView mapView, IMapMode mapMode) {
		ArrayList<OverlayChunk> chunks = new ArrayList<OverlayChunk>(chunkLoadEventHandler.chunks.size());
		synchronized (chunkLoadEventHandler.chunks) {
			chunks.addAll(chunkLoadEventHandler.chunks.values());
		}
		for (OverlayChunk chunk : chunks) {
			long now = System.currentTimeMillis();
			if (chunk.unloadTime <= 0) {
				double age = (now - chunk.loadTime) / 1000d;
				double r = 8.5 * Math.min(1, 1 * age);
				Render.setColour((int) (0xff * Math.min(1, age)) << 24 | 0x00ffff);
				Point2D.Double fnw = blockToMap(mapView, mapMode, chunk.getMinX() + r, chunk.getMinZ() + r);
				Point2D.Double fse = blockToMap(mapView, mapMode, chunk.getMaxX() + 1 - r, chunk.getMaxZ() + 1 - r);
				if (!bothOutside(mapMode, fnw, fse))
					Render.drawRectBorder(fnw.x, fnw.y, fse.x - fnw.x, fse.y - fnw.y, 1);
			} else {
				double age = (now - chunk.unloadTime) / 1000d;
				if (age <= 1) {
					double r = 8.5 * age;
					Point2D.Double center = blockToMap(mapView, mapMode, chunk.getMinX() + 8.5, chunk.getMinZ() + 8.5);
					Render.setColour((int) (0xff * (1 - age)) << 24 | 0x00ff00ff);
					Render.drawCircleBorder(center.x, center.y, r, 1);
				}
			}
		}
	}

	private static Point.Double blockToMap(IMapView mapView, IMapMode mapMode, double x, double z) {
		Point2D.Double p = mapMode.blockXZtoScreenXY(mapView, x, z);
		if (p.x < mapMode.getX())
			p.x = mapMode.getX();
		else if (p.x > mapMode.getX() + mapMode.getW())
			p.x = mapMode.getX() + mapMode.getW();
		if (p.y < mapMode.getY())
			p.y = mapMode.getY();
		else if (p.y > mapMode.getY() + mapMode.getH())
			p.y = mapMode.getY() + mapMode.getH();
		return p;
	}

	private boolean bothOutside(IMapMode mapMode, Point2D.Double nw, Point2D.Double se) {
		return outside(mapMode, nw) && outside(mapMode, se);
	}

	private boolean outside(IMapMode mapMode, Point2D.Double p) {
		return p.x < mapMode.getX() || p.y < mapMode.getY()
				|| p.x > mapMode.getX() + mapMode.getW()
				|| p.y > mapMode.getY() + mapMode.getH();
	}

	@Override
	public ArrayList<IMwChunkOverlay> getChunksOverlay(int i, double v, double v1, double v2, double v3, double v4, double v5) {
		return null;
	}

	@Override
	public String getStatusString(int dim, int bX, int bY, int bZ) {
		return null;
	}

	@Override
	public void onMiddleClick(int i, int i1, int i2, IMapView iMapView) {
	}

	@Override
	public void onDimensionChanged(int i, IMapView iMapView) {
	}

	@Override
	public void onMapCenterChanged(double v, double v1, IMapView iMapView) {
	}

	@Override
	public void onZoomChanged(int i, IMapView iMapView) {
	}

	@Override
	public void onOverlayActivated(IMapView iMapView) {
	}

	@Override
	public void onOverlayDeactivated(IMapView iMapView) {
	}

	@Override
	public boolean onMouseInput(IMapView iMapView, IMapMode iMapMode) {
		return false;
	}

	@Override
	public ILabelInfo getLabelInfo(int i, int i1) {
		return null;
	}
}
