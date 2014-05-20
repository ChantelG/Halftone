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
import android.widget.RadioButton;
import android.widget.TextView;

public class NewspaperFragment extends Fragment implements View.OnClickListener{
	private OnButtonClickedListener mCallback;

	private String caption;
	private int selectedRadio;
	private EditText captionText;
	private TextView sliderValue;
	private ArrayList<RadioButton> radioButtons;

	/**
	 * Create the layout for the newspaper fragment of the application with its buttons (standard and radio buttons)
	 * 
	 * @return NewspaperFragmentView - the view of the layout
	 */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		View NewspaperFragmentView = inflater.inflate(R.layout.newspaper_fragment, container, false);
		
		// Keep record of all of the standard buttons on this screen
		ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add((Button) NewspaperFragmentView.findViewById(R.id.shareScreenBtn));
        buttons.add((Button) NewspaperFragmentView.findViewById(R.id.updateCaptionBtn));
        buttons.add((Button) NewspaperFragmentView.findViewById(R.id.removeCaptionBtn));
        
        // Keep record of all of the radio buttons on this screen
        radioButtons = new ArrayList<RadioButton>();
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.halftoneDotRadio));
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.halftoneSquareRadio));
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.halftoneDiamondRadio));
        
        // For each button, set the on click listener to the onClickListener implemented in this class
        for(Button button: buttons) {
        	button.setOnClickListener(this);
        }
        
        // For each radio button, set the click listener to the onClickListener implemented in this class 
        for(RadioButton radioButton: radioButtons){
        	radioButton.setOnClickListener(this);
        }
        
        // Determine which button to set to be checked
        switch(selectedRadio){
        	case R.id.halftoneDotRadio:
        		radioButtons.get(0).setChecked(true);
        		break;
        	case R.id.halftoneSquareRadio:
        		radioButtons.get(1).setChecked(true);
        		break;
        	case R.id.halftoneDiamondRadio:
        		radioButtons.get(2).setChecked(true);
        		break;
        	default:
        		break;
        }
        
        // Initialise the caption EditText
        this.captionText = (EditText) NewspaperFragmentView.findViewById(R.id.captionTxt);
        this.captionText.setText(this.caption);
        
        // Initialise the slider value
        this.sliderValue = (TextView) NewspaperFragmentView.findViewById(R.id.halftoneAngleValue);
        this.sliderValue.setText("0");
        
		// Inflate the layout for this fragment
        return NewspaperFragmentView;
    }
	
	/**
	 * Returns the caption in the EditText
	 * @return the caption in the edit text as a string
	 */
	public String getCaption() {
		return this.captionText.getText().toString();
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
	 * Update the radio selected based on the index of the radio selected
	 * @param radio - The index of the radio selected
	 */
	public void setSelectedRadio(int radio) {
		this.selectedRadio = radio;
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
