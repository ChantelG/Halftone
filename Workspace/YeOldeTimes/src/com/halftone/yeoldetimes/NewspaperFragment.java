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
	private int halftoneStyleRadio;
	private int designRadio;
	private int gaussianBlurRadio;
	private ArrayList<RadioButton> radioButtons;

	private LinearLayout halftoneLayout;
	private LinearLayout halftoneAngleLayout;
	private LinearLayout gaussianBlurLayout;
	
	/**
	 * Create the layout for the newspaper fragment of the application with its buttons (standard and radio buttons)
	 * 
	 * @return NewspaperFragmentView - the view of the layout
	 */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		View NewspaperFragmentView = inflater.inflate(R.layout.newspaper_fragment, container, false);
		
		halftoneLayout = (LinearLayout) NewspaperFragmentView.findViewById(R.id.halftoneLayout);
    	halftoneAngleLayout = (LinearLayout) NewspaperFragmentView.findViewById(R.id.halftoneAngleLayout);
    	gaussianBlurLayout = (LinearLayout) NewspaperFragmentView.findViewById(R.id.gaussianBlurLayout);
		
		// Keep record of all of the standard buttons on this screen
		ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add((Button) NewspaperFragmentView.findViewById(R.id.nextSettingsBtn));
        buttons.add((Button) NewspaperFragmentView.findViewById(R.id.updateAngleBtn));
        
        // Keep record of all of the radio buttons on this screen
        radioButtons = new ArrayList<RadioButton>();
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.halftoneDotRadio));
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.halftoneRectangleRadio));
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.halftoneDiamondRadio));
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.halftoneRadio));
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.negativeRadio));
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.blurRadio));
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.weakGaussianBlurRadio));
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.mediumGaussianBlurRadio));
        radioButtons.add((RadioButton) NewspaperFragmentView.findViewById(R.id.strongGaussianBlurRadio));
        
        // For each button, set the on click listener to the onClickListener implemented in this class
        for(Button button: buttons) {
        	button.setOnClickListener(this);
        }
        
        // For each radio button, set the click listener to the onClickListener implemented in this class 
        for(RadioButton radioButton: radioButtons){
        	radioButton.setOnClickListener(this);
        }
        
     /* Determine which button to set to be checked (of the design radios). 
      * This is to restore the screen to its original state on clicking back from the captions screen.
      */
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
        
        // Restore the radio buttons selected to whatever they were set to before the fragment was opened
        updateHalftoneStyleRadio();
        updateGaussianBlurRadio();
	    
        // Initialise the slider value
        this.sliderValue = (TextView) NewspaperFragmentView.findViewById(R.id.halftoneAngleValue);
        this.sliderValue.setText(String.valueOf(halftoneAngle));
        
        this.halftoneAngleSelector = (SeekBar) NewspaperFragmentView.findViewById(R.id.halftoneAngleSelector);
        halftoneAngleSelector.setMax(90);
        halftoneAngleSelector.incrementProgressBy(1);
        halftoneAngleSelector.setProgress(halftoneAngle);
        
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
	 * Update the halftone style radio to have the correct radio selected (this is on launching the newspaper fragment, as there
	 * might be stored data dictating which halftone style radio should be selected).
	 */
	public void updateHalftoneStyleRadio(){
        /* Determine which button to set to be checked (of the halftone style radios). 
         * This is to restore the screen to its original state on clicking back from the captions screen.
         */
        switch(halftoneStyleRadio){
        	case R.id.halftoneDotRadio:
        		radioButtons.get(0).setChecked(true);
        		break;
        	case R.id.halftoneRectangleRadio:
        		radioButtons.get(1).setChecked(true);
        		break;
        	case R.id.halftoneDiamondRadio:
        		radioButtons.get(2).setChecked(true);
        		break;
        	default:
        		break;
        }
	}
	
	/**
	 * Update the gaussian blur  radio to have the correct radio selected (this is on launching the newspaper fragment, as there
	 * might be stored data dictating which gaussian blur radio should be selected).
	 */
	public void updateGaussianBlurRadio() {
		/* Determine which button to set to be checked (of the gaussian blur intensity radios). 
         * This is to restore the screen to its original state on clicking back from the captions screen.
         */
        switch(gaussianBlurRadio){
	    	case R.id.weakGaussianBlurRadio:
	    		radioButtons.get(6).setChecked(true);
	    		break;
	    	case R.id.mediumGaussianBlurRadio:
	    		radioButtons.get(7).setChecked(true);
	    		break;
	    	case R.id.strongGaussianBlurRadio:
	    		radioButtons.get(8).setChecked(true);
	    		break;
	    	default:
	    		break;
	    }
	}

	/**
	 * This method determines what components of the layout to display based on the design radio buttons selected.
	 */
	public void setLayoutsVisible(){
		if(radioButtons.get(3).isChecked()){
			halftoneLayout.setVisibility(View.VISIBLE);
    		halftoneAngleLayout.setVisibility(View.VISIBLE);
    		gaussianBlurLayout.setVisibility(View.GONE);
		}
		else if(radioButtons.get(5).isChecked()){
			gaussianBlurLayout.setVisibility(View.VISIBLE);
			halftoneLayout.setVisibility(View.GONE);
    		halftoneAngleLayout.setVisibility(View.GONE);
		}
		else{
			halftoneLayout.setVisibility(View.GONE);
    		halftoneAngleLayout.setVisibility(View.GONE);
    		gaussianBlurLayout.setVisibility(View.GONE);
		}		
	}
	
	/** 
	 * Update the radio selected based on the index of the radio selected
	 * @param radio - The index of the radio selected
	 */
	public void setHalftoneStyleRadio(int radio) {
		this.halftoneStyleRadio = radio;
	}
	
	/**
	 * Mutator for the designRadio variable, which keeps track of the "design style" for the image (i.e. halftone, difference, gaussian blur)
	 * @param radio - The new radio button value to update the old one to.
	 */
	public void setDesignRadio(int radio) {
		this.designRadio = radio;
	}
	
	/**
	 * Mutator for the designRadio variable, which keeps track of the "gaussian blur intensity" for the image (i.e. weak, medium, strong)
	 * @param radio - The new radio button value to update the old one to.
	 */
	public void setGaussianBlurRadio(int radio){
		this.gaussianBlurRadio = radio;
	}
	
	/**
	 * Sets the halftone angle of the text corresponding to the progress bar
	 */
	public void setHalftoneAngle() {
		this.halftoneAngleSelector.setProgress(this.halftoneAngle);
	}
	
	/**
	 * Set the halftone angle variable to the angle parameter passed in
	 * @param angle - The angle to set the halftone angle to. (This is displayed in the slider's text)
	 */
	public void setHalftoneAngle(int halftoneAngle) {
		this.halftoneAngle = halftoneAngle;
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
