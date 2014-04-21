package com.halftone.yeoldetimes;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Place all of the buttons on the screen into easy access storage (ArrayList)
        ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add((Button)findViewById(R.id.galleryBtn));
        buttons.add((Button)findViewById(R.id.urlBtn));
        
        // For each button, set the on click listener to the onClickListener implemented in this class
        for(Button button: buttons)
        {
        	button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) 
    {
         switch(view.getId())
         {
             case R.id.galleryBtn:
            	 openGetFromGalleryActivity();
            	 break;
             case R.id.urlBtn:
            	 //openGetFromUrlActivity();
                 break;
             default: 
            	 break;
          }
    }

    public void openGetFromGalleryActivity() {
    	Intent intent = new Intent(this, UploadImageActivity.class);
    	startActivity(intent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
