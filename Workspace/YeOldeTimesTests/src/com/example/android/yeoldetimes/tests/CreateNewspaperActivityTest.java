package com.example.android.yeoldetimes.tests;

import java.io.InputStream;

import com.halftone.yeoldetimes.CreateNewspaperActivity;
import com.halftone.yeoldetimes.NewspaperFragment;
import com.halftone.yeoldetimes.R;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;

public class CreateNewspaperActivityTest extends ActivityInstrumentationTestCase2<CreateNewspaperActivity> {

	public CreateNewspaperActivityTest() {
		super(CreateNewspaperActivity.class);
	}

	private CreateNewspaperActivity createNewspaperActivity;
	private Instrumentation instrumentation;
	private InputStream image;
	private Bitmap bitmap;
	
    /**
     * Set up the test (we look at gallery first, then we're going to try to open the gallery fragment with no image)
     * (because of an upload type of 1)
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
     * Test the preconditions of the activity: 
     * It should not be null.
     */
    @SmallTest
    public void testPreconditions() {
        assertNotNull("The create newspaper activity is null", createNewspaperActivity);
    }
    
    /**
     * Test that we are able to open the newspaper fragment from the create newspaper activity with an image loaded into the image view
     * 
     * Then verify that the newspaper fragment is indeed displaying
     */
    @LargeTest
    public void testCanOpenNewspaperActivityWithImage(){
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	// Set up all of the variables to do with the imageView
            	createNewspaperActivity.getImageFragment().updateImage(bitmap);
            	createNewspaperActivity.getImageFragment().setOriginalImage();
            	createNewspaperActivity.setOldBitmaps(bitmap, 0);
            	
            	// Make sure the variables belonging to the imageView are not null (holding image data)
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView());
            	assertNotNull(createNewspaperActivity.getImageFragment().getBitmap());
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageBytes());
            
            	createNewspaperActivity.openNewspaperCreator();
            }
        });
        instrumentation.waitForIdleSync();
        
        // Verify that the newspaper fragment is displaying
        NewspaperFragment frag = ((NewspaperFragment) createNewspaperActivity.getSupportFragmentManager().findFragmentById(R.id.fragment_container));
    	assertTrue(frag.isVisible());
    }
}
