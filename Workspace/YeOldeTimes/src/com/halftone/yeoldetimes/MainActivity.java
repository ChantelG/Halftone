package com.halftone.yeoldetimes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

// XX ASK: I have min sdk as honeycomb. Is this okay?
// XX ASK: Layout only fits on nexus 7. Okay?

public class MainActivity extends FragmentActivity implements OnButtonClickedListener{
	
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
    
    public void openUploadImageActivity(int type) {
		Intent intent = new Intent(this, CreateNewspaperActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("uploadType", type);
		intent.putExtras(bundle); 
		startActivity(intent);
    }

	@Override
	public void onButtonClicked(int buttonId) {
        switch(buttonId)
        {
            case R.id.galleryBtn:
            	openUploadImageActivity(UploadType.GALLERY.getValue());
           	 break;
            case R.id.urlBtn:
            	openUploadImageActivity(UploadType.URL.getValue());
                break;
                // TODO Camera Btn
            default: 
           	 break;
         }
	}
}
