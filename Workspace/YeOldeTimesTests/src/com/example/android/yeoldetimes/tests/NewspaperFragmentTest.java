package com.example.android.yeoldetimes.tests;

import java.io.InputStream;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.halftone.yeoldetimes.AddCaptionFragment;
import com.halftone.yeoldetimes.CreateNewspaperActivity;
import com.halftone.yeoldetimes.NewspaperFragment;
import com.halftone.yeoldetimes.R;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

/**
 * NewspaperFragmentTest tests all the functionality provided by the Create Newspaper screen. Such tests include testing
 * the radio buttons, slider bar and layout of the screen.
 * 
 * Firstly, there are a few tests for the halftone shape radio buttons such that when one of the radio buttons(for example: dots) is checked,
 * it is verified that all other radio buttons(rectangle and diamond for the case when dots is checked) are unchecked.
 * Also, it is verified that when one of the radio buttons is checked, the image will be updated in the image view.
 * There is also a test to ensure that widgets in charge of changing the halftone grid angle are performing correctly. 
 * In one such test, the halftone grid angle is set to 45 degrees and the halftone shape chosen is the dot shape. 
 * When the "Update Angle" button is clicked, the image is verified whether it is updated in the image view.
 * It is also verified that if one of the design style radio buttons is chosen, the others will be unchecked. 
 * When the Negative radio button is checked, it is verified that the halftone options layout and Gaussian blur options layout are not visible,
 * which means the extra options layout is empty.
 * When the Halftone radio button is checked, it is verified that the halftone options layout is visible and Gaussian blur options layout is 
 * hidden.
 * When the Gaussian blur radio button is checked, it is verified that the Gaussian blur options layout is visible instead of halftone options layout.
 * When the design style selected is Gaussian blur, the radio buttons associated with the Gaussian blur options are tested to ensure that
 * when one of the radio buttons (weak, medium or strong) is checked, the other radio buttons are unchecked.
 * Lastly when Next button is clicked, it is verified that the application advances to the Add Caption screen.
 * 
 * @author Chantel Garcia & Carmen Pui
 */

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
    	
    	RadioButton squareRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneRectangleRadio);
    	RadioButton circleRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneDotRadio);
    	RadioButton diamondRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneDiamondRadio);
    	
    	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
    	assertTrue(circleRadio.isChecked()); // Circle radio is checked
    	assertFalse(squareRadio.isChecked()); // Rectangle is not
    	assertFalse(diamondRadio.isChecked()); // Diamond is not
    }
    
    /**
     * Tests that we can select rectangle halftone button, the image is updated and all other buttons are not checked
     */
    @LargeTest
    public void testRectangleHalftoneSelected(){
    	loadUpImageAndGoToNewspaperActivity();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	RadioButton rectangleRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneRectangleRadio);
            	RadioButton circleRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneDotRadio);
            	RadioButton diamondRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneDiamondRadio);
            	
            	// Reload the image into the "old bitmap" so that we can test clicking the diamond radio to halftone the image
            	bitmap = BitmapFactory.decodeStream(image);
            	createNewspaperActivity.setOldBitmaps(bitmap, 0);
            	
            	rectangleRadio.performClick();
            	
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	assertTrue(rectangleRadio.isChecked()); // Rectangle radio is checked
            	assertFalse(circleRadio.isChecked()); // Circle is not 
            	assertFalse(diamondRadio.isChecked()); // Diamond is not
            }
        });
        instrumentation.waitForIdleSync();
    }
    
    /**
     * Tests that we can select diamond halftone button, the image is updated and all other buttons are not checked
     */
    @LargeTest
    public void testDiamondHalftoneSelected(){
    	loadUpImageAndGoToNewspaperActivity();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	RadioButton rectangleRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneRectangleRadio);
            	RadioButton circleRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneDotRadio);
            	RadioButton diamondRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneDiamondRadio);
            	
            	assertNotNull(diamondRadio);
            	
            	// Reload the image into the "old bitmap" so that we can test clicking the diamond radio to halftone the image
            	bitmap = BitmapFactory.decodeStream(image);
            	createNewspaperActivity.setOldBitmaps(bitmap, 0);
            	
            	// Perform a click on the diamond halftone radio 
            	diamondRadio.performClick();
            	
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	assertTrue(diamondRadio.isChecked()); // Diamond radio is checked
            	assertFalse(circleRadio.isChecked()); // Circle is not
            	assertFalse(rectangleRadio.isChecked()); // Rectangle is not
            }
        });
        instrumentation.waitForIdleSync();
    }
    
    /**
     * Tests that the angle of the halftone grid can be changed for circle halftone
     */
    @LargeTest
    public void testHalftoneAngleCricle(){
    	loadUpImageAndGoToNewspaperActivity();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	RadioButton circleRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneDotRadio);
            	
            	assertNotNull(circleRadio);
            	
            	// Reload the image into the "old bitmap" so that we can test clicking the circle radio to halftone the image
            	bitmap = BitmapFactory.decodeStream(image);
            	createNewspaperActivity.setOldBitmaps(bitmap, 0);
            	
            	// Perform a click on the diamond halftone radio 
            	circleRadio.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	
            	// Get the newspaper fragment so we can call methods on it
            	NewspaperFragment frag = ((NewspaperFragment) createNewspaperActivity.getSupportFragmentManager().findFragmentByTag("Newspaper Fragment"));
             	assertNotNull(frag);
                assertTrue(frag.isVisible());
            	
            	// Change the halftone grid angle to 45 degrees (update the slider value)
            	frag.setHalftoneAngle(50);
            	frag.setHalftoneAngle();
            	
            	Button changeGridAngleButton = (Button) createNewspaperActivity.findViewById(R.id.updateAngleBtn);
            	assertNotNull(changeGridAngleButton);
            	changeGridAngleButton.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	
            	// Change the halftone grid angle to 90 degrees
            	frag.setHalftoneAngle(90);
            	frag.setHalftoneAngle();
            	
            	assertNotNull(changeGridAngleButton);
            	changeGridAngleButton.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            }
        });
        instrumentation.waitForIdleSync();
    }
    
    /**
     * Tests that the angle of the halftone grid can be changed for rectangle halftone
     */
    @LargeTest
    public void testHalftoneAngleRectangle(){
    	loadUpImageAndGoToNewspaperActivity();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	RadioButton rectangleRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneRectangleRadio);
            	
            	assertNotNull(rectangleRadio);
            	
            	// Reload the image into the "old bitmap" so that we can test clicking the rectangle radio to halftone the image
            	bitmap = BitmapFactory.decodeStream(image);
            	createNewspaperActivity.setOldBitmaps(bitmap, 0);
            	
            	// Perform a click on the diamond halftone radio 
            	rectangleRadio.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	
            	// Get the newspaper fragment so we can call methods on it
            	NewspaperFragment frag = ((NewspaperFragment) createNewspaperActivity.getSupportFragmentManager().findFragmentByTag("Newspaper Fragment"));
             	assertNotNull(frag);
                assertTrue(frag.isVisible());
            	
            	// Change the halftone grid angle to 45 degrees (update the slider value)
            	frag.setHalftoneAngle(50);
            	frag.setHalftoneAngle();
            	
            	Button changeGridAngleButton = (Button) createNewspaperActivity.findViewById(R.id.updateAngleBtn);
            	assertNotNull(changeGridAngleButton);
            	changeGridAngleButton.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	
            	// Change the halftone grid angle to 90 degrees
            	frag.setHalftoneAngle(90);
            	frag.setHalftoneAngle();
            	
            	assertNotNull(changeGridAngleButton);
            	changeGridAngleButton.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            }
        });
        instrumentation.waitForIdleSync();
    }
    
    /**
     * Tests that the angle of the halftone grid can be changed for diamond halftone
     */
    @LargeTest
    public void testHalftoneAngleDiamond(){
    	loadUpImageAndGoToNewspaperActivity();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	RadioButton diamondRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneDiamondRadio);
            	
            	assertNotNull(diamondRadio);
            	
            	// Reload the image into the "old bitmap" so that we can test clicking the diamond radio to halftone the image
            	bitmap = BitmapFactory.decodeStream(image);
            	createNewspaperActivity.setOldBitmaps(bitmap, 0);
            	
            	// Perform a click on the diamond halftone radio 
            	diamondRadio.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	
            	// Get the newspaper fragment so we can call methods on it
            	NewspaperFragment frag = ((NewspaperFragment) createNewspaperActivity.getSupportFragmentManager().findFragmentByTag("Newspaper Fragment"));
             	assertNotNull(frag);
                assertTrue(frag.isVisible());
            	
            	// Change the halftone grid angle to 45 degrees (update the slider value)
            	frag.setHalftoneAngle(50);
            	frag.setHalftoneAngle();
            	
            	Button changeGridAngleButton = (Button) createNewspaperActivity.findViewById(R.id.updateAngleBtn);
            	assertNotNull(changeGridAngleButton);
            	changeGridAngleButton.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	
            	// Change the halftone grid angle to 90 degrees
            	frag.setHalftoneAngle(90);
            	frag.setHalftoneAngle();
            	
            	assertNotNull(changeGridAngleButton);
            	changeGridAngleButton.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            }
        });
        instrumentation.waitForIdleSync();
    }
    
    /**
     * Tests that we progress to the addCaption screen on click of the next button
     */
    @LargeTest
    public void testClickingNextGoesToAddCaptionScreen(){
    	loadUpImageAndGoToNewspaperActivity();
    	
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
        
        AddCaptionFragment frag = ((AddCaptionFragment) createNewspaperActivity.getSupportFragmentManager().findFragmentByTag("Add Caption Fragment"));
    	assertNotNull(frag);
        assertTrue(frag.isVisible());
    }
    
    /**
     * Tests that once the negative radio is selected, both the halftone layout and the gaussian blur layout is not visible
     */
    @LargeTest
    public void testNegativeRadio(){
    	loadUpImageAndGoToNewspaperActivity();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	RadioButton negativeRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.negativeRadio);
            	
            	assertNotNull(negativeRadio);
            	
            	// Reload the image into the "old bitmap" so that we can test that the image is made negative when the radio is clicked
            	bitmap = BitmapFactory.decodeStream(image);
            	createNewspaperActivity.setOldBitmaps(bitmap, 0);
            	
            	// Perform a click on the negative radio
            	negativeRadio.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	
            	LinearLayout halftoneLayout = (LinearLayout) createNewspaperActivity.findViewById(R.id.halftoneLayout);
            	LinearLayout halftoneAngleLayout = (LinearLayout) createNewspaperActivity.findViewById(R.id.halftoneAngleLayout);
            	LinearLayout gaussianBlurLayout = (LinearLayout) createNewspaperActivity.findViewById(R.id.gaussianBlurLayout);
            	
            	// Assert that all of the layouts exist
            	assertNotNull(halftoneLayout);
            	assertNotNull(halftoneAngleLayout);
            	assertNotNull(gaussianBlurLayout);
            	
            	/* Assert that none of the above layouts are visible (because the negative radio is selected and no additional layout is 
            	 * added because there are no additional operations that the user can perform in making an image a negative image)
            	 */
            	assertTrue(halftoneLayout.getVisibility() == View.GONE);
            	assertTrue(halftoneAngleLayout.getVisibility() == View.GONE);
            	assertTrue(gaussianBlurLayout.getVisibility() == View.GONE);
            }
        });
        instrumentation.waitForIdleSync();
    }
    
    /**
     * Tests that once the gaussian blur radio is selected, only the gaussian blur layout is visible
     */
    @LargeTest
    public void testGaussianBlurRadio(){
    	loadUpImageAndGoToNewspaperActivity();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	RadioButton gaussianBlurRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.blurRadio);
            	
            	assertNotNull(gaussianBlurRadio);
            	
            	// Reload the image into the "old bitmap" so that we can test that the image is made negative when the radio is clicked
            	bitmap = BitmapFactory.decodeStream(image);
            	createNewspaperActivity.setOldBitmaps(bitmap, 0);
            	
            	// Perform a click on the gaussian blur radio
            	gaussianBlurRadio.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	
            	LinearLayout halftoneLayout = (LinearLayout) createNewspaperActivity.findViewById(R.id.halftoneLayout);
            	LinearLayout halftoneAngleLayout = (LinearLayout) createNewspaperActivity.findViewById(R.id.halftoneAngleLayout);
            	LinearLayout gaussianBlurLayout = (LinearLayout) createNewspaperActivity.findViewById(R.id.gaussianBlurLayout);
            	
            	// Assert that all of the layouts exist
            	assertNotNull(halftoneLayout);
            	assertNotNull(halftoneAngleLayout);
            	assertNotNull(gaussianBlurLayout);
            	
            	/* Assert that ONLY the gaussian blur layout is visible because we should only be able to modify controls to do with 
            	 * gaussian blur because the gaussian blur radio has been selected
            	 */
            	assertTrue(gaussianBlurLayout.getVisibility() == View.VISIBLE);
            	assertTrue(halftoneLayout.getVisibility() == View.GONE);
            	assertTrue(halftoneAngleLayout.getVisibility() == View.GONE);
            }
        });
        instrumentation.waitForIdleSync();
    }
    
    /**
     * Tests that if another design radio is selected (in this case, gaussian blur) once the halftone radio is selected, all of
     * the layouts that allow the user to change their halftone image's properties (primitive type to draw the image and halftone grid 
     * angle) are visible.
     * 
     * This test was included because the halftone radio is selected on initial loading of the screen, and it should be verified
     * that when another radio is selected and the halftone radio is selected again, the layouts applicable to halftoning an image are
     * displayed as expected
     */
    @LargeTest
    public void testHalftoneRadio(){
    	loadUpImageAndGoToNewspaperActivity();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	RadioButton gaussianBlurRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.blurRadio);
            	RadioButton halftoneRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.halftoneRadio);
            	
            	assertNotNull(gaussianBlurRadio);
            	assertNotNull(halftoneRadio);
            	
            	// Reload the image into the "old bitmap" so that we can test that the image is made negative when the radio is clicked
            	bitmap = BitmapFactory.decodeStream(image);
            	createNewspaperActivity.setOldBitmaps(bitmap, 0);
            	
            	// Perform a click on the gaussian blur radio
            	gaussianBlurRadio.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	
            	LinearLayout halftoneLayout = (LinearLayout) createNewspaperActivity.findViewById(R.id.halftoneLayout);
            	LinearLayout halftoneAngleLayout = (LinearLayout) createNewspaperActivity.findViewById(R.id.halftoneAngleLayout);
            	LinearLayout gaussianBlurLayout = (LinearLayout) createNewspaperActivity.findViewById(R.id.gaussianBlurLayout);
            	
            	// Assert that all of the layouts exist
            	assertNotNull(halftoneLayout);
            	assertNotNull(halftoneAngleLayout);
            	assertNotNull(gaussianBlurLayout);
            	
            	/* Assert that ONLY the gaussian blur layout is visible because we should only be able to modify controls to do with 
            	 * gaussian blur because the gaussian blur radio has been selected
            	 */
            	assertTrue(gaussianBlurLayout.getVisibility() == View.VISIBLE);
            	assertTrue(halftoneLayout.getVisibility() == View.GONE);
            	assertTrue(halftoneAngleLayout.getVisibility() == View.GONE);
            	
            	// Now, press the halftone radio 
            	halftoneRadio.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	
            	// Now, assert that ONLY the two halftone layouts (halftoneLayout and halftoneAngleLayout) are visible
            	assertTrue(gaussianBlurLayout.getVisibility() == View.GONE);
            	assertTrue(halftoneLayout.getVisibility() == View.VISIBLE);
            	assertTrue(halftoneAngleLayout.getVisibility() == View.VISIBLE);
            }
        });
        instrumentation.waitForIdleSync();
    }
    
    /**
     * Tests gaussian blurring on a "weak" intensity. Thus it tests that only the "weak" radio button is selected upon calling 
     * gaussian blur with a weak intensity and that it completes within a reasonable time
     */
    @LargeTest
    public void testWeakGaussianBlur(){
    	loadUpImageAndGoToNewspaperActivity();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	RadioButton gaussianBlurRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.blurRadio);
            	RadioButton weakBlurRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.weakGaussianBlurRadio);
            	RadioButton mediumBlurRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.mediumGaussianBlurRadio);
            	RadioButton strongBlurRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.strongGaussianBlurRadio);
            	
            	assertNotNull(gaussianBlurRadio);
            	assertNotNull(weakBlurRadio);
            	
            	// Reload the image into the "old bitmap" so that we can test that the image is made negative when the radio is clicked
            	bitmap = BitmapFactory.decodeStream(image);
            	createNewspaperActivity.setOldBitmaps(bitmap, 0);
            	
            	// Perform a click on the gaussian blur radio
            	gaussianBlurRadio.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	
            	// Perform a click on the weak gaussian blur radio
            	weakBlurRadio.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	
            	// Assert that ONLY the weak blur radio is selected
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	assertTrue(weakBlurRadio.isChecked()); // weak blur radio is checked
            	assertFalse(mediumBlurRadio.isChecked()); // medium blur is not
            	assertFalse(strongBlurRadio.isChecked()); // strong blur is not
            }
        });
        instrumentation.waitForIdleSync();
    }
    
    /**
     * Tests gaussian blurring on a "medium" intensity. Thus it tests that only the "medium" radio button is selected upon calling 
     * gaussian blur with a medium intensity and that it completes within a reasonable time
     */
    @LargeTest
    public void testMediumGaussianBlur(){
    	loadUpImageAndGoToNewspaperActivity();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	RadioButton gaussianBlurRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.blurRadio);
            	RadioButton weakBlurRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.weakGaussianBlurRadio);
            	RadioButton mediumBlurRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.mediumGaussianBlurRadio);
            	RadioButton strongBlurRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.strongGaussianBlurRadio);
            	
            	assertNotNull(gaussianBlurRadio);
            	assertNotNull(weakBlurRadio);
            	
            	// Reload the image into the "old bitmap" so that we can test that the image is made negative when the radio is clicked
            	bitmap = BitmapFactory.decodeStream(image);
            	createNewspaperActivity.setOldBitmaps(bitmap, 0);
            	
            	// Perform a click on the gaussian blur radio
            	gaussianBlurRadio.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	
            	// Perform a click on the medium gaussian blur radio
            	mediumBlurRadio.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	
            	// Assert that ONLY the medium blur radio is selected
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	assertTrue(mediumBlurRadio.isChecked()); // medium blur is checked
            	assertFalse(weakBlurRadio.isChecked()); // weak blur radio is not
            	assertFalse(strongBlurRadio.isChecked()); // strong blur is not
            }
        });
        instrumentation.waitForIdleSync();
    }
    
    /**
     * Tests gaussian blurring on a "strong" intensity. Thus it tests that only the "strong" radio button is selected upon calling 
     * gaussian blur with a strong intensity and that it completes within a reasonable time
     */
    @LargeTest
    public void testStrongGaussianBlur(){
    	loadUpImageAndGoToNewspaperActivity();
    	
    	createNewspaperActivity.runOnUiThread(new Runnable() {
            public void run() {
            	RadioButton gaussianBlurRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.blurRadio);
            	RadioButton weakBlurRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.weakGaussianBlurRadio);
            	RadioButton mediumBlurRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.mediumGaussianBlurRadio);
            	RadioButton strongBlurRadio = (RadioButton) createNewspaperActivity.findViewById(R.id.strongGaussianBlurRadio);
            	
            	assertNotNull(gaussianBlurRadio);
            	assertNotNull(weakBlurRadio);
            	
            	// Reload the image into the "old bitmap" so that we can test that the image is made negative when the radio is clicked
            	bitmap = BitmapFactory.decodeStream(image);
            	createNewspaperActivity.setOldBitmaps(bitmap, 0);
            	
            	// Perform a click on the gaussian blur radio
            	gaussianBlurRadio.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	
            	// Perform a click on the strong gaussian blur radio
            	strongBlurRadio.performClick();
            	
            	// Assert that the image view contains a drawable image
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	
            	// Assert that ONLY the strong blur radio is selected
            	assertNotNull(createNewspaperActivity.getImageFragment().getImageView().getDrawable());
            	assertTrue(strongBlurRadio.isChecked()); // strong blur is checked
            	assertFalse(weakBlurRadio.isChecked()); // weak blur radio is not
            	assertFalse(mediumBlurRadio.isChecked()); // medium blur is not
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
