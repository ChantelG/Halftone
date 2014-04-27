package com.halftone.yeoldetimes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class CreateNewspaperActivity extends FragmentActivity implements OnButtonClickedListener{
	
	// Declaration of request codes
	final int LOAD_IMAGE_REQUEST_CODE = 1;
	final int CAMERA_REQUEST_CODE = 1337;
	
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
	}
	
    public void openGallery()
    {
    	// Create a new intent with the image type and action
    	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    	intent.setType("image/*");
    	startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
    }
    
	@Override
	public void onButtonClicked(int buttonId) {
		switch(buttonId)
        {
            case R.id.uploadFromGalleryBtn:
            	openGallery();
           	 	break;
            case R.id.urlBtn:
            	
                break;
            case R.id.nextBtn:
            	openNewspaperCreator();
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
					// Tell the image fragment to update its image
					Uri selectedImageUri = intent.getData();
					imageFragment.updateImage(selectedImageUri);
					break;
				case CAMERA_REQUEST_CODE:
					break;
    		}
        }
    }
}
