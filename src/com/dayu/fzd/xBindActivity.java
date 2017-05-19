package com.dayu.fzd;

import android.os.Bundle;
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

import com.dayu.fzd.R;

import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class xBindActivity extends Activity
{
	String Imei = "00000000000000000000";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind);
		
		 Imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		
		Button mbButton = (Button)findViewById(R.id.bind_btnqueding);
		mbButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Bindphone();
			}
		});
	}
	
	 public int getWordlength(String s)
	    {
	        int length = 0;
	        for(int i = 0; i < s.length(); i++)
	        {
	            int ascii = Character.codePointAt(s, i);
	            if(ascii >= 0 && ascii <=255)
	                length++;
	            else
	                length += 2;
	                
	        }
	        return length;
	        
	    }
	
	private void Bindphone()
	{
		EditText mEditText = (EditText)findViewById(R.id.bind_text);
		String serialno = mEditText.getText().toString();
		
		if (getWordlength(serialno)==0 || getWordlength(serialno)>10)
		{
			Toast.makeText(xBindActivity.this, "请输入设备号（不能超过10位字符）", Toast.LENGTH_LONG).show();
			return;
		}
		
	 	HttpClient mHttpClient = new DefaultHttpClient();
	//	 String uri = "http://"+SpeehService.serverip+"/Regsiter";
	 	String serverurl = getResources().getString(R.string.url);
		String uri = serverurl+"/Bindphone";
		    HttpPost httppost = new HttpPost(uri);   
		    List<NameValuePair> params = new ArrayList<NameValuePair>();
		    // 添加要传递的参数
		    NameValuePair pair1 = new BasicNameValuePair("owner", Imei);
		    params.add(pair1);
		     pair1 = new BasicNameValuePair("serialno", serialno);
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
		 		    		
		 			mHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);	
		 		     
		 		    HttpResponse httpresponse = null;  
		 		    try
		 			{
		 				httpresponse = mHttpClient.execute(httppost);
		 				
		 			   if (httpresponse.getStatusLine().getStatusCode()==200)	
		 			   {
		 				  String response = EntityUtils.toString(httpresponse.getEntity(), "utf-8");
		 				
		 				 JSONObject mJsonObject = new JSONObject(response);
				    	
						try
						{
							String resp = mJsonObject.getString("resp");
							
							switch (resp)
							{
							case "0":
								Toast.makeText(xBindActivity.this, "绑定成功", Toast.LENGTH_LONG).show();
								break;
							case "4":
								Toast.makeText(xBindActivity.this, "设备已绑定", Toast.LENGTH_LONG).show();
								break;
							case "2":
								Toast.makeText(xBindActivity.this, "设备号不存在请重新输入\n如未注册请注册", Toast.LENGTH_LONG).show();
								break;
							case "3":
								Toast.makeText(xBindActivity.this, "已达到免费绑定数量上限", Toast.LENGTH_LONG).show();
								break;
							default:
								Toast.makeText(xBindActivity.this, "绑定失败", Toast.LENGTH_LONG).show();
								break;
							} 
						} catch (JSONException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 		
						if (TraceActivity.isdebug) Log.e("url",response);
		 			   }
		 			  if (TraceActivity.isdebug) Log.e("url","rescode:"+httpresponse.getStatusLine().getStatusCode());
		 				
		 				
		 			} catch (ClientProtocolException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
						if (TraceActivity.isdebug) Log.e("loghere", "sockettimeout");
						
					} catch (JSONException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
	      }       

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bind, menu);
		return true;
	}

}
