package com.halftone.yeoldetimes;

import android.graphics.Bitmap;

public interface Drawable {
	
	public Bitmap makeHalftone(Bitmap bitmap, PrimitiveType type);
}
