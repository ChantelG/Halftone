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

public class NewspaperFragment extends Fragment implements View.OnClickListener{
	OnButtonClickedListener mCallback;

	String caption;
	int selectedRadio;
	EditText captionText;
	ArrayList<RadioButton> radioButtons;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		View NewspaperFragmentView = inflater.inflate(R.layout.newspaper_fragment, container, false);
		
		ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add((Button) NewspaperFragmentView.findViewById(R.id.shareScreenBtn));
        buttons.add((Button) NewspaperFragmentView.findViewById(R.id.updateCaptionBtn));
        buttons.add((Button) NewspaperFragmentView.findViewById(R.id.removeCaptionBtn));
        
        radioButtons = new ArrayList<RadioButton>();
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.halftoneDotRadio));
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.halftoneSquareRadio));
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.halftoneDiamondRadio));
        
        // For each button, set the on click listener to the onClickListener implemented in this class
        for(Button button: buttons) {
        	button.setOnClickListener(this);
        }
        
        for(RadioButton radioButton: radioButtons){
        	radioButton.setOnClickListener(this);
        }
        
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
        
        this.captionText = (EditText) NewspaperFragmentView.findViewById(R.id.captionTxt);
        this.captionText.setText(this.caption);
        
		// Inflate the layout for this fragment
        return NewspaperFragmentView;
    }
	
	public String getCaption() {
		return this.captionText.getText().toString();
	}
	
	public void setCaption(String text) {
		this.caption = text;
	}
	
	public void updateCaptionText() {
		this.captionText.setText(this.caption);
	}
	
	public void setSelectedRadio(int radio) {
		this.selectedRadio = radio;
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
