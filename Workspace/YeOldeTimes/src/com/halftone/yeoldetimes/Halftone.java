package com.halftone.yeoldetimes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

public class Halftone {
	
    //Draws a white square which will be filled with a black circle in the drawCircle method. This is to 
    //show that the kind of image manipulation done in halftoning is possible on Android
    /*public void drawSquare(ImageView imageView, Bitmap bitmap){
    	Bitmap myBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		
		Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        
        float radius = 0.0f;
        
        if(imageView.getWidth() > imageView.getHeight())
        	radius = imageView.getWidth() / 6;
        else
        	radius = imageView.getHeight() / 6;
        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas tempCanvas = new Canvas(tempBitmap);
        
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);
        
        tempCanvas.drawRect(0, 0, 2*radius, 2*radius, paint);
        
        imageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    }
    //Draws a black circle to filled the white square drawn in the drawSquare method. This is to 
    //show that the kind of image manipulation done in halftoning is possible on Android
    public void drawCircle(ImageView imageView)
    {
		Bitmap myBitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
		
		Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        
        float radius = 0.0f;
        
        if(imageView.getWidth() < imageView.getHeight())
        	radius = imageView.getWidth() / 6;
        else
        	radius = imageView.getHeight() / 6;

		float xOrigin = 0+radius;
		float yOrigin = 0+radius;
		
		//Create a new image bitmap and attach a brand new canvas to it
		Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
		Canvas tempCanvas = new Canvas(tempBitmap);

		//Draw the image bitmap into the cavas
		tempCanvas.drawBitmap(myBitmap, 0, 0, null);

		//Draw everything else you want into the canvas, in this example a rectangle with rounded edges
		tempCanvas.drawCircle(xOrigin, yOrigin, radius, paint);

		//Attach the canvas to the ImageView
		imageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    }*/
}
