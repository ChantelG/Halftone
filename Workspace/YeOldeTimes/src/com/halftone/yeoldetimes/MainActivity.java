package com.halftone.yeoldetimes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity implements OnButtonClickedListener{
	
	/**
	 * Create the main activity, with the home fragment
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /* Create an instance of homeFragment, pass Intent's extras to the fragment as args, then 
         * add the fragment to the fragment_container fragment
         */
        HomePageFragment homeFragment = new HomePageFragment();
        homeFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, homeFragment).commit();
    }
    
    /**
     * Create and launch the Intent to open the CreateNewspapertActivty (start the CreateNewspaperActivity)
     * 
     * @param type - upload type to determine which screen to display after the home fragment (i.e. the get from gallery fragment or the
     * get from camera fragment)
     */
    private void openUploadImageActivity(int type) {
		Intent intent = new Intent(this, CreateNewspaperActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("uploadType", type);
		intent.putExtras(bundle); 
		startActivity(intent);
    }

    /**
     * onButtonClicked determines what operation to perform based on the button clicked
     */
	@Override
	public void onButtonClicked(int buttonId) {
        switch(buttonId)
        {
            case R.id.galleryBtn:
            	openUploadImageActivity(UploadType.GALLERY.getValue()); // Open the gallery fragment
           	 	break;
            case R.id.cameraBtn:
            	openUploadImageActivity(UploadType.CAMERA.getValue());
            	break;
            default: 
           	 break;
         }
	}
}
