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
	
	// Variables pertaining to storing copies of the image for transformation, display and restore
	private Uri imageUri;
	private Bitmap originalImage;
	private Bitmap halftonedBitmap;
	private Bitmap bitmap;
	private byte[] imageBytes;
	private String path;
	private File file;
	
	// Keep track of whether the image is captioned or not
	private boolean isCaptioned;
	
	// The fundamental components of the image view
	private ImageView imageView;
	private ErrorDialog errorDialog;
	private Caption caption;

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
		Bitmap tempBitmap = Bitmap.createBitmap(halftonedBitmap.getWidth(), halftonedBitmap.getHeight()+Caption.getCaptionHeight(), Bitmap.Config.ARGB_8888);
		Canvas tempCanvas = new Canvas(tempBitmap);
		
		// Draw a white rectangle over the slightly enlarged canvas (to allow space for a caption)
		Paint white = new Paint();
        white.setColor(Color.WHITE);
        tempCanvas.drawRect(0, 0, tempCanvas.getWidth(), tempCanvas.getHeight(), white);

		// Draw the image bitmap into the canvas
		tempCanvas.drawBitmap(halftonedBitmap, 0, 0, null);

		// Draw the caption onto the canvas
		tempCanvas.drawText(this.caption.getCaption(), Caption.getCaptionPadding(), halftonedBitmap.getHeight()+(Caption.getCaptionPadding()*4), this.caption.getCaptionPaint());
		
		// Update local variables corresponding to image
		isCaptioned = true;
		this.imageView.setImageBitmap(tempBitmap);
    	this.imageBytes = getBytesFromBitmap(tempBitmap);
    	this.bitmap = tempBitmap;
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
		Bitmap tempBitmap = Bitmap.createBitmap(this.bitmap.getWidth(), this.bitmap.getHeight()-Caption.getCaptionHeight(), Bitmap.Config.ARGB_8888);
		Canvas tempCanvas = new Canvas(tempBitmap);

		isCaptioned = false;
		
		tempCanvas.drawBitmap(this.bitmap, 0, 0, null);
		
		this.imageView.setImageBitmap(tempBitmap);
    	this.imageBytes = getBytesFromBitmap(tempBitmap);
    	this.bitmap = tempBitmap;
	}

	/**
	 * Get the bitmap of the image and return it
	 * 
	 * @return bitmap - copy of the image bitmap
	 */
	public Bitmap getBitmap() {
		return this.bitmap.copy(Bitmap.Config.ARGB_8888, true);
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
    public boolean getCaptioned(){
    	return this.isCaptioned;
    }
    
    /**
     * Method to create a halftone image 
     * 
     * @param bitmap - the image bitmap
     * @param type - the shape of the primitive to halftone with
     */
    public void halftoneImage(Bitmap bitmap, PrimitiveType type) {
    	Halftone halftoner = new Halftone();
    	halftonedBitmap = halftoner.makeHalftone(bitmap, type);
    	
    	//Create a new image bitmap and attach a brand new canvas to it
		Bitmap tempBitmap = Bitmap.createBitmap(this.bitmap.getWidth()-(bitmap.getWidth()%Halftone.gridSize), this.bitmap.getHeight()-(bitmap.getHeight()%Halftone.gridSize), Bitmap.Config.ARGB_8888);
		Canvas tempCanvas = new Canvas(tempBitmap);

		//Draw the image bitmap into the canvas
		tempCanvas.drawBitmap(this.bitmap, 0, 0, null);
		tempCanvas.drawBitmap(halftonedBitmap, 0, 0, null);
    	
    	this.imageView.setImageBitmap(tempBitmap);
		this.imageBytes = getBytesFromBitmap(tempBitmap);
		this.bitmap = tempBitmap;
    }
}
