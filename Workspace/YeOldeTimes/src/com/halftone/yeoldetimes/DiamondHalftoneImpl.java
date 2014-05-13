package com.halftone.yeoldetimes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class DiamondHalftoneImpl implements HalftoneAdapter{

	private Halftone halftone;
	private Bitmap bitmap;
	
	/**
	 * Diamond halftone constructor
	 * 
	 * @param bitmap - the image to be halftoned
	 */
	public DiamondHalftoneImpl(Bitmap bitmap){
		this.bitmap = bitmap;
		this.halftone = new Halftone();
	}

	/**
	 * A method that draws different sizes of diamonds on the bitmap to create the halftone image
	 * 
	 * @param tempCanvas - the canvas to draw diamonds
	 * @param y - the top left y coordinate of the square
	 * @param x - the top left x coordinate of the square
	 * @param height - the height of the diamond/2
	 * @param width - the width of the diamond/2
	 * @param paint - the paint object used to paint the diamond (has a colour and a fill style e.g. BLACK, FILL)
	 */
	@Override
	public void drawPrimitive(Canvas tempCanvas, int x, int y, float height, float width, Paint paint) {
		Path path = new Path();
		width = width*2;
		height = height*2;
		
		// Draw the diamond shape starting at the top middle, going the right point, bottom point left point and them top point again
		path.moveTo(x+(width/2), y);
		path.lineTo(x, y+(height/2));
		path.lineTo(x+(width/2),y+(height));
		path.lineTo(x+(width), y+(height/2));
		path.lineTo(x+(width/2), y);
		tempCanvas.drawPath(path, paint);
	}
}
