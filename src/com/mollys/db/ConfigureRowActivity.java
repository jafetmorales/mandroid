package com.mollys.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class ConfigureRowActivity extends Activity {

	private String m_stDbName;
	private String m_stTbName;
	private int m_id;
	private List<String> m_stFdNames;
	private List<EditText> m_eds;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conf_row);

		Intent in=this.getIntent();
		m_stDbName=in.getStringExtra("dbname");
		m_stTbName=in.getStringExtra("tbname");
		Log.d("mytag", (String) this.m_stTbName);
		m_id=in.getIntExtra("id", 0);

		initLayout();
	}

	protected void initLayout() {
		//LOAD ROW DATA
		DBManager dbm=new DBManager(this.getApplicationContext());
		dbm.setDB(this.m_stDbName);
		dbm.setTable(this.m_stTbName);
		List<HashMap<String,Object>> rows=new ArrayList<HashMap<String,Object>>();
		if(this.m_id>0)
		{
			rows=(List<HashMap<String,Object>>) dbm.execStatement(DBManager.StatementType.SELECT, "id="+this.m_id,null);
		}

		this.m_stFdNames=new ArrayList<String>();
		this.m_eds=new ArrayList<EditText>();

		LinearLayout vlaytop=new LinearLayout(this);
		vlaytop.setOrientation(LinearLayout.VERTICAL);

		LinearLayout vlayrows=new LinearLayout(this);
		vlayrows.setOrientation(LinearLayout.VERTICAL);

		TextView tv;
		EditText ed;
		Table tb=dbm.getFields(this.m_stTbName);
		Iterator<HashMap<String,Object>> it=tb.fields.iterator();
		while(it.hasNext())
		{
			String fName=(String)it.next().get("name");
			this.m_stFdNames.add(fName);
			tv=new TextView(this);
			tv.setText(fName);
			ed=new EditText(this);
			if(this.m_id>0)
			{
				Object fValue=rows.get(0).get(fName);
				if(fValue instanceof java.lang.String)
					ed.setText((String)rows.get(0).get(fName));
				else if(fValue instanceof java.lang.Integer)
					ed.setText(Integer.toString((Integer)rows.get(0).get(fName)));
				else if(fValue instanceof java.lang.Long)
					ed.setText(Long.toString((Long)rows.get(0).get(fName)));
			}
			
			LayoutParams params=new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
			ed.setLayoutParams(params);
			this.m_eds.add(ed);
			vlayrows.addView(tv);
			vlayrows.addView(ed);
		}

		ScrollView scroller=new ScrollView(this);
		LayoutParams params=new LayoutParams(LayoutParams.FILL_PARENT,350);
		scroller.setLayoutParams(params);
		scroller.addView(vlayrows);

		vlaytop.addView(scroller);

		Button but=new Button(this);
		but.setText("Insert Row");
		but.setOnClickListener(new SaveRowListener());
		vlaytop.addView(but);

		setContentView(vlaytop);
	}


	private class SaveRowListener implements OnClickListener {

		public SaveRowListener(){}

		public void onClick(View v)
		{
			DBManager dbm=new DBManager(ConfigureRowActivity.this.getApplicationContext());
			dbm.setDB(ConfigureRowActivity.this.m_stDbName);
			dbm.setTable(ConfigureRowActivity.this.m_stTbName);

			//INSERTION EXAMPLE
			HashMap<String,Object> values=new HashMap<String,Object>();
			values.put(ConfigureRowActivity.this.m_stFdNames.get(1), ConfigureRowActivity.this.m_eds.get(1).getText().toString());		
			Long row=(Long) dbm.execStatement(DBManager.StatementType.INSERT,null,values);

			finish();
		}
	}
}