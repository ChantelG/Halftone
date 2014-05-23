package com.halftone.yeoldetimes;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * This class allows a circle primitive to be drawn when drawPrimitive is invoked on a particular canvas, with a particular origin
 * coordinate to draw from (x,y), width, height and paint which denotes the colour and paint style of the circle
 * 
 * @author chantelgarcia
 */

public class CircleHalftoneImpl implements HalftoneAdapter{

	/**
	 * Circle halftone constructor
	 * 
	 */
	public CircleHalftoneImpl(){}

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
