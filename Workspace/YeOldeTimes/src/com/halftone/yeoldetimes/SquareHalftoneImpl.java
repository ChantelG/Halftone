package com.halftone.yeoldetimes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class SquareHalftoneImpl implements HalftoneAdapter{

	private Halftone halftone;
	private Bitmap bitmap;
	
	/**
	 * Square halftone constructor
	 * 
	 * @param bitmap - the image to be halftoned
	 */
	public SquareHalftoneImpl(Bitmap bitmap){
		this.bitmap = bitmap;
		this.halftone = new Halftone();
	}

	/**
	 * A method that draws different sizes of squares on the bitmap to create the halftone image
	 * 
	 * @param tempCanvas - the canvas to draw the square on
	 * @param y - the top left y coordinate to start the square drawing at
	 * @param x - the top left x coordinate to start the square drawing at
	 * @param height - the height of the square
	 * @param width - the width of the square
	 * @param paint - the paint object used to paint the square (has a colour and a fill style e.g. BLACK, FILL)
	 */
	@Override
	public void drawPrimitive(Canvas tempCanvas, int x, int y, float height, float width, Paint paint) {
		width = width*2;
		height = height*2;
		tempCanvas.drawRect(x, y, width+x, height+y, paint);
	}
}