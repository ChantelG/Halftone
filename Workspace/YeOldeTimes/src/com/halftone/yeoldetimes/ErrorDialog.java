package com.halftone.yeoldetimes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class ErrorDialog {
	AlertDialog.Builder builder;

	public ErrorDialog(final Context context, int title, int message, final ErrorDialogType type) { 
		// Initialise the builder with a title and message
		builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		
		if(type == ErrorDialogType.NOT_SAVED)
		{	
			// Add the buttons
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int id) 
				{		
					Intent saveIntent = new Intent("SAVE_IMAGE");
					context.sendBroadcast(saveIntent);
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
				public void onClick(DialogInterface dialog, int id) 
				{		
					//
				}
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
