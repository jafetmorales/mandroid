package com.mollys.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class RowListActivity extends Activity {

	/**
	 * Adapter used to bind an AdapterView to List of Jokes.
	 */
	protected RowListAdapter m_rowAdapter;
	protected ListView m_vwRowLayout;
	protected List<String> m_arrRowList;	

//	private DBManager m_dbManager;
	private String m_stDbName;
	private String m_stTbName;


	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.row_list);

		Intent in=this.getIntent();
		this.m_stDbName=in.getStringExtra("dbname");
		this.m_stTbName=in.getStringExtra("tbname");
		
		initList();
		
		this.m_vwRowLayout=(ListView)this.findViewById(R.id.rowListViewGroup);
		this.m_rowAdapter=new RowListAdapter(this.getApplicationContext(),this.m_arrRowList,this.m_stDbName,this.m_stTbName);
		this.m_vwRowLayout.setAdapter(this.m_rowAdapter);

		Button but1 = (Button) findViewById(R.id.fieldListButton);
		but1.setOnClickListener(new FieldListListener());
		Button but2 = (Button) findViewById(R.id.addRowButton);
		but2.setOnClickListener(new AddRowListener());
	}

	public void onActivityResult (int requestCode, int resultCode, Intent data)
	{
		initList();
		
		this.m_rowAdapter=new RowListAdapter(this.getApplicationContext(),this.m_arrRowList,this.m_stDbName,this.m_stTbName);
		this.m_vwRowLayout.setAdapter(this.m_rowAdapter);
		this.m_rowAdapter.notifyDataSetChanged();		
	}
	
	public void initList()
	{
		
		this.m_arrRowList=new ArrayList<String>();
		
		DBManager dbm=new DBManager(this.getApplicationContext());
		dbm.setDB(this.m_stDbName);
		Cursor curs=dbm.dbCurrent.dbSql.rawQuery("SELECT * FROM '"+RowListActivity.this.m_stTbName+"'", null);
		curs.moveToFirst();

		int iNumRows=curs.getCount();
		for(int iRow=0;iRow<iNumRows;iRow++)
		{
			this.m_arrRowList.add(curs.getString(0));
			curs.moveToNext();
		}
	}
	

	private class FieldListListener implements OnClickListener {

		public FieldListListener(){}
		
		public void onClick(View v)
		{
			Intent in=new Intent(RowListActivity.this.getApplicationContext(), FieldListActivity.class);
			in.putExtra("dbname", RowListActivity.this.m_stDbName);
			in.putExtra("tbname", RowListActivity.this.m_stTbName);

			startActivityForResult(in,0);
		}
	}

	private class AddRowListener implements OnClickListener {

		public AddRowListener(){}
		
		public void onClick(View v)
		{
			Intent in=new Intent(RowListActivity.this.getApplicationContext(), ConfigureRowActivity.class);
			in.putExtra("dbname", RowListActivity.this.m_stDbName);
			in.putExtra("tbname", RowListActivity.this.m_stTbName);
			in.putExtra("id",0);

			startActivityForResult(in,0);
		}
	}
}