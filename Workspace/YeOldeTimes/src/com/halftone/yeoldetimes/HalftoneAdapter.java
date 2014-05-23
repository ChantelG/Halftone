package com.halftone.yeoldetimes;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * This interface is an adapter for halftoning which defines a method that must be implemented which should take a canvas,
 * x and y position, height, width and paint object in order to draw a particular primitive onto the provided canvas.
 * 
 * @author Chantel Garcia & Carmen Pui
 */

public interface HalftoneAdapter {
	
	/**
	 * This method will be implemented by the CircleHalftoneImpl, SquareHalftoneImpl and DiamondHalftoneImpl, it allows the appropriate
	 * primitive to be drawn (Adapter pattern)
	 * 
	 * @param tempCanvas - a canvas to draw on
	 * @param x - the x coordinate of the top left of the gridSquare
	 * @param y - the y coordinate of the top left of the gridSquare 
	 * @param height - the height of the halftone primitive to draw
	 * @param width - the width of the halftone primitive to draw
	 * @param paint - paint object to paint the shape primitive with
	 */
	public void drawPrimitive(Canvas tempCanvas, int x, int y, float height, float width, Paint paint);	
}
