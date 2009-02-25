package com.wendal.java.xrace.toolkit.bmpconvert;

import java.util.HashMap;
import java.util.Set;

import android.util.Log;

public final class ObjectNumber {
	
	private static final HashMap<String, Integer> map = new HashMap<String, Integer>();
	
	public static void regNew(Object object){
		String classname  =object.getClass().getSimpleName();
		Integer integer = map.get(classname);
		int num = 0;
		if(integer != null){
			num = integer;
		}
		num++;
		map.put(classname, new Integer(num));
	}

	public static void getResult(){
		Set<String> set = map.keySet();
		for (String string : set) {
			Log.e("Number of Object",string + " " + map.get(string));
		}
	}
}
