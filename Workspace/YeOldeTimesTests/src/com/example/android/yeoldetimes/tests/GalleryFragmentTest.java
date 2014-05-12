package com.example.android.yeoldetimes.tests;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;

import com.halftone.yeoldetimes.CreateNewspaperActivity;
import com.halftone.yeoldetimes.GetFromGalleryFragment;
import com.halftone.yeoldetimes.ImageFragment;
import com.halftone.yeoldetimes.R;

public class GalleryFragmentTest extends ActivityInstrumentationTestCase2<CreateNewspaperActivity> {

	private CreateNewspaperActivity createNewspaperActivity;
	private Button openGalleryBtn;
	
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
        
        createNewspaperActivity = getActivity(); 
        setActivityInitialTouchMode(true);
        
        openGalleryBtn = (Button) createNewspaperActivity.findViewById(R.id.uploadFromGalleryBtn);
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
     * Test that the gallery fragment is visible
     */
    @SmallTest
    public void testGalleryFragment() {  
        GetFromGalleryFragment frag = ((GetFromGalleryFragment) createNewspaperActivity.getSupportFragmentManager().findFragmentById(R.id.fragment_container));
    	assertTrue(frag.isVisible());
    }
    
    /**
     * Test that the gallery button is visible
     * 
     * Actually clicking the gallery button is untestable as it opens the gallery app which is external
     * to the YeOldeTimes application. A manual test case has been implemented for this test.
     */
    @MediumTest
    public void testGalleryOpens() {
    	assertTrue(openGalleryBtn.isShown());
    }
}
