package com.halftone.yeoldetimes;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ShareFragment extends Fragment implements View.OnClickListener{
	OnButtonClickedListener mCallback;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		View GetFromGalleryFragmentView = inflater.inflate(R.layout.share_fragment, container, false);
		
		ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add((Button) GetFromGalleryFragmentView.findViewById(R.id.shareBtn));
        
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

	@Override
	public void onClick(View view) {
		mCallback.onButtonClicked(view.getId());
	}

}
