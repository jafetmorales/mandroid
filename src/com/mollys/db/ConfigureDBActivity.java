package com.mollys.db;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class ConfigureDBActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conf_db);

		EditText ed=(EditText) findViewById(R.id.name);

		Button but = (Button) findViewById(R.id.saveDBConf);
		but.setOnClickListener(new SaveDBListener(this.getApplicationContext(),ed));
	}
	
	private class SaveDBListener implements OnClickListener {
		
		private Context mCtx;
		private EditText mEd;
		
		public SaveDBListener(Context ctx, EditText ed)
		{
			this.mCtx=ctx;
			this.mEd=ed;
		}
		
		public void onClick(View v)
		{
			//ADD AND SET DATABASE
			DBManager dbm=new DBManager(this.mCtx);
			dbm.addDB(this.mEd.getText().toString());
			
			finish();
		}
	}
}