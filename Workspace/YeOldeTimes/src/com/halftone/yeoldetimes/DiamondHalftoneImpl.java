package com.halftone.yeoldetimes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class DiamondHalftoneImpl implements HalftoneAdapter{

	private Halftone halftone;
	private Bitmap bitmap;
	
	public DiamondHalftoneImpl(Bitmap bitmap){
		this.bitmap = bitmap;
		halftone = new Halftone();
	}

	@Override
	public void drawPrimitive(Canvas tempCanvas, int x, int y, float height, float width, Paint paint) {
		Path path;
		path = new Path();
		width = width*2;
		height = height*2;
		path.moveTo(x+(width/2), y);
		path.lineTo(x, y+(height/2));
		path.lineTo(x+(width/2),y+(height));
		path.lineTo(x+(width), y+(height/2));
		path.lineTo(x+(width/2), y);
		tempCanvas.drawPath(path, paint);
	}
}
