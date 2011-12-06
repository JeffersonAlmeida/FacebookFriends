package com.facebook.cin.exercicio;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowJason {

	private static ShowJason jason;
	
	private ShowJason(){
		super();
	}
	
	public static synchronized ShowJason getInstance(){
		if(jason==null){
			jason = new ShowJason();
		}
		return jason;
	}
	
	public void printjson(JSONArray jsonArray) throws JSONException{
		System.out.println("\n\n#Array JSON#\n\n");
		int i = 0 ; 
		while(i<jsonArray.length()){
			 JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			 System.out.println(i+ " - " + jsonObject.toString());
			 i++;
		 }
	}
	
	
	public static void main(String[] args) {

	}

}
