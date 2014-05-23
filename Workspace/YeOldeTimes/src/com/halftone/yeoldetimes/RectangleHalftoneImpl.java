package com.halftone.yeoldetimes;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * This class allows a rectangle primitive to be drawn when drawPrimitive is invoked on a particular canvas, with a particular origin
 * coordinate to draw from (x,y), width, height and paint which denotes the colour and paint style of the rectangle
 * 
 * @author Chantel Garcia & Carmen Pui
 */

public class RectangleHalftoneImpl implements HalftoneAdapter{
	
	/**
	 * Rectangle halftone constructor
	 */
	public RectangleHalftoneImpl(){}

	/**
	 * A method that draws different sizes of rectangles on the bitmap to create the halftone image
	 * 
	 * @param tempCanvas - the canvas to draw the rectangle on
	 * @param y - the top left y coordinate to start the rectangle drawing at
	 * @param x - the top left x coordinate to start the rectangle drawing at
	 * @param height - the height of the rectangle
	 * @param width - the width of the rectangle/2
	 * @param paint - the paint object used to paint the rectangle (has a colour and a fill style e.g. BLACK, FILL)
	 */
	@Override
	public void drawPrimitive(Canvas tempCanvas, int x, int y, float height, float width, Paint paint) {
		width = width*2;
		tempCanvas.drawRect(x, y, width+x, height+y, paint);
	}
}