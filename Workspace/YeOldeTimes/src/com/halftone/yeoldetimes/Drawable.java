package com.halftone.yeoldetimes;

import android.graphics.Bitmap;

/**
 * This interface enforces a single method to be implemented, which takes a bitmap and a primitive type to create a halftone image 
 * with that primitive type of the given bitmap
 * 
 * @author Chantel Garcia & Carmen Pui
 */

public interface Drawable {
	
	/**
	 * Halftone method that will be implemented based on the type of halftone required (i.e. will be implemented by Circle halftone,
	 * Diamond halftone and Square halftone Impl
	 * 
	 * @param bitmap - the image bitmap to halftone
	 * @param type - the type of shape to use for halftone (e.g. CIRCLE, SQUARE, DIAMOND)
	 * 
	 */
	public Bitmap makeHalftone(Bitmap bitmap, PrimitiveType type);
}
