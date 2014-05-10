package com.halftone.yeoldetimes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public interface HalftoneAdapter {
	
	public void drawPrimitive(Canvas tempCanvas, int x, int y, float height, float width, Paint paint);	
}
