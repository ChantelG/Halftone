package com.halftone.yeoldetimes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;

public class Halftone implements Drawable{

	private Paint white;
	private Paint black;
	
	public Halftone(){
		white = new Paint();
        white.setColor(Color.WHITE);
        
        black = new Paint();
        black.setColor(Color.BLACK);
	}
	
	public Bitmap convertToGrayscale(Bitmap bitmap) {
		// Create the grayScale bitmap, canvas and paint object
		Bitmap grayScale = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(grayScale);
		Paint paint = new Paint();
		ColorMatrix colorMatrix = new ColorMatrix();
		        
		// Desaturate the bitmap and apply the filter, then draw it to the canvas
        colorMatrix.setSaturation(0);
        final ColorMatrixColorFilter f = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(f);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        
        // Recycle new bitmap
        bitmap.recycle();
       
        // Update the new bitmap to the grayscale bitmap and halftone it
        return grayScale;
	}
	
	@Override
	public Bitmap makeHalftone(Bitmap bitmap, PrimitiveType type) {
    	int gridSize = 5;
    	int MAX_CIRCLE_DIAMETER = 5;
    	HalftoneAdapter halftoner = null;
    	
    	switch(type){
    	case CIRCLE:
    		halftoner = new CircleHalftoneImpl(bitmap);
    		break;
    	case SQUARE:
    		halftoner = new SquareHalftoneImpl(bitmap);
    		break;
    	case DIAMOND:
    		halftoner = new DiamondHalftoneImpl(bitmap);
    		break;
    	}
    	
    	bitmap = convertToGrayscale(bitmap);
    	
		//Create a new image bitmap and attach a brand new canvas to it
		Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth()-(bitmap.getWidth()%gridSize), bitmap.getHeight()-(bitmap.getHeight()%gridSize), Bitmap.Config.ARGB_8888);
		Canvas tempCanvas = new Canvas(tempBitmap);

		//Draw the image bitmap into the canvas
		tempCanvas.drawBitmap(bitmap, 0, 0, null);
		
		Bitmap theTempBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		
		tempCanvas.drawRect(0, 0, tempCanvas.getWidth(), tempCanvas.getHeight(), white);

    	for(int i=0; i < tempBitmap.getHeight(); i++) {
		      for (int j=0; j < tempBitmap.getWidth(); j++) {
		        if(i%(gridSize) == 0 && j%(gridSize) == 0) {
		        	// Calculate the average colour of portion of the image starting at i,j and extending out to gridSize in height and width
		        	float greyAvg = ImageUtils.calculateAverage(theTempBitmap, i, j, gridSize);
		        	
		        	if(greyAvg != 0 && greyAvg != 255){
			        	// Determine the diameter of the circle based on the average grey colour and draw the circle
		        		float circleDiameter = ImageUtils.calculateCircleRadius(greyAvg, MAX_CIRCLE_DIAMETER, gridSize);
		        		
		        		halftoner.drawPrimitive(tempCanvas, j, i, circleDiameter, circleDiameter, black);
		        	}
		        	else {
		        		if(greyAvg == 255)
		        			tempCanvas.drawRect(j, i, gridSize+j, gridSize+i, white);
		        		else
		        			tempCanvas.drawRect(j, i, gridSize+j, gridSize+i, black);
		        	}
		        }
		      }
		}
    	
		//Attach the canvas to the ImageView
		return tempBitmap;
		
	}
}
