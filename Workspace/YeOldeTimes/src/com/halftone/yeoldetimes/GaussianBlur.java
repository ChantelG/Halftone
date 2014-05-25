package com.halftone.yeoldetimes;

import android.graphics.Bitmap;

/**
 * This class provides the facility to apply a Gaussian Blur to an image of a level of strength of weak, medium or strong.
 * A weak intensity is a weak blur, medium is a medium blur (in between weak and strong) and a strong blur is very blurred.
 * 
 * @author Chantel Garcia & Carmen Pui
 */

public class GaussianBlur {
	
	// Declare the weak, medium and strong blur matrices which determine a weak, medium and strong blur on the image
	private final double[][] WEAK_BLUR_MATRIX = {
	        { 1, 2, 1 },
	        { 2, 4, 2 },
	        { 1, 2, 1 }
    };
	private final double[][] MEIDUM_BLUR_MATRIX = {
	        { 1, 4, 6, 4, 1 },
	        { 2, 8, 12, 8, 2 },
	        { 4, 16, 24, 16, 4 },
	        { 2, 8, 12, 8, 2 },
	        { 1, 4, 6, 4, 1 }
	};
	private final double[][] STRONG_BLUR_MATRIX = {
			{ 1, 6, 15, 20, 15, 6, 1 },
	        { 2, 12, 30, 40, 30, 12, 2 },
	        { 4, 24, 60, 80, 60, 24, 4 },
	        { 8, 48, 120, 160, 120, 48, 8 },
	        { 4, 24, 60, 80, 60, 24, 4 },
	        { 2, 12, 30, 40, 30, 12, 2 },
	        { 1, 6, 15, 20, 15, 6, 1 }
	};
	
	// Determine the factors that must be applied to the matrices
	private final int WEAK_FACTOR = 16;
	private final int MEDIUM_FACTOR = 160;
	private final int STRONG_FACTOR = 1408;
	
	// Determine the amount to scale the image down by depending on the strength of the blur
	private final int WEAK_SCALE_DOWN = 2;
	private final int MEDIUM_SCALE_DOWN = 4;
	private final int STRONG_SCALE_DOWN = 6;
	
	// Keep track of the current blur matrix, blur factor and scale down factor
	private double blurMatrix[][];
	private int blurFactor;
	private int scaleDownFactor;
	
	/**
	 * The constructor sets the blur matrix to a weak blur, the blur factor to a weak factor and the scale down factor to the weak scale
	 * down factor.
	 * 
	 * Thus, the blur is set to weak by default. 
	 */
	public GaussianBlur() {
		// Set the gaussian blur to be weak by default
		blurMatrix = WEAK_BLUR_MATRIX;
		blurFactor = WEAK_FACTOR; 
		scaleDownFactor = WEAK_SCALE_DOWN;
	}
	
	/**
	 * This method sets the gaussian blur strength for the gaussian blur
	 * @param gaussianBlurStrength - A strength of WEAK, MEDIUM or STRONG
	 */
	public void setGaussianBlurStrength(GaussianBlurStrength gaussianBlurStrength){
			switch(gaussianBlurStrength){
			case WEAK:
				blurMatrix = WEAK_BLUR_MATRIX;
				blurFactor = WEAK_FACTOR; 
				scaleDownFactor = WEAK_SCALE_DOWN;
				break;
			case MEDIUM:
				blurMatrix = MEIDUM_BLUR_MATRIX;
				blurFactor = MEDIUM_FACTOR;
				scaleDownFactor = MEDIUM_SCALE_DOWN;
				break;
			case STRONG:
				blurMatrix = STRONG_BLUR_MATRIX;
				blurFactor = STRONG_FACTOR;
				scaleDownFactor = STRONG_SCALE_DOWN;
				break;
		}
	}
	
	/**
	 * This method blurs the bitmap passed in as a parameter by the level of intensity set for the blurMatrix, blurFactor and scaleDownFactor.
	 * It returns a bitmap which has had the gaussian blur applied to it.
	 * Cropping will occur after the gaussian blur is performed as the gaussian blur process shrinks the image down.
	 * 
	 * @param bitmap - The bitmap with the gaussian blur applied to it.
	 * @return The bitmap that was created that has a gaussian blur applied to it 
	 */
	public Bitmap blur(Bitmap bitmap){
		// Set up a new bitmap which has the convolution matrix applied to it with the gaussian blur matrix passed in
		ConvolutionMatrix convMatrix = new ConvolutionMatrix(blurMatrix.length, blurFactor, 0, blurMatrix);
	    Bitmap newBitmap = convMatrix.applyConvolutionMatrixToBitmap(bitmap);
	    
	    /* Create a cropped bitmap, to crop the bitmap depending on the level of intensity of the blur (the strong the blur, the more
	     * cropping needs to occur
	     */
		Bitmap croppedBitmap = Bitmap.createBitmap(newBitmap, 1, 1, bitmap.getWidth()-scaleDownFactor, bitmap.getHeight()-scaleDownFactor);
		
		// Recycle the old bitmap that we are no longer using
		newBitmap.recycle();
		newBitmap = null;
		
		// Return the final gaussian blurred, cropped image
		return croppedBitmap;
	}
}
