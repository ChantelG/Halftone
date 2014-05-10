package com.halftone.yeoldetimes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class CircleHalftoneImpl implements HalftoneAdapter{

	Halftone halftone;
	Bitmap bitmap;
	
	public CircleHalftoneImpl(Bitmap bitmap){
		this.bitmap = bitmap;
		halftone = new Halftone();
	}

	@Override
	public void drawPrimitive(Canvas tempCanvas, int x, int y, float height, float width, Paint paint) {
			float radius = height;
			float xOrigin = (float)(x+radius);
			float yOrigin = (float)(y+radius);
			tempCanvas.drawCircle(xOrigin, yOrigin, (float)radius, paint);
	}
}
