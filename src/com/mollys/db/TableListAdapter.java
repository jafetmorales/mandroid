//The convertView object allows you to re-use a previously constructed view for better performance. Since convertView is a View object that was previously returned by JokeListAdapter.getView(...), then you can safely assume it is JokeView.

package com.mollys.db;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class TableListAdapter extends BaseAdapter implements OnItemLongClickListener{

	/**
	 * The application Context in which this JokeListAdapter is being used.
	 */
	private Context m_context;

	/**
	 * The dataset to which this JokeListAdapter is bound.
	 */
	private List<String> m_tbList;
	
	/**
	 * The position in the dataset of the currently selected Joke.
	 */
	private int m_nSelectedPosition;
	
	private String m_stDbName;

	/**
	 * Parameterized constructor that takes in the application Context in which
	 * it is being used and the Collection of Joke objects to which it is bound.
	 * m_nSelectedPosition will be initialized to Adapter.NO_SELECTION.
	 * 
	 * @param context
	 *            The application Context in which this JokeListAdapter is being
	 *            used.
	 * 
	 * @param jokeList
	 *            The Collection of Joke objects to which this JokeListAdapter
	 *            is bound.
	 */
	public TableListAdapter(Context context, List<String> tbList, String stDbName) {
		//TODO
		this.m_context=context;
		this.m_tbList=tbList;
		this.m_nSelectedPosition=Adapter.NO_SELECTION;
		this.m_stDbName=stDbName;
	}

	/**
	 * Accessor method for retrieving the position in the dataset of the
	 * currently selected Joke.
	 * 
	 * @return an integer representing the position in the dataset of the
	 *         currently selected Joke.
	 */
	public int getSelectedPosition() {
		//TODO
		return this.m_nSelectedPosition;
		//return 0;
	}

	public int getCount() {
		return this.m_tbList.size();
	}

	public Object getItem(int position) {
		return this.m_tbList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		TextView tv=new TextView(this.m_context);
		tv.setText(this.m_tbList.get(position));
		tv.setOnClickListener(new TableViewListener(this.m_context,this.m_tbList.get(position)));

		return tv;
	}
	
	private class TableViewListener implements OnClickListener{
		private Context mctx;
		private String m_stTbName;
		public TableViewListener(Context ctx,String stTbName)
		{
			mctx=ctx;
			this.m_stTbName=stTbName;
		}

		public void onClick(View v) 
		{
			Toast.makeText(TableListAdapter.this.m_context,TableListAdapter.this.m_stDbName+"--->"+this.m_stTbName, Toast.LENGTH_SHORT).show();
			Intent clickInt=new Intent(this.mctx, RowListActivity.class);
			clickInt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			clickInt.putExtra("dbname", TableListAdapter.this.m_stDbName);
			clickInt.putExtra("tbname", this.m_stTbName);
			this.mctx.startActivity(clickInt);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		Toast.makeText(TableListAdapter.this.m_context,"Hello", Toast.LENGTH_LONG).show();
		TableListAdapter.this.m_nSelectedPosition=arg2;
		return false;
	}
}