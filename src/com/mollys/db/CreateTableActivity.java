//THIS CLASS IS USED TO PREPARE A DATABASE THAT CAN BE USED BY THE SQLITE2XML MODULE

package com.mollys.db;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

public class CreateTableActivity {
	private Context ctx;
	private DBManager dbm;
	public CreateTableActivity(Context ctxin)
	{
		this.ctx=ctxin;
		//ADD AND SET DATABASE
		dbm=new DBManager(this.ctx);
		dbm.removeAllDBs();
		dbm.addDB("xmldb");

		//		addTables();
		//		insertData();

		dbm.setDB("xmldb");
		dbm.dbCurrent.dbSql.execSQL("DROP TABLE IF EXISTS 'Attributes'");
		dbm.dbCurrent.dbSql.execSQL("CREATE TABLE IF NOT EXISTS 'Attributes'([at_id] integer primary key asc,[name] text,[tag_id] int8,[value] text);");
		dbm.dbCurrent.dbSql.execSQL("INSERT INTO 'Attributes'(name,tag_id,value) VALUES('size',1,'13');");
		dbm.dbCurrent.dbSql.execSQL("INSERT INTO 'Attributes'(name,tag_id,value) VALUES('color',5,'red');");
		dbm.dbCurrent.dbSql.execSQL("INSERT INTO 'Attributes'(name,tag_id,value) VALUES('extension',4,'x1441');");
		dbm.dbCurrent.dbSql.execSQL("INSERT INTO 'Attributes'(name,tag_id,value) VALUES('size',4,'12');");
		dbm.dbCurrent.dbSql.execSQL("INSERT INTO 'Attributes'(name,tag_id,value) VALUES('color',1,'blue');");
		dbm.dbCurrent.dbSql.execSQL("INSERT INTO 'Attributes'(name,tag_id,value) VALUES('exten',1,'x1557');");
		dbm.dbCurrent.dbSql.execSQL("DROP TABLE IF EXISTS 'Tags'");
		dbm.dbCurrent.dbSql.execSQL("CREATE TABLE IF NOT EXISTS 'Tags'([tag_id] integer primary key asc,[name] text,[parent_id] int8);");
		dbm.dbCurrent.dbSql.execSQL("INSERT INTO 'Tags'(name,parent_id) VALUES('tag1',NULL);");
		dbm.dbCurrent.dbSql.execSQL("INSERT INTO 'Tags'(name,parent_id) VALUES('tag1.1',1);");
		dbm.dbCurrent.dbSql.execSQL("INSERT INTO 'Tags'(name,parent_id) VALUES('tag2',NULL);");
		dbm.dbCurrent.dbSql.execSQL("INSERT INTO 'Tags'(name,parent_id) VALUES('tag2.1',3);");
		dbm.dbCurrent.dbSql.execSQL("INSERT INTO 'Tags'(name,parent_id) VALUES('tag1.1.1',2);");
		dbm.dbCurrent.dbSql.execSQL("INSERT INTO 'Tags'(name,parent_id) VALUES('tag1.2',1);");
		dbm.dbCurrent.dbSql.execSQL("INSERT INTO 'Tags'(name,parent_id) VALUES('tag1.1.2',2);");
		dbm.dbCurrent.dbSql.execSQL("INSERT INTO 'Tags'(name,parent_id) VALUES('tag1.1.2.1',7);");
		dbm.dbCurrent.dbSql.execSQL("INSERT INTO 'Tags'(name,parent_id) VALUES('tag1.1.2.1.1',8);");
	}
	public void insertData()
	{
		dbm.setDB("xmldb");

		//INSERTION AN ELEMENT
		dbm.setTable("Tags");
		HashMap<String,Object> values=new HashMap<String,Object>();
		values.put("name", (String)"tag1");
		values.put("parent", (int)0);
		Long rowid=(Long) dbm.execStatement(DBManager.StatementType.INSERT,null,values);
		//INSERTION AN ATTRIBUTE
		dbm.setTable("Attributes");
		values=new HashMap<String,Object>();
		values.put("name", (String)"tag1_attribute1");
		values.put("element_id", (Long)rowid);
		rowid=(Long) dbm.execStatement(DBManager.StatementType.INSERT,null,values);
	}

	//ADDS THE TABLES NECESSARY FOR XML STORAGE
	public void addTables()
	{
		//PROTOTYPE THE ELEMENTS TABLE
		Table tbEl=new Table();
		tbEl.name="Tags";
		tbEl.fields=new ArrayList<HashMap<String,Object>>();
		HashMap<String,Object> field1=new HashMap<String,Object>();		
		field1.put("name", (String)"name");
		field1.put("type", DBManager.DataType.TEXT);
		field1.put("default", (String)"DefaultTagName");
		tbEl.fields.add(field1);
		HashMap<String,Object> field2=new HashMap<String,Object>();		
		field2.put("name", (String)"parent_id");
		field2.put("type", DBManager.DataType.INT8);
		field2.put("default", Long.valueOf(0));
		tbEl.fields.add(field2);

		//PROTOTYPE THE ATTRIBUTES TABLE
		Table tbAt=new Table();
		tbAt.name="Attributes";
		tbAt.fields=new ArrayList<HashMap<String,Object>>();
		field1=new HashMap<String,Object>();		
		field1.put("name", (String)"name");
		field1.put("type", DBManager.DataType.TEXT);
		field1.put("default", (String)"DefaultAttributeName");
		tbAt.fields.add(field1);
		field2=new HashMap<String,Object>();		
		field2.put("name", (String)"tag_id");
		field2.put("type", DBManager.DataType.INT8);
		field2.put("default", Long.valueOf(0));
		tbAt.fields.add(field2);
		HashMap<String,Object> field3=new HashMap<String,Object>();		
		field3.put("name", (String)"value");
		field3.put("type", DBManager.DataType.TEXT);
		field3.put("default", (String)"DefaultAttributeValue");
		tbAt.fields.add(field3);

		//ADD ELEMENTS AND ATTRIBUTES TABLES
		dbm.removeAllTables();
		dbm.addTable(tbEl);
		dbm.addTable(tbAt);
	}
}
