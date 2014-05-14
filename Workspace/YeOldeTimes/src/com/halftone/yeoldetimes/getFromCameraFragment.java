package com.halftone.yeoldetimes;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class GetFromCameraFragment extends Fragment implements View.OnClickListener{
	private OnButtonClickedListener mCallback;
	
	/**
	 * Create the layout of the camera fragment with buttons
	 * 
	 * @return GetFromCameraFragmentView - the View of the fragment
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View GetFromCameraFragmentView = inflater.inflate(R.layout.get_from_camera_fragment, container, false);
		
		List<Button> buttons = new ArrayList<Button>();
		buttons.add((Button) GetFromCameraFragmentView.findViewById(R.id.uploadFromCameraBtn));
		buttons.add((Button) GetFromCameraFragmentView.findViewById(R.id.nextBtn));
		
		// For each button, set the on click listener to the onClickListener implemented in this class
		for(Button button: buttons) {
			button.setOnClickListener(this);
		}
		
		// Inflate the layout for the fragment
		return GetFromCameraFragmentView;
	}
	
	/**
	 * On attach of the activity, make the activity a callback for this activity (hears this activities button clicks)
	 */
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

	/**
	 * Tell the callback activity to register the click on this fragment
	 */
	@Override
	public void onClick(View view) {
		mCallback.onButtonClicked(view.getId());	
	}
}
