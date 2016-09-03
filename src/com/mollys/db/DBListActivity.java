package com.mollys.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class DBListActivity extends Activity {

	/**
	 * Adapter used to bind an AdapterView to List of Jokes.
	 */
	protected DBListAdapter m_dbAdapter;
	protected ListView m_vwDBLayout;
	protected List<String> m_arrDBList;

	private DBManager m_dbManager;


	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.db_list);
		
//		this.m_dbManager=new DBManager(this.getApplicationContext());
//		this.m_dbManager.removeAllDBs();
//		this.m_dbManager.addDB("xmldb");

		
		initDBList();
		
		this.m_vwDBLayout=(ListView)this.findViewById(R.id.dbListViewGroup);
		this.m_dbAdapter=new DBListAdapter(this.getApplicationContext(),this.m_arrDBList);
		Toast.makeText(this.getApplicationContext(),Integer.toString(this.m_dbAdapter.getCount()), Toast.LENGTH_LONG).show();

		this.m_vwDBLayout.setAdapter(this.m_dbAdapter);
//		this.m_vwDBLayout.setA
		
		Button but = (Button) findViewById(R.id.addDBButton);
		but.setOnClickListener(new AddDBListener(this.getApplicationContext()));
	}

	public void onActivityResult (int requestCode, int resultCode, Intent data)
	{
		initDBList();
		this.m_dbAdapter=new DBListAdapter(this.getApplicationContext(),this.m_arrDBList);
		this.m_vwDBLayout.setAdapter(this.m_dbAdapter);
		this.m_dbAdapter.notifyDataSetChanged();		
	}
	
	public void initDBList()
	{
		this.m_arrDBList=Arrays.asList(this.getApplicationContext().databaseList());
		
//		//USE THIS CODE IF THERE IS NO DATABASES AVAILABLE TO AVOID GETTING A NULL EXCEPTION
//		this.m_arrDBList=new ArrayList<String>();
//		this.m_arrDBList.add("THERE IS NO DATABASES");
	}
	
	private class AddDBListener implements OnClickListener {

		private Context mContext;
		
		public AddDBListener(Context context)
		{
			this.mContext=context;
		}
		
		public void onClick(View v)
		{
			Intent clickInt=new Intent(mContext, ConfigureDBActivity.class);
			startActivityForResult(clickInt,0);
		}
	}
	
	
}