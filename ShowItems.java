//TODO:
//NEED TO PULL THE ITEMS FROM DATABASE INSTEAD OF CREATING THEM IN THIS ACTIVITY
//NEED TO MODIFY ItemView SO THAT IT CREATES THE VIEWS DYNAMICALLY DEPENDING ON THE DATATYPE

package com.mollys.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ShowItems extends Activity {

	private ListView lv1;
	private DBManager dbm;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list);

		//INITIALIZE THE DESCRIPTION OF TWO ITEMS
		HashMap<String,Object> hmDesc1=new HashMap<String,Object>();
		hmDesc1.put("type", new String("TextView"));
		hmDesc1.put("text", new String("I am Item 1"));
		hmDesc1.put("color", (int)Color.GREEN);
		HashMap<String,Object> hmDesc2=new HashMap<String,Object>();
		hmDesc2.put("type", new String("EditText"));
		hmDesc2.put("text", new String("I am Item 2"));
		//CREATE THE ITEM ITSELF
		Item it1=new Item(hmDesc1);
		Item it2=new Item(hmDesc2);
		//ADD THIS ITEM TO A LIST OF ITEMS
		List<Item> lsItems=new ArrayList<Item>();
		lsItems.add(it1);
		lsItems.add(it2);
		//INITIALIZE THE LISTVIEW
		initListDisplay(lsItems);
	}

	private void initListDisplay(List<Item> lsItemsIn)
	{

		lv1=(ListView)findViewById(R.id.ListView01);
		lv1.setAdapter(new MyListAdapter(this,lsItemsIn));		
	}

	private class MyListAdapter extends BaseAdapter
	{
		private Context mContext;
		private List<Item> lsItems;

		public MyListAdapter(Context context, List<Item> lsItemsIn) {
			mContext = context;
			lsItems=lsItemsIn;
		}
		public int getCount() {
			return lsItems.size();
		}

		public Item getItem(int position) {
			return lsItems.get(position);
		}
		public long getItemId(int position) {
			return position;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			ItemView iv;
			//			if (convertView == null) {
			iv = new ItemView(mContext,lsItems.get(position));//(String)lsItems.get(position).get("name"));
			return iv;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		//		initList();
	}

}
