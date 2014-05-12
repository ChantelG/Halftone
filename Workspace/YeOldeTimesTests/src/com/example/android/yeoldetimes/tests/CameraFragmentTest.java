package com.example.android.yeoldetimes.tests;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;

import com.halftone.yeoldetimes.CreateNewspaperActivity;
import com.halftone.yeoldetimes.GetFromCameraFragment;
import com.halftone.yeoldetimes.ImageFragment;
import com.halftone.yeoldetimes.R;

public class CameraFragmentTest extends ActivityInstrumentationTestCase2<CreateNewspaperActivity> {

	private CreateNewspaperActivity createNewspaperActivity;
	private Button openCameraBtn;
	
	public CameraFragmentTest() {
		super(CreateNewspaperActivity.class);  
    }

    /**
     * Set up the test
     * 
     * Start the CreateNewspaperActivity with an uploadType of Camera
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        Intent createActivityIntent = new Intent();
		createActivityIntent.putExtra("uploadType", 1); 
		setActivityIntent(createActivityIntent);
        
        createNewspaperActivity = getActivity(); 
        setActivityInitialTouchMode(true);
        
        openCameraBtn = (Button) createNewspaperActivity.findViewById(R.id.uploadFromCameraBtn);
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
     * Test that the image fragment is visible
     */
    @SmallTest
    public void testImageFragment() {  
        ImageFragment frag = ((ImageFragment) createNewspaperActivity.getSupportFragmentManager().findFragmentById(R.id.image_fragment));
    	assertTrue(frag.isVisible());
    }
   
    /** 
     * Test that the camera fragment is visible
     */
    @SmallTest
    public void testCameraFragment() {  
        GetFromCameraFragment frag = ((GetFromCameraFragment) createNewspaperActivity.getSupportFragmentManager().findFragmentById(R.id.fragment_container));
    	assertTrue(frag.isVisible());
    }
    
    /**
     * Test that the camera button is visible
     * 
     * Actually clicking the camera button is untestable as it opens the camera app which is external
     * to the YeOldeTimes application. A manual test case has been implemented for this test.
     */
    @MediumTest
    public void testGalleryOpens() {
    	assertTrue(openCameraBtn.isShown());
    }
}
