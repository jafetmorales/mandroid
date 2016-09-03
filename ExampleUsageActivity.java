package com.mollys.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ExampleUsageActivity extends Activity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Table tb=new Table();
		tb.name="La Tableada";
		tb.fields=new ArrayList<HashMap<String,String>>();
//		HashMap<String,String> field1=new HashMap<String,String>();
//		field1.put("name", "id");
//		field1.put("type", "INTEGER PRIMARY KEY");
//		field1.put("default", "0");
//		tb.fields.add(field1);
		HashMap<String,String> field2=new HashMap<String,String>();		
		field2.put("name", "name");
		field2.put("type", "TEXT");
		field2.put("default", "Default Name");
		tb.fields.add(field2);
		
		DBManager dbm=new DBManager(getApplicationContext());
		dbm.addTable(tb);
		dbm.setTable(tb.name);

		//INSERTION EXAMPLE
		HashMap<String,String> values=new HashMap<String,String>();
		values.put("name", "Aaron");		
		Long row=(Long) dbm.execStatement(DBManager.StatementType.INSERT,null,values);

		//UPDATE EXAMPLE
		HashMap<String,String> valuesNew=new HashMap<String,String>();
		valuesNew.put("name", "Arton");
		int affUpd=(Integer) dbm.execStatement(DBManager.StatementType.UPDATE, "name='Aaron'",valuesNew);

		//SELECTION EXAMPLE
		List<HashMap<String,String>> rows=(List<HashMap<String,String>>) dbm.execStatement(DBManager.StatementType.SELECT, "id=1",null);

		//DELETION EXAMPLE
		int affDel=(Integer) dbm.execStatement(DBManager.StatementType.DELETE, "name='Arton'",null);
		
		TextView tv=(TextView) findViewById(R.id.testView);
		tv.setText(rows.get(0).get("name"));//Integer.toString(affDel));//
	}
}