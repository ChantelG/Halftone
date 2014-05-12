package com.halftone.yeoldetimes;

public enum UploadType {
	GALLERY(0),
	CAMERA(1),
	URL(2);
	
	private final int value;
	
    private UploadType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
