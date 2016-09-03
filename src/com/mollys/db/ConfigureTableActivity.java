package com.mollys.db;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class ConfigureTableActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conf_table);

		Intent in=this.getIntent();
		String stDbName=in.getStringExtra("dbname");
		
		EditText ed=(EditText) findViewById(R.id.newTableName);

		Button but = (Button) findViewById(R.id.saveTableConf);
		but.setOnClickListener(new SaveTBListener(this.getApplicationContext(),stDbName,ed));
	}
	
	private class SaveTBListener implements OnClickListener {
		
		private Context mCtx;
		private String mStDbName;
		private EditText mEd;
		
		public SaveTBListener(Context ctx, String stDbName, EditText ed)
		{
			this.mCtx=ctx;
			this.mStDbName=stDbName;
			this.mEd=ed;//ed.getText().toString();
		}
		
		public void onClick(View v)
		{
			DBManager dbm=new DBManager(this.mCtx);
			dbm.setDB(this.mStDbName);
			
			//PROTOTYPE THE ELEMENTS TABLE
			Table tb=new Table();
			tb.name=this.mEd.getText().toString();
//			tb.fields=new ArrayList<HashMap<String,Object>>();
//			HashMap<String,Object> field1=new HashMap<String,Object>();		
//			field1.put("name", (String)"name");
//			field1.put("type", DBManager.DataType.TEXT);
//			field1.put("default", (String)"DefaultTagName");
//			tb.fields.add(field1);
//			HashMap<String,Object> field2=new HashMap<String,Object>();		
//			field2.put("name", (String)"parent_id");
//			field2.put("type", DBManager.DataType.INT8);
//			field2.put("default", Long.valueOf(0));
//			tb.fields.add(field2);
			
			dbm.addTable(tb);
			
			finish();
		}
	}
}