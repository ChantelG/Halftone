package com.example.android.yeoldetimes.tests;

import java.io.InputStream;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;

import com.halftone.yeoldetimes.CreateNewspaperActivity;
import com.halftone.yeoldetimes.GetFromGalleryFragment;
import com.halftone.yeoldetimes.ImageFragment;
import com.halftone.yeoldetimes.R;

/**
 * The GalleryFragmentTest Suite tests that Get Image From Gallery screen can be launched when Gallery button is clicked on the home screen
 * This test will make sure that the image view fragment and the buttons on the screen including the "Upload Image" and "Next" buttons are all visible on the screen.
 * The Next button is tested to verify that if no image is loaded in the image view, an Error Dialog appears and the application does not advance to the next screen.
 * The GalleryFragmentTest cannot test whether the native gallery app can be started up when the Upload Image button is clicked.
 * Therefore, a manual test case has been implemented for this test.
 * To make sure that the image view can be updated with an image, a test image is set up and loaded into the image view and 
 * it is verified that the image view is not empty.
 * 
 * @author Chantel Garcia & Carmen Pui
 */

public class GalleryFragmentTest extends ActivityInstrumentationTestCase2<CreateNewspaperActivity> {

	private CreateNewspaperActivity createNewspaperActivity;
	private Instrumentation instrumentation;
	private Button openGalleryBtn;
	private Button nextBtn;
	private InputStream image;
	private Bitmap bitmap;
	
	public GalleryFragmentTest() {
		super(CreateNewspaperActivity.class);  
    }

    /**
     * Set up the test
     * 
     * Start the CreateNewspaperActivity with an uploadType of Gallery
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        Intent createActivityIntent = new Intent();
		createActivityIntent.putExtra("uploadType", 0); 
		setActivityIntent(createActivityIntent);
        
        instrumentation = getInstrumentation();
        createNewspaperActivity = getActivity(); 
        setActivityInitialTouchMode(true);
        
        // Set up an image to put into the image view later on 
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
        
        openGalleryBtn = (Button) createNewspaperActivity.findViewById(R.id.uploadFromGalleryBtn);
        nextBtn = (Button) createNewspaperActivity.findViewById(R.id.nextBtn);
    }
    
    /**
     * Test the preconditions of the activity: 
     * 1. It should not be null.
     */
    @SmallTest
    public void testPreconditions() {
        assertNotNull("The create newspaper activity is null", createNewspaperActivity);
    }
    
    /** 
     * Test that the image fragment is not null and is visible
     */
    @SmallTest
    public void testImageFragment() {  
        ImageFragment frag = ((ImageFragment) createNewspaperActivity.getSupportFragmentManager().findFragmentById(R.id.image_fragment));
    	assertNotNull(frag);
        assertTrue(frag.isVisible());
    }
   
    /** 
     * Test that the gallery fragment is not null and is visible
     */
    @SmallTest
    public void testGalleryFragment() {  
        GetFromGalleryFragment frag = ((GetFromGalleryFragment) createNewspaperActivity.getSupportFragmentManager().findFragmentById(R.id.fragment_container));
    	assertNotNull(frag);
        assertTrue(frag.isVisible());
    }
    
    /**
     * Test that the gallery button is visible
     * 
     * Actually clicking the gallery button is untestable as it opens the gallery app which is external
     * to the YeOldeTimes application. A manual test case has been implemented for this test.
     */
    @MediumTest
    public void testGalleryButtonVisible() {
    	assertTrue(openGalleryBtn.isShown());
    }
    
    /**
     * Test to verify that when we click the next button, we dont advance to the next screen because no image is loaded yet
     */
    @LargeTest
    public void testNextBtnNotAdvance() {
    	  	  
		// Run the button click on the UI thread
		createNewspaperActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				nextBtn.performClick();
			}
		});
        instrumentation.waitForIdleSync();
		
		GetFromGalleryFragment frag = ((GetFromGalleryFragment) createNewspaperActivity.getSupportFragmentManager().findFragmentById(R.id.fragment_container));
    	assertTrue(frag.isVisible());
    }
    
    /**
     * Assert that an error dialog displays on clicking next (because no image was loaded in)
     */
    @LargeTest
    public void testDialogOnNextWithNoImage(){
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	nextBtn.performClick();
            }
        });
        instrumentation.waitForIdleSync();
        
    	assertTrue(createNewspaperActivity.getErrorDialog().isShowing());
    }
    
    /** 
     * Test that the image view can be updated with an image
     */
    @SmallTest
    public void testUpdateImage() { 
		createNewspaperActivity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		    	createNewspaperActivity.getImageFragment().updateImage(bitmap);
		    	assertNotNull(createNewspaperActivity.getImageFragment().getImageView());
		    	assertNotNull(createNewspaperActivity.getImageFragment().getBitmap());
		    	assertNotNull(createNewspaperActivity.getImageFragment().getImageBytes());
		    }
		  });
        instrumentation.waitForIdleSync();
    }
}
