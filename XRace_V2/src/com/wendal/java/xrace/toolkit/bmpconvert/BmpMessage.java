package com.wendal.java.xrace.toolkit.bmpconvert;
import java.io.Serializable;


public class BmpMessage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7768821690141508951L;

	private String filename;
	
	private byte data [];
	
	public BmpMessage() {
		
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	
}
