package com.halftone.yeoldetimes;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * This class provides functionality for the AddCaptionFragment.
 * Thus it keeps track of the caption to add to a bitmap image, a text field that contains the caption to add and delegates the handling
 * of the button clicking for the fragment to its parent activity
 * 
 * @author Chantel Garcia & Carmen Pui
 */

public class AddCaptionFragment extends Fragment implements View.OnClickListener {
	
	private String caption;
	private EditText captionText;
	private OnButtonClickedListener mCallback;
	
	/**
	 * Create the layout for the newspaper fragment of the application with its buttons (standard and radio buttons)
	 * 
	 * @return NewspaperFragmentView - the view of the layout
	 */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		View HalftoneAngleFragmentView = inflater.inflate(R.layout.add_caption_fragment, container, false);
		
		// Keep record of all of the standard buttons on this screen
		ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add((Button) HalftoneAngleFragmentView.findViewById(R.id.shareScreenBtn));
        buttons.add((Button) HalftoneAngleFragmentView.findViewById(R.id.updateCaptionBtn));
        buttons.add((Button) HalftoneAngleFragmentView.findViewById(R.id.removeCaptionBtn));
        
        // For each button, set the on click listener to the onClickListener implemented in this class
        for(Button button: buttons) {
        	button.setOnClickListener(this);
        }
        
        // Initialise the caption EditText
        this.captionText = (EditText) HalftoneAngleFragmentView.findViewById(R.id.captionTxt);
        this.captionText.setText(this.caption);
        
		// Inflate the layout for this fragment
        return HalftoneAngleFragmentView;
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
	 * Sets the caption string for storage
	 * @param text - the text to set the caption to
	 */
	public void setCaption(String text) {
		this.caption = text;
	}
	
	/**
	 * Update the edit text's caption to the caption stored as a variable in this class
	 */
	public void updateCaptionText() {
		this.captionText.setText(this.caption);
	}
	
	/**
	 * Returns the caption in the EditText
	 * @return the caption in the edit text as a string
	 */
	public String getCaption() {
		return this.captionText.getText().toString();
	}
	
	/**
	 * Tell the callback activity to register the click on this fragment
	 */
	@Override
	public void onClick(View view) {
		mCallback.onButtonClicked(view.getId());
	}

}
