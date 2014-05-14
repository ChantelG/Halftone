package com.halftone.yeoldetimes;

import android.graphics.Color;
import android.graphics.Paint;

public class Caption {
	private static final int CAPTION_HEIGHT = 30;
	private static final int CAPTION_PADDING = 5;
	private Paint captionPaint;
	private String caption;
	
	/**
	 * Constructor for caption - sets up the caption paint variable
	 */
	public Caption(){
		// Initialise the caption paint
		captionPaint = new Paint();
		captionPaint.setColor(Color.BLACK);
		captionPaint.setTextSize(20);
		captionPaint.setTextScaleX(1.0f);
		
		caption = "";
	}
	
	/**
	 * Mutator for caption
	 * @param caption - The new caption to set the caption text to
	 */
	public void setCaption(String caption){
		this.caption = caption;
	}
	
	/**
	 * Accessor for caption
	 * @return caption - The caption's value
	 */
	public String getCaption(){
		return this.caption;
	}
	
	/**
	 * Accessor for paint object used to paint the caption
	 * @return captionPaint - the paint object used to pain the caption
	 */
	public Paint getCaptionPaint(){
		return this.captionPaint;
	}
	
	/**
	 * Accessor for caption height
	 * @return CAPTION_HEIGHT - static variable for height of caption
	 */
	public static int getCaptionHeight() {
		return CAPTION_HEIGHT;
	}
	
	/**
	 * Accessor for caption padding
	 * @return CAPTION_PADDING - static variable for padding of caption
	 */
	public static int getCaptionPadding() {
		return CAPTION_PADDING;
	} 
}
