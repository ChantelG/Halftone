package com.example.android.yeoldetimes.tests;

import java.io.InputStream;

import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.Button;

import com.halftone.yeoldetimes.CreateNewspaperActivity;
import com.halftone.yeoldetimes.R;
import com.halftone.yeoldetimes.ShareFragment;

/**
 * ShareFragmentTest is a test for verifying that the Share screen can be launched and the buttons and image view are 
 * visible on the screen.
 * It is verified that the Sharing screen is able to be launched within the application. 
 * The visibility of the buttons on the screen including the "Share" and "Finish" buttons and the visibility of the image view are also verified.
 * Also, there is a small test to verify that on click of the Finish button, an error dialog appears and prompts the user for confirmation
 * to return to the home screen. When the OK button is clicked, the same test verifies that the Home screen is loaded up.
 *
 * @author Chantel Garcia & Carmen Pui
 */

public class ShareFragmentTest extends ActivityInstrumentationTestCase2<CreateNewspaperActivity> {

	private CreateNewspaperActivity createNewspaperActivity;
	private InputStream image;
	private Bitmap bitmap;
	private Instrumentation instrumentation;
	
	public ShareFragmentTest() {
		super(CreateNewspaperActivity.class);  
    }
	
	/**
     * Set up the test
     * We create the activity and instrumentation, then we get an image from our assets folder to test our fragment with.
     * Our activity will launch the gallery screen first (because of an upload type of 1)
     * 
     * NOTE: This set up is the same as in create newspaper activity test and newspaper fragment test
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
     * Tests that the share fragment is now displaying (not null and is visible)
     */
    @LargeTest
    public void testShareFragmentDisplaysCorrectly (){
    	loadUpImageAndGoToShareActivity();
    	
    	ShareFragment frag = ((ShareFragment) createNewspaperActivity.getSupportFragmentManager().findFragmentById(R.id.fragment_container));
    	assertNotNull(frag);
    	assertTrue(frag.isVisible());
    }
    
    /**
     * Tests that pressing the next button opens a dialog asking the user to confirm that they are going back to the home screen
     */
    @LargeTest
    public void testDialogAppearsOnFinishClick (){
    	loadUpImageAndGoToShareActivity();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	Button finishButton = (Button) createNewspaperActivity.findViewById(R.id.finishBtn);
            	
            	// Ensure that the button is not null before we attempt to click it
            	assertNotNull(finishButton);
            	finishButton.performClick();
            }
        });
        instrumentation.waitForIdleSync();
    	
        // Ensure that the error dialog is displaying after clicking the finish button
    	assertTrue(createNewspaperActivity.getErrorDialog().isShowing());
    }
    
    /**
     * Tests that pressing the next button and clicking ok on the dialog closes the activity
     */
    @LargeTest
    public void testActivityClosesOnFinish (){
    	loadUpImageAndGoToShareActivity();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	Button finishButton = (Button) createNewspaperActivity.findViewById(R.id.finishBtn);
            	
            	// Ensure that the button is not null before we attempt to click it
            	assertNotNull(finishButton);
            	finishButton.performClick();
            	
            	// Ensure that the error dialog is not null
            	assertNotNull(createNewspaperActivity.getErrorDialog());
            	
            	// Click on the positive button (OK) to close the dialog
            	createNewspaperActivity.getErrorDialog().getDialog().getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            }
        });
        instrumentation.waitForIdleSync();
    	
        // Ensure that the activity is closing off now we have clicked finish and ok on the dialog
        assertTrue(createNewspaperActivity.isFinishing());
    }
    
    /**
     * Utility method to launch the share fragment with a dummy image such that we can test all that relates to the share fragment
     */
    public void loadUpImageAndGoToShareActivity(){
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
            
            	// Open the createNewspaperActivity's share fragment with this dummy image we loaded in from our assets
            	createNewspaperActivity.saveToGallery();
            	createNewspaperActivity.openShareFragment();
            }
        });
        instrumentation.waitForIdleSync();
    }
}
