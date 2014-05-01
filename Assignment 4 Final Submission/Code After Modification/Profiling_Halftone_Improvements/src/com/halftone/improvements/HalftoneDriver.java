package com.halftone.improvements;

/**
 * @author Chantel Garcia & Carmen Pui
 * @since 11/03/14
 * 
 * Driver class for converting a standard image (coloured or grey) to a halftone image and then outputting it as a new file
 */

public class HalftoneDriver 
{
	public static void main (String [] args) throws Exception
	{	
		int gridSize = 0;
		
		// Expect exactly 3 arguments, otherwise output an exception
		if(args.length == 3)
		{
			/* Handle argument type issues (i.e. empty arguments OR invalid file with no file extension passed as input / output file name
			*  OR third argument is not an integer)
			*/
			for(String arg : args)
			{
				if(arg.length() == 0)
					throw new Exception("One or more of the arguments are empty. Please populate arguments with: " +
					" input file, output file, grid size");
			}
			
			if(args[0].indexOf('.') == -1 || args[1].indexOf('.') == -1)
				throw new Exception("The file extention for the file that you are reading in and the file " +
						"that you wish to output must be included.");
			
			try
			{
				gridSize = Integer.parseInt(args[2]);
			}
			catch(Exception e)
			{
				throw new Exception("The 3rd parameter must be an integer value representing the grid size");
			}
			
			/*
			 * Load in the image, convert the image to grayscale and scale the image down on initialisation.
			 * Convert the image to halftone and then output the image.
			 * 
			 * If the image loaded in is of a type unsupported OR is not an image OR is not an image but has been given an image extension
			 * like .png or .jpeg, an exception is thrown
			 */
			Image image = new Image(args[0], args[1], gridSize);
			
			image.convertToHalftone();
			image.write();
		}
		else
			throw new Exception("Incorrect number of arguments inputted. Arguments should be: " +
					" input file, output file, grid size");
	}
}
