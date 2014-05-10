package com.halftone.yeoldetimes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ErrorDialog {
	AlertDialog.Builder builder;

	public ErrorDialog(final Context context, int title, int message, final ErrorDialogType type) { 
		// Initialise the builder with a title and message
		builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		
		if(type == ErrorDialogType.NOT_SAVED || type == ErrorDialogType.CONFIRM_FINISH)
		{	
			// Add the buttons
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int id) 
				{	
					CreateNewspaperActivity owner = (context instanceof CreateNewspaperActivity) ? (CreateNewspaperActivity)context : null;
					if(type == ErrorDialogType.NOT_SAVED){
				        if (owner != null) {
				        	owner.saveToGalleryAndAdvance();
				        }
					}
					else if(type == ErrorDialogType.CONFIRM_FINISH){
						if (owner != null) {
				        	owner.finish();
				        }
					}
				}
			});
			builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
			{
				// Do nothing, user has clicked cancel, dialog should only close
				public void onClick(DialogInterface dialog, int id) {}
			});
		}
		else
		{
			builder.setCancelable(false);
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int id) { }
			});
		}
		
		// Create the AlertDialog and show it
		builder.create();
	}
	
	public void show() {
		// Can return stuff
		builder.show();
	}
}
