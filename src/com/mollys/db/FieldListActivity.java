package com.mollys.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class FieldListActivity extends Activity {

	/**
	 * Adapter used to bind an AdapterView to List of Jokes.
	 */
	protected FieldListAdapter m_fieldAdapter;
	protected ListView m_vwFieldLayout;
	protected List<String> m_arrFieldList;	

//	private DBManager m_dbManager;
	private String m_stDbName;
	private String m_stTbName;


	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.field_list);

		Intent in=this.getIntent();
		this.m_stDbName=in.getStringExtra("dbname");
		this.m_stTbName=in.getStringExtra("tbname");
		
		initList();
		
		this.m_vwFieldLayout=(ListView)this.findViewById(R.id.fieldListViewGroup);
		this.m_fieldAdapter=new FieldListAdapter(this.getApplicationContext(),this.m_arrFieldList,this.m_stDbName,this.m_stTbName);
		this.m_vwFieldLayout.setAdapter(this.m_fieldAdapter);

		Button but1 = (Button) findViewById(R.id.addFieldButton);
		but1.setOnClickListener(new AddFieldListener());
	}

	public void onActivityResult (int requestCode, int resultCode, Intent data)
	{
		initList();
		
		this.m_fieldAdapter=new FieldListAdapter(this.getApplicationContext(),this.m_arrFieldList,this.m_stDbName,this.m_stTbName);
		this.m_vwFieldLayout.setAdapter(this.m_fieldAdapter);
		this.m_fieldAdapter.notifyDataSetChanged();		
	}
	
	public void initList()
	{
		DBManager dbm=new DBManager(this.getApplicationContext());
		dbm.setDB(this.m_stDbName);
		Table tb=dbm.getFields(this.m_stTbName);
		
		this.m_arrFieldList=new ArrayList<String>();
		
		Iterator<HashMap<String,Object>> it=tb.fields.iterator();
		while(it.hasNext())
		{
			this.m_arrFieldList.add((String)it.next().get("name"));
		}
	}
	

	private class AddFieldListener implements OnClickListener {

		public AddFieldListener(){}
		
		public void onClick(View v)
		{
			Intent in=new Intent(FieldListActivity.this.getApplicationContext(), ConfigureFieldActivity.class);
			in.putExtra("dbname", FieldListActivity.this.m_stDbName);
			in.putExtra("tbname", FieldListActivity.this.m_stTbName);

			startActivityForResult(in,0);
		}
	}

}