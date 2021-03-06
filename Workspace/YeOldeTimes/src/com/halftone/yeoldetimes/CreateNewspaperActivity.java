package com.halftone.yeoldetimes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.webkit.MimeTypeMap;

/**
 * This class handles all of the functionality of the screens that reside under the Create Newspaper Activity. 
 * Thus it handles all of the button clicks within its child fragments, creates fragments, hides fragments and shows fragments.
 * It also initiates all of the intents corresponding to its child fragments.
 * It keeps track of bitmaps used in the system over time, such that when the user presses the back button, they are given the image
 * in the modified state that it was before they navigated to the next fragment. 
 * It also handles a lot of error dialog displaying for alerting the user when they need to perform a specific task (like save their image)
 * 
 * @author Chantel Garcia & Carmen Pui
 */

public class CreateNewspaperActivity extends FragmentActivity implements OnButtonClickedListener{
	
	// Declaration of request codes
	private final int LOAD_IMAGE_REQUEST_CODE = 1;
	private final int CAMERA_REQUEST_CODE = 1337;
	private final String CAPTURE_TITLE = "temporaryImage";
	private Bitmap[] oldBitmaps = new Bitmap[3];
	private PrimitiveType currentPrimitiveType;
	private String caption;
	private int halftoneRadioSelected;
	private int designRadioSelected;
	private int gaussianRadioSelected;
	private boolean imageUploaded;
	private boolean saved;
	
	// The upload type which determines the screen to display (Gallery upload or Camera upload)
	private UploadType uploadType;
	
	// The two main fragments of the screen that we want to keep track of (imageFragment is persistent on each screen)
	private ImageFragment imageFragment;
	private NewspaperFragment newspaperFragment;
	private AddCaptionFragment addCaptionFragment;
	
	// A shared errorDialog object that gets set to display the appropriate error
	private ErrorDialog errorDialog;
	
	/**
	 * onCreate is a method to create the item needed for the fragments of the application
	 * such as the buttons, radio buttons and get value from the button clicked to invoke 
	 * activity or method
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_newspaper_activity);
        
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
        	int value = bundle.getInt("uploadType");
        	
        	switch(value) {
	        	case 0:
	        		uploadType = UploadType.GALLERY;
	        		break;
	        	case 1: 
	        		uploadType = UploadType.CAMERA;
	        		break;
	    		default:
	    			break;
        	}
        	
        	switch(uploadType) {
	        	case GALLERY:
	        		openGetFromGalleryFragment();
	        		break;
	        	case CAMERA:
	        		openGetFromCameraFragment();
	        		break;
				default:
					break;
        	}
        	
        	imageUploaded = false;
        	saved = false;
        	caption = "";
        	halftoneRadioSelected = R.id.halftoneDotRadio;
        	designRadioSelected = R.id.halftoneRadio;
        	gaussianRadioSelected = R.id.weakGaussianBlurRadio;
        }
        /* Otherwise, put an error dialog to say that there was an unexpected error occured. 
         * (The bundle did not pass any extra through) 
         */
        else {
        	errorDialog = new ErrorDialog(this, R.string.unexpected_error_title, R.string.unexpected_error_msg, ErrorDialogType.GENERAL_ERROR);
    		errorDialog.show();
        }
        
        // Set the current primitive type (i.e. halftone type) to be circle by default
        currentPrimitiveType = PrimitiveType.CIRCLE;
    }
	
	/**
	 * Create the image fragment to hold the image persistent across each screen (this is updated if it is halftoned, a caption is added etc)
	 */
	public void createImageFragment() {
		imageFragment = new ImageFragment();
		imageFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.image_fragment, imageFragment, "Image Fragment").commit();
	}
	
	/**
	 * Create a gallery fragment layout with an image fragment
	 */
	public void openGetFromGalleryFragment() {
		createImageFragment();
		GetFromGalleryFragment uploadImageFragment = new GetFromGalleryFragment();
		uploadImageFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, uploadImageFragment, "Upload Image Fragment").commit();
	}
	
	/**
	 * Create the newspaper fragment (but keep the image fragment from the previous layout (the Gallery with image fragment or the Camera
	 * with image fragment)
	 * 
	 * By default, the image is set to halftone to circle as the circle halftone radio button is selected when we open the screen
	 */
	public void openNewspaperCreator() {
		newspaperFragment = new NewspaperFragment();
		newspaperFragment.setHalftoneStyleRadio(halftoneRadioSelected);
		newspaperFragment.setDesignRadio(designRadioSelected);
		newspaperFragment.setGaussianBlurRadio(gaussianRadioSelected);
		newspaperFragment.setHalftoneAngle(imageFragment.getRotationAngle());
		
		newspaperFragment.setArguments(getIntent().getExtras());
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		transaction.replace(R.id.fragment_container, newspaperFragment, "Newspaper Fragment");
		transaction.addToBackStack(null);
		
		transaction.commit();
		
		if(oldBitmaps[1] != null)
			imageFragment.updateImage(oldBitmaps[1]);
		else{
			imageFragment.halftoneImage(imageFragment.getOriginalImage(), PrimitiveType.CIRCLE);
		}
	}

	/**
	 * Create the camera fragment alongside an image fragment
	 */
	public void openGetFromCameraFragment() {
		createImageFragment();
		GetFromCameraFragment captureImageFragment = new GetFromCameraFragment();
		captureImageFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, captureImageFragment, "Capture Image Fragment").commit();
	}
	
	/**
	 * This method will create the fragment for sharing the halftone newspaper image (share fragment), if
	 * the image is not saved it will show an error dialog to prompt user to save image before sharing.
	 */
    public void openShareFragment() {
    	if(!saved) {
    		errorDialog = new ErrorDialog(this, R.string.image_not_saved_title, R.string.image_not_saved_msg, ErrorDialogType.NOT_SAVED);
    		errorDialog.show();
    	}
    	else {
    		advanceToShare();
    	}
    }
    
    /**
     * This method opens the Add Caption Fragment, which allows the user to add a caoption to their modified image 
     */
    public void openAddCaptionFragment() {
    	addCaptionFragment = new AddCaptionFragment();
    	addCaptionFragment.setArguments(getIntent().getExtras());
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		addCaptionFragment.setCaption(caption);
		
		transaction.replace(R.id.fragment_container, addCaptionFragment, "Add Caption Fragment");
		transaction.addToBackStack(null);
		transaction.commit();
	
		if(oldBitmaps[2] != null)
			imageFragment.updateImage(oldBitmaps[2]);
    }
	
    /**
     * This method will open up the native gallery using the open gallery Intent, allowing a chooser to display such that the user can
     * select an image
     */
    public void openGallery() {
    	// Create a new intent with the image type and action
    	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    	intent.setType("image/*");
    	startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
    }
    
    /**
     * getImageUri gets the image uri pointing to the folder dcim and file name capture_title and returns it
     *  
     * @return imgUri - the image uri (locator) pointing to the location of the image in the DCIM folder with the given capture title
     */
    private Uri getImageUri() {
        // Get image Uri from dcim folder (camera default image location folder) with the given image title (CAPTURE_TITLE)
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM", CAPTURE_TITLE);
        Uri imgUri = Uri.fromFile(file);
        return imgUri;
    }
    
    /**
     * A method to start the native camera app to capture a photo using the camera (using the capture intent)
     */
    public void openCamera() {
    	// Use the stock camera app to take a photo
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
	}
    
    /**
     * getCurrentTime is a method to get the current timestamp to give the halftone image a unique name to save it out with
     * 
     * @return date - a formatted string date
     */
    public String getCurrentTime() {
        	// Make a name for the file by getting the current date from the phone and formatting it
    		Calendar calendar = Calendar.getInstance();
    		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSSz", Locale.ENGLISH);
    		String date = dateFormat.format(calendar.getTime());
    		date = date.replace(":", "-");
    		date = date.replace(".", "-");
    		date = date.replace(" ", "-");
    		return date;
    }
    
    /**
     * getNewFilePath will create a filePath to save the image with .jpg extension and the name of the image to be saved in the 
     * is obtained from the getCurrentTime method (so the file is saved out as something like 19-01-2014 09:00:00.0000.jpg)
     * 
     * @return filePath - the filePath that the image will be saved into
     */
    public String getNewFilePath() {
    	String fileName = getCurrentTime();
		File extStore = Environment.getExternalStorageDirectory();
		String filePath = extStore.getAbsolutePath() + "/" + fileName + ".jpg";
		return filePath;
    }
    
    /**
     * getNewFile creates a new file with the filePath. If filePath does not exist in the directory then it will create the new 
     * file in the directory from the filePath
     * 
     * @param filePath - the file's complete path
     * @return myFile - the created file
     */
    public File getNewFile(String filePath) {
    	try {
			// Create a new file with the current name
			File myFile = new File(filePath);
			
			// If file doesn't exist, then create it
			if (!myFile.exists()) 
				myFile.createNewFile();
			
			return myFile;
    	} catch (Exception e) {
    		errorDialog = new ErrorDialog(this, R.string.file_save_error_title, R.string.file_load_error_msg, ErrorDialogType.NOT_SAVED);
    		errorDialog.show();
    	}
    	
    	return null;
    }
    
    /** 
     * Returns the current error dialog (last created error dialog)
     * 
     * @return last created error dialog
     */
    public ErrorDialog getErrorDialog() {
    	return this.errorDialog;
    }
    
    /**
     * saveToGalleryAndAdvance is a method to invoke saveToGallery method, saving the image out to the gallery and the advanceToShare
     * method which displays the share newspaper fragment so the user can share their image
     */
    public void saveToGalleryAndAdvance() {
    	saveToGallery();
    	advanceToShare();
    }
    
    /**
     * advanceToShare upens up the share image fragment
     * It also sets the halftoned image into the oldBitmaps array so it can be restored on clicking back
     */
    public void advanceToShare() {
    	ShareFragment shareFragment = new ShareFragment();
    	shareFragment.setArguments(getIntent().getExtras());
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		transaction.replace(R.id.fragment_container, shareFragment, "Share Fragment");
		transaction.addToBackStack(null);
		transaction.commit();
    }
    
    /** 
     * Update the old bitmaps array (array keeping history of the changed bitmaps over time) at the given index, to have the given bitmap
     * 
     * This oldBitmapsArray is used to display old bitmaps from previous screens
     * @param bitmap - The new bitmap to put nto the oldBitmaps array
     * @param index - The index at which to put the new bitmap
     */
    public void setOldBitmaps(Bitmap bitmap, int index) {
    	oldBitmaps[index] = bitmap;
    }
    
    /**
     * saveToGallery is a method to get and set the filePath and create the file for the image bitmap in the image fragment.
     * It then saves that file containing the image from the image fragment out to the gallery. 
     * 
     * If it is not possible to create the file or set the path or save to the gallery, output an error dialog indicating a file save
     * error message.
     */
    public void saveToGallery() {
    	try 
    	{
    		String filePath = getNewFilePath();
    		File myFile = getNewFile(filePath);
    		
    		// Save the bitmap image out using the created filename and the bitmap image
    		imageFragment.setPath(filePath);
    		imageFragment.setFile(myFile);
    		imageFragment.updateFile();
    		
    		saveToGallery(myFile, imageFragment.getPath());
    		saved = true;
		} 
		catch (Exception e) 
		{
			errorDialog = new ErrorDialog(this, R.string.file_save_error_title, R.string.file_save_error_msg, ErrorDialogType.GENERAL_ERROR);
    		errorDialog.show();
		}
    }
    
    /**
     * saveToGallery saves the image into the gallery and refreshes the gallery
     * 
     * @param file - the file to save the image bitmap
     * @param path - the name and the path to be saved for the file
     */
    public void saveToGallery(File file, String path)
    {
    	Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
     }
    
    /**
     * shareImage is a method to allow for an image to share to social media using the share Intent
     * However, it will get the image Uri from the gallery in order to share the image therefore the image must be saved in the 
     * gallery before sharing
     */
    public void shareImage() {
		Intent share = new Intent(Intent.ACTION_SEND); 
		MimeTypeMap map = MimeTypeMap.getSingleton(); 

		String ext = imageFragment.getFile().getAbsolutePath().substring(imageFragment.getPath().lastIndexOf('.') + 1);
		String mime = map.getMimeTypeFromExtension(ext);
		share.setType(mime);

		share.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(imageFragment.getFile()));
		startActivity(Intent.createChooser(share, "share"));
    }
    
    /**
     * updateImageWithCaption allows the user to add a caption at the bottom of the image. However some checking is performed to verify 
     * that the caption is within the limits of the width of the image (so it does not draw off the end of the image).
     * 
     * Some more checking is done to verify that the caption is not an empty string. If it is, then an alert dialog is provided, informing
     * the user to input a caption.
     */
    public void updateImageWithCaption() {    	
    	caption = addCaptionFragment.getCaption();
    	if(caption.compareTo("") == 0) {
    		errorDialog = new ErrorDialog(this, R.string.caption_empty_title, R.string.caption_empty_msg, ErrorDialogType.NOT_EDITED);
    		errorDialog.show();
    	}
    	else if (!imageFragment.getCaptionWithinBounds(caption)) {
    		errorDialog = new ErrorDialog(this, R.string.caption_too_large_title, R.string.caption_too_large_msg, ErrorDialogType.NOT_EDITED);
    		errorDialog.show();
    	}
    	else 
    		imageFragment.updateImageCaption(caption);
    }
    
    /**
     * removeImageCaption allows the caption to be removed from the image if there exists any captions under the image, otherwise
     * an error is displayed stating that there are no captions below the image and therefore the image cannot be removed.
     */
    public void removeImageCaption() {
    	if(imageFragment.getCaptioned() == false){
    		errorDialog = new ErrorDialog(this, R.string.caption_not_exist_title, R.string.caption_not_exist_msg, ErrorDialogType.NOT_EDITED);
    		errorDialog.show();
    	}
    	else {
    		imageFragment.removeCaption();
    		caption = "";
    		addCaptionFragment.setCaption(caption);
    		addCaptionFragment.updateCaptionText();
    	}
    }
    
    /**
     * When the device's back button is pressed, if the share fragment is displaying, perform the behaviour of clicking the back button
     * PLUS restore the image that was displayed in that image view on that screen
     * 
     * Otherwise, if the newspaper fragment is showing, then perform the behaviour of clicking the back button PLUS restore the original
     * image as taken out of the gallery (without halftone)
     */
    @Override
    public void onBackPressed(){
    	// If we are on the share fragment, restore the newspaper fragment's image (halftoned image)
    	ShareFragment shareFragment = (ShareFragment)getSupportFragmentManager().findFragmentByTag("Share Fragment");
    	if (shareFragment != null && shareFragment.isVisible()){
    		oldBitmaps[1] = imageFragment.getImageBitmap();
    		oldBitmaps[2] = imageFragment.getBitmap(); 
    		performBackPressed(2);
    		return;
    	}
    	
    	// If we are on the newspaper fragment, restore the gallery or camera fragment's image (non-halftoned, original image)
    	NewspaperFragment newspaperFragment = (NewspaperFragment)getSupportFragmentManager().findFragmentByTag("Newspaper Fragment");
    	if (newspaperFragment != null && newspaperFragment.isVisible()){
    		oldBitmaps[1] = imageFragment.getImageBitmap();
    		performBackPressed(0);
    		return;
    	}
    	
    	// If we are on the add caption fragment, restore the halftoned/negative/gaussian blurred image WITHOUT a caption
    	AddCaptionFragment addCaptionFragment = (AddCaptionFragment)getSupportFragmentManager().findFragmentByTag("Add Caption Fragment");
    	if (addCaptionFragment != null && addCaptionFragment.isVisible()){
    		oldBitmaps[1] = imageFragment.getImageBitmap();
    		oldBitmaps[2] = imageFragment.getBitmap(); // But store the image with a caption
    		performBackPressed(1);
    		return;
    	}
    	
    	// Do the native back button behaviour
    	super.onBackPressed();
    }
    
    /**
     * Accessor for the image fragment
     * @return the image fragment
     */
    public ImageFragment getImageFragment() {
    	return this.imageFragment;
    }
    
    /**
     * Accessor for the newspaper fragment
     * @return the newspaper fragment
     */
    public NewspaperFragment getNewspaperFragment() {
    	return this.newspaperFragment;
    }
    
    /**
     * When back button is pressed, update the image fragment to have the image in it that it had on the screen correlating to the 
     * image index passed in
     * 
     * @param currImage - the bitmap of the image
     */
    public void performBackPressed(int currImage) {
    	imageFragment.updateImage(oldBitmaps[currImage]);
    	super.onBackPressed();
    }
    
    /**
     * showFinishDialog is a method to show the comfirmation dialog to exit back to the main menu when the finish button is clicked on the 
     * last screen
     */
    public void showFinishDialog() {
    	errorDialog = new ErrorDialog(this, R.string.finish_confirmation_title, R.string.finish_confirmation_msg, ErrorDialogType.CONFIRM_FINISH);
    	errorDialog.show();
    }
    
    /**
     * This method performs a strong gaussian blur on the image (but is only invoked when the dialog
     * asking whether a strong gaussian blur is allowed to be performed has OK clicked)
     */
    public void performStrongGaussianBlur() {
		imageFragment.gaussianBlur(oldBitmaps[0], GaussianBlurStrength.STRONG);	
		gaussianRadioSelected = R.id.strongGaussianBlurRadio;
    }
    
    /**
     * Resets the gaussian blur radio button to the previously gaussian radio button in the situation
     * that we cancel out of a strong gaussian blur
     * @return
     */
    public int getLastGaussianRadioSelected() {
    	return gaussianRadioSelected;
    }
    
    /**
     * Invoke methods according to the button clicked
     * 
     * @param buttonId - the button pressed by user
     */
	@Override
	public void onButtonClicked(int buttonId) {
		
		// Set saved to false
		if(buttonId == R.id.halftoneDotRadio || buttonId == R.id.halftoneRectangleRadio || buttonId == R.id.halftoneDiamondRadio || 
				buttonId == R.id.halftoneRadio || buttonId == R.id.negativeRadio || buttonId == R.id.blurRadio || 
				buttonId == R.id.weakGaussianBlurRadio || buttonId == R.id.mediumGaussianBlurRadio || 
				buttonId == R.id.strongGaussianBlurRadio || buttonId == R.id.updateAngleBtn || buttonId == R.id.removeCaptionBtn ||
				buttonId == R.id.updateCaptionBtn)
		{
			// Update saved to false (a change has been made)
			saved = false;
			
			// If there was a captioned image to restore, it can no longer be restored because the image was modified
			oldBitmaps[2] = null;
			
			// Update the design radio when a design radio is selected, and hide/show the layout components accordingly
			if(buttonId == R.id.halftoneRadio || buttonId == R.id.negativeRadio || buttonId == R.id.blurRadio) {
				newspaperFragment.setLayoutsVisible();
			}
		}
		
		switch(buttonId)
        {
            case R.id.uploadFromGalleryBtn:
            	openGallery(); // Open the gallery app (native)
           	 	break;
            case R.id.uploadFromCameraBtn:
            	openCamera(); // Open the camera app (native)
           	 	break;
            case R.id.nextBtn:
            	// Move to the newspaper fragment, or show an error if an image is not loaded yet
            	if(imageUploaded)
            		openNewspaperCreator();
            	else {
                	errorDialog = new ErrorDialog(this, R.string.image_not_uploaded_title, R.string.image_not_uploaded_msg, ErrorDialogType.NO_IMAGE);
            		errorDialog.show();
            	}
            	break;
            case R.id.nextSettingsBtn:
            	openAddCaptionFragment();
            	break;
            case R.id.shareScreenBtn:
            	openShareFragment(); // Open the share fragment
            	break;
            case R.id.shareBtn:
            	shareImage(); // Share the image using the share intent
            	break;
            case R.id.finishBtn:
            	showFinishDialog(); // Show the finish dialog (confirmation dialog asking the user if they want to go back to the main menu)
            	break;
            case R.id.halftoneDotRadio:
            	try{
	            	// If the halftone dot radio was not already selected, halftone the image with dots 
	            	if(halftoneRadioSelected != R.id.halftoneDotRadio) {
		            	imageFragment.halftoneImage(oldBitmaps[0], PrimitiveType.CIRCLE); // Halftone the image with circle shape
		            	currentPrimitiveType = PrimitiveType.CIRCLE; // Update the current primitive type
	            	}
            	} catch(Exception e){
            		errorDialog = new ErrorDialog(this, R.string.halftone_error_title, R.string.halftone_error_msg, ErrorDialogType.NOT_EDITED);
            		errorDialog.show();
            	}
            	break;
            case R.id.halftoneRectangleRadio:
            	try{
	            	// If the halftone rectangle radio was not already selected, halftone the image with rectangles 
	            	if(halftoneRadioSelected != R.id.halftoneRectangleRadio) {
		            	imageFragment.halftoneImage(oldBitmaps[0], PrimitiveType.RECTANGLE); // Halftone the image with rectangle shape
		            	currentPrimitiveType = PrimitiveType.RECTANGLE; // Update the current primitive type
	            	}
            	} catch(Exception e){
            		errorDialog = new ErrorDialog(this, R.string.halftone_error_title, R.string.halftone_error_msg, ErrorDialogType.NOT_EDITED);
            		errorDialog.show();
            	}
            	break;
            case R.id.halftoneDiamondRadio:
            	try{
	            	// If the halftone diamond radio was not already selected, halftone the image with diamonds 
	            	if(halftoneRadioSelected != R.id.halftoneDiamondRadio) {
		            	imageFragment.halftoneImage(oldBitmaps[0], PrimitiveType.DIAMOND); // Halftone the image with diamond shape
		            	currentPrimitiveType = PrimitiveType.DIAMOND; // Update the current primitive type
	            	}
            	} catch(Exception e){
            		errorDialog = new ErrorDialog(this, R.string.halftone_error_title, R.string.halftone_error_msg, ErrorDialogType.NOT_EDITED);
            		errorDialog.show();
            	}
            	break;
            case R.id.halftoneRadio:
            	try{
	            	// Only halftone the image if the halftone radio was not already selected
	            	if(designRadioSelected != R.id.halftoneRadio){
	            		/* Set the image to halftone using circle primitives by default when the halftone radio is selected after another design 
	                	 * radio was selected
	                	 */
		            	imageFragment.setRotationAngle(0);
		            	imageFragment.halftoneImage(imageFragment.getOriginalImage(), PrimitiveType.CIRCLE);
		            	newspaperFragment.resetHalftoneAngle(); // Reset the angle slider to 0
		            	currentPrimitiveType = PrimitiveType.CIRCLE; // Reset the current primitive type to circle
		            	halftoneRadioSelected = R.id.halftoneDotRadio;
		            	
		            	// Update the halftone style to circle again (reset to circle) 
		            	newspaperFragment.setHalftoneStyleRadio(R.id.halftoneDotRadio);
		            	newspaperFragment.updateHalftoneStyleRadio();
	            	}
            	} catch(Exception e){
            		errorDialog = new ErrorDialog(this, R.string.halftone_error_title, R.string.halftone_error_msg, ErrorDialogType.NOT_EDITED);
            		errorDialog.show();
            	}
            	break;
            case R.id.negativeRadio:
            	// Only update the "negative image" if the negative radio was not already selected
            	if(designRadioSelected != R.id.negativeRadio){
            		imageFragment.makeNegative(imageFragment.getOriginalImage());
            	}
            	break;
            case R.id.blurRadio:
            	// Only gaussian blur the image if the gaussian blur radio was not already selected
            	if(designRadioSelected != R.id.blurRadio){
            		/* Set the image to gaussian blur using weak blur by default when the gaussian blur radio is selected after another design
                	 * radio was selected
                	 */
	            	gaussianRadioSelected = R.id.weakGaussianBlurRadio;
	            	imageFragment.gaussianBlur(imageFragment.getOriginalImage(), GaussianBlurStrength.WEAK);
	            	
	            	// Update the gaussian blur style to weak (reset to weak)
	            	newspaperFragment.setGaussianBlurRadio(R.id.weakGaussianBlurRadio);
	            	newspaperFragment.updateGaussianBlurRadio();
            	}
            	break;
            case R.id.updateCaptionBtn:
            	updateImageWithCaption(); // Update the image to have a caption if possible
            	saved = false; // Update saved status
            	break;
            case R.id.removeCaptionBtn:
            	removeImageCaption(); // Remove the caption under the image
            	break;
            case R.id.weakGaussianBlurRadio:
            	// Only update the image if the weak gaussian blur radio was not already selected
            	if(gaussianRadioSelected != R.id.weakGaussianBlurRadio){
            		imageFragment.gaussianBlur(oldBitmaps[0], GaussianBlurStrength.WEAK);
            		gaussianRadioSelected = buttonId;
            	}
            	break;
            case R.id.mediumGaussianBlurRadio:
            	// Only update the image if the medium gaussian blur radio was not already selected
            	if(gaussianRadioSelected != R.id.mediumGaussianBlurRadio){
            		imageFragment.gaussianBlur(oldBitmaps[0], GaussianBlurStrength.MEDIUM);
            		gaussianRadioSelected = buttonId;
            	}
            	break;
            case R.id.strongGaussianBlurRadio:
            	// Only update the image if the strong gaussian blur radio was not already selected
            	if(gaussianRadioSelected != R.id.strongGaussianBlurRadio) {
            		errorDialog = new ErrorDialog(this, R.string.gaussian_blur_latency_title, R.string.gaussian_blur_latency_msg, ErrorDialogType.STRONG_GAUSSIAN_BLUR);
            		errorDialog.show();
            	}
            	break;
            case R.id.updateAngleBtn:
            	// Update the rotation angle of the image and re-halftone it with this new angle
            	imageFragment.setRotationAngle(newspaperFragment.getHalftoneAngle());
            	imageFragment.halftoneImage(oldBitmaps[0], currentPrimitiveType);
            	break;
            default: 
           	 	break;
        }
		
		/* Update the halftone button so on the next button click, if the user clicks a radio that was already selected, 
		 * the actual halftoning is not performed because it would have been performed already
		 */
		if(buttonId == R.id.halftoneDotRadio || buttonId == R.id.halftoneRectangleRadio || buttonId == R.id.halftoneDiamondRadio)
			halftoneRadioSelected = buttonId;
		
		/* Update the design radio when a design radio is selected, so that if the user clicks a radio that was already selected,
		 * the image is not unnecessarily updated
		 */
		if(buttonId == R.id.halftoneRadio || buttonId == R.id.negativeRadio || buttonId == R.id.blurRadio) {
			designRadioSelected = buttonId;
		}
	}
	
	/**
	 * onActivityResult obtains the result from the intent called 
	 * 
	 * If the request code for the intent was to do with loading an image from the gallery, reset the image (load the image into the image view)
	 * Also, handle errors associated with the gallery not being able to obtain the image (bad uri, image corrupted etc)
	 * 
	 * Otherwise, if it was to do with loading the image from the camera, reset the image (load the image into the image view)
	 */
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) { 
        if (resultCode == RESULT_OK) {
    		switch(requestCode) {
    			// If the image was to get the image from the gallery, update image or error
				case LOAD_IMAGE_REQUEST_CODE:
					if(intent != null) 
						resetImage(intent.getData());
					else {
						errorDialog = new ErrorDialog(this, R.string.file_load_error_title, R.string.file_load_error_msg, ErrorDialogType.GENERAL_ERROR);
			    		errorDialog.show();
					}
					break;
				// If the intent was to get the image from the camera, update image
				case CAMERA_REQUEST_CODE:
					resetImage(getImageUri());
					break;
    		}
        }
    }
	
	/**
	 * the resetImage method checks if the selectedImageUri is null or not. If it is not null, the image fragment is updated with the 
	 * selectedImageUri. If oldBitmaps[1] is not null, then it will clear it and set to null (reset the image that was the "old image"
	 * corresponding to the newspaper fragment (the old halftoned image)) to restore on clicking the back button.
	 * 
	 * @param selectedImageUri - the image Uri to resetImage
	 */
	public void resetImage(Uri selectedImageUri) {
		if(selectedImageUri != null) {
			imageFragment.updateImage(selectedImageUri);
			imageFragment.setOriginalImage();
			oldBitmaps[0] = imageFragment.getBitmap();
			imageUploaded = true;
			caption = "";
			halftoneRadioSelected = R.id.halftoneDotRadio; 
			designRadioSelected = R.id.halftoneRadio;
			
			if(oldBitmaps[1] != null){
				oldBitmaps[1].recycle();
				oldBitmaps[1] = null;
			}
		}
	}
}
