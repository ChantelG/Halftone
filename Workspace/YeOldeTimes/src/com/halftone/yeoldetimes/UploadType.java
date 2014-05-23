package com.halftone.yeoldetimes;

/**
 * This enumeration describes the different types of uploading of images into the application. 
 * It is used in primarily determining whether to display the Get From Gallery Fragment or the Get From Camera Fragment.
 * 
 * @author Chantel Garcia & Carmen Pui
*/

public enum UploadType {
	GALLERY(0),
	CAMERA(1);
	
	/**
	 * Mutator for the uploadType 
	 * @param value - the upload type chosen
	 */
	private final int value;
	
    private UploadType(int value) {
        this.value = value;
    }

    /**
     * Assessor for the uploadType
     * @return the value of the upload type
     */
    public int getValue() {
        return value;
    }
}
