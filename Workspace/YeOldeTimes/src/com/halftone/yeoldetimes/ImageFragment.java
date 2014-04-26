package com.halftone.yeoldetimes;

import java.io.FileNotFoundException;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
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
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.image_fragment, container, false);
    }
	
	public void updateImage(Uri imageUriLoaded) {
		this.imageUri = imageUriLoaded;
		ImageView imageView = (ImageView) getView().findViewById(R.id.imageView);
		
		Bitmap bitmapLoaded;
		bitmapLoaded = decodeSampledBitmapFromUri( this.imageUri, IMAGE_VIEW_WIDTH_HEIGHT, IMAGE_VIEW_WIDTH_HEIGHT);
		this.bitmap = bitmapLoaded;
		
		imageView.setImageBitmap(this.bitmap);
	}
	
	 public Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth, int reqHeight) {

		Bitmap bm = null;
				  
		try{
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

}
