package com.mollys.db;

import java.util.HashMap;

import android.graphics.Color;

//GENERIC ITEM
//YOU DEFINE ATTRIBUTES ABOUT THE ITEM AND ITS SUBITEMS HERE
//MODULES TO BE CALLED ARE ALSO INDICATED HERE
public class Item{
	//HASHMAP DESCRIPTION OF THE OBJECT
	private HashMap<String,Object> hmDesc;

	public Item(HashMap<String,Object> hmDescIn){
		this.hmDesc=hmDescIn;
	}
	
	public String getType()
	{
		return (String)this.hmDesc.get("type");
	}
	
	public int getColor()
	{
		return (Integer)this.hmDesc.get("color");
	}
	
	public String getText()
	{
		return (String)this.hmDesc.get("text");
	}
}