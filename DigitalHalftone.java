
import java.awt.Color;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import java.awt.Graphics2D;

/**********************************************************************************************************
 * This Class takes an image and creates a halftone version of it in another file.
 * 
 * It takes 3 command line arguments the first is an image file that can be of several types,
 * the second is a string that will be used as a filename for the converted image to be called and the
 * third is a grid size that determines the dimensions of the area containing one halftone dot.
 * 
 * The arguments must be of a valid type and size for the program to produce the halftone image file.
 *
 *********************************************************************************************************/
public class DigitalHalftone {
	
	//
	//This method takes the original image from args[0] and resizes it so that the number of pixels in
	//the resized version is cleanly divisible by the grid size entered in args[2]. The resized image is
	//then passed to the toGreyScale method.
	//
	public static BufferedImage resize(BufferedImage before, int gridSize){
		
		int toWidth = (before.getWidth()) - (gridSize%before.getWidth());
		int toHeight = (before.getHeight()) - (gridSize%before.getHeight());
		
		BufferedImage resized = new BufferedImage(toWidth, toHeight, before.getType());
		Graphics2D temp = resized.createGraphics();
		temp.drawImage(before, 0, 0, toWidth, toHeight, null);
		temp.dispose();
	 
		return toGreyScale(resized, gridSize);
	}
	
	//
	//This method goes through each pixel in the resized image and gets the average luma of the pixel
	//by multiplying red, green and blue by the constants 0.30, 0.59 and 0.11 respectively. This gives the
	//best quality grey scale conversion for computer screens(source for this information documented below).
	//The pixel is then set to be the appropriate shade of grey by putting the same luma value into all 3 
	//colour categories (as this gives a shade of grey). The greyscale image is then passed to the 
	//toHalftone method.
	//
	private static BufferedImage toGreyScale(BufferedImage resized, int gridSize){
		
		for(int i = 0; i<resized.getWidth(); i++){
			for(int j = 0; j<resized.getHeight(); j++){
				Color pixel = new Color(resized.getRGB(i, j));
				
				//The numbers I used to multiply red, green and blue by were found here:
				//http://en.wikipedia.org/wiki/Grayscale#Luma_coding_in_video_systems
				int lum = (int)((0.30*pixel.getRed()) + (0.59*pixel.getGreen())
						+ (0.11*pixel.getBlue()));
			
				//When red, green and blue are all at the same level you get grey
				Color shadeOfGrey = new Color(lum, lum, lum);
				resized.setRGB(i, j, shadeOfGrey.getRGB());
			}
		}
		return toHalftone(resized, gridSize);
	}
	
	//
	//This method goes through each gridSize x gridSize set of pixels and finds the average red value for 
	//that area (because it is greyscale red, green and  blue values will be the same so only one of these
	//colours needs to be measured). It then calculates the mod value based on the average - this value is 
	//0 when black and gridSize/2 when white because it determines how far from x and y the square that 
	//will be bounded by the dot is originates.
	//
	//The diameter of the dot is then calculated using Pythgoras' theorem (the diameter is the 
	//hypotenuse of the triangle produced by bisecting the square diagonally). The dot is then drawn, once
	//all areas have been processed in this way the BufferedImage is a halftone version of the original 
	//input image. This halftone image is then returned to the main method through the toGreyScale and 
	//resize methods.
	//
	private static BufferedImage toHalftone(BufferedImage greyScale, int gridSize){										 
		
		int total = 0;
		int gridCol = 0;
		int gridRow = 0;
		int x = 0;
		int y = 0;
	
		BufferedImage halftone = new BufferedImage(greyScale.getWidth(), greyScale.getHeight(), greyScale.getType());
		Graphics2D g = halftone.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, halftone.getWidth(), halftone.getHeight());
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(Color.black);
		
		while(gridCol<= (greyScale.getWidth()/gridSize)){
			while( gridRow<= (greyScale.getHeight()/gridSize)){
				for(int i = (gridCol*gridSize); i<((gridCol*gridSize)+ gridSize); i++){
					for(int j = (gridRow*gridSize); j<((gridRow*gridSize)+gridSize); j++){
						if(i<greyScale.getWidth() && j<greyScale.getHeight()){
							Color pixel = new Color(greyScale.getRGB(i, j));
							total += pixel.getRed();
									
							if(i%gridSize == 0 && j%gridSize == 0){
								x=i;
								y=j;
							}
						}
					}
				}
					
				if(x%gridSize == 0 && y%gridSize == 0){
					double average = (total / (gridSize * gridSize));
					double mod = (((double)average/255.0)*((double)gridSize/2)); 
					double aSquared = Math.pow(((gridSize - (2*mod))/2),2);
					double dia = 2 * Math.pow((2*aSquared),0.5);
					
					if(average == 0){
						Rectangle2D rect = new Rectangle2D.Float(x, y, gridSize+1, gridSize+1);
						g2.fill(rect);
					}
					
					if(average<=254 && average != 0){
						Ellipse2D ellipse = new Ellipse2D.Double(x+mod, y+mod, dia, dia);
						g2.fill(ellipse);
					}
				}
				gridRow++;
				total = 0;
			}
			gridRow = 0;
			gridCol++;
		}
		g2.dispose();
		return halftone;
	}
	
	//
	//This method is called by main once the halftone buffered image has been created. It takes this 
	//and writes it to a PNG file that has the filename originally entered by the user into args[1].
	//If there is an error writing the file the try-catch will let the user know.
	//
	public static void write(BufferedImage toWrite, String filename) {
		
		try {
			ImageIO.write(toWrite, "png", new File(filename));
		} catch (Exception e) {
			System.out.println("An error occurred. File could not be produced.");
		}			
	}
	
	//
	//This method takes the args entered by the user and puts them into objects/variables of the correct
	//type. This action is surrounded by a try-catch statement so that if the args are and incorrect type
	//or size, or there are the wrong number of args (eg. args[2] is double instead of int, args[0]
	//is an MP3 file or args[0] is a 100000000x100000000 pixel image) the problem will be caught. 
	// 
	//Then if the grid size is > 1 the program produces and writes a halftone image of args[0] by calling 
	//the appropriate methods. If the grid size is any other value a message will be displayed instead 
	//because creating a halftone image would be impossible on such a grid size.
	//
	public static void main(String[] args){
		
		try{
			BufferedImage before = ImageIO.read(new File(args[0]));
			String filename = args[1];
			int gridSize = Integer.parseInt(args[2]);
			
			if(gridSize>1){
				BufferedImage toWrite = resize(before, gridSize);
				write(toWrite, filename);
			}else{
				System.out.println("Invalid gridsize value was entered");
			}
			
		}catch(Exception e){
			System.out.println("There were not enough args or args were of an invalid type/size");
		}
	}
}