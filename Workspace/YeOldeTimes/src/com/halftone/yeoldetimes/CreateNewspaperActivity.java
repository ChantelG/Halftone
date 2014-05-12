package com.halftone.yeoldetimes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.webkit.MimeTypeMap;

// TODO : Ask: Intent.getData() is returning null. Should I handle this (its only when gallery can't get image). Or not? 
// TODO : Sort out length of text being too long
// TODO : Sort out put popup if error occurs (In the TODO s where have exception)
// TODO : A little bit of padding on the left for the caption? 

public class CreateNewspaperActivity extends FragmentActivity implements OnButtonClickedListener{
	
	// Declaration of request codes
	private final int LOAD_IMAGE_REQUEST_CODE = 1;
	private final int CAMERA_REQUEST_CODE = 1337;
	private final String CAPTURE_TITLE = "temporaryImage";
	private Bitmap[] oldBitmaps = new Bitmap[3];
	private String caption;
	private int radioSelected;
	private boolean imageUploaded;
	private boolean saved;
	
	private UploadType uploadType;
	
	private ImageFragment imageFragment;
	private NewspaperFragment newspaperFragment;
	
	private ErrorDialog errorDialog;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_newspaper_activity);
        
        Bundle b = getIntent().getExtras();
        if(b != null){
        	int value = b.getInt("uploadType");
        	
        	switch(value) {
	        	case 0:
	        		uploadType = UploadType.GALLERY;
	        		break;
	        	case 1: 
	        		uploadType = UploadType.CAMERA;
	        		break;
	        	case 2: 
	        		uploadType = UploadType.URL;
	        		break;
        	}
        	
        	switch(uploadType) {
        	case GALLERY:
        		openGetFromGalleryFragment();
        		break;
        	case CAMERA:
        		openGetFromCameraFragment();
        		break;
        	case URL:
        		openGetFromUrlFragment();
        		break;
        	}
        	
        	imageUploaded = false;
        	saved = false;
        	caption = "";
        	radioSelected = R.id.halftoneDotRadio;
        }
        // TODO: Else throw error
    }
	
	public void createImageFragment() {
		imageFragment = new ImageFragment();
		imageFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.image_fragment, imageFragment, "Image Fragment").commit();
	}
	
	public void openGetFromGalleryFragment() {
		createImageFragment();
		GetFromGalleryFragment uploadImageFragment = new GetFromGalleryFragment();
		uploadImageFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, uploadImageFragment, "Upload Image Fragment").commit();
	}
	
	public void openGetFromUrlFragment() {
		createImageFragment();
		GetFromUrlFragment getFromUrlFragment = new GetFromUrlFragment();
		getFromUrlFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, getFromUrlFragment).commit();
	}
	
	public void openNewspaperCreator() {
		newspaperFragment = new NewspaperFragment();
		newspaperFragment.setCaption(caption);
		newspaperFragment.setSelectedRadio(radioSelected);
		
		newspaperFragment.setArguments(getIntent().getExtras());
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		transaction.replace(R.id.fragment_container, newspaperFragment, "Newspaper Fragment");
		transaction.addToBackStack(null);
		
		transaction.commit();
		
		if(oldBitmaps[1] != null)
			imageFragment.updateImage(oldBitmaps[1]);
		else
			imageFragment.halftoneImage(imageFragment.getOriginalImage(), PrimitiveType.CIRCLE);
	}

	public void openGetFromCameraFragment() {
		createImageFragment();
		GetFromCameraFragment captureImageFragment = new GetFromCameraFragment();
		captureImageFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, captureImageFragment, "Capture Image Fragment").commit();
	}
	
    public void openShareFragment() {
    	if(!saved) {
    		errorDialog = new ErrorDialog(this, R.string.image_not_saved_title, R.string.image_not_saved_msg, ErrorDialogType.NOT_SAVED);
    		errorDialog.show();
    	}
    	else {
    		advanceToShare();
    	}
    }
	
    public void openGallery() {
    	// Create a new intent with the image type and action
    	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    	intent.setType("image/*");
    	startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
    }
    
    private Uri getImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM", CAPTURE_TITLE);
        Uri imgUri = Uri.fromFile(file);
        return imgUri;
    }
    
    public void openCamera() {
    	// We use the stock camera app to take a photo
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
	}
    
    public String getCurrentTime() {
        	// Make a name for the file by getting the current date from the phone and formatting it
    		Calendar calendar = Calendar.getInstance();
    		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSSz");
    		String date = dateFormat.format(calendar.getTime());
    		date = date.replace(":", "-");
    		date = date.replace(".", "-");
    		date = date.replace(" ", "-");
    		return date;
    }
    
    public String getNewFilePath() {
    	String fileName = getCurrentTime();
		File extStore = Environment.getExternalStorageDirectory();
		String filePath = extStore.getAbsolutePath() + "/" + fileName + ".jpg";
		return filePath;
    }
    
    public File getNewFile(String filePath) {
    	try {
			// Create a new file with the current name
			File myFile = new File(filePath);
			
			// If file doesn't exist, then create it
			if (!myFile.exists()) 
				myFile.createNewFile();
			
			return myFile;
    	} catch (Exception e) {
    		// TODO
    	}
    	
    	return null;
    }
    
    public ErrorDialog getErrorDialog() {
    	return this.errorDialog;
    }
    
    public void saveToGalleryAndAdvance() {
    	saveToGallery();
    	advanceToShare();
    }
    
    public void advanceToShare() {
    	ShareFragment shareFragment = new ShareFragment();
    	shareFragment.setArguments(getIntent().getExtras());
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		transaction.replace(R.id.fragment_container, shareFragment, "Share Fragment");
		transaction.addToBackStack(null);
		
		transaction.commit();
	
		oldBitmaps[1] = imageFragment.getBitmap();
    }
    
    public void setOldBitmaps(Bitmap bitmap, int index) {
    	oldBitmaps[index] = bitmap;
    }
    
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
			// If there is an exception, print it out to the logCat
			e.printStackTrace();
		}
    }
    
    public void saveToGallery(File file, String path)
    {
    	Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
     }
    
    public void shareImage() {
		Intent share = new Intent(Intent.ACTION_SEND); 
		MimeTypeMap map = MimeTypeMap.getSingleton(); 

		String ext = imageFragment.getFile().getAbsolutePath().substring(imageFragment.getPath().lastIndexOf('.') + 1);
		String mime = map.getMimeTypeFromExtension(ext);
		share.setType(mime);

		share.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(imageFragment.getFile()));
		startActivity(Intent.createChooser(share, "share"));
    }
    
    public void updateImageWithCaption() {
    	Paint paint = new Paint();
    	
    	caption = newspaperFragment.getCaption();
    	if(caption.compareTo("") == 0) {
    		errorDialog = new ErrorDialog(this, R.string.caption_empty_title, R.string.caption_empty_msg, ErrorDialogType.NOT_EDITED);
    		errorDialog.show();
    	}
    	else if (paint.measureText(caption) > imageFragment.getBitmap().getWidth()) {
    		errorDialog = new ErrorDialog(this, R.string.caption_too_large_title, R.string.caption_too_large_msg, ErrorDialogType.NOT_EDITED);
    		errorDialog.show();
    	}
    	else 
    		imageFragment.updateImageCaption(caption);
    }
    
    public void removeImageCaption() {
    	if(caption.compareTo("") == 0){
    		errorDialog = new ErrorDialog(this, R.string.caption_not_exist_title, R.string.caption_not_exist_msg, ErrorDialogType.NOT_EDITED);
    		errorDialog.show();
    	}
    	else {
    		imageFragment.removeCaption();
    		caption = "";
    		newspaperFragment.setCaption(caption);
    		newspaperFragment.updateCaptionText();
    	}
    }
    
    @Override
    public void onBackPressed(){
    	ShareFragment shareFragment = (ShareFragment)getSupportFragmentManager().findFragmentByTag("Share Fragment");
    	if (shareFragment != null && shareFragment.isVisible()){
    		performBackPressed(1);
    		return;
    	}
    	
    	NewspaperFragment newspaperFragment = (NewspaperFragment)getSupportFragmentManager().findFragmentByTag("Newspaper Fragment");
    	if (newspaperFragment != null && newspaperFragment.isVisible()){
    		oldBitmaps[1] = imageFragment.getBitmap();
    		performBackPressed(0);
    		return;
    	}
    	
    	super.onBackPressed();
    }
    
    public ImageFragment getImageFragment() {
    	return this.imageFragment;
    }
    
    public NewspaperFragment getNewspaperFragment() {
    	return this.newspaperFragment;
    }
    
    public void performBackPressed(int currImage) {
    	imageFragment.updateImage(oldBitmaps[currImage]);
    	super.onBackPressed();
    }
    
    public void showFinishDialog() {
    	errorDialog = new ErrorDialog(this, R.string.finish_confirmation_title, R.string.finish_confirmation_msg, ErrorDialogType.CONFIRM_FINISH);
    	errorDialog.show();
    }
    
	@Override
	public void onButtonClicked(int buttonId) {
		switch(buttonId)
        {
            case R.id.uploadFromGalleryBtn:
            	openGallery();
           	 	break;
            case R.id.uploadFromCameraBtn:
            	openCamera();
           	 	break;
            case R.id.nextBtn:
            	if(imageUploaded)
            		openNewspaperCreator();
            	else {
                	errorDialog = new ErrorDialog(this, R.string.image_not_uploaded_title, R.string.image_not_uploaded_msg, ErrorDialogType.NO_IMAGE);
            		errorDialog.show();
            	}
            	break;
            case R.id.shareScreenBtn:
            	openShareFragment();
            	break;
            case R.id.shareBtn:
            	shareImage();
            	break;
            case R.id.finishBtn:
            	showFinishDialog();
            	break;
            case R.id.halftoneDotRadio:
            	imageFragment.halftoneImage(oldBitmaps[0], PrimitiveType.CIRCLE);
            	radioSelected = R.id.halftoneDotRadio;
            	saved = false;
            	break;
            case R.id.halftoneSquareRadio:
            	imageFragment.halftoneImage(oldBitmaps[0], PrimitiveType.SQUARE);
            	radioSelected = R.id.halftoneSquareRadio;
            	saved = false;
            	break;
            case R.id.halftoneDiamondRadio:
            	imageFragment.halftoneImage(oldBitmaps[0], PrimitiveType.DIAMOND);
            	radioSelected = R.id.halftoneDiamondRadio;
            	saved = false;
            	break;
            case R.id.updateCaptionBtn:
            	updateImageWithCaption();
            	saved = false;
            	break;
            case R.id.removeCaptionBtn:
            	removeImageCaption();
            	saved = false;
            	break;
            default: 
           	 	break;
         }
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) { 
        if (resultCode == RESULT_OK) {
    		switch(requestCode) {
				case LOAD_IMAGE_REQUEST_CODE:
					if(intent != null) 
						resetImage(intent.getData());
					// TODO else put error
					break;
				case CAMERA_REQUEST_CODE:
					resetImage(getImageUri());
					break;
    		}
        }
    }
	
	public void resetImage(Uri selectedImageUri) {
		if(selectedImageUri != null) {
			imageFragment.updateImage(selectedImageUri);
			imageFragment.setOriginalImage();
			oldBitmaps[0] = imageFragment.getBitmap();
			imageUploaded = true;
			caption = "";
			radioSelected = R.id.halftoneDotRadio; 
			
			if(oldBitmaps[1] != null){
				oldBitmaps[1].recycle();
				oldBitmaps[1] = null;
			}
		}
	}
}
