package com.halftone.yeoldetimes;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class NewspaperFragment extends Fragment implements View.OnClickListener{
	OnButtonClickedListener mCallback;

	EditText captionText;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		View NewspaperFragmentView = inflater.inflate(R.layout.newspaper_fragment, container, false);
		
		ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add((Button) NewspaperFragmentView.findViewById(R.id.shareScreenBtn));
        buttons.add((Button) NewspaperFragmentView.findViewById(R.id.updateCaptionBtn));
        
        // For each button, set the on click listener to the onClickListener implemented in this class
        for(Button button: buttons) {
        	button.setOnClickListener(this);
        }
        
        captionText = (EditText) NewspaperFragmentView.findViewById(R.id.captionTxt);
        
		// Inflate the layout for this fragment
        return NewspaperFragmentView;
    }
	
	public String getCaption() {
		return captionText.getText().toString();
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
