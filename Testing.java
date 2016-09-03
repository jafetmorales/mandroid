package com.mollys.db;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class Testing extends Activity {
	
	private ListView lv1;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list);
		

		CreateTableActivity dtb=new CreateTableActivity(getApplicationContext());
		DB2xml dbx=new DB2xml(getApplicationContext());
		dbx.getTemporaryHierarchicTable("xmldb");
		dbx.getHierarchicHM();

		TextView tv=(TextView) findViewById(R.id.testView);
		tv.setText(dbx.getXml());
	}
}