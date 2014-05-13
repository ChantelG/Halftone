package com.halftone.yeoldetimes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class CircleHalftoneImpl implements HalftoneAdapter{

	private Halftone halftone;
	private Bitmap bitmap;
	
	/**
	 * Circle halftone constructor
	 * 
	 * @param bitmap
	 */
	public CircleHalftoneImpl(Bitmap bitmap){
		this.bitmap = bitmap;
		this.halftone = new Halftone();
	}

	/**
	 * A method that draws different sizes of circles on the bitmap to create the halftone image
	 * 
	 * @param tempCanvas - the canvas to draw circle
	 * @param y - the y coordinate to start the circle drawing at
	 * @param x - the x coordinate to start the circle drawing at
	 * @param height - the radius
	 * @param width - the same as height, so only height is used for the radius
	 * @param paint - the paint object used to paint the circle (has a colour and a fill style e.g. BLACK, FILL)
	 */
	@Override
	public void drawPrimitive(Canvas tempCanvas, int x, int y, float height, float width, Paint paint) {
			float radius = height;
			float xOrigin = (float)(x+radius);
			float yOrigin = (float)(y+radius);
			tempCanvas.drawCircle(xOrigin, yOrigin, (float)radius, paint);
	}
}
