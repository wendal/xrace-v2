package com.sa.xrace.client.loader;

import java.util.ArrayList;

public class SenceObj {
	private ArrayList<ModelObj> lModelList;
	private String name ;
	public ArrayList<ModelObj> getLModelList() {
		return lModelList;
	}
	public void setLModelList(ArrayList<ModelObj> modelList) {
		lModelList = modelList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
