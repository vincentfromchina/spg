package com.dayu.fzd;

import android.os.Bundle;
import android.util.Log;

import com.dayu.fzd.R;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class BindpicActivity extends Activity
{
	String selectpic = "p4.png";
	int serialid = 6000;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bindpic);
		Bundle bundle = this.getIntent().getExtras();
		
		serialid = bundle.getInt("serialid");
		
		RadioGroup mRadioGroup = (RadioGroup)findViewById(R.id.radioGroup1);
		
		/*mRadioGroup.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				
			}
		});*/
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				// TODO Auto-generated method stub
				switch (checkedId)
				{
				case R.id.radio0:
					selectpic = "p1.png";
					break;
				case R.id.radio1:
					selectpic = "p2.png";
					break;
				case R.id.radio2:
					selectpic = "p3.png";
					break;
				case R.id.radio3:
					selectpic = "p4.png";
					break;
				case R.id.radio4:
					selectpic = "p5.png";
					break;
				case R.id.radio5:
					selectpic = "p6.png";
					break;
				case R.id.radio6:
					selectpic = "p7.png";
					break;
				default:
					
					break;
				}
				if (TraceActivity.isdebug) Log.e("gps", String.valueOf(checkedId));
			}
		});
		
		Button mButton = (Button)findViewById(R.id.but_bindpic);
		mButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("picname", selectpic);
				intent.putExtra("serialid", serialid);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
	}
	
	

	@Override
	public void finishActivity(int requestCode)
	{
		// TODO Auto-generated method stub
		super.finishActivity(requestCode);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bindpic, menu);
		return true;
	}

}
