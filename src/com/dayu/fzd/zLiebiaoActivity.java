package com.dayu.fzd;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dayu.fzd.R;

import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class zLiebiaoActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liebiao);
		
		ListView lv1 = (ListView)findViewById(R.id.weizhi_listview);
		SimpleAdapter madapter = null;
		List<HashMap<String,String>> mlist = new ArrayList<HashMap<String,String>>();
		
		HashMap<String,String> mHashMap = new HashMap<String,String>();
		mHashMap.put("1", "1");
		mHashMap.put("2", "起：无数据");
		mHashMap.put("3", "位置：无数据");
	//	mHashMap.put("4", "止：无数据");
		
		madapter = new SimpleAdapter(this, 
				TraceActivity.gpsdata, 
				R.layout.listdetail ,
				new String[] {"1","2","3"} , 
				new int[] {R.id.pos, R.id.stime, R.id.addr});
		
		lv1.setAdapter(madapter);
		
		Button  btn_fanhu = (Button)findViewById(R.id.btn_fanhuiditu);
		btn_fanhu.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				zLiebiaoActivity.this.finish();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.liebiao, menu);
		return true;
	}

	@Override
	protected void onPause()
	{
		ListView lv1 = (ListView)findViewById(R.id.weizhi_listview);
		SimpleAdapter madapter = null;
		List<HashMap<String,String>> mlist = new ArrayList<HashMap<String,String>>();
		
		HashMap<String,String> mHashMap = new HashMap<String,String>();
		mHashMap.put("1", "1");
		mHashMap.put("2", "起：无数据");
		mHashMap.put("3", "位置：无数据");
	//	mHashMap.put("4", "止：无数据");
		
		madapter = new SimpleAdapter(this, 
				TraceActivity.gpsdata, 
				R.layout.listdetail ,
				new String[] {"1","2","3"} , 
				new int[] {R.id.pos, R.id.stime, R.id.addr});
		
		lv1.setAdapter(madapter);
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		ListView lv1 = (ListView)findViewById(R.id.weizhi_listview);
		
		SimpleAdapter madapter = null;
		
		if (TraceActivity.gpsdata.size()>0)
		{  madapter = new SimpleAdapter(this, 
				TraceActivity.gpsdata, 
				R.layout.listdetail ,
				new String[] {"pos","time","address","locationtype","accuracy"} , 
				new int[] {R.id.pos, R.id.stime, R.id.addr, R.id.lctype, R.id.accuracy});
		}else
		{
			
			List<HashMap<String,String>> mlist = new ArrayList<HashMap<String,String>>();
			
			HashMap<String,String> mHashMap = new HashMap<String,String>();
			mHashMap.put("1", "1");
			mHashMap.put("2", "起：无数据");
			mHashMap.put("3", "位置：无数据");
		//	mHashMap.put("4", "止：无数据");
			
			madapter = new SimpleAdapter(this, 
					TraceActivity.gpsdata, 
					R.layout.listdetail ,
					new String[] {"1","2","3"} , 
					new int[] {R.id.pos, R.id.stime, R.id.addr});
		}
		
		lv1.setAdapter(madapter);
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	

}
