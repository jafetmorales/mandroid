package com.mollys.db;

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

public class TableListActivity extends Activity {

	/**
	 * Adapter used to bind an AdapterView to List of Jokes.
	 */
	protected TableListAdapter m_tableAdapter;
	protected ListView m_vwTableLayout;
	protected List<String> m_arrTableList;	

	private DBManager m_dbManager;
	private String m_stDbName;



	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_list);

		Intent in=this.getIntent();
		this.m_stDbName=in.getStringExtra("dbname");

		initList();

		this.m_vwTableLayout=(ListView)this.findViewById(R.id.tableListViewGroup);
		this.m_tableAdapter=new TableListAdapter(this.getApplicationContext(),this.m_arrTableList,this.m_stDbName);
		this.m_vwTableLayout.setAdapter(this.m_tableAdapter);

		Button but = (Button) findViewById(R.id.addTableButton);
		but.setOnClickListener(new AddTableListener(this.getApplicationContext(),this.m_stDbName));
	}

	public void onActivityResult (int requestCode, int resultCode, Intent data)
	{
		initList();

		this.m_tableAdapter=new TableListAdapter(this.getApplicationContext(),this.m_arrTableList,this.m_stDbName);
		this.m_vwTableLayout.setAdapter(this.m_tableAdapter);
		this.m_tableAdapter.notifyDataSetChanged();		
	}

	public void initList()
	{
		DBManager dbm=new DBManager(this.getApplicationContext());
		dbm.setDB(this.m_stDbName);
		this.m_arrTableList=dbm.getTableNames();
	}


	private class AddTableListener implements OnClickListener {

		private Context mContext;
		private String mStDbName;

		public AddTableListener(Context context,String stDbName)
		{
			this.mContext=context;
			this.mStDbName=stDbName;
		}

		public void onClick(View v)
		{
			Intent in=new Intent(mContext, ConfigureTableActivity.class);
			in.putExtra("dbname", this.mStDbName);

			startActivityForResult(in,0);
		}
	}

}