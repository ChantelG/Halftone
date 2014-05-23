package com.halftone.yeoldetimes;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * This class provides all of the functionality for the Share Fragment.
 * The Share Fragment is the last fragment before the user is prompted to return to the home screen. It is opened on click of the next 
 * button on the Add Caption Fragment screen.
 * It delegates the behaviour of the button clicks in the fragment to its parent activity.
 * 
 * @author Chantel Garcia & Carmen Pui
 */

public class ShareFragment extends Fragment implements View.OnClickListener{
	private OnButtonClickedListener mCallback;
	
	/**
	 * Create the the share image fragment with its buttons
	 * 
	 * @return ShareFragmentView - the view of the layout
	 */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		View ShareFragmentView = inflater.inflate(R.layout.share_fragment, container, false);
		
		ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add((Button) ShareFragmentView.findViewById(R.id.shareBtn));
        buttons.add((Button) ShareFragmentView.findViewById(R.id.finishBtn));
        
        // For each button, set the on click listener to the onClickListener implemented in this class
        for(Button button: buttons) {
        	button.setOnClickListener(this);
        }
        
		// Inflate the layout for this fragment
        return ShareFragmentView;
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
