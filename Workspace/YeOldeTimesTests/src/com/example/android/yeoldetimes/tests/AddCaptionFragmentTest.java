package com.example.android.yeoldetimes.tests;

import java.io.InputStream;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.Button;
import android.widget.EditText;

import com.halftone.yeoldetimes.CreateNewspaperActivity;
import com.halftone.yeoldetimes.R;

/**
 * The AddCaptionFragmentTest Suite tests that the Add Caption screen can be launched when the Next button is clicked in the Create Newspaper screen.
 * Then from the Add Caption screen, a test is conducted to ensure that the caption can be updated and attached to the the image in the image view 
 * as well as removing the caption from the image when the Remove Caption button is clicked.
 * This test will also test that an Error Dialog appears if no caption is available and the Update Caption button was clicked or
 * if there is no caption is attached on the image when the Remove Caption button was clicked.
 * Finally, it is tested that when Next button is clicked, there will be an Alert Dialog to ask user to save the image. 
 * 
 * @author Chantel Garcia & Carmen Pui
 */

public class AddCaptionFragmentTest extends ActivityInstrumentationTestCase2<CreateNewspaperActivity>{
	
	private CreateNewspaperActivity createNewspaperActivity;
	private InputStream image;
	private Bitmap bitmap;
	private Instrumentation instrumentation;
	
	public AddCaptionFragmentTest() {
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
     * Tests that we can input a caption and click the update caption button
     */
    @LargeTest
    public void testInputCaption(){
    	loadUpImageAndGoToNewspaperActivity();
    	openAddCaptionsScreen();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	EditText captionTxt = (EditText) createNewspaperActivity.findViewById(R.id.captionTxt);
            	Button updateCaptionButton = (Button) createNewspaperActivity.findViewById(R.id.updateCaptionBtn);
            	
            	// Make sure the caption text object exists and update its text
            	assertNotNull(captionTxt);
            	captionTxt.setText("A test caption");
            	
            	// Make sure the caption text's contents is the same as what we set it to
            	assertEquals("A test caption", captionTxt.getText().toString());
            	
            	// Ensure that the update caption button exists, then perform a click on it
            	assertNotNull(updateCaptionButton);
            	updateCaptionButton.performClick();
            	
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
    	openAddCaptionsScreen();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	EditText captionTxt = (EditText) createNewspaperActivity.findViewById(R.id.captionTxt);
            	Button updateCaptionButton = (Button) createNewspaperActivity.findViewById(R.id.updateCaptionBtn);
            	Button removeCaptionButton = (Button) createNewspaperActivity.findViewById(R.id.removeCaptionBtn);
            	
            	// Make sure the caption text object exists and update its text
            	assertNotNull(captionTxt);
            	captionTxt.setText("A test caption");
            	
            	// Make sure the caption text's contents is the same as what we set it to
            	assertEquals("A test caption", captionTxt.getText().toString());
            	
            	// Ensure that the remove caption button exists, then perform a click on it
            	assertNotNull(updateCaptionButton);
            	updateCaptionButton.performClick();
            	
            	// The caption is now in the image, so we will remove it using the remove caption button
            	removeCaptionButton.performClick();
            	
            	// Assert that the caption field is now empty (cleared out)
            	assertEquals("",captionTxt.getText().toString());
            }
        });
        instrumentation.waitForIdleSync();
    }
    
	 /**
     * Tests that if a caption input is too long, an error dialog appears when the user clicks update caption
     */
    @LargeTest
    public void testInputCaptionTooLong(){
    	loadUpImageAndGoToNewspaperActivity();
    	openAddCaptionsScreen();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	EditText captionTxt = (EditText) createNewspaperActivity.findViewById(R.id.captionTxt);
            	Button updateCaptionButton = (Button) createNewspaperActivity.findViewById(R.id.updateCaptionBtn);
            	
            	// Make sure the caption text object exists and update its text
            	assertNotNull(captionTxt);
            	captionTxt.setText("A very very long test caption that is so very very long it is off the edge of the image");
            	
            	// Make sure the caption text's contents is the same as what we set it to
            	assertEquals("A very very long test caption that is so very very long it is off the edge of the image", captionTxt.getText().toString());
            	
            	// Ensure that the update caption button exists, then perform a click on it
            	assertNotNull(updateCaptionButton);
            	updateCaptionButton.performClick();
            	
            	// Assert that an error dialog displayed because the caption is too long
            	assertTrue(createNewspaperActivity.getErrorDialog().isShowing());
            }
        });
        instrumentation.waitForIdleSync();
    }
    
    /**
     * Tests that if no caption exists, an error dialog displays because no caption can be removed from the image
     */
    @LargeTest
    public void testRemoveCaptionThatDoesNotExist(){
    	loadUpImageAndGoToNewspaperActivity();
    	openAddCaptionsScreen();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	Button removeCaptionButton = (Button) createNewspaperActivity.findViewById(R.id.removeCaptionBtn);
            	
            	// Make sure the caption text object exists 
            	assertNotNull(removeCaptionButton);

            	// Ensure that the update caption button exists, then perform a click on it
            	assertNotNull(removeCaptionButton);
            	removeCaptionButton.performClick();
            	
            	// Assert that an error dialog displayed because no caption exists, so no caption can be removed
            	assertTrue(createNewspaperActivity.getErrorDialog().isShowing());
            }
        });
        instrumentation.waitForIdleSync();
    }
    
	 /**
     * Tests that if we try to add a caption and there is no text in the caption text input field, then an error dialog displays to the user
     * because the user has tried to add a caption when they have not input one
     */
    @LargeTest
    public void testAddCaptionThatDoesNotExist(){
    	loadUpImageAndGoToNewspaperActivity();
    	openAddCaptionsScreen();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	EditText captionTxt = (EditText) createNewspaperActivity.findViewById(R.id.captionTxt);
            	Button updateCaptionButton = (Button) createNewspaperActivity.findViewById(R.id.updateCaptionBtn);
            	
            	// Make sure the caption text object exists and do nothing to its text (leave it empty)
            	assertNotNull(captionTxt);
            	
            	// Make sure the caption text's contents is empty
            	assertEquals("", captionTxt.getText().toString());
            	
            	// Ensure that the update caption button exists, then perform a click on it
            	assertNotNull(updateCaptionButton);
            	updateCaptionButton.performClick();
            	
            	// Assert that the error dialog displayed because the user tried to add a caption with no text
            	assertTrue(createNewspaperActivity.getErrorDialog().isShowing());
            }
        });
        instrumentation.waitForIdleSync();
    }
    
    /**
     * Tests that when the next button is clicked, the user is prompted with a dialog that asks them to save their image
     */
    @LargeTest
    public void testSaveDialogIsShowing(){
    	loadUpImageAndGoToNewspaperActivity();
    	openAddCaptionsScreen();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	Button shareScreenButton = (Button) createNewspaperActivity.findViewById(R.id.shareScreenBtn);
            	shareScreenButton.performClick();
            	
            	// Assert that the save dialog displayed
            	assertTrue(createNewspaperActivity.getErrorDialog().isShowing());
            }
        });
        instrumentation.waitForIdleSync();
    }
    
    /**
     * Utility method to simulate navigating from the create newspaper screen to the add caption screen
     */
    public void openAddCaptionsScreen() {
    	createNewspaperActivity.runOnUiThread(new Runnable() {
    		public void run() {
		    	Button addCaptionsScreenButton = (Button) createNewspaperActivity.findViewById(R.id.nextSettingsBtn);
		    	
		    	// Ensure that the button is not null before we attempt to click it
		    	assertNotNull(addCaptionsScreenButton);
		    	
		    	// Click the add caption screen button to go to the add caption screen
		    	addCaptionsScreenButton.performClick();
    		}
    	});
        instrumentation.waitForIdleSync();
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
