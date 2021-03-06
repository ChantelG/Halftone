package com.halftone.yeoldetimes;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * This class is a utility class which provides a method that calculates the average grey colour of a series of pixels in a particular 
 * region and a method that determines the circle radius for a dot drawn on a halftone image. This circle radius is actually used
 * to also determine the height and width of the diamond primitives drawn onto the image and the height and width of the rectangle
 * primitives drawn.
 * 
 * @author Chantel Garcia & Carmen Pui
 */

public class ImageUtils 
{
	/**
	 * Calculate Average of Pixels - Calculates from a particular x,y coordinate to x+gridSize, y+gridSize, the average colour of all 
	 * pixels in that region
	 * 
	 * @param bitmap - the bitmap of an image to get the average square of cells from
	 * @param yCoord - the y coordinate to start the square area of cells to average from
	 * @param xCoord - the x coordinate to start the square area of cells to average from
	 * @param gridSize - the width and height of the cell
	 * @return average - the average grey colour of all of the pixels in the area
	 */
	public static float calculateAverage(Bitmap bitmap, int yCoord, int xCoord, int gridSize) {
		float runningSum = 0;
		float amountInSquare = 0;
		
		// Iterate over every grey pixel in the image and keep a running total of their grey values in runningSum
		for(int i = yCoord; i < (yCoord + gridSize); i++) {
			for(int j = xCoord; j < (xCoord + gridSize); j++) {
				amountInSquare++;
				int pixelRGB = bitmap.getPixel(j,i);
				int r = Color.red(pixelRGB);
				int g = Color.green(pixelRGB);
				int b = Color.blue(pixelRGB);
				int pixelVal = (r+g+b)/3;
				runningSum += pixelVal;
			}
		}
		// Obtain the average from the running sum of all grey pixels divided by the amount of pixels in the square
		float average = runningSum/amountInSquare;

		return average;
	}
	
	/**
	 * Calculates the radius of the circle to be drawn based on the colour average determined
	 * 
	 * @param colourAverage - The grey average colour obtained for a square area of pixels
	 * @param maxDiameter - The maximum diameter of a circle drawable
	 * @param gridSize - The size of the cell in which to draw the circle
	 * @return radius - The final calculated radius of the circle
	 */
	public static float calculateCircleRadius(float colourAverage, float maxDiameter, float gridSize)
	{	
		float colourRatio = (colourAverage / 255.0f);
		float inverseColourRatio = 1.0f - colourRatio;
		
		// Determine the area of the circle that we need to draw
		float areaOfCircle;
		areaOfCircle = (inverseColourRatio * (gridSize * gridSize));
		
		// Determine the radius of the circle using standard circular geometry equation to obtain radius
		float radius = (float) Math.sqrt(areaOfCircle / Math.PI);
		return radius;
	}
}
