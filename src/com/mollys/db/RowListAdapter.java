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



public class RowListAdapter extends BaseAdapter implements OnItemLongClickListener{

	/**
	 * The application Context in which this JokeListAdapter is being used.
	 */
	private Context m_context;

	/**
	 * The dataset to which this JokeListAdapter is bound.
	 */
	private List<String> m_rowList;
	
	/**
	 * The position in the dataset of the currently selected Joke.
	 */
	private int m_nSelectedPosition;

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
	
	private String m_stDbName;
	private String m_stTbName;
	
	public RowListAdapter(Context context, List<String> rowList, String stDbName, String stTbName) {
		//TODO
		this.m_context=context;
		this.m_rowList=rowList;
		this.m_nSelectedPosition=Adapter.NO_SELECTION;

		this.m_stDbName=stDbName;
		this.m_stTbName=stTbName;
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
	}

	public int getCount() {
		return this.m_rowList.size();
	}

	public Object getItem(int position) {
		return this.m_rowList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		TextView tv=new TextView(this.m_context);
		tv.setText(this.m_rowList.get(position));
		tv.setOnClickListener(new RowViewListener(position));

		return tv;
	}
	
	private class RowViewListener implements OnClickListener {

		int m_iRowViewPos;
		public RowViewListener(int iRowViewPos)
		{
			this.m_iRowViewPos=iRowViewPos;
		}
		
		public void onClick(View v)
		{
			Intent in=new Intent(RowListAdapter.this.m_context, ConfigureRowActivity.class);
			in.putExtra("dbname", RowListAdapter.this.m_stDbName);
			in.putExtra("tbname", RowListAdapter.this.m_stTbName);
			in.putExtra("id",this.m_iRowViewPos+1);

			in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			RowListAdapter.this.m_context.startActivity(in);
		}
	}

	
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		Toast.makeText(RowListAdapter.this.m_context,"Hello", Toast.LENGTH_LONG).show();
		RowListAdapter.this.m_nSelectedPosition=arg2;
		return false;
	}
}