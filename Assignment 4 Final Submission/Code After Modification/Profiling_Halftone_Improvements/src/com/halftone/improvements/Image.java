package com.halftone.improvements;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * @author Chantel Garcia & Carmen Pui
 * @since 11/03/14
 * @modified 22/04/14
 * 
 * Image class to handle the loading and saving of images, conversion to greyscale and conversion to halftone.
 */

public class Image 
{
	private BufferedImage image = null;
	private String outputFileName;
	private int gridSize;
	private int height;
	private int width;
	
	// Store all of the pixels in the image so that there is a reference back to the previous image after the halftoning begins
	private ArrayList<ArrayList<Integer>> pixels = new ArrayList<ArrayList<Integer>>();
	
	private final int MAX_CIRCLE_DIAMETER;
	
	/**
	 * Image constructor - Initialises file, reads file into image, initialises member variables, initialises image
	 * 
	 * @param inputFile - The name of the input file obtained from args[0]
	 * @param outputFile - The name of the output file obtained from args [1]
	 * @param cellWidthHeight - The size of the grid obtained from args [2]
	 * @throws Exception - If the file cannot be read into BufferedImage, throw an exception that image file was unable to be read
	 */
	public Image(String inputFile, String outputFile, int cellWidthHeight) throws Exception
	{
		outputFileName = outputFile;
		
		File file = new File(inputFile);
		
		try 
		{
			this.image = ImageIO.read(file);
		}
		catch (IOException e) 
		{
			throw new Exception("Unable to read image file provided.");
		}
		
		// Store the image's width and height
		try
		{
			height = image.getHeight();
			width = image.getWidth();
		}
		catch(Exception e)
		{
			throw new Exception("The file provided could not be initialised as an image. The file may not be of a supported image type.");
		}
		
		// Handle the case where the user inputs a grid size that is less than or equal to 1 or greater than the image's diameters
		if(cellWidthHeight <= 1 || cellWidthHeight > width || cellWidthHeight > height)
			throw new Exception("The grid size that you specified is either too small or too big for the image that you want to halftone");
		else
			gridSize = cellWidthHeight;
		
		// Determine the maximum diameter for any circle in the grid
		MAX_CIRCLE_DIAMETER = (int)ImageUtils.calculateHypotenuse(gridSize, gridSize);

		initialiseImage();
	}
	
	/** 
	 * Initialiser for image - converts the image to grayscale and then scales it down to fit the grid size
	 */
	private void initialiseImage()
	{
		convertToGrayscale();
		squashToFit();
	}
	
	/**
	 * Writer for image - writes image file out
	 */
	public void write()
	{
		try 
		{
			ImageIO.write(image, "png", new File(outputFileName));
		} 
		catch (IOException e) 
		{
			System.out.println("Could not write image to file.");
		}
	}
	
	/**
	 * Squasher for image - squashes the image down to the required size by squashing it to fit the required grid size
	 */
	public void squashToFit()
	{
		int newWidth = width;
		int newHeight = height;
		
		// Determine the new "squashed" width and height in order to fit the required grid size
		if(width % gridSize != 0)
			newWidth = width-(width%gridSize);
		if(height % gridSize != 0)
			newHeight = height-(height%gridSize);
		
		// If squashing was necessary, update the image's new width and height
		if(width != newWidth || height != newHeight)
		{	
			width = newWidth;
		    height = newHeight;
		}
		
	    /*
	     *  Determine padding to go around the image so that we can draw maximum sized circles that encompass the entire area of a 
	     *  cell on the boundaries of the image without getting a "drawing out of bounds" error
	     */
	    int widthFrame = width+(gridSize*2);
		int heightFrame = height+(gridSize*2);
		
	    BufferedImage tempImage = new BufferedImage(widthFrame, heightFrame, image.getType());
		
	    // Scale/squash the input image to a size that is a multiple of the grid size (so we can fit the required circles)
        Graphics2D outputImageGraphic = tempImage.createGraphics();
        
        outputImageGraphic.drawImage(image, gridSize, gridSize, newWidth, newHeight, null);
        outputImageGraphic.dispose();
        
        image = tempImage;
	}

	/**
	 * Convert image to grayscale - Converts an image to grayscale by processing it pixel by pixel and applying a luminoscity weighting
	 * formula that weights the red, green and blue components to give a better fidelity grayscale image.
	 */
	public void convertToGrayscale()
	{
		// Loop over each pixel in the image
		for (int i = 0; i < height; i++) 
		{
			pixels.add(new ArrayList<Integer>());
		      for (int j = 0; j < width; j++) 
		      {
		    	/**
		    	 * NOTE: Modifications have been made to this method to make it run faster.
		    	 * 
		    	 * The modifications made include : 
		    	 * 1. Calculating the average of the pixels to obtain a grey colour in a simpler way 
		    	 * (add the red green and blue values together and divide them by 3)
		    	 * 2. The removal of addition of the different types of grey colours to the HashMap
		    	 * 3. Removal of the declaration of a new variable for the new grey colour passed
		    	 * into the image.setRGB call (instead, the new Colour is created in the parameter call)
		    	 */
		    	  
		    	// Obtain the pixel's red blue and green values
		        Color pixelRGB = new Color(image.getRGB(j,i));
		        
		        // Obtain grey colour through adding the pixel RGB values together and dividing by 3 (the number of values)
		        int grey = (int)(((pixelRGB.getRed())+(pixelRGB.getGreen())+(pixelRGB.getBlue()))/3);
                
                // Update image to greyscale
                image.setRGB(j, i, new Color(grey, grey, grey).getRGB());
              
                // Store pixel data so we can later process averages of cells without overlapping circles from the halftone effect interfering
                pixels.get(i).add(grey);
		      }
		}
	}
	
	/**
	 * Convert to halftone - Takes the pixels ArrayList containing data about every pixel in the image and calculates the average 
	 * of every cell (determined in height and width by the gridSize argument).
	 * It then determines the size of the circle to draw and draws the circle for that cell in the cell. 
	 */
	public void convertToHalftone()
	{
		Graphics2D outputImageGraphic = image.createGraphics();
		
	    // Fill the background of the image with white so that we can draw our black dots on top
      	outputImageGraphic.setPaint(Color.white);
        outputImageGraphic.fillRect(0, 0, image.getWidth(), image.getHeight());

		for(int i=0; i < height; i++)
		{
		      for (int j=0; j < width; j++) 
		      {
		        if(i%(gridSize) == 0 && j%(gridSize) == 0)
		        {
		        	// Calculate the average colour of portion of the image starting at i,j and extending out to gridSize in height and width
		        	double greyAvg = ImageUtils.calculateAverage(pixels, i, j, gridSize);

		        	// Determine the diameter of the circle based on the average grey colour and draw the circle
	        		double circleDiameter = ImageUtils.calculateCircleDiameter(greyAvg, MAX_CIRCLE_DIAMETER, gridSize);
	        		outputImageGraphic.setPaint(Color.black);
	        		
	        		// Fill the entire cell with black if we have 100% black or fill the entire cell with white if we have 100% white
	        		if(circleDiameter > gridSize + 2.0)
	        		{
	        			outputImageGraphic.fillRect(j+(gridSize), i+(gridSize), gridSize, gridSize);
	        		}
	        		else if(circleDiameter <= (0.1*gridSize))
	        		{
	        			outputImageGraphic.setPaint(Color.white);
	        			outputImageGraphic.fillRect(j+(gridSize), i+(gridSize), gridSize, gridSize);
	        		}
	        		else
	        		{
	        			/*
	        			 * Determine where to draw the circle. In both the cases where the circle diameter is greater than the gridsize or less
	        			 * than the gridsize, we draw at an offset to the origin to ensure that the circle is drawn in the middle of the cell.
	        			 */
	        			if(circleDiameter > gridSize)
	        			{
	        				Ellipse2D.Double circle = new Ellipse2D.Double(j+gridSize-(circleDiameter-gridSize), i+gridSize-(circleDiameter-gridSize), circleDiameter, circleDiameter);
			        		outputImageGraphic.fill(circle);	        				
	        			}
	        			else
	        			{
	        				Ellipse2D.Double circle = new Ellipse2D.Double(j+gridSize+(gridSize-circleDiameter), i+gridSize+(gridSize-circleDiameter), circleDiameter, circleDiameter);
			        		outputImageGraphic.fill(circle);
	        			}
	        		}
		        }
		      }
		}
		
		// Restore the image to its original size
		crop(gridSize, gridSize);
	}
	
	/**
	 * Cropper - Crops the image starting at a specified x and y coordinate and going to the width and height of the original squashed image
	 * 
	 * @param xCoord - x Coordinate to crop from
	 * @param yCoord - y coordinate to crop from
	 */
	public void crop(int xCoord, int yCoord)
	{
		BufferedImage croppedImage = image.getSubimage(xCoord, yCoord, width, height);
		image = croppedImage; 
	}
}
