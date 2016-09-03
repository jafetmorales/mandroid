package com.mollys.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Table {

//	public static final String AUTHORITY = "com.mollys.provider.tipprovider";
	
//	public static final String PATH = "tips";

	public String name;

//	public static final int NUM_FIELDS=3;

//	/**
//	 * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
//	 */
//	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mollys.tip";
//
//	/**
//	 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single note.
//	 */
//	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mollys.tip";

	
//	public static final String[][]FIELDS={
////		{"_ID","INTEGER PRIMARY KEY","0"},
//		{"NAME","TEXT","INTEGER"}};
////		{"AGE","INTEGER","99"}};
	public List<HashMap<String,Object>>fields;//=new ArrayList<HashMap<String,String>>();
//	=Arrays.asList(
//			new String[]{"id","INTEGER PRIMARY KEY","0"},
//			new String[]{"name","TEXT","Default Name"});

	public Table() {}

//	public void setName(String inName)
//	{
//	this.name=inName;
//	}
//	
//	public void setFields(List<HashMap<String,String>> inFields)
//	{
//		this.fields=inFields;
//	}
		
//	/**
//	 * The timestamp for when the note was created
//	 * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
//	 */
//	public static final String CREATED_DATE = "created";
//
//	/**
//	 * The timestamp for when the note was last modified
//	 * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
//	 */
//	public static final String MODIFIED_DATE = "modified";
//
//	/**
//	 * The title of the note
//	 * <P>Type: TEXT</P>
//	 */
//	public static final String TITLE = "title";
//
//	/**
//	 * The note itself
//	 * <P>Type: TEXT</P>
//	 */
//	public static final String NOTE = "note";


//	/**
//	 * The content:// style URL for this table
//	 */
//	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
}
