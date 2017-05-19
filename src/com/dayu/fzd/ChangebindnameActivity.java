package com.dayu.fzd;

import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.dayu.fzd.R;
import com.dayu.fzd.R.drawable;

import android.R.mipmap;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class ChangebindnameActivity extends Activity
{
	private static final int REQUEST_BINDPIC = 9;
	private static int[] picid;
	private  String owner="";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	//	setContentView(R.layout.activity_changebindname);
		
		 owner = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		
		Point mPoint = getDisplayInfomation();
		
		LinearLayout mLayout = new LinearLayout(this);
		mLayout.setOrientation(LinearLayout.VERTICAL);
		mLayout.setLayoutParams(new    ViewGroup.LayoutParams(mPoint.x,mPoint.y));
		
		
		if (TraceActivity.isdebug) Log.e("gps", String.valueOf(TraceActivity.getbindphone));
		if (TraceActivity.getbindphone)
		{
		 
		   if (TraceActivity.seriallist.length > 0)	
		   {   TextView text1 = new TextView(this);
			   text1.setText("修改设备别名和图片，例如：我的宝贝、我的手机\n");
			   text1.setLeft(10);
			//text.setId(3000+i);
			   text1.setLayoutParams(new    ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			   mLayout.addView(text1);
			   
			    picid = new int[TraceActivity.seriallist.length];
			   
			for (int i = 0; i < TraceActivity.seriallist.length; i++)
			{
			
				LinearLayout mLayout2 = new LinearLayout(this);
				mLayout2.setOrientation(LinearLayout.HORIZONTAL);
			//	mLayout2.setLayoutParams(new    ViewGroup.LayoutParams(mPoint.x,mPoint.y));
				
				TextView text = new TextView(this);
				text.setText(TraceActivity.seriallist[i]+"\n");
				text.setLeft(20);
				//text.setId(3000+i);
				text.setLayoutParams(new    ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				mLayout2.addView(text);
				
				EditText edit = new EditText(this);
				String bieming = "";
				bieming = TraceActivity.bieming[i];
				if (bieming.toString().equals(""))
				 {
					edit.setHint("请输入别名");
				 }else
				 {
					 edit.setText(bieming); 
				 }
				
				edit.setLeft(20);
				edit.setId(3000+i);
				edit.setSingleLine(true);
				
				edit.setLayoutParams(new    ViewGroup.LayoutParams(densityutil.dip2px(this,150),LayoutParams.WRAP_CONTENT));
				mLayout2.addView(edit);
				
				
				/*text2.setText(String.valueOf(i));
				text2.setLeft(30);
			    text2.setId(6000+i);
				text2.setLayoutParams(new    ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				text2.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						Bundle bundle = new Bundle();
						bundle.putInt("serialid", v.getId());
						Intent mIntent = new Intent();
						mIntent.putExtras(bundle);
						mIntent.setClass(ChangebindnameActivity.this, BindpicActivity.class);
						startActivityForResult(mIntent, REQUEST_BINDPIC);
					}
				});
				mLayout2.addView(text2);*/
				ImageView img1 = new ImageView(this);
				BitmapDescriptor iBitmap = null;
        			 
            	iBitmap = BitmapDescriptorFactory.fromAsset(TraceActivity.piclist[i]);
            		// 获得图片的宽高
				img1.setImageBitmap(iBitmap.getBitmap());
			//	img1.setImageResource(Integer.valueOf(TraceActivity.piclist[i]));
				img1.setId(6000+i);
				img1.setLayoutParams(new    ViewGroup.LayoutParams(densityutil.dip2px(this, 35),densityutil.dip2px(this, 35)));
				img1.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						Bundle bundle = new Bundle();
						bundle.putInt("serialid", v.getId());
						Intent mIntent = new Intent();
						mIntent.putExtras(bundle);
						mIntent.setClass(ChangebindnameActivity.this, BindpicActivity.class);
						startActivityForResult(mIntent, REQUEST_BINDPIC);
					}
				});
				mLayout2.addView(img1);
				 
				 Button mButton = new Button(this);
				 mButton.setText("修改");
				 mButton.setLeft(30);
				 mButton.setId(9000+i);
				 mButton.setLayoutParams(new    ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				 mButton.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						if (TraceActivity.isdebug) Log.e("gps", String.valueOf(v.getId()%9000)) ;
						
						updatebindinfo(v.getId()%9000);
					}
				});
				 mLayout2.addView(mButton);
				
				 mLayout.addView(mLayout2);
			}
		   
		 
		   setContentView(mLayout);
		   if (TraceActivity.isdebug) Log.e("gps", String.valueOf(mLayout.getMeasuredWidth()));
		  }
		}
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
	
    private Point getDisplayInfomation() {  
        Point point = new Point();  
        getWindowManager().getDefaultDisplay().getSize(point);  
        if (TraceActivity.isdebug)  Log.e("gps","the screen size is "+point.toString());  
        return point;
    }  
    
    private Boolean updatebindinfo(int id)
    {
    	
    	 HttpClient mHttpClient = new DefaultHttpClient();
		   String serverurl = getResources().getString(R.string.url);
		   String uri = serverurl+"/Updatebindinfo";
			HttpPost mHttpPost = new HttpPost(uri);
			NameValuePair pair1 = new BasicNameValuePair("serialno",TraceActivity.seriallist[id]);
			List<NameValuePair>  params = new ArrayList<NameValuePair>();
			params.add(pair1);
			EditText ed1 = (EditText)findViewById(3000+id);
			
			if (getWordlength(ed1.getText().toString())==0 || getWordlength(ed1.getText().toString())>12)
			{
				Toast.makeText(ChangebindnameActivity.this, "请输入别名（不能超过12位字符）", Toast.LENGTH_LONG).show();
				return false;
			}
			
			pair1 = new BasicNameValuePair("bieming",ed1.getText().toString());
			params.add(pair1);
			pair1 = new BasicNameValuePair("pic",TraceActivity.piclist[id]);
			params.add(pair1);
			pair1 = new BasicNameValuePair("owner",owner);
			params.add(pair1);
			try
			{
				mHttpPost.setEntity(new UrlEncodedFormEntity(params, "GBK"));
			} catch (UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
			mHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
			HttpResponse mhttpResponse;
			try
			{
				mhttpResponse = mHttpClient.execute(mHttpPost);
				if (TraceActivity.isdebug) Log.e("gps","rescode:"+mhttpResponse.getStatusLine().getStatusCode());
				if (mhttpResponse.getStatusLine().getStatusCode()==200)
				{
					 String response = EntityUtils.toString(mhttpResponse.getEntity(), "utf-8");
		 				
	 				 JSONObject mJsonObject = new JSONObject(response);
			    	
					try
					{
						String resp = mJsonObject.getString("resp");
						
						switch (resp)
						{
						case "0":
							Toast.makeText(ChangebindnameActivity.this, "修改成功", Toast.LENGTH_LONG).show();
							break;
						case "3":
							Toast.makeText(ChangebindnameActivity.this, "修改失败，无此设备", Toast.LENGTH_LONG).show();
							break;
						
						default:
							Toast.makeText(ChangebindnameActivity.this, "修改失败", Toast.LENGTH_LONG).show();
							break;
						} 
						if (TraceActivity.isdebug) Log.e("gps",response);
					} catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			} catch (IOException | JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(ChangebindnameActivity.this, "修改失败，请重试", Toast.LENGTH_LONG).show();
				return false;
			}
			
			return true;
    }
    
    

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==REQUEST_BINDPIC)
		{
			if (data!=null)
			{
			  /*TextView  tv1 = (TextView)findViewById( data.getIntExtra("serialid", 6000) );	
			  tv1.setText(String.valueOf(data.getIntExtra("picname",0)));*/
			 ImageView img1 = (ImageView)findViewById(data.getIntExtra("serialid", 6000));
		//	 img1.setImageResource(data.getIntExtra("picname",0)); 
				BitmapDescriptor iBitmap = null;
   			 
            	iBitmap = BitmapDescriptorFactory.fromAsset(data.getStringExtra("picname"));
            		// 获得图片的宽高
				img1.setImageBitmap(iBitmap.getBitmap());
			 TraceActivity.piclist[data.getIntExtra("serialid", 6000)%6000] = String.valueOf(data.getStringExtra("picname"));
			 if (TraceActivity.isdebug)  Log.e("gps", String.valueOf(data.getStringExtra("picname")));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.changebindname, menu);
		return true;
	}

}
