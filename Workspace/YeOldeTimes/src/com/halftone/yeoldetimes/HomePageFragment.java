package com.halftone.yeoldetimes;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomePageFragment extends Fragment implements View.OnClickListener{
	OnButtonClickedListener mCallback;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View HomePageFragmentView = inflater.inflate(R.layout.home_page_fragment, container, false);
        
		ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add((Button) HomePageFragmentView.findViewById(R.id.galleryBtn));
        // TODO add Camera btn
        buttons.add((Button) HomePageFragmentView.findViewById(R.id.cameraBtn));
        buttons.add((Button) HomePageFragmentView.findViewById(R.id.urlBtn));
        
        // For each button, set the on click listener to the onClickListener implemented in this class
        for(Button button: buttons)
        {
        	button.setOnClickListener(this);
        }
		
        // Inflate the layout for this fragment
        return HomePageFragmentView;
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnButtonClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnButtonClickedListener");
        }
    }
	
    @Override
    public void onClick(View view) 
    {
    	mCallback.onButtonClicked(view.getId());
    }
}
