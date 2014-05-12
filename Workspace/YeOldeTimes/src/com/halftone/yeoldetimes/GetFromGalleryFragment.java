package com.halftone.yeoldetimes;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class GetFromGalleryFragment extends Fragment implements View.OnClickListener {
	private OnButtonClickedListener mCallback;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		View GetFromGalleryFragmentView = inflater.inflate(R.layout.get_from_gallery_fragment, container, false);
		
		ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add((Button) GetFromGalleryFragmentView.findViewById(R.id.uploadFromGalleryBtn));
        buttons.add((Button) GetFromGalleryFragmentView.findViewById(R.id.nextBtn));
        
        // For each button, set the on click listener to the onClickListener implemented in this class
        for(Button button: buttons) {
        	button.setOnClickListener(this);
        }
        
		// Inflate the layout for this fragment
        return GetFromGalleryFragmentView;
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnButtonClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnButtonClickedListener");
        }
    }
	
    public void openGallery() {
    	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    	intent.setType("image/*");
    	startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
    }
    
	@Override
	public void onClick(View view) {
		mCallback.onButtonClicked(view.getId());
	}

}
