package com.halftone.yeoldetimes;

import android.graphics.Bitmap;

public class ImageUtils 
{
	/**
	 * Calculate Average of Pixels - Calculates from a particular x,y coordinate to x+gridSize,y+gridSize, the average colour of all pixels
	 * in that region
	 * 
	 * @param pixels - the pixel data of an image stored in an ArrayList
	 * @param yCoord - the y coordinate to start the square area of cells to average from
	 * @param xCoord - the x coordinate to start the square area of cells to average from
	 * @param gridSize - the width and height of the cell
	 * @return average - the average grey colour of all of the pixels in the area
	 */
	public static double calculateAverage(Bitmap bitmap, int yCoord, int xCoord, int gridSize)
	{
		double runningSum = 0;
		double amountInSquare = 0;
		
		// Iterate over every grey pixel in the image and keep a running total of their grey values in runningSum
		for(int i = yCoord; i < (yCoord + gridSize) && i < (bitmap.getHeight()); i++)
		{
			for(int j = xCoord; j < xCoord + gridSize && i < (bitmap.getWidth()); j++)
			{
				amountInSquare++;
				int pixelRGB = bitmap.getPixel(j,i);
				runningSum += pixelRGB;
			}
		}
		
		// Obtain the average from the running sum of all grey pixels divided by the amount of pixels in the square
		double average = runningSum/amountInSquare;
		
		return average;
	}
	
	/**
	 * @param colourAverage - The grey average colour obtained for a square area of pixels
	 * @param maxDiameter - The maximum diameter of a circle drawable
	 * @param gridSize - The size of the cell in which to draw the circle
	 * @return circleDiameter - The final calculated diameter of the circle
	 */
	public static double calculateCircleRadius(double colourAverage, double maxDiameter, double gridSize)
	{	
		double colourRatio = (colourAverage / 255.0);
		double inverseColourRatio = 1.0 - colourRatio;
		
		// Determine the area of the circle that we need to draw
		double areaOfCircle;
		areaOfCircle = (inverseColourRatio * (gridSize * gridSize));
		
		// Determine the radius and diameter of the circle using standard circular geometry equation to obtain radius
		double radius = Math.sqrt(areaOfCircle / Math.PI);
		return radius;
	}
}
