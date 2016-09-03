package com.mollys.db;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

//THIS IS WHERE YOU INSTANTIATE THE VIEW THAT
//CORRESPONDS TO A PARTICULAR ITEM
//YOU DISPLAY THE ITEM AND ITS SUBITEMS HERE
public class ItemView extends LinearLayout {
	private Context mContext;
	private View mv;

	//ONLY THESE THREE PARAMETERS ARE ACTUALLY NEEDED
	public ItemView(Context context, Item it) {
		super(context);
		mContext=context;

		this.setOrientation(VERTICAL);

		if(it.getType().matches("TextView"))
		{
			TextView tv=new TextView(context);
			tv.setText(it.getText());
			tv.setBackgroundColor(it.getColor());
			mv=tv;
		}	
		else if(it.getType().matches("EditText"))
		{
			EditText ed=new EditText(context);
			ed.setText(it.getText());
			mv=ed;
		}


		//		mv=new TextView(context);
		//		mTv.setText(it.getText());
		//		mTv.setBackgroundColor(Color.YELLOW);		
		//		mTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
		//		mTv.setOnClickListener(new ItemClickListener(mContext));

		addView(mv);
	}

	private class ItemClickListener implements OnClickListener{
		private int mwid;
		private Context mContext;
		public ItemClickListener(Context context)
		{
			mContext=context;
		}

		public void onClick(View v) {
			//				Intent clickInt=new Intent(mContext, stickm_ConfigureActivity.class);
			//				clickInt.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mwid);
			//				startActivityForResult(clickInt,0);
		}
	}

}
