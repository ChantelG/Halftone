package com.example.android.yeoldetimes.tests;

import java.io.InputStream;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.halftone.yeoldetimes.CreateNewspaperActivity;
import com.halftone.yeoldetimes.R;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class NewspaperFragmentTest extends ActivityInstrumentationTestCase2<CreateNewspaperActivity> {

	private CreateNewspaperActivity createNewspaperActivity;
	private InputStream image;
	private Bitmap bitmap;
	private Instrumentation instrumentation;
	
	public NewspaperFragmentTest() {
		super(CreateNewspaperActivity.class);  
    }

    /**
     * Set up the test
     * We create the activity and instrumentation, then we get an image from our assets folder to test our fragment with.
     * Our activity will launch the gallery screen first (because of an upload type of 1)
     * 
     * NOTE: This set up is the same as in create newspaper activity test & share fragment test
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        Intent createActivityIntent = new Intent();
		createActivityIntent.putExtra("uploadType", 1); 
		setActivityIntent(createActivityIntent);
        
        createNewspaperActivity = getActivity();
        instrumentation = getInstrumentation();
        setActivityInitialTouchMode(true);
        
        // Set up an image to put into the image view later on (we first want to test without an image)
        AssetManager assets = getInstrumentation().getContext().getAssets();
		image = null;
		try{
			// Try and open the image
			image = assets.open("TestImage.jpg");	
			bitmap = BitmapFactory.decodeStream(image);
		}
		catch(Exception e){
			e.printStackTrace();
		}
    }
    
    /**
     * Tests that the circle halftone button is the first selected (as the image is halftoned using circles/dots when we launch)
     * and that all other buttons are not checked
     */
    @LargeTest
    public void testCircleHalftoneInitiallySelected(){
    	loadUpImageAndGoToNewspaperActivity();
    	
    	RadioButton squareRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneSquareRadio);
    	RadioButton circleRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneDotRadio);
    	RadioButton diamondRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneDiamondRadio);
    	
    	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
    	assertTrue(circleRadio.isChecked()); // Circle radio is checked
    	assertFalse(squareRadio.isChecked()); // Square is not
    	assertFalse(diamondRadio.isChecked()); // Diamond is not
    }
    
    /**
     * Tests that the we can select square halftone button, the image is updated and all other buttons are not checked
     */
    @LargeTest
    public void testSquareHalftoneSelected(){
    	loadUpImageAndGoToNewspaperActivity();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	RadioButton squareRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneSquareRadio);
            	RadioButton circleRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneDotRadio);
            	RadioButton diamondRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneDiamondRadio);
            	squareRadio.performClick();
            	
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	assertTrue(squareRadio.isChecked()); // Square radio is checked
            	assertFalse(circleRadio.isChecked()); // Circle is not 
            	assertFalse(diamondRadio.isChecked()); // Diamond is not
            }
        });
        instrumentation.waitForIdleSync();
    }
    
    /**
     * Tests that the we can select diamond halftone button, the image is updated and all other buttons are not checked
     */
    @LargeTest
    public void testDiamondHalftoneSelected(){
    	loadUpImageAndGoToNewspaperActivity();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	RadioButton squareRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneSquareRadio);
            	RadioButton circleRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneDotRadio);
            	RadioButton diamondRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneDiamondRadio);
            	diamondRadio.performClick();
            	
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	assertTrue(diamondRadio.isChecked()); // Diamond radio is checked
            	assertFalse(circleRadio.isChecked()); // Circle is not
            	assertFalse(squareRadio.isChecked()); // Square is not
            }
        });
        instrumentation.waitForIdleSync();
    }
    
    /**
     * Tests that we can input a caption and click the update caption button
     */
    @LargeTest
    public void testInputCaption(){
    	loadUpImageAndGoToNewspaperActivity();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	EditText captionTxt = (EditText) createNewspaperActivity.findViewById(R.id.captionTxt);
            	Button updateCaptionBtn = (Button) createNewspaperActivity.findViewById(R.id.updateCaptionBtn);
            	
            	// Make sure the caption text object exists and update its text
            	assertNotNull(captionTxt);
            	captionTxt.setText("A test caption");
            	
            	// Make sure the caption text's contents is the same as what we set it to
            	assertEquals("A test caption", captionTxt.getText().toString());
            	
            	// Ensure that the update caption button exists, then perform a click on it
            	assertNotNull(updateCaptionBtn);
            	updateCaptionBtn.performClick();
            	
            	/* The text of the caption should be the same. A visual change should happen to the image view as well, but this is not 
            	 * verifiable using automated test cases (it is a visual change) 
            	 */
            	assertEquals("A test caption", captionTxt.getText().toString());
            }
        });
        instrumentation.waitForIdleSync();
    }
    
    /**
     * Tests that we can remove a caption (when it exists, and in this test, we ensure that it exists) by clicking the remove caption button
     */
    @LargeTest
    public void testRemoveCaption(){
    	loadUpImageAndGoToNewspaperActivity();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	EditText captionTxt = (EditText) createNewspaperActivity.findViewById(R.id.captionTxt);
            	Button updateCaptionBtn = (Button) createNewspaperActivity.findViewById(R.id.updateCaptionBtn);
            	Button removeCaptionBtn = (Button) createNewspaperActivity.findViewById(R.id.removeCaptionBtn);
            	
            	// Make sure the caption text object exists and update its text
            	assertNotNull(captionTxt);
            	captionTxt.setText("A test caption");
            	
            	// Make sure the caption text's contents is the same as what we set it to
            	assertEquals("A test caption", captionTxt.getText().toString());
            	
            	// Ensure that the update caption button exists, then perform a click on it
            	assertNotNull(updateCaptionBtn);
            	updateCaptionBtn.performClick();
            	
            	// The caption is now in the image, so we will remove it using the remove caption button
            	removeCaptionBtn.performClick();
            	
            	// Assert that the caption field is now empty (cleared out)
            	assertEquals("",captionTxt.getText().toString());
            }
        });
        instrumentation.waitForIdleSync();
    }
    
    /**
     * Tests that we recieve a dialog on clicking next (because we need to save the image before we progress to share)
     */
    @LargeTest
    public void testDialogAppearsOnNext(){
    	loadUpImageAndGoToNewspaperActivity();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	Button shareButton = (Button) createNewspaperActivity.findViewById(R.id.shareScreenBtn);
            	
            	// Ensure that the button is not null before we attempt to click it
            	assertNotNull(shareButton);
            	
            	shareButton.performClick();
            }
        });
        instrumentation.waitForIdleSync();
        
        // Assert that the dialog is now showing
    	assertTrue(createNewspaperActivity.getErrorDialog().isShowing());
    }
    
    /**
     * Utility method to open up the relevant screen that we want to test with a bitmap loaded in
     */
    public void loadUpImageAndGoToNewspaperActivity(){
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	// Set up all of the properties of the imageFragment so it can hold an image
            	createNewspaperActivity.getImageFragment().updateImage(bitmap);
            	createNewspaperActivity.getImageFragment().setOriginalImage();
            	createNewspaperActivity.setOldBitmaps(bitmap, 0);
            	
            	// Ensure that the properties of the imageView are set up (not null)
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView());
            	assertNotNull(createNewspaperActivity.getImageFragment().getBitmap());
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageBytes());
            
            	// Open the createNewspaperActivity's newspaper fragment
            	createNewspaperActivity.openNewspaperCreator();
            }
        });
        instrumentation.waitForIdleSync();
    }
}
