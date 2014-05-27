package com.example.android.yeoldetimes.tests;

import com.halftone.yeoldetimes.CreateNewspaperActivity;
import com.halftone.yeoldetimes.HomePageFragment;
import com.halftone.yeoldetimes.MainActivity;
import com.halftone.yeoldetimes.R;

import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;

/**
 * The MainActivityTest Suite tests that the home screen launches appropriately with the Gallery and Camera buttons displaying on the 
 * screen accordingly.
 * The Gallery button is clicked to make sure that the application can advance to the Get From Gallery screen.
 * The Camera button is clicked to make sure that the application can advance to Get From Camera screen.
 * There is also a test to make sure that the Create Newspaper screen can be launched from both the Get From Gallery screen and 
 * the Get From Camera screen.
 * 
 * @author Chantel Garcia & Carmen Pui
 */

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mainActivity;
	private Instrumentation instrumentation;
	private Button galleryButton;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    /**
     * Set up the test. This involves setting up the ability to touch on the activity (click buttons etc.)
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainActivity = getActivity(); 
        instrumentation = getInstrumentation();
        setActivityInitialTouchMode(true);
        
        // Set up buttons
        galleryButton = (Button) mainActivity.findViewById(R.id.galleryBtn);
    }
    
    /**
     * Test the preconditions of the activity: 
     * It should not be null.
     */
    @SmallTest
    public void testPreconditions() {
        assertNotNull("The main activity is null", mainActivity);
    }
    
    /** 
     * Test that the home screen fragment is visible
     */
    @SmallTest
    public void testHomeScreen() {  
        HomePageFragment frag = ((HomePageFragment) mainActivity.getSupportFragmentManager().findFragmentById(R.id.fragment_container));
        assertNotNull(frag);
        assertTrue(frag.isVisible());
    }
    
    /**
     * Ensure that the gallery button is opening the Create Newspaper Activity correctly
     * 
     * Check that the Create Newspaper Activity is not null on click of the gallery button to open it.
     */
    @MediumTest
    public void testGalleryButton() {
    	
	  ActivityMonitor activityMonitor = getInstrumentation().addMonitor(CreateNewspaperActivity.class.getName(), null, false);
	  
	  // Run the button click on the UI thread
	  mainActivity.runOnUiThread(new Runnable() {
	    @Override
	    public void run() {
	      galleryButton.performClick();
	    }
	  });
      instrumentation.waitForIdleSync();
	
	  // Make sure the create newspaper activity is now not null
	  CreateNewspaperActivity createNewspaperActivity = (CreateNewspaperActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 100);
	  assertNotNull(createNewspaperActivity);
	  createNewspaperActivity .finish();
	}
    
    /**
     * Ensure that the camera button is opening the Create Newspaper Activity correctly
     * 
     * Check that the Create Newspaper Activity is not null on click of the camera button to open it.
     */
    @MediumTest
    public void testCameraButton() {
    	
	  ActivityMonitor activityMonitor = getInstrumentation().addMonitor(CreateNewspaperActivity.class.getName(), null, false);
	  
	  // Run the button click on the UI thread
	  mainActivity.runOnUiThread(new Runnable() {
	    @Override
	    public void run() {
	    	Button cameraButton = (Button) mainActivity.findViewById(R.id.cameraBtn);
	    	cameraButton.performClick();
	    }
	  });
      instrumentation.waitForIdleSync();

	  // Make sure the create newspaper activity is now not null
	  CreateNewspaperActivity createNewspaperActivity = (CreateNewspaperActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 100);
	  assertNotNull(createNewspaperActivity);
	  createNewspaperActivity .finish();
	}
}
