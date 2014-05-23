package com.halftone.yeoldetimes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * This class provides all of the halftoning functionality required to halftone an image using any primitive shape as the dots in the
 * halftone image. 
 * It keeps track of two paint objects which are used to paint the background of the image and the primitive shapes on top of the background
 * It also keeps track of the grid size, which has a constant value of 5
 * It performs the functionality to convert an image to grayscale and halftone an image
 * 
 * @author Chantel Garcia & Carmen Pui
 */

public class Halftone implements Drawable{

	private Paint white;
	private Paint black;
	public static final int GRID_SIZE = 5;
	
	/**
	 * Halftone constructor
	 * Makes white and black paint objects
	 */
	public Halftone(){
		white = new Paint();
        white.setColor(Color.WHITE);
        
        black = new Paint();
        black.setColor(Color.BLACK);
	}
	
	/**
	 * convertToGrayscale converts the bitmap image to a grayscale image and draws it on the canvas
	 * 
	 * @param bitmap - the original image to be converted to grayscale
	 * @return grayscale - the grayscale bitmap image on the canvas
	 */
	public Bitmap convertToGrayscale(Bitmap bitmap) {
		// Create the grayScale bitmap, canvas and paint object
		Bitmap grayScale = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(grayScale);
		Paint paint = new Paint();
		ColorMatrix colorMatrix = new ColorMatrix();
		        
		// Desaturate the bitmap and apply the filter, then draw it to the canvas
        colorMatrix.setSaturation(0);
        final ColorMatrixColorFilter f = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(f);
        canvas.drawBitmap(bitmap, 0, 0, paint);
       
        // Update the new bitmap to the grayscale bitmap and halftone it
        return grayScale;
	}
	
	/**
	 * Convert the greyscale bitmap image to a halftone image with the shape chosen
	 * 
	 * @param bitmap - the greyscale bitmap
	 * @param type - the shape of the halftoner
	 * @return tempBitmap - the halftoned image bitmap
	 */
	@Override
	public Bitmap makeHalftone(Bitmap bitmap, PrimitiveType type) {
    	int MAX_CIRCLE_DIAMETER = 5;
    	HalftoneAdapter halftoner = null;
    	
    	switch(type){
    	case CIRCLE:
    		halftoner = new CircleHalftoneImpl();
    		break;
    	case RECTANGLE:
    		halftoner = new RectangleHalftoneImpl();
    		break;
    	case DIAMOND:
    		halftoner = new DiamondHalftoneImpl();
    		break;
    	}
    	
    	bitmap = convertToGrayscale(bitmap);
    	
		//Create a new image bitmap and attach a brand new canvas to it
		Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth()-(bitmap.getWidth()%GRID_SIZE), bitmap.getHeight()-(bitmap.getHeight()%GRID_SIZE), Bitmap.Config.RGB_565);
		Canvas tempCanvas = new Canvas(tempBitmap);

		// Draw the image bitmap into the canvas
		tempCanvas.drawBitmap(bitmap, 0, 0, null);
		
		// Draw the white background on the canvas
		tempCanvas.drawRect(0, 0, tempCanvas.getWidth(), tempCanvas.getHeight(), white);

    	for(int i=0; i < tempBitmap.getHeight(); i++) {
		      for (int j=0; j < tempBitmap.getWidth(); j++) {
		        if(i%(GRID_SIZE) == 0 && j%(GRID_SIZE) == 0) {
		        	// Calculate the average colour of portion of the image starting at i,j and extending out to gridSize in height and width
		        	float greyAvg = ImageUtils.calculateAverage(bitmap, i, j, GRID_SIZE);
		        	
		        	if(greyAvg != 0 && greyAvg != 255){
			        	// Determine the diameter of the circle based on the average grey colour and draw the circle
		        		float circleDiameter = ImageUtils.calculateCircleRadius(greyAvg, MAX_CIRCLE_DIAMETER, GRID_SIZE);
		        		
		        		halftoner.drawPrimitive(tempCanvas, j, i, circleDiameter, circleDiameter, black);
		        	}
		        	else {
		        		if(greyAvg == 255)
		        			tempCanvas.drawRect(j, i, GRID_SIZE+j, GRID_SIZE+i, white);
		        		else
		        			tempCanvas.drawRect(j, i, GRID_SIZE+j, GRID_SIZE+i, black);
		        	}
		        }
		    }
		}
    	
    	bitmap.recycle();
    	
		//Attach the canvas to the ImageView
		return tempBitmap;
	}
}
