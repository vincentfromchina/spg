package com.dayu.fzd;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import android.R.color;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class zShowseriActivity extends Activity
{

	private String URL_Post;
	private String owner ="0000000";
	private String serialid = "00000";
	static Loopthread mLoopthread = null;

	private static final int REG_OK = 1015,REG_EXITS = 1016,REG_FAIL = 1017,REG_BACKLIST = 1018;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showseri);
		URL_Post = getResources().getString(R.string.url)+"/OwnerRegsiter"; 
		 owner  = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
	
		 if (TraceActivity.isdebug)  Log.e("loghere", ""+Thread.currentThread().getId());

         mLoopthread = new Loopthread();
         mLoopthread.start();
         getownerser();
				 
		 Button btn_back = (Button)findViewById(R.id.btn_fanhuishouye);
			btn_back.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					finish();
				}
			});
	}

	private class Loopthread extends Thread{

		 public Handler mHandler;

		@Override
		public void run()
		{
			Looper.prepare();
			 mHandler = new Handler() {
					@Override
				    public void handleMessage(android.os.Message msg) {
				        super.handleMessage(msg);
				        
				        if (TraceActivity.isdebug)  Log.e("loghere", "msg:"+msg);
				        switch (msg.what) {
				           
				            case REG_OK:
				            //	Toast.makeText(ShowseriActivity.this, "激活成功", Toast.LENGTH_LONG).show();
				            	
				            	TextView tv1 = (TextView)findViewById(R.id.seri);
								 tv1.setTextSize(densityutil.dip2px(zShowseriActivity.this,23));
							//	 tv1.setTextColor(color.holo_red_light);
								tv1.setText("本设备序列号为："+serialid+"\n请牢记");
				               break;
				            case REG_EXITS:
				            //   Toast.makeText(ShowseriActivity.this, "本设备已存在", Toast.LENGTH_LONG).show();
									 TextView tv2 = (TextView)findViewById(R.id.seri);
									 tv2.setTextSize(densityutil.dip2px(zShowseriActivity.this,23));
								//	 tv2.setTextColor(color.holo_red_light);
									tv2.setText("本设备序列号为："+serialid+"\n请牢记");
							
					            break;
				            case REG_BACKLIST:
					            //   Toast.makeText(ShowseriActivity.this, "本设备已存在", Toast.LENGTH_LONG).show();
										 TextView tv4 = (TextView)findViewById(R.id.seri);
										 tv4.setTextSize(densityutil.dip2px(zShowseriActivity.this,23));
									//	 tv2.setTextColor(color.holo_red_light);
										tv4.setText("本设备不允许激活！请更换设备");
								
						            break;
				            case REG_FAIL:
				            //	Toast.makeText(ShowseriActivity.this, "获取失败，请重试", Toast.LENGTH_LONG).show();
				            	TextView tv3 = (TextView)findViewById(R.id.seri);
								 tv3.setTextSize(densityutil.dip2px(zShowseriActivity.this,23));
							//	 tv2.setTextColor(color.holo_red_light);
								tv3.setText("获取失败，请重试");
				            	break;
				        default:
				        	if (TraceActivity.isdebug)   Log.i("loghere", "Unhandled msg - " + msg.what);
				        }
				    }                                       
				};
		    Looper.loop();		
			
			super.run();
		}
		
	}
	
	

	 public boolean getownerser()
	   {
		 HttpClient mHttpClient = new DefaultHttpClient();
								
			    HttpPost httppost = new HttpPost(URL_Post);   
			    List<NameValuePair> params = new ArrayList<NameValuePair>();
			     // 添加要传递的参数
			    NameValuePair pair1 = new BasicNameValuePair("serialno",owner);
			    params.add(pair1);
			   
			    HttpEntity mHttpEntity;
			 			try
			 			{
			 				mHttpEntity = new UrlEncodedFormEntity(params, "gbk");
			 			
			 				httppost.setEntity(mHttpEntity); 
			 				if (TraceActivity.isdebug) Log.e("url", "发送数据");
			 			} catch (UnsupportedEncodingException e1)
			 			{
			 				// TODO Auto-generated catch block
			 				if (TraceActivity.isdebug) Log.e("url", "数据传递出错了");
			 				e1.printStackTrace();
			 			}
			 		    		
			 			mHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 4000);	
			 		     
			 		    HttpResponse httpresponse = null;  
			 		    try
			 			{
			 				httpresponse = mHttpClient.execute(httppost);
			 				
			 			   if (httpresponse.getStatusLine().getStatusCode()==200)	
			 			   {
			 				  String response = EntityUtils.toString(httpresponse.getEntity(), "utf-8");
			 				 
			 				 JSONObject mJsonObject = new JSONObject(response);
			 				if (TraceActivity.isdebug) Log.e("loghere", response);
							try
							{
								String resp = mJsonObject.getString("resp");
								switch (resp)
								{
								case "1":
									serialid  = mJsonObject.getString("serialid");
									mLoopthread.mHandler.sendEmptyMessage(REG_OK);
									break;
								case "2":
									serialid = mJsonObject.getString("serialid");
									if (TraceActivity.isdebug) Log.e("loghere", serialid);
									mLoopthread.mHandler.sendEmptyMessage(REG_EXITS);
									break;
								case "4":
									serialid = mJsonObject.getString("serialid");
									if (TraceActivity.isdebug) Log.e("loghere", serialid);
									mLoopthread.mHandler.sendEmptyMessage(REG_BACKLIST);
									break;
								default:
									mLoopthread.mHandler.sendEmptyMessage(REG_FAIL);
									break;
								} 
							} catch (JSONException e)
							{
								e.printStackTrace();
							}
						 		
			 			   }
			 			  if (TraceActivity.isdebug) Log.e("url","rescode:"+httpresponse.getStatusLine().getStatusCode());
			 				
			 				
			 			} catch (ClientProtocolException e1)
						{
							e1.printStackTrace();
						} catch (IOException e1)
						{
							e1.printStackTrace();
							if (TraceActivity.isdebug) Log.e("loghere", "sockettimeout");
							
						} catch (JSONException e1)
						{
							e1.printStackTrace();
						} 
			
		  return true;	
	   }
	 
	
	
	
}
