package com.halftone.yeoldetimes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * This class describes the structure of a special kind of alert dialog, which is set according to its type.
 * If an error dialog is displayed because an image has not been saved or the error dialog is confirming that the user has finished an
 * activity, the user will be presented with an ok and cancel button, allowing them to only go through with the action if they click ok.
 * If an error dialog is of any other type, it will have a single button which dismisses the dialog.
 * 
 * @author Chantel Garcia & Carmen Pui
 */

public class ErrorDialog {
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	
	/**
	 * Create an error dialog pop up on the screen with the content message and button
	 * 
	 * @param context - context for the error dialog
	 * @param title - the title of the dialog
	 * @param message - the main message of the dialog
	 * @param type - type of error/alert
	 */
	public ErrorDialog(final Context context, int title, int message, final ErrorDialogType type) { 
		// Initialise the builder with a title and message
		builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		
		if(type == ErrorDialogType.NOT_SAVED || type == ErrorDialogType.CONFIRM_FINISH)
		{	
			/* Provide a positive button if we have a type of NOT_SAVED or CONFIRM_FINISH (user has to say "YES" I want to accept what 
			 * the dialog is telling me to do)
			 */
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
		// The other type of dialog 
		else
		{
			builder.setCancelable(false);
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int id) { }
			});
		}
		
		// Create the AlertDialog and show it
		dialog = builder.create();
	}
	
	/**
	 * Displays the error dialog
	 */
	public void show() {
		dialog.show();
	}
	
	/**
	 * Determines if the dialog is showing
	 * @return true if the dialog is showing, false otherwise
	 */
	public boolean isShowing() {
		return dialog.isShowing();
	}
	
	/**
	 * Obtains the dialog created using the builder here
	 * @return the dialog created using the builder
	 */
	public AlertDialog getDialog() {
		return dialog;
	}
}
