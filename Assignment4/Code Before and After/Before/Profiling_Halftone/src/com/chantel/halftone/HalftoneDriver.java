package com.chantel.halftone;
/**
 * @author Chantel Garcia & Carmen Pui 
 * @since 11/03/14
 * @modified 22/04/14
 * 
 * Driver class for converting a standard image (coloured or grey) to a halftone image and then outputting it as a new file
 */

public class HalftoneDriver 
{
	public static void main (String [] args) throws Exception
	{	
		/* Below is the call to halftone a given image. Note that as it was required to run the 
		 * application in eclipse in order to be able to catch it in time to profile it, the arguments
		 * of the image to be processed are directly passed in where:
		 * first arg = name of file to read in
		 * second arg = name of file to save out
		 * third arg = size of dot to draw
		 */
		Image image = new Image("person.jpg", "person.png", 10);
		
		image.convertToHalftone();
		image.write();
	}
}
