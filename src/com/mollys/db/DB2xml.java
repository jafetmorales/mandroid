//USE SQLITEQUERYBUILDER TO EXECUTE COMPLEX QUERIES THEY SUCK!!!
//YOU THEN TRANSFORM THAT HASHMAP TO AN XML DOCUMENT IN WHICH THEY ARE ORDER

package com.mollys.db;

import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.database.Cursor;

public class DB2xml {

	Context ctx;
	DBManager dbm;
	HashMap<String,Object> hmList;
	HashMap<String,Object> hmHierarchy;
	public DB2xml(Context ctxin)
	{
		this.ctx=ctxin;
	}

	public void getTemporaryHierarchicTable(String stDbname)
	{
		dbm=new DBManager(this.ctx);
		dbm.setDB(stDbname);

		dbm.dbCurrent.dbSql.execSQL("DROP TABLE IF EXISTS 'hierarchy'");
		dbm.dbCurrent.dbSql.execSQL("CREATE TEMPORARY TABLE IF NOT EXISTS 'hierarchy'([a] text,[b] text,[c] text,[d] text,[e] text,[f] text,[g] text);");
		dbm.dbCurrent.dbSql.execSQL(
				"INSERT INTO 'hierarchy' " +
				"SELECT attributes.name AS attribute,value,col1,col2,col3,col4,col5 FROM "+ 
				"(SELECT S4.tag_id AS tag_id,col1,col2,col3,col4,Tags.name AS col5,tags.parent_id AS parent_id "+ 
				"FROM ("+
				"     SELECT S3.tag_id AS tag_id,col1,col2,col3, Tags.name AS col4,tags.parent_id AS parent_id "+ 
				"     FROM ("+
				"          SELECT S2.tag_id AS tag_id,col1,col2,Tags.name AS col3, Tags.parent_id AS parent_id "+ 
				"          FROM ("+                                
				"               SELECT  Tags.tag_id AS tag_id,Tags.name AS col1,Dummy.name AS col2,Dummy.parent_id AS parent_id "+ 
				"               FROM Tags LEFT OUTER JOIN (Tags)Dummy ON Tags.parent_id=Dummy.tag_id ORDER BY col2 "+
				"               )S2 "+
				"          LEFT OUTER JOIN Tags ON S2.parent_id=Tags.tag_id ORDER BY col3 "+
				"          )S3 "+ 
				"     LEFT OUTER JOIN Tags ON S3.parent_id=tags.tag_id "+
				"     )S4 "+ 
				"LEFT OUTER JOIN tags ON S4.parent_id=tags.tag_id)SH "+
				"LEFT JOIN attributes ON SH.tag_id=attributes.tag_id "
		);
	}

	public void getHierarchicHM()
	{
		hmHierarchy=new HashMap<String,Object>();
		hmList=new HashMap<String,Object>();
		Cursor curs=dbm.dbCurrent.dbSql.rawQuery("SELECT * FROM 'hierarchy'", null);
		curs.moveToFirst();

		int iNumRows=curs.getCount();
		for(int iRow=0;iRow<iNumRows;iRow++)
		{
			//FIRST ADD THE ATTRIBUTE IN THIS ROW TO THE HASHMAP LIST
			String stAttributeName=curs.getString(0);
			String stAttributeValue=curs.getString(1);
			//			hmList.put(stAttributeName, stAttributeValue);
			String stTagName=curs.getString(2);
			if(hmList.get(stTagName)==null)
			{
				HashMap<String,Object> hmTag=new HashMap<String,Object>();
				hmList.put(stTagName, hmTag);
			}
			if(stAttributeName!=null)
				((HashMap<String,Object>)hmList.get(stTagName)).put(stAttributeName, stAttributeValue);			

			int iFieldNum=3;
			String stParentName=curs.getString(iFieldNum);
			boolean done=false;
			while(stParentName!=null && !done)
			{
				if(hmList.get(stParentName)==null)
				{
					HashMap<String,Object> hmParent=new HashMap<String,Object>();
					hmList.put(stParentName, hmParent);
				}
				else
				{
					done=true;
				}

				((HashMap<String,Object>)hmList.get(stParentName)).put(stTagName,hmList.get(stTagName));

				if(!done)
				{
					iFieldNum++;
					stTagName=stParentName;
					stParentName=curs.getString(iFieldNum);
				}
			}
			if(stParentName==null)
				hmHierarchy.put(stTagName, hmList.get(stTagName));

			curs.moveToNext();
		}
	}

	public String getXml()
	{
		String toret="";

		String eol= System.getProperty("line.separator"); 

		//		toret="size:"+hmList.size()+" "+eol;

		//		String stTagPart1="";
		Iterator<String> itTagNames1=hmHierarchy.keySet().iterator();
		while(itTagNames1.hasNext())
		{
			String stTagName1=itTagNames1.next();
			toret=toret+eol+"<"+stTagName1;

			HashMap<String,Object> hmL1=(HashMap<String,Object>)hmHierarchy.get(stTagName1);			

			//GET ALL THE ATTRIBUTES
			String stAttributes1="";
			Iterator<String> itAtNames1=hmL1.keySet().iterator();
			while(itAtNames1.hasNext())
			{
				String stAtName1=itAtNames1.next();
				if(hmL1.get(stAtName1) instanceof java.lang.String)
				{
					stAttributes1=stAttributes1+" "+stAtName1+"='"+hmL1.get(stAtName1)+"'";
					hmL1.remove(stAtName1);
					itAtNames1=hmL1.keySet().iterator();
				}				
			}
			toret=toret+stAttributes1+">";

			//GET ALL THE TAGS LOWER IN THE HIERARCHY
			Iterator<String> itTagNames2=hmL1.keySet().iterator();
			while(itTagNames2.hasNext())
			{
				String stTagName2=itTagNames2.next();
				toret=toret+eol+"    <"+stTagName2;

				HashMap<String,Object> hmL2=(HashMap<String,Object>)hmL1.get(stTagName2);			
				
				//GET ALL THE ATTRIBUTES
				String stAttributes2="";
				if(hmL1.get(stTagName2) instanceof java.util.HashMap<?, ?>)
				{
					Iterator<String> itAtNames2=hmL2.keySet().iterator();
					while(itAtNames2.hasNext())
					{
						String stAtName2=itAtNames2.next();
						if(hmL2.get(stAtName2) instanceof java.lang.String)
						{
							stAttributes2=stAttributes2+" "+stAtName2+"='"+hmL2.get(stAtName2)+"'";
							hmL2.remove(stAtName2);
							itAtNames2=hmL2.keySet().iterator();
						}				
					}
				}
				toret=toret+stAttributes2+">";

				//GET ALL THE TAGS LOWER IN THE HIERARCHY
				if(hmL1.get(stTagName2) instanceof java.util.HashMap<?,?>)
				{
//					HashMap<String,Object> hmL2=((HashMap<String,Object>)hmL1.get(stTagName2));
					Iterator<String> itTagNames3=hmL2.keySet().iterator();
					while(itTagNames3.hasNext())
					{
						String stTagName3=itTagNames3.next();
						toret=toret+eol+"        <"+stTagName3;//((String)hmList.get(key));												

						HashMap<String,Object> hmL3=(HashMap<String,Object>)hmL2.get(stTagName3);			
						
						//GET ALL THE ATTRIBUTES
						String stAttributes3="";
						if(hmL2.get(stTagName3) instanceof java.util.HashMap<?, ?>)
						{
							Iterator<String> itAtNames3=hmL3.keySet().iterator();
							while(itAtNames3.hasNext())
							{
								String stAtName3=itAtNames3.next();
								if(hmL3.get(stAtName3) instanceof java.lang.String)
								{
									stAttributes3=stAttributes3+" "+stAtName3+"='"+hmL3.get(stAtName3)+"'";
									hmL3.remove(stAtName3);
									itAtNames3=hmL3.keySet().iterator();
								}				
							}
						}
						toret=toret+stAttributes3+">";
						
						//GET ALL THE TAGS LOWER IN THE HIERARCHY
						if(hmL2.get(stTagName3) instanceof java.util.HashMap<?,?>)
						{
//							HashMap<String,Object> hmL3=((HashMap<String,Object>)hmL2.get(stTagName3));
							Iterator<String> itTagNames4=hmL3.keySet().iterator();
							while(itTagNames4.hasNext())
							{
								String stTagName4=itTagNames4.next();
								toret=toret+eol+"            <"+stTagName4;//((String)hmList.get(key));												

								HashMap<String,Object> hmL4=(HashMap<String,Object>)hmL3.get(stTagName4);			
								
								//GET ALL THE ATTRIBUTES
								String stAttributes4="";
								if(hmL3.get(stTagName4) instanceof java.util.HashMap<?, ?>)
								{
									Iterator<String> itAtNames4=hmL4.keySet().iterator();
									while(itAtNames4.hasNext())
									{
										String stAtName4=itAtNames4.next();
										if(hmL4.get(stAtName4) instanceof java.lang.String)
										{
											stAttributes4=stAttributes4+" "+stAtName4+"='"+hmL4.get(stAtName4)+"'";
											hmL4.remove(stAtName4);
											itAtNames4=hmL4.keySet().iterator();
										}				
									}
								}
								toret=toret+stAttributes4+">";

								//GET ALL THE TAGS LOWER IN THE HIERARCHY
								if(hmL3.get(stTagName4) instanceof java.util.HashMap<?,?>)
								{
//									HashMap<String,Object> hmL4=((HashMap<String,Object>)hmL3.get(stTagName4));
									Iterator<String> itTagNames5=hmL4.keySet().iterator();
									while(itTagNames5.hasNext())
									{
										String stTagName5=itTagNames5.next();
										if(hmL4.get(stTagName5) instanceof java.util.HashMap<?,?>)
										{
											toret=toret+eol+"                <"+stTagName5+">";//((String)hmList.get(key));

											toret=toret+eol+"                </"+stTagName5+">";
										}
									}
								}
								toret=toret+eol+"            </"+stTagName4+">";//((String)hmList.get(key));												
							}
						}
						toret=toret+eol+"        </"+stTagName3+">";//((String)hmList.get(key));												
					}
				}
				toret=toret+eol+"    </"+stTagName2+">";
			}
			toret=toret+eol+"</"+stTagName1+">";
		}

		return toret;

		//		curs.moveToPosition(8);
		//		toret="row9: "+curs.getString(0)+" "+curs.getString(1)+" "+curs.getString(2)+" "+curs.getString(3)+" "+curs.getString(4);
		//		return toret;
	}
}
