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
	private Bitmap originalImage;
	private Bitmap bitmap;
	private byte[] imageBytes;
	private String path;
	private File file;
	private ImageView imageView;
	
	private Bitmap captionedBitmap;
	private byte[] captionedImageBytes;
	
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
	
	public void setOriginalImage(){
		this.originalImage = getBitmap();
	}
	
	public Bitmap getOriginalImage(){
		return this.originalImage;
	}
	
	public void updateImage(Bitmap bitmap) {
		this.imageUri = null;
		this.bitmap = bitmap;

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
		// TODO : ONLY ONE LINE OF TEXT
		/*
		//Create a new image bitmap and attach a brand new canvas to it
		Bitmap tempBitmap = Bitmap.createBitmap(this.bitmap.getWidth(), this.bitmap.getHeight()+50, Bitmap.Config.ARGB_8888);
		Canvas tempCanvas = new Canvas(tempBitmap);
		
		Paint white = new Paint();
        white.setColor(Color.WHITE);
        white.setStyle(Paint.Style.FILL);
        
        drawSquare(tempCanvas, 0, 0, tempCanvas.getHeight(), tempCanvas.getWidth(), white);

		//Draw the image bitmap into the canvas
		tempCanvas.drawBitmap(this.bitmap, 0, 0, null);
		
		Paint black = new Paint();
    	black.setColor(Color.BLACK);
    	black.setStyle(Paint.Style.FILL);
		
		tempCanvas.drawText(caption, 0, this.bitmap.getHeight()+20, black);
		
		this.imageView.setImageBitmap(tempBitmap);
    	this.imageBytes = getBytesFromBitmap(tempBitmap);*/
	}
	
	public Bitmap getBitmap() {
		return this.bitmap.copy(Bitmap.Config.ARGB_8888, true);
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
    
    public void halftoneImage(Bitmap bitmap, PrimitiveType type) {
    	Halftone halftoner = new Halftone();
    	this.bitmap = halftoner.makeHalftone(bitmap, type);
    	
    	this.imageView.setImageBitmap(this.bitmap);
		this.imageBytes = getBytesFromBitmap(this.bitmap);
    }

}
