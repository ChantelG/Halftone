package com.halftone.yeoldetimes;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends Fragment {
	
	private final int IMAGE_VIEW_WIDTH_HEIGHT = 300;
	private Uri imageUri;
	private Bitmap bitmap;
	private byte[] imageBytes;
	private String path;
	private File file;
	private ImageView imageView;
	
	private Bitmap captionedBitmap;
	private byte[] captionedImageBytes;
	
	// TODO : Fix draw square
	// TODO : ASK - need to compress bitmap from camera too? 
	// TODO : Fix zygote error
	// TODO : Draw text
	// TODO : refactor
	// TODO : Share on save button
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the view, set up the imageView and return the inflated view
		View imageFragmentView = inflater.inflate(R.layout.image_fragment, container, false);
		this.imageView = (ImageView) imageFragmentView.findViewById(R.id.imageView);
        return imageFragmentView;
    }
	
	public void updateImage(Uri imageUriLoaded) {
		this.imageUri = imageUriLoaded;
		
		Bitmap bitmapLoaded;
		bitmapLoaded = decodeSampledBitmapFromUri( this.imageUri, IMAGE_VIEW_WIDTH_HEIGHT, IMAGE_VIEW_WIDTH_HEIGHT);
		this.bitmap = bitmapLoaded;
		
		this.imageView.setImageBitmap(this.bitmap);
		this.imageBytes = getBytesFromBitmap(this.bitmap);	
	}
	
	public void updateImage(Bitmap bitmap) {
		this.imageUri = null;
		this.bitmap = bitmap;

		this.imageView.setImageBitmap(this.bitmap);
		this.imageBytes = getBytesFromBitmap(this.bitmap);
	}
	
	public void halftoneImage(){
		halftone();
		this.imageView.setImageBitmap(this.bitmap);
		this.imageBytes = getBytesFromBitmap(this.bitmap);
	}
	
	public void updateFile() {
		try {
        FileOutputStream fileOutputStream = new FileOutputStream(this.file);

        // Create an output stream with the file output stream containing the file to save and compress it
        BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOutputStream);
        bufferedOut.write(this.imageBytes);

        // Flush the buffered output and close it
        bufferedOut.flush();
        bufferedOut.close();
		} catch(IOException io) {
			// TODO throw
		}
	}
	
	public void updateImageCaption(String caption) {
		//Create a new image bitmap and attach a brand new canvas to it
		Bitmap tempBitmap = Bitmap.createBitmap(this.bitmap.getWidth(), this.bitmap.getHeight()+50, Bitmap.Config.ARGB_8888);
		Canvas tempCanvas = new Canvas(tempBitmap);

		//Draw the image bitmap into the canvas
		tempCanvas.drawBitmap(this.bitmap, 0, 0, null);
		
		Paint black = new Paint();
    	black.setColor(Color.BLACK);
    	black.setStyle(Paint.Style.FILL);
		
		tempCanvas.drawText(caption, 0, this.bitmap.getHeight()+20, black);
		
		this.imageView.setImageBitmap(tempBitmap);
    	this.imageBytes = getBytesFromBitmap(tempBitmap);
	}
	
	public void setFile(File file) {
		this.file = file;
	}
	
	public File getFile() {
		return this.file;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public void setUri(Uri uri){
		this.imageUri = uri;
	}
	
	public Uri getUri() {
		return this.imageUri;
	}
	
	public byte[] getImageBytes() {
		return this.imageBytes;
	}
	
	public byte[] getBytesFromBitmap(Bitmap bitmap) {
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    bitmap.compress(CompressFormat.JPEG, 100, stream);
	    return stream.toByteArray();
	}
	
	 public Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth, int reqHeight) {

		Bitmap bm = null;
				  
		try {
			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri), null, options);
			
			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
			
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			bm = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri), null, options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		  
		return bm;
	 }
	 
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        /* Calculate the largest inSampleSize value that is a power of 2 and keeps both height and width larger 
	         * than the requested height and width.
	         */
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }
	    return inSampleSize;
	}
    
    public void halftone() {
    	// Create the grayScale bitmap, canvas and paint object
		Bitmap grayScale = Bitmap.createBitmap(this.bitmap.getWidth(), this.bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(grayScale);
		Paint paint = new Paint();
		ColorMatrix colorMatrix = new ColorMatrix();
		        
		// Desaturate the bitmap and apply the filter, then draw it to the canvas
        colorMatrix.setSaturation(0);
        final ColorMatrixColorFilter f = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(f);
        canvas.drawBitmap(this.bitmap, 0, 0, paint);
        
        // Recycle new bitmap
        this.bitmap.recycle();
       
        // Update the new bitmap to the grayscale bitmap and halftone it
        this.bitmap = grayScale;
        this.bitmap = halftoneImage(this.bitmap);
    	this.imageView.setImageBitmap(this.bitmap);
    	this.imageBytes = getBytesFromBitmap(this.bitmap);
    }
    
    public Bitmap halftoneImage(Bitmap oldBitmap) {
    	int gridSize = 5;
    	int MAX_CIRCLE_DIAMETER = 5;
    	
    	Paint black = new Paint();
    	black.setColor(Color.BLACK);
    	black.setStyle(Paint.Style.FILL);
        
        Paint white = new Paint();
        white.setColor(Color.WHITE);
        white.setStyle(Paint.Style.FILL);
    	
		//Create a new image bitmap and attach a brand new canvas to it
		Bitmap tempBitmap = Bitmap.createBitmap(this.bitmap.getWidth()-(gridSize%this.bitmap.getWidth()), this.bitmap.getHeight()-(gridSize%this.bitmap.getHeight()), Bitmap.Config.ARGB_8888);
		Canvas tempCanvas = new Canvas(tempBitmap);

		//Draw the image bitmap into the canvas
		tempCanvas.drawBitmap(this.bitmap, 0, 0, null);
		
		Bitmap theTempBitmap = tempBitmap.copy(Bitmap.Config.ARGB_8888, true);
		
		drawSquare(tempCanvas, 0, 0, tempCanvas.getHeight(), tempCanvas.getWidth(), white);

    	for(int i=0; i < tempBitmap.getHeight(); i++) {
		      for (int j=0; j < tempBitmap.getWidth(); j++) {
		        if(i%(gridSize) == 0 && j%(gridSize) == 0) {
		        	// Calculate the average colour of portion of the image starting at i,j and extending out to gridSize in height and width
		        	double greyAvg = calculateAverage(theTempBitmap, i, j, gridSize);
		        	
		        	//if(greyAvg != 0 && greyAvg != 255){
			        	// Determine the diameter of the circle based on the average grey colour and draw the circle
		        		double circleDiameter = ImageUtils.calculateCircleRadius(greyAvg, MAX_CIRCLE_DIAMETER, gridSize);
		        		drawCircle(tempCanvas, circleDiameter, j, i, black); 
		        	/*}
		        	else {
		        		if(greyAvg == 255){
		        			drawSquare(tempCanvas, j, i, gridSize, gridSize, black);
		        		}
		        	}*/
		        }
		      }
		}
    	
		//Attach the canvas to the ImageView
		return tempBitmap;
    }
    
	public double calculateAverage(Bitmap theBitmap, int yCoord, int xCoord, int gridSize) {
			double runningSum = 0;
			double amountInSquare = 0;
			
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
			double average = runningSum/amountInSquare;

			return average;
	}
    
    public void drawSquare(Canvas tempCanvas, int x, int y, int height, int width, Paint paint) {
        tempCanvas.drawRect(x, y, 1000, 1000, paint);
    }

    public void drawCircle(Canvas tempCanvas, double radius, double x, double y, Paint paint) {
		float xOrigin = (float)(x+radius);
		float yOrigin = (float)(y+radius);
		tempCanvas.drawCircle(xOrigin, yOrigin, (float)radius, paint);
    }
}
