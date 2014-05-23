package com.halftone.yeoldetimes;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * This class provides all of the functionality for the Newspaper Fragment.
 * The Newspaper Fragment displays after the Get From Gallery Fragment or Get From Camera Fragment.
 * It is a relatively simple class that delegates the fragment's button clicks to its parent activity.
 * It also keeps track of a series of radio buttons corresponding to the options to halftone, create a "differenced" image and 
 * create a gaussian blurred image. It also keeps track of radios corresponding to the halftone type (dot, rectangle, diamond).
 * Finally, it does a bit of showing and hiding of components based on the "design type" radio button clicked (for example, if we are
 * creating a "differenced" image, then we don't need access to any of the halftoning controls so they are removed.
 * 
 * @author Chantel Garcia & Carmen Pui
 */

public class NewspaperFragment extends Fragment implements View.OnClickListener{
	private OnButtonClickedListener mCallback;

	private TextView sliderValue;	
	private SeekBar halftoneAngleSelector;
	private int halftoneAngle;
	private int selectedRadio;
	private int designRadio;
	private ArrayList<RadioButton> radioButtons;

	private LinearLayout halftoneLayout;
	private LinearLayout halftoneAngleLayout;
	
	/**
	 * Create the layout for the newspaper fragment of the application with its buttons (standard and radio buttons)
	 * 
	 * @return NewspaperFragmentView - the view of the layout
	 */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		View NewspaperFragmentView = inflater.inflate(R.layout.newspaper_fragment, container, false);
		
		halftoneAngle = 0;
		
		halftoneLayout = (LinearLayout) NewspaperFragmentView.findViewById(R.id.halftoneLayout);
    	halftoneAngleLayout = (LinearLayout) NewspaperFragmentView.findViewById(R.id.halftoneAngleLayout);
		
		// Keep record of all of the standard buttons on this screen
		ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add((Button) NewspaperFragmentView.findViewById(R.id.nextSettingsBtn));
        buttons.add((Button) NewspaperFragmentView.findViewById(R.id.updateAngleBtn));
        
        // Keep record of all of the radio buttons on this screen
        radioButtons = new ArrayList<RadioButton>();
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.halftoneDotRadio));
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.halftoneSquareRadio));
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.halftoneDiamondRadio));
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.halftoneRadio));
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.negativeRadio));
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.blurRadio));
        
        // For each button, set the on click listener to the onClickListener implemented in this class
        for(Button button: buttons) {
        	button.setOnClickListener(this);
        }
        
        // For each radio button, set the click listener to the onClickListener implemented in this class 
        for(RadioButton radioButton: radioButtons){
        	radioButton.setOnClickListener(this);
        }
        
     // Determine which button to set to be checked
        switch(designRadio){
        	case R.id.halftoneRadio:
        		radioButtons.get(3).setChecked(true);
        		break;
        	case R.id.negativeRadio:
        		radioButtons.get(4).setChecked(true);
        		break;
        	case R.id.blurRadio:
        		radioButtons.get(5).setChecked(true);
        		break;
        	default:
        		break;
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
        
        // Initialise the slider value
        this.sliderValue = (TextView) NewspaperFragmentView.findViewById(R.id.halftoneAngleValue);
        this.sliderValue.setText("0");
        
        this.halftoneAngleSelector = (SeekBar) NewspaperFragmentView.findViewById(R.id.halftoneAngleSelector);
        halftoneAngleSelector.setMax(90);
        halftoneAngleSelector.incrementProgressBy(1);
        halftoneAngleSelector.setProgress(0);
        
        /* Keep track of when the "angle" bar value is changed. When it is changed, update the halftone angle so that when the "update angle"
         * button is clicked, the angle of the halftone grid is updated.
         */
        halftoneAngleSelector.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) { }

            public void onStartTrackingTouch(SeekBar seekBar) { }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            	halftoneAngle = progress;
            	sliderValue.setText(String.valueOf(halftoneAngle));
            }
        });
        
		// Inflate the layout for this fragment
        return NewspaperFragmentView;
    }

	/**
	 * This method determines what components of the layout to display based on the design radio buttons selected.
	 */
	public void setLayoutsVisible(){
		if(radioButtons.get(3).isChecked()){
			halftoneLayout.setVisibility(View.VISIBLE);
    		halftoneAngleLayout.setVisibility(View.VISIBLE);
		}
		else{
			halftoneLayout.setVisibility(View.GONE);
    		halftoneAngleLayout.setVisibility(View.GONE);
		}		
	}
	
	/** 
	 * Update the radio selected based on the index of the radio selected
	 * @param radio - The index of the radio selected
	 */
	public void setSelectedRadio(int radio) {
		this.selectedRadio = radio;
	}
	
	/**
	 * Mutator for the designRadio variable, which keeps track of the "design style" for the image (i.e. halftone, difference, gaussian blur)
	 * @param radio - The new radio button value to update the old one to.
	 */
	public void setDesignRadio(int radio) {
		this.designRadio = radio;
	}
	
	/**
	 * Sets the halftone angle of the text corresponding to the progress bar
	 */
	public void setHalftoneAngle() {
		this.halftoneAngleSelector.setProgress(this.halftoneAngle);
	}
	
	/**
	 * Resets the halftone angle selector (seek bar) to 0
	 */
	public void resetHalftoneAngle() {
		this.halftoneAngleSelector.setProgress(0);
	}
	
	/**
	 * Accessor for the halftone angle
	 * @return - The Halftone angle
	 */
	public int getHalftoneAngle() {
		return this.halftoneAngle;
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
	 * When we resume the screen, determine which layouts to show
	 */
	@Override
	  public void onResume(){
	    super.onResume();
	    setLayoutsVisible();
	 }

	/**
	 * Tell the callback activity to register the click on this fragment
	 */
	@Override
	public void onClick(View view) {
		mCallback.onButtonClicked(view.getId());
	}

}
