package com.halftone.improvements;
import java.util.ArrayList;

/**
 * @author Chantel Garcia - 22629394
 * @since 11/03/14
 *
 * ImageUtils class contains various utilities performable on any image. These utilities include: calculating the colour average of a 
 * square section of pixels, calculating the diameter of a circle given the average colour of a square section of pixels and calculating
 * the hypotenuse of the triangle contained within a square section of pixels. 
 */

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
	public static double calculateAverage(ArrayList<ArrayList<Integer>> pixels, int yCoord, int xCoord, int gridSize)
	{
		double runningSum = 0;
		double amountInSquare = gridSize * gridSize;
		
		// Iterate over every grey pixel in the image and keep a running total of their grey values in runningSum
		for(int i = yCoord; i < yCoord + gridSize; i++)
		{
			for(int j = xCoord; j < xCoord + gridSize; j++)
			{
				runningSum += Math.round((double)pixels.get(i).get(j));
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
	public static double calculateCircleDiameter(double colourAverage, double maxDiameter, double gridSize)
	{	
		/* Obtain the colour ratio (a percentage of 255) to determine the percentage of the circle that we will draw. 
		 * Then take the inverse of the ratio, as 255 represents white and we want black returning a colourRatio of 1.0 not white as
		 * the largest circles will be to represent dark areas
		 */
		double colourRatio = (colourAverage / 255.0);
		double inverseColourRatio = 1.0 - colourRatio;
		
		// Determine the area of the circle that we need to draw
		double areaOfCircle;
		areaOfCircle = (inverseColourRatio * ((gridSize+1) * (gridSize+1)));
		
		// Determine the radius and diameter of the circle using standard circular geometry equation to obtain radius
		double radius = Math.sqrt(areaOfCircle / Math.PI);
		double circleDiameter = radius * 2;
		
		return circleDiameter;
	}
	
	/**
	 * @param height - The height of the triangle
	 * @param width - The width of the triangle
	 * @return hypotenuse - The hypotenuse of the triangle
	 */
	public static double calculateHypotenuse(int height, int width)
	{
		double hypotenuse = (Math.pow(height, 2) + Math.pow(width, 2));
		hypotenuse = Math.sqrt(hypotenuse);
		
		return hypotenuse;
	}
}
