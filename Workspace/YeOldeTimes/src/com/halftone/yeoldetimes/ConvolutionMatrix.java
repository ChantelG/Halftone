package com.halftone.yeoldetimes;

import android.graphics.Bitmap;
import android.graphics.Color;
 
/**
 * This class constructs and applies a convolution matrix to a bitmap. 
 * Thus, it allows a wide variety of different image modification techniques to be performed depending on the convolution matrix
 * provided to the class. In the current iteration of the Ye Olde Times app, only Gaussian Blur is being supported
 * 
 * @author Chantel Garcia & Carmen Pui
 */

public class ConvolutionMatrix
{
	/* Keep track of the convolution matrix, the size of the matrix (matrix.length), the factor to divide the sum of each pixel's red, 
	 * green and blue components by and the offset to add to this sum for each pixel's red, green and blue components
	 */
    private double[][] matrix;
    private int size;
    private double factor;
    private double offset;
 
    /**
     * Constructor for the convolution matrix
     * @param size - The size of the convolution matrix (e.g. if it is 5*5 then the size would be 5)
     * @param factor - The factor to divide the sum of each pixel's red, green and blue components by
     * @param offset - The offset to add to the sum of each pixel's red, green and blue components
     * @param convolutionMatrix - The convolution matrix to apply to a given bitmap image
     */
    public ConvolutionMatrix(int size, int factor, int offset, double[][] convolutionMatrix) {
        matrix = new double[size][size];
        this.size = size;
        this.factor = factor;
        this.offset = offset;
        
        for(int x = 0; x < size; ++x) {
            for(int y = 0; y < size; ++y) {
                matrix[x][y] = convolutionMatrix[x][y];
            }
        }
    }
 
    /**
     * This method takes a bitmap and breaks it up into sections of size*size pixels, for each pixel in the bitmap. 
     * It then obtains the red, green and blue values of each pixel in a given section and multiplies it by its corresponding value in the
     * convolution matrix (this.matrix) and adds each value to a corresponding running sum for red, green and blue pixels in the section.
     * It then obtains the final value for the red, green and blue pixel value for the given pixel that is being computed in the new, 
     * mutated bitmap. 
     * This is achieved by dividing the pixel red, green and blue values by the factor provided and adding the offset
     * to the final value. 
     * Then finally, the pixel in the mutated bitmap corresponding to this section of pixels is updated.
     * 
     * @param bitmap - The bitmap to apply the convolution matrix to
     * @return a bitmap that has had the convolution matrix applied to it such that every pixel has been transformed according to the
     * convolution matrix
     */
    public Bitmap applyConvolutionMatrixToBitmap(Bitmap bitmap) {
    	// Set up a bitmap that we will compute the changes into (this will be the modified bitmap after the convolution matrix is applied)
        Bitmap mutatedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
 
        // Set up variables to hold pixel colour values with convolution matrix applied
        int red, green, blue;
        int redSum, greenSum, blueSum;
        
        // Set up an array to hold the current matrix of pixels that we are applying our convolution matrix to 
        int[][] bitmapPixels = new int[size][size];
 
        for(int y = 0; y < bitmap.getHeight() - (size-1); y++) {
            for(int x = 0; x < bitmap.getWidth() - (size-1); x++) {
 
                // Construct the matrix for the current set of size * size cells
                for(int i = 0; i < size; ++i) {
                    for(int j = 0; j < size; ++j) {
                    	bitmapPixels[i][j] = bitmap.getPixel(x + i, y + j);
                    }
                }

                // Empty out sums of pixel coloru values for this iteration
                redSum = 0;
                greenSum = 0;
                blueSum = 0;
 
                // Obtain the running sum of all red pixels, green pixels and blue pixels
                for(int i = 0; i < size; ++i) {
                    for(int j = 0; j < size; ++j) {
                        redSum += (Color.red(bitmapPixels[i][j]) * matrix[i][j]);
                        greenSum += (Color.green(bitmapPixels[i][j]) * matrix[i][j]);
                        blueSum += (Color.blue(bitmapPixels[i][j]) * matrix[i][j]);
                    }
                }
 
                /* The red value of the current pixel will be the sum of all red pixels in the matrix region, divided by the factor given
                 * plus any offset applied for the red value
                 */
                red = (int)((redSum / factor) + offset);
                if(red < 0) 
                	red = 0; 
                else if(red > 255)
                	red = 255; 
 
                /* The green value of the current pixel will be the sum of all green pixels in the matrix region, divided by the factor given
                 * plus any offset applied for the green value
                 */
                green = (int)((greenSum / factor) + offset);
                if(green < 0)
                	green = 0; 
                else if(green > 255) 
                	green = 255;
 
                /* The blue value of the current pixel will be the sum of all blue pixels in the matrix region, divided by the factor given
                 * plus any offset applied for the blue value
                 */
                blue = (int)((blueSum / factor) + offset);
                if(blue < 0) 
                	blue = 0; 
                else if(blue > 255)
                	blue = 255;
 
                // Place the new modified pixelinto the mutated bitmap
                mutatedBitmap.setPixel(x + 1, y + 1, Color.rgb(red, green, blue));
            }
        }

        return mutatedBitmap;
    }
}