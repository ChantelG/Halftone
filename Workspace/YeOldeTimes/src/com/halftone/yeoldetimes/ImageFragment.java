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
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * This class provides all of the functionality for the Image Fragment.
 * The Image Fragment is persistent across all screens in the application except for the home screen and consists of a single image view
 * and is displayed at the top half of the screen.
 * The Image Fragment allows a bitmap image stored in the image view of the fragment to be halftoned, have a difference filter applied to 
 * it, have a gaussian blur applied to it and captioned.
 * 
 * @author Chantel Garcia & Carmen Pui
 */

public class ImageFragment extends Fragment {
	
	private final int IMAGE_VIEW_WIDTH_HEIGHT = 300;
	
	// Variables pertaining to storing copies of the image for transformation, display and restore
	private Uri imageUri;
	private Bitmap originalImage;
	private Bitmap imageBitmap;
	private Bitmap bitmap;
	private byte[] imageBytes;
	private String path;
	private File file;
	private Halftone halftoner;
	private GaussianBlur gaussianBlurrer;
	
	// Keep track of whether the image is captioned or not
	private boolean isCaptioned;
	
	// The fundamental components of the image view
	private ImageView imageView;
	private ErrorDialog errorDialog;
	private Caption caption;
	
	// Angle to rotate the grid by
	private int rotationAngle;

	/**
	 * Create the image fragment (with buttons)
	 * 
	 * @return imageFragmentView - the view of the layout
	 */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the view, set up the imageView and return the inflated view
		View imageFragmentView = inflater.inflate(R.layout.image_fragment, container, false);
		this.imageView = (ImageView) imageFragmentView.findViewById(R.id.imageView);
		isCaptioned = false;
		caption = new Caption();
		rotationAngle = 0;
		halftoner = new Halftone();
		gaussianBlurrer = new GaussianBlur();
		
        return imageFragmentView;
    }
	
	/**
	 * updateImage is a method to load the bitmap into the image view and store the new bitmap so that it can be referenced later
	 * It also sets up a reference to the uri of the image
	 * 
	 * @param imageUriLoaded - the image to be loaded into the image view
	 */
	public void updateImage(Uri imageUriLoaded) {
		this.imageUri = imageUriLoaded;
		
		Bitmap bitmapLoaded;
		bitmapLoaded = decodeSampledBitmapFromUri( this.imageUri, IMAGE_VIEW_WIDTH_HEIGHT, IMAGE_VIEW_WIDTH_HEIGHT);
		this.bitmap = bitmapLoaded;
		
		this.imageView.setImageBitmap(this.bitmap);
		this.imageBytes = getBytesFromBitmap(this.bitmap);	
	}
	
	/**
	 * setOriginalImage is a method to set the original image bitmap (not halftoned)
	 */
	public void setOriginalImage(){
		this.originalImage = getBitmap();
	}
	
	/**
	 * getOriginalImage is a method to get the original bitmap of the image (not halftoned)
	 * 
	 * @return originalImage - the bitmap set earlier in the setOriginalImage method
	 */
	public Bitmap getOriginalImage(){
		return this.originalImage;
	}
	
	/**
	 * This method returns the image bitmap modified (without captions!)
	 * @return - The image bitmap modified without captions
	 */
	public Bitmap getImageBitmap() {
		return this.imageBitmap;
	}
	
	/**
	 * setRotationAngle sets the rotation angle of the grid for halftoning
	 * 
	 * @param angle - the angle to update the halftoning grid to
	 */
	public void setRotationAngle(int angle){
		this.rotationAngle = angle;
	}
	
	/**
	 * getRotationAngle returns the rotation angle of the grid for halftoning in the image
	 * 
	 * @return the rotation angle of the grid
	 */
	public int getRotationAngle(){
		return this.rotationAngle;
	}
	
	/**
	 * updateImage also updates the image in the image view, but without setting a URI
	 * 
	 * @param bitmap - image to be loaded
	 */
	public void updateImage(Bitmap bitmap) {
		this.imageUri = null;
		this.bitmap = bitmap;

		this.imageView.setImageBitmap(this.bitmap);
		this.imageBytes = getBytesFromBitmap(this.bitmap);
	}
	
	/**
	 * Obtain the image view
	 */
	public ImageView getImageView(){
		return this.imageView;
	}
	
	/**
	 * Save the image as a file or output an error dialog if there is an input output error
	 */
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
			errorDialog = new ErrorDialog(this.getActivity(), R.string.unexpected_error_title, R.string.unexpected_error_msg, ErrorDialogType.GENERAL_ERROR);
    		errorDialog.show();
		}
	}
	
	/**
	 * updateImageCaption updates the caption under the image with the caption passed in
	 * 
	 * @param caption - string to put under the image
	 */
	public void updateImageCaption(String caption) {
		this.caption.setCaption(caption);
		
		// Create a new image bitmap and attach a brand new canvas to it
		Bitmap tempBitmap = Bitmap.createBitmap(this.imageBitmap.getWidth(), this.imageBitmap.getHeight()+Caption.getCaptionHeight(), Bitmap.Config.RGB_565);
		Canvas tempCanvas = new Canvas(tempBitmap);
		
		// Draw a white rectangle over the slightly enlarged canvas (to allow space for a caption)
		Paint white = new Paint();
        white.setColor(Color.WHITE);
        tempCanvas.drawRect(0, 0, tempCanvas.getWidth(), tempCanvas.getHeight(), white);

		// Draw the image bitmap into the canvas
		tempCanvas.drawBitmap(this.imageBitmap, 0, 0, null);

		// Draw the caption onto the canvas
		tempCanvas.drawText(this.caption.getCaption(), Caption.getCaptionPadding(), this.imageBitmap.getHeight()+(Caption.getCaptionPadding()*4), this.caption.getCaptionPaint());
		
		// Update local variables corresponding to image
		this.isCaptioned = true;
		updateStoredImages(tempBitmap);
	}
	
	/**
	 * Determine whether the caption is of a length that does not exceed the width of the image  
	 * 
	 * @param caption - The caption to draw
	 * @return true if the caption fit within the width of the image, false otherwise
	 */
	public boolean getCaptionWithinBounds(String caption) { 
    	int size = (int) this.caption.getCaptionPaint().measureText(caption);
    	int bitmapWidth = this.bitmap.getWidth()-(Caption.getCaptionPadding()*2);
    	
    	return (size > bitmapWidth) ? false : true;
	}
	
	/**
	 * removeCaption removes the caption under the image by creating a new canvas attach to it
	 */
	public void removeCaption(){
		//Create a new image bitmap and attach a brand new canvas to it
		Bitmap tempBitmap = Bitmap.createBitmap(this.bitmap.getWidth(), this.bitmap.getHeight()-Caption.getCaptionHeight(), Bitmap.Config.RGB_565);
		Canvas tempCanvas = new Canvas(tempBitmap);

		isCaptioned = false;
		
		tempCanvas.drawBitmap(this.bitmap, 0, 0, null);
		
		updateStoredImages(tempBitmap);
	}

	/**
	 * Get the bitmap of the image and return it
	 * 
	 * @return bitmap - copy of the image bitmap
	 */
	public Bitmap getBitmap() {
		return this.bitmap.copy(Bitmap.Config.RGB_565, true);
	}
	
	/**
	 * Mutator for file
	 * 
	 * @param file - the file to update the local file with
	 */
	public void setFile(File file) {
		this.file = file;
	}
	
	/**
	 * Accessor to get the file 
	 * 
	 * @return file - the file stored in the fragment
	 */
	public File getFile() {
		return this.file;
	}
	
	/**
	 * Mutator to set path for the image to be saved
	 * @param path - the new file path of the image
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * Accessor to get the path of the image 
	 * @return path - the file path of the image 
	 */
	public String getPath() {
		return this.path;
	}
	
	/**
	 * Mutator for the Uri of the image 
	 * @param uri - the image uri
	 */
	public void setUri(Uri uri){
		this.imageUri = uri;
	}
	
	/**
	 * Accessor for the Uri of the image image
	 * @return imageUri - the image's image uri
	 */
	public Uri getUri() {
		return this.imageUri;
	}
	
	/**
	 * Accessor to the imageBytes array (stores image data)
	 * @return this.imageBytes - imageBytes that is stored in array
	 */
	public byte[] getImageBytes() {
		return this.imageBytes;
	}
	
	/**
	 * Store the bitmap image into the byte array
	 * 
	 * @param bitmap - the bitmap of the image
	 * @return the bitmap stored as the byte array
	 */
	public byte[] getBytesFromBitmap(Bitmap bitmap) {
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    bitmap.compress(CompressFormat.JPEG, 100, stream);
	    return stream.toByteArray();
	}
	
	/**
	 * decodeSampledBitmapFromUri efficiently decode the bitmap from the given uri containing the image
	 * 
	 * @param uri - the image uri
	 * @param reqWidth - the width of the image
	 * @param reqHeight - the height of the image
	 * @return bitmap - the efficiently decoded bitmap
	 */
	 public Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth, int reqHeight) {
		Bitmap bitmap = null;
				  
		try {
			// First decode with inJustDecodeBounds = true to verify dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri), null, options);
			
			// Calculate the sample size
			options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
			
			// Decode bitmap with the sample size set
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri), null, options);
		} catch (FileNotFoundException e) {
			errorDialog = new ErrorDialog(this.getActivity(), R.string.file_load_error_title, R.string.file_load_error_msg, ErrorDialogType.GENERAL_ERROR);
    		errorDialog.show();
		}
		  
		return bitmap;
	 }
	 
	 /**
		 * Calculate the required height and width of the sample (for efficient loading)
		 * @param options - the bitmap options
		 * @param reqWidth - the required width of the image
		 * @param reqHeight - the required height of the image
		 * 
		 * @return inSampleSize - the sampled size
		 */
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
    
    /**
     * Accessor for whether the image is captioned or not
     * @return true if the image is captioned, false otherwise
     */
    public boolean getCaptioned() {
    	return this.isCaptioned;
    }
    
    /** Convert the image passed in (bitmap) into a negative image. We convert to grayscale first, then we run a matrix filter 
	 * over the top of the image to invert it (make it negative)
	 * @param bitmap - The bitmap to make negative
	 */
    public void makeNegative(Bitmap bitmap) {
		float[] negativeColourMatrix = { 
			-1.0f, 0, 0, 0, 255, // Red
			0, -1.0f, 0, 0, 255, // Green
			0, 0, -1.0f, 0, 255, // Blue
			0, 0, 0, 1.0f, 0 // Alpha  
		};

    	// Set up the colour filter object for the matrix to invert the image
		Paint negativePaint = new Paint();
		ColorFilter negativeColourFilter = new ColorMatrixColorFilter(negativeColourMatrix);
		negativePaint.setColorFilter(negativeColourFilter);
    	  
		// Create a bitmap to work with (without modifying the original bitmap)
		Bitmap originalBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(originalBitmap);
		
		/* Convert the bitmap to grayscale and store it in another bitmap, then draw the grayscaled bitmap to a negative image by
		 * applying the filter
		 */
		Bitmap newBitmap = halftoner.convertToGrayscale(bitmap);
		canvas.drawBitmap(newBitmap, 0, 0, negativePaint);
		newBitmap.recycle();
		
		// Update the image view and image variables
		updateStoredImages(originalBitmap);
		this.imageBitmap = originalBitmap;
    }
    
    /**
     * Perform the gaussian blur to the given strength on the given image passed into the method
     * @param bitmap - The bitmap to gaussian blur
     * @param gaussianBlurStrength - The strength of the gaussian blur
     */
    public void gaussianBlur(Bitmap bitmap, GaussianBlurStrength gaussianBlurStrength) {
    	// Gaussian blur the image
    	gaussianBlurrer.setGaussianBlurStrength(gaussianBlurStrength);
    	Bitmap newBitmap = gaussianBlurrer.blur(bitmap);
	    
    	// Update the image view and image variables
	    updateStoredImages(newBitmap);
		this.imageBitmap = newBitmap;
    }
    
    /**
     * This method is a utility method which updates the images stored by the image fragment for restoration on navigating back
     * and forward in the application and for updating the image view
     * @param bitmap
     */
    public void updateStoredImages(Bitmap bitmap){
    	this.imageView.setImageBitmap(bitmap);
	    this.imageBytes = getBytesFromBitmap(bitmap);
		this.bitmap = bitmap;
    }
    
    /**
     * Method to create a halftone image 
     * 
     * @param bitmap - the image bitmap
     * @param type - the shape of the primitive to halftone with
     */
    public void halftoneImage(Bitmap bitmap, PrimitiveType type) {
    	Bitmap modifiableBitmap;
    	
    	// Keep track of the height and width of the image divided by 2 for resizing purposes later
    	int heightDiv2 = (int)(originalImage.getHeight()/2);
    	int widthDiv2 = (int)(originalImage.getWidth()/2);
    	
    	/* Keep track of the constant amount that we need to crop by in the situation that we rotate the image (due to the primitives
    	 * drawing into sections of the image that are outside the bounds of the image and overlapping
    	 */
    	final int TOP_LEFT_CROP = 4;
    	final int BOTTOM_RIGHT_CROP = 8;
    	
    	modifiableBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
    	
    	// Only rotate if the rotation angle was not equal to 0 or 90
    	if((rotationAngle != 0 && rotationAngle != 90 && rotationAngle != 180) || (rotationAngle == 90 && type == PrimitiveType.RECTANGLE)){
	    	// Use pythagoras to find the largest possible width/ height of the image (this will be the diagonal of the image) 
	    	double diagonalVal = (originalImage.getWidth()*originalImage.getWidth()) + (originalImage.getHeight()*originalImage.getHeight());
	    	diagonalVal = Math.sqrt(diagonalVal);
	    	
	    	// Rotate the image to draw the grid on the given angle
	    	Matrix matrix = new Matrix();
			matrix.postRotate(-rotationAngle);
			
			//Create a new image bitmap and attach a brand new canvas to it
			Bitmap originalBmpEnlarged = Bitmap.createBitmap((int)diagonalVal, (int)diagonalVal, Bitmap.Config.RGB_565);
			Canvas largeCanvas = new Canvas(originalBmpEnlarged);
			//Draw the image bitmap into the canvas
			largeCanvas.drawBitmap(originalImage, (originalBmpEnlarged.getWidth()/2)-widthDiv2, (originalBmpEnlarged.getHeight()/2)-heightDiv2, null);
	
			modifiableBitmap = Bitmap.createBitmap(originalBmpEnlarged, 0, 0, originalBmpEnlarged.getWidth(), originalBmpEnlarged.getHeight(), matrix, true);
			
			// Reycle the enlarged bitmap
			originalBmpEnlarged.recycle();
			originalBmpEnlarged = null;
    	}

    	modifiableBitmap = halftoner.makeHalftone(modifiableBitmap, type);
    	
    	if(this.imageBitmap!= null)
    	this.imageBitmap.recycle();
    	this.bitmap.recycle();

    	// Only rotate back if rotationAngle was not 0 or 90, AND the primitive type used was not  otherwise no rotation is required
    	if((rotationAngle != 0 && rotationAngle != 90 && rotationAngle != 180) || (rotationAngle == 90 && type == PrimitiveType.RECTANGLE)) {
			// Rotate the image back for display (so that the image is upright)
	    	Matrix rotateBackMatrix = new Matrix();
	    	rotateBackMatrix.postRotate(rotationAngle);
	
			Bitmap rotatedBackBm = Bitmap.createBitmap(modifiableBitmap, 0, 0, modifiableBitmap.getWidth(), modifiableBitmap.getHeight(), rotateBackMatrix, true);
	
			modifiableBitmap.recycle();
			modifiableBitmap = null;
			
			double centerX = rotatedBackBm.getWidth()/2;
			double centerY = rotatedBackBm.getHeight()/2;

			// We must crop the rotated image because the 
			Bitmap croppedBm = Bitmap.createBitmap(rotatedBackBm, (int)(centerX-widthDiv2)+TOP_LEFT_CROP, (int)(centerY-heightDiv2)+TOP_LEFT_CROP, originalImage.getWidth()-BOTTOM_RIGHT_CROP, originalImage.getHeight()-BOTTOM_RIGHT_CROP);
			
			rotatedBackBm.recycle();
			rotatedBackBm = null;
			
			this.imageBitmap = croppedBm;
    	}
    	else
    		this.imageBitmap = modifiableBitmap;
    	
    	updateStoredImages(this.imageBitmap);
    }
}
