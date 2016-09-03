//package com.mollys.db;
//
//import java.util.HashMap;
//
//import android.content.ContentProvider;
//import android.content.ContentUris;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.UriMatcher;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.database.sqlite.SQLiteQueryBuilder;
//import android.net.Uri;
//
//public class TipProvider extends ContentProvider {
//
//	private static final String DATABASE_NAME="tip.db";
//	private static final int DATABASE_VERSION=2;
//
//	private static final UriMatcher sUriMatcher;
//
//	private static HashMap<String, String> sTipProjectionMap;
//	//	private static HashMap<String, String> sLiveFolderProjectionMap;
//
//	private static final int TIPS = 1;
//	private static final int TIP_ID = 2;
//	
//	//THIS CLASS IS USED BY PROVIDER WHEN A DB NEEDS TO BE CREATED FOR THE FIRST TIME
//	//OR WHEN THERE IS AN UPGRADE
//	private static class DatabaseHelper extends SQLiteOpenHelper {
//		
//		DatabaseHelper(Context context) {
//			super(context, DATABASE_NAME, null, DATABASE_VERSION);
//		}
//
//		//DON'T CREATE ANY TABLES WHEN DATABASE IS CREATED TO GET BETTER CONTROL
//		//ON WHAT IS BEING CREATED OR DESTROYED
//		@Override
//		public void onCreate(SQLiteDatabase db) {
////			db.execSQL("CREATE TABLE "+Table.TABLE_NAME+" ("
////					+Table.ID_FIELD+" "+Table.TYPE_1+","
////					+Table.FIELD_2+" "+Table.TYPE_2//+","
//////					+Table.FIELD_3+" "+Table.TYPE_3
////					+");"
////			);
//		}
//
//		//THIS WILL NEED TO BE MADE BETTER SINCE MORE TABLES COULD BE THERE
//		//AND THEY NEED TO BE BACKED UP SOMEHOW NOT JUST DROPPED!!!!
//		@Override
//		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//			db.execSQL("DROP TABLE IF EXISTS "+Table.TABLE_NAME);
//			onCreate(db);
//		}
//	}
//
//	private DatabaseHelper mOpenHelper;
//
//	//THIS HAS TO BE DONE EVERY TIME SINCE YOU NEVER KNOW IF THE DATABASE EXISTS ALREADY OR
//	//IF AN UPDATE NEEDS TO BE DONE, SO YOU WILL ALWAYS NEED THE HELPER
//	@Override
//	public boolean onCreate() {
//		mOpenHelper=new DatabaseHelper(getContext());
//		return true;
//	}
//
//	
//	//YOU COULD DO ONE URI PER TABLE OR PER DATABASE OR WHATEVER
//	@Override
//	public Cursor query(Uri uri, String[] projection, String selection,
//			String[] selectionArgs, String sortOrder) {
//
//		SQLiteQueryBuilder qb=new SQLiteQueryBuilder();
//		qb.setTables(Table.TABLE_NAME);
//
//		switch(sUriMatcher.match(uri)) {
//		case TIPS:
//			qb.setProjectionMap(sTipProjectionMap);
//			break;
//
////		case TIP_ID:
////			qb.setProjectionMap(sTipProjectionMap);
////			qb.appendWhere(Table.ID_FIELD + "=" + uri.getPathSegments().get(1));
////			break;
//
//		default:
//			throw new IllegalArgumentException("Unknown URI " + uri);
//		}
//
//		//		// If no sort order is specified use the default
//		//		String orderBy;
//		//		if (TextUtils.isEmpty(sortOrder)) {
//		//			orderBy = NotePad.Notes.DEFAULT_SORT_ORDER;
//		//		} else {
//		//			orderBy = sortOrder;
//		//		}
//
//		// Get the database and run the query
//		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
//		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, null);//orderBy);
//
//		// Tell the cursor what uri to watch, so it knows when its source data changes
//		c.setNotificationUri(getContext().getContentResolver(), uri);
//		return c;
//
//	}
//
//	@Override
//	public String getType(Uri uri) {
//		switch (sUriMatcher.match(uri)) {
//		case TIPS:
//			return Table.CONTENT_TYPE;
////YOU ONLY NEED ONE URI PER TABLE; DON'T MAKE IT MORE CONFUSING YET
////		case TIP_ID:
////			return Table.CONTENT_ITEM_TYPE;
//		default:
//			throw new IllegalArgumentException("Unknown URI " + uri);
//		}
//	}
//
//
//	@Override
//	public Uri insert(Uri uri, ContentValues initialValues) {
//		// Validate the requested uri
//		if (sUriMatcher.match(uri) != TIPS) {
//			throw new IllegalArgumentException("Unknown URI " + uri);
//		}
//
//		ContentValues values;
//		if (initialValues != null) {
//			values = new ContentValues(initialValues);
//		} else {
//			values = new ContentValues();
//		}
//
//		// Make sure that the fields are all set
//		if (values.containsKey(Table.FIELD_1) == false) {
//			values.put(Table.FIELD_1, Table.DEFAULT_1);
//		}
//
//		if (values.containsKey(Table.FIELD_2) == false) {
//			values.put(Table.FIELD_2, Table.DEFAULT_2);
//		}
//
////		if (values.containsKey(Table.FIELD_3) == false) {
////			values.put(Table.FIELD_3, Table.DEFAULT_3);
////		}
//
//		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//		long rowId = db.insert(Table.TABLE_NAME, Table.FIELD_2, values);
//		if (rowId > 0) {
//			Uri noteUri = ContentUris.withAppendedId(Table.CONTENT_URI, rowId);
//			getContext().getContentResolver().notifyChange(noteUri, null);
//			return noteUri;
//		}
//
//		throw new SQLException("Failed to insert row into " + uri);
//	}
//	
//    @Override
//    public int delete(Uri uri, String where, String[] whereArgs) {
//        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//        int count;
//        switch (sUriMatcher.match(uri)) {
//        case TIPS:
//            count = db.delete(Table.TABLE_NAME, where, whereArgs);
//            break;
////        case TIP_ID:
////            String tipId = uri.getPathSegments().get(1);
////            count = db.delete(Table.TABLE_NAME, Table.ID_FIELD + "=" + tipId
////                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
////            break;
//
//        default:
//            throw new IllegalArgumentException("Unknown URI " + uri);
//        }
//
//        getContext().getContentResolver().notifyChange(uri, null);
//        return count;
//    }
//
//    @Override
//    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
//        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//        int count;
//        switch (sUriMatcher.match(uri)) {
//        case TIPS:
//            count = db.update(Table.TABLE_NAME, values, where, whereArgs);
//            break;
////        case TIP_ID:
////            String tipId = uri.getPathSegments().get(1);
////            count = db.update(Table.TABLE_NAME, values, Table.ID_FIELD + "=" + tipId
////                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
////            break;
//
//        default:
//            throw new IllegalArgumentException("Unknown URI " + uri);
//        }
//
//        getContext().getContentResolver().notifyChange(uri, null);
//        return count;
//    }
//
//    static {
//        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//        sUriMatcher.addURI(Table.AUTHORITY, Table.PATH, TIPS);
//        sUriMatcher.addURI(Table.AUTHORITY, Table.PATH+"/#", TIP_ID);
//
//        sTipProjectionMap = new HashMap<String, String>();
//        sTipProjectionMap.put(Table.FIELD_1, Table.FIELD_1);
//        sTipProjectionMap.put(Table.FIELD_2, Table.FIELD_2);
//        sTipProjectionMap.put(Table.FIELD_3, Table.FIELD_3);
//
////        // Support for Live Folders.
////        sLiveFolderProjectionMap = new HashMap<String, String>();
////        sLiveFolderProjectionMap.put(LiveFolders._ID, Notes._ID + " AS " +
////                LiveFolders._ID);
////        sLiveFolderProjectionMap.put(LiveFolders.NAME, Notes.TITLE + " AS " +
////                LiveFolders.NAME);
//        // Add more columns here for more robust Live Folders.
//    }
//    
////ADDED BY JAFERGAS
////    public void execSQL(String sql)
////    {
////		// Get the database and run the query
////		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
////    	db.execSQL(sql);
////    }
//}