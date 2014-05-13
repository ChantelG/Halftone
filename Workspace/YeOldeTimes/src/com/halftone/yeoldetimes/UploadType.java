package com.halftone.yeoldetimes;

public enum UploadType {
	GALLERY(0),
	CAMERA(1),
	URL(2);
	
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
