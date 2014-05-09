package com.halftone.yeoldetimes;

import android.graphics.Bitmap;

public interface Drawable {
	
	Bitmap makeHalftone(Bitmap bitmap, PrimitiveType type);
}
