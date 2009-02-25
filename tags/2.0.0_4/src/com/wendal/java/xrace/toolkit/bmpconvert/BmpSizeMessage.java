package com.wendal.java.xrace.toolkit.bmpconvert;

import java.io.Serializable;

public final class BmpSizeMessage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4145794847038361383L;

	private int [] size = new int[2];
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[] getSize() {
		return size;
	}

	public void setSize(int[] size) {
		this.size = size;
	}

	
}
