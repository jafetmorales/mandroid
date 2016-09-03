//TODO: NEED TO REMOVE SQLITE API STATEMENTS AND CHANGE IT TO WHERE
//EACH STATEMENTTYPE THAT CAN BE CHOSEN BY THE USER MAPS TO AN ACTUAL SQL STATEMENT
//THAT CAN BE DISPLAYED TO THE USER OF THE DBMANAGER. THE USER SHOULD THEN BE ABLE
//TO MODIFY THIS STATEMENT (FOR THE GUI VERSION).

//TODO: NEED TO BE ABLE TO REMOVE A DATABASE FROM BOTH PHONE AND FROM SD CARD
//AFTER FIX, THEN UNCOMMENT THE LINE FROM SHOWLIST THAT SAYS LOADFROMSDCARD
//EVERYTHING ELSE SEEMS TO BE WORKING

//TODO: IMPLEMENT METHOD TO GET TABLE FIELDS

package com.mollys.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DBManager {

	public static enum StatementType {INSERT,SELECT,DELETE,UPDATE};
	public static enum DataType {INTEGER, INT8, TEXT};

	private static final int DATABASE_VERSION = 2;

	private Table tbCurrentTable=null;

	private Context context;
	//THIS SHOULD BE PRIVATE TOO
	public Database dbCurrent=null;

	public DBManager(Context context) {
		this.context = context;

		//CREATE THE TABLE THAT WILL HOLD ALL DATABASE DATA
		DBAdapter crMaster = new DBAdapter(this.context,"master.db");
		SQLiteDatabase dbMaster = crMaster.getWritableDatabase();
		dbMaster.execSQL("CREATE TABLE IF NOT EXISTS 'dbs'(" +
				"name TEXT DEFAULT 'unknown'" +
				")");
		dbMaster.close();
		crMaster.close();
	}

	public void addDB(String stDBName)
	{
		//CREATE THE DATABASE
		DBAdapter crAdd = new DBAdapter(this.context,stDBName);
		Database dbNew=new Database(crAdd.getWritableDatabase());
		dbNew.name=stDBName;

		//ADD THIS DATABASE TO THE DATABASE LIST
		DBAdapter crMaster = new DBAdapter(this.context,"master.db");
		SQLiteDatabase dbMaster = crMaster.getWritableDatabase();
		dbMaster.execSQL("INSERT INTO dbs(name) VALUES('"+dbNew.name+"')");
		dbMaster.close();
		crMaster.close();
	}

	public void setDB(String stDBName)
	{
		try{
			this.dbCurrent.dbSql.close();
		}
		catch(Exception ex)
		{}
		DBAdapter creator = new DBAdapter(this.context,stDBName);
		this.dbCurrent = new Database(creator.getWritableDatabase());
		this.dbCurrent.name=stDBName;
	}

	public void removeDB(String stDBName)
	{
		//DELETE ENTRY FOR THIS DATABASE FROM THE DATABASE LIST
		DBAdapter crMaster = new DBAdapter(this.context,"master.db");
		SQLiteDatabase dbMaster = crMaster.getWritableDatabase();		
		String sql="DELETE FROM 'dbs' WHERE name='"+stDBName+"'";
		dbMaster.execSQL(sql);
		//DELETE THE FILE
		this.context.getFileStreamPath(stDBName).delete();
	}

	public void removeAllDBs()
	{
		List<String> lsDBNames=Arrays.asList(this.context.databaseList());
	}

	public boolean saveDBToExternalDrive()
	{
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();
			if (sd.canWrite()) {
				String currentDBPath ="\\data\\"+this.context.getPackageName()+"\\databases\\"+this.dbCurrent.name;
				String backupDBPath = "Download/"+this.dbCurrent.name;

				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				if (currentDB.exists()) {
					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();

					dst.transferFrom(src, 0, src.size());

					src.close();
					dst.close();
					return true;
				}
				else
				{
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public boolean loadFromExternalDrive(String stDBName)
	{
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();
			if (sd.canWrite()) {
				String currentDBPath = "Download/"+stDBName;
				String backupDBPath ="\\data\\"+this.context.getPackageName()+"\\databases\\"+stDBName;

				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				if (currentDB.exists()) {
					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();

					dst.transferFrom(src, 0, src.size());

					src.close();
					dst.close();

					//ADD THE METADATA FOR THIS DATABASE
					addDB(stDBName);
					return true;
				}
				else
				{
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;		
	}

	public void removeAllTables()
	{
		List<String> lsTableNames=getTableNames();
		Iterator<String> itTableNames=lsTableNames.iterator();
		while(itTableNames.hasNext())
		{
			String sTableName=itTableNames.next();
			//REMOVE THE IF STATEMENT; IT'S TEMPORARY; ALL TABLES SHOULD BE DELETED WHEN YOU REMOVE THE IF
			if(!sTableName.matches("fields"))
				this.removeTable(sTableName);
		}
	}

	//MODIFIED TO USE INTERNAL TABLES
	public List<String> getTableNames()
	{
		List<String> lsTableNames=new ArrayList<String>();
		String sql="SELECT tbl_name FROM 'sqlite_master'";//"SELECT name FROM 'tabs' ORDER BY name";
		Cursor cursor=dbCurrent.dbSql.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				lsTableNames.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}	
		return lsTableNames;
	}

	//GETS THE NAME AND FIELDS OF A TABLE, THEIR TYPE, AND DEFAULT VALUE
	public Table getFields(String stTbName)
	{
		Table atb=new Table();
		atb.name=stTbName;
		Log.d("mytag", (String) atb.name);
		atb.fields=new ArrayList<HashMap<String,Object>>();
		Cursor cursor=this.dbCurrent.dbSql.rawQuery("PRAGMA table_info('"+stTbName+"')", null);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String,Object> afield=new HashMap<String,Object>();
				afield.put("name", cursor.getString(1));
				String type=cursor.getString(2);
				if(type.matches("INTEGER"))
				{
					afield.put("type", DataType.INTEGER);
					afield.put("default", (Integer)cursor.getInt(4));
				}
				else if(type.matches("INT8"))
				{
					afield.put("type", DataType.INT8);
					afield.put("default", (Long)cursor.getLong(4));
				}
				else if(type.matches("TEXT"))
				{
					afield.put("type", DataType.TEXT);
					afield.put("default", (String)cursor.getString(4));
				}
				else
				{
					afield.put("type", DataType.TEXT);
					afield.put("default", (String)"Type Definition error during TABLE INFO RECOLLECTION");
				}
				afield.put("tab", stTbName);
				atb.fields.add(afield);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return atb;
	}

	//THIS METHOD SHOULD NOT BE USED, UNLESS YOU NEED A LIST OF ALL TABLES WITH THE INFORMATION FOR EACH INCLUDED
	/**  
	 * Loads all the tables that are currently in the database
	 * It loads their fields along with type of field and default value
	 */
	//MODIFIED TO USE INTERNAL TABLES
	public HashMap<String,Table> getAllTablesInfo()
	{
		HashMap<String,Table> hmTables=new HashMap<String,Table>();

		String tableName;
		Iterator<String> it=getTableNames().iterator();
		while(it.hasNext())
		{
			tableName=it.next();

			Table atb=new Table();
			atb=getFields(tableName);
			hmTables.put(tableName, atb);
		}
		
		return hmTables;
	}

	/** Returns: 
	 * TRUE (if table was added successfully or already exists)
	 * FALSE (if table will not be in DB)
	 */
	//WORKS WITH INTERNAL TABLES
	//ALLOWS YOU TO ADD A TABLE WITH FIELDS OR WITHOUT FIELDS
	public void addTable(Table tb)
	{
		//DELETE IT JUST FOR DEBUGGING

		String createStatement="CREATE TABLE IF NOT EXISTS '"+tb.name+"'";

		String fieldString;
		if(tb.fields!=null)
		{
			fieldString="(";
			int pos;
			for(pos=0;pos<tb.fields.size();pos++)
			{
				//ADD THIS FIELD TO THE FIELDS TABLE
				String nativeType;
				Object defVal;
				switch((DataType)tb.fields.get(pos).get("type"))
				{	
				case INTEGER:
					nativeType=(String)"INTEGER";
					defVal=(Integer)tb.fields.get(pos).get("default");
					break;
				case INT8:
					nativeType=(String)"INT8";
					defVal=(Long)tb.fields.get(pos).get("default");
					break;
				case TEXT:
					nativeType=(String)"TEXT";
					defVal=(String)tb.fields.get(pos).get("default");
					break;
				default:
					nativeType=(String)"TEXT";
					defVal=(String)"Type Definition error at TABLE CREATION";
					nativeType=(String)"TEXT";
				}

				//ADD THIS FIELD TO THE TABLE CREATION STATEMENT
				fieldString=fieldString+tb.fields.get(pos).get("name")+" "+nativeType;
				fieldString=fieldString+" DEFAULT '"+defVal+"'";
				if(pos<(tb.fields.size()-1))
					fieldString=fieldString+",";
			}					

			fieldString=fieldString+")";
		}
		else
		{
			fieldString="(id INTEGER PRIMARY KEY)";
		}

		createStatement=createStatement+fieldString;

		//CREATE THE TABLE
		dbCurrent.dbSql.execSQL(createStatement);
		//ADD THIS TABLE TO THE LIST OF TABLES
		//		dbCurrent.dbSql.execSQL("INSERT INTO tabs(name) VALUES ('"+tb.name+"')");
	}

	//WORKS WITH INTERNAL TABLES
	public void removeTable(String name)
	{		
		//DROP THE TABLE ITSELF
		dbCurrent.dbSql.execSQL("DROP TABLE IF EXISTS'" + name+"'");
		//DELETE TABLE DATA STORED IN TABS TABLE
		//		dbCurrent.dbSql.execSQL("DELETE FROM 'tabs' WHERE name='"+name+"'");
		//DELETE THE FIELD DATA STORED FOR THIS TABLE
		//		dbCurrent.dbSql.execSQL("DELETE FROM 'fields' WHERE tab='"+name+"'");
	}

	public void setTable(String name)
	{
//		tbCurrentTable=this.getAllTablesInfo().get(name);
		Log.d("mytag", (String) name);
		this.tbCurrentTable=this.getFields(name);
		Log.d("mytag", (String) this.getFields(name).name);
	}

	/** Basic operations go here*/
	public Object execStatement(StatementType type,String whereClause, HashMap<String,Object> values) {
		Object retObj;
		switch(type)	{
		case INSERT:
		{
			Iterator<String> it=values.keySet().iterator();
			ContentValues cv=new ContentValues();
			while(it.hasNext())
			{
				String key=(String)it.next();
				if(values.get(key) instanceof java.lang.String)
					cv.put(key, (String)values.get(key));
				else if(values.get(key) instanceof java.lang.Integer)
					cv.put(key, (Integer)values.get(key));				
				else if(values.get(key) instanceof java.lang.Long)
					cv.put(key, (Long)values.get(key));				
				//				else if(values.get(key) instanceof java.lang.Boolean)
				//					cv.put(key, (Integer)(((Boolean)values.get(key))?1:0));
				else
					cv.put(key, (String)"Type Casting error at INSERT");
			}

			retObj=this.dbCurrent.dbSql.insert("'"+this.tbCurrentTable.name+"'", (String)this.tbCurrentTable.fields.get(0).get("name"), cv);
		}
		break;
		case SELECT:
		{			
			Cursor cursor = this.dbCurrent.dbSql.query("'"+this.tbCurrentTable.name+"'", null, whereClause, null, null, null, null);
//			String sql="SELECT "+whereClause+" FROM '"+this.tbCurrentTable.name+"'";
//			Cursor cursor=dbCurrent.dbSql.rawQuery(sql, null);
			List<HashMap<String,Object>> data = new ArrayList<HashMap<String,Object>>();
			if (cursor.moveToFirst()) {
				do {
					HashMap<String,Object> hms=new HashMap<String,Object>();
					for(int i=0;i<cursor.getColumnCount();i++)
					{
						switch((DataType)tbCurrentTable.fields.get(i).get("type"))
						{	
						case INTEGER:
							hms.put((String)this.tbCurrentTable.fields.get(i).get("name"), (Integer)cursor.getInt(i));
							Log.d("type","Integer");
							break;
						case INT8:
							hms.put((String)this.tbCurrentTable.fields.get(i).get("name"), (Long)cursor.getLong(i));
							Log.d("type","Long");
							break;
						case TEXT:
							hms.put((String)this.tbCurrentTable.fields.get(i).get("name"), (String)cursor.getString(i));
							Log.d("type","String");
							break;
							//						case BOOLEAN:
							//							hms.put((String)this.tbCurrentTable.fields.get(i).get("name"), (Boolean)(cursor.getInt(i)!=0)?true:false);
							//							break;
						default:
							Log.d("type","none matched");
							hms.put((String)this.tbCurrentTable.fields.get(i).get("name"), (String)"Type Casting error at SELECT");
						}
					}
					data.add(hms);
				} while (cursor.moveToNext());
			}
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
			retObj=data;
		}
		break;
		case DELETE:
		{
			retObj=this.dbCurrent.dbSql.delete("'"+this.tbCurrentTable.name+"'", whereClause, null);
		}
		break;
		case UPDATE:
		{
			Iterator<String> it=values.keySet().iterator();
			ContentValues cv=new ContentValues();
			while(it.hasNext())
			{
				String key=(String)it.next();
				if(values.get(key) instanceof java.lang.String)
					cv.put(key, (String)values.get(key));
				else if(values.get(key) instanceof java.lang.Integer)
					cv.put(key, (Integer)values.get(key));
				else if(values.get(key) instanceof java.lang.Long)
					cv.put(key, (Long)values.get(key));
				//				else if(values.get(key) instanceof java.lang.Boolean)
				//					cv.put(key, (Integer)(((Boolean)values.get(key))?1:0));
				else
					cv.put(key, "Type Casting error at UPDATE");
			}
			retObj=this.dbCurrent.dbSql.update("'"+this.tbCurrentTable.name+"'", cv, whereClause, null);
		}
		break;
		default:
			retObj=null;
		}
		return retObj;
	}
	
	public void addField(String stTbName,HashMap<String,Object> hmField)
	{
		//DELETE ENTRY FOR THIS DATABASE FROM THE DATABASE LIST
		this.dbCurrent.dbSql.execSQL("ALTER TABLE "+stTbName+" ADD COLUMN "+hmField.get("name")+" "+hmField.get("type")+" DEFAULT 0");
	}

	//THIS IS THE HELPER
	private static class DBAdapter extends SQLiteOpenHelper {

		DBAdapter(Context context, String stDBName) {
			super(context, stDBName, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("Example", "Upgrading database, this will drop tables and recreate.");
			//			db.execSQL("DROP TABLE IF EXISTS " + tb.name);
			onCreate(db);
		}
	}
}

