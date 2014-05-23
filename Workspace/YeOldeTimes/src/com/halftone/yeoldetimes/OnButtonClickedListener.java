package com.halftone.yeoldetimes;

/**
 * This interface provides the definition of a single method which takes a button ID and in its implementation will perform
 * functionality based on the button ID provided
 * 
 * @author Chantel Garcia & Carmen Pui
 */

public interface OnButtonClickedListener {

	/**
	 * This method will determine what to perform as a result of the button with buttonId passed in being clicked
	 * 
	 * @param buttonId - the id of the button pressed
	 */
	public void onButtonClicked(int buttonId);
}
