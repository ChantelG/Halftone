package com.halftone.yeoldetimes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class SquareHalftoneImpl implements HalftoneAdapter{

	private Halftone halftone;
	private Bitmap bitmap;
	
	public SquareHalftoneImpl(Bitmap bitmap){
		this.bitmap = bitmap;
		halftone = new Halftone();
	}

	@Override
	public void drawPrimitive(Canvas tempCanvas, int x, int y, float height, float width, Paint paint) {
		width = width*2;
		height = height*2;
		tempCanvas.drawRect(x, y, width+x, height+y, paint);
	}
}