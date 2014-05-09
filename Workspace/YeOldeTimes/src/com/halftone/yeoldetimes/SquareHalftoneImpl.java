package com.halftone.yeoldetimes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class SquareHalftoneImpl implements HalftoneAdapter{

	Halftone halftone;
	Bitmap bitmap;
	
	public SquareHalftoneImpl(Bitmap bitmap){
		this.bitmap = bitmap;
		halftone = new Halftone();
	}

	@Override
	public void drawPrimitive(Canvas tempCanvas, int x, int y, float height, float width, Paint paint) {
		tempCanvas.drawRect(x, y, width+x, height+y, paint);
	}

	@Override
	public void halftone(Bitmap bitmap) {
		halftone.makeHalftone(bitmap, PrimitiveType.CIRCLE);
	}
}