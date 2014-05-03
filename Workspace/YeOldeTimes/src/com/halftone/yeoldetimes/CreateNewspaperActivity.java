package com.halftone.yeoldetimes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.webkit.MimeTypeMap;

// TODO : Ask: Intent.getData() is returning null. Should I handle this (its only when gallery cant get image). Or not? 
// TODO: Ask: Is it okay if we get the user to save out their image taken with their camera first, then halftone and save again into separate image?

public class CreateNewspaperActivity extends FragmentActivity implements OnButtonClickedListener{
	
	// Declaration of request codes
	final int LOAD_IMAGE_REQUEST_CODE = 1;
	final int CAMERA_REQUEST_CODE = 1337;
	final String CAPTURE_TITLE = "temporaryImage";
	boolean halftoned;
	boolean saved;
	
	UploadType uploadType;
	
	ImageFragment imageFragment;

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
        	
        	// The image is not halftoned yet
        	halftoned = false;
        	saved = false;
        }
        // TODO: Else throw error
    }
	
	public void createImageFragment() {
		imageFragment = new ImageFragment();
		imageFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.image_fragment, imageFragment).commit();
	}
	
	public void openGetFromGalleryFragment() {
		createImageFragment();
		GetFromGalleryFragment uploadImageFragment = new GetFromGalleryFragment();
		uploadImageFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, uploadImageFragment).commit();
	}
	
	public void openGetFromUrlFragment() {
		createImageFragment();
		GetFromUrlFragment getFromUrlFragment = new GetFromUrlFragment();
		getFromUrlFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, getFromUrlFragment).commit();
	}
	
	public void openNewspaperCreator() {
		NewspaperFragment newspaperFragment = new NewspaperFragment();
		newspaperFragment.setArguments(getIntent().getExtras());
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		transaction.replace(R.id.fragment_container, newspaperFragment);
		transaction.addToBackStack(null);
		
		transaction.commit();
	}
	
	public void openGetFromCameraFragment() {
		// TODO
		createImageFragment();
		getFromCameraFragment captureImageFragment = new getFromCameraFragment();
		captureImageFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, captureImageFragment).commit();
	}
	
    public void openShareFragment() {
    	ShareFragment shareFragment = new ShareFragment();
    	shareFragment.setArguments(getIntent().getExtras());
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		transaction.replace(R.id.fragment_container, shareFragment);
		transaction.addToBackStack(null);
		
		transaction.commit();
    }
	
    public void openGallery()
    {
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
    
    public void openCamera()
	{
    	// We use the stock camera app to take a photo
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
	}
    
    public String getCurrentTime()
    {
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
			if (!myFile.exists()) {
				myFile.createNewFile();
			}
			
			return myFile;
    	}catch (Exception e) 
    	{
    		// TODO
    	}
    	
    	return null;
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
    
    public void shareImage()
    {
    	if(!saved) {
    		// TODO display error dialog and force user to save
    		saveToGallery();
    	}

		Intent share = new Intent(Intent.ACTION_SEND); 
		
		MimeTypeMap map = MimeTypeMap.getSingleton(); 
		
		// TODO Test many extensions
		String ext = imageFragment.getFile().getAbsolutePath().substring(imageFragment.getPath().lastIndexOf('.') + 1);
		String mime = map.getMimeTypeFromExtension(ext);
		share.setType(mime);

		share.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(imageFragment.getFile()));
		startActivity(Intent.createChooser(share, "share"));
    }
    
    public void halftoneImage() 
    {
    	if(!halftoned) {
    		halftoned = true;
    	}
    	else {
    		// TODO Give an error
    	}
    }
    
	@Override
	public void onButtonClicked(int buttonId) {
		switch(buttonId)
        {
            case R.id.uploadFromGalleryBtn:
            	openGallery();
           	 	break;
            case R.id.urlBtn:
            	// TODO
                break;
            case R.id.uploadFromCameraBtn:
            	openCamera();
           	 	break;
            case R.id.nextBtn:
            	openNewspaperCreator();
            	break;
            case R.id.shareScreenBtn:
            	openShareFragment();
            	break;
            case R.id.shareBtn:
            	shareImage();
            	break;
            case R.id.halftoneDotRadio:
            	halftoneImage();
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
					if(intent != null) {
						Uri selectedImageUri = intent.getData();
						if(selectedImageUri != null)
							imageFragment.updateImage(selectedImageUri);
					}
					break;
				case CAMERA_REQUEST_CODE:
					Uri selectedImageUri = getImageUri();
					if(selectedImageUri != null)
						imageFragment.updateImage(selectedImageUri);
					break;
    		}
        }
    }
}
