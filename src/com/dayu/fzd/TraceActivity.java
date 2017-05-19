package com.dayu.fzd;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.*;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.*;

import com.dayu.fzd.R;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**  
 * 创建时间：2016-03-12 下午5:48:15  
 * 项目名称：gpstracer  
 * @author liuyu
 * @version 1.0   
 * @since JDK 1.6.0_21  
 * 文件名称：TraceActivity.java  
 * 类说明：  
 */
public class TraceActivity extends Activity implements OnMapLoadedListener, OnMarkerClickListener{
	MapView mapview;
	double trace[] = {23.023094083182514,113.34585363976657,
			          23.023104083182514,113.34605363976657,
			          23.023114083182514,113.34625363976657};
	CameraUpdateFactory cuf1= new CameraUpdateFactory();
	LatLng org1 = new LatLng(23.133656, 113.315473);
	LatLng org2 = new LatLng(23.023094083182514, 113.34585363976657);
	 LatLng[]  gpsresult;
	MarkerOptions[] mo;
	public static String[] seriallist, bieming ;
	public static String[] imeilist, piclist, isexplist;
	Marker[] mk = new Marker[0];
	private static  String mlist[]={"无设备列表"},mpic = "p4.png";
	private static  String numlist[]={"5","10","15","20"};
	private String serialno="",num="",owner="";
	private String select_serial = "无设备列表";
	private String select_list = "11111";
	private int select_pos = 0;
	public static Boolean getbindphone = false,isrefreshgps = false;
	private static final int BINDPHONE = 33,REFRESHGPS = 35,DOWNLOAD_ING = 37, DOWNLOAD_FINISH = 39;
	protected static final int doplay = 34;
	private static Spinner sp1 = null;
	private static int nowplayid = 0,progessperct = 0;
	private static guijiplay mguijiplay = null;
	private static Boolean isstart = false, isplaypause = false,cancelupdate = false;
	public static Boolean isdebug = false;
	public static ProgressBar mProgressBar;
	private static String mSavepath = "", apkurl = "", apkname = "", apkversion = "";
	
	List<LatLng> list;
	TextView batterinfo,liebiao;

	
	public static List<HashMap<String, String>> gpsdata = new ArrayList<HashMap<String, String>>();
	
	List<HashMap<String, String>> imeidata = new ArrayList<HashMap<String, String>>();
	
	  String URL_Post = ""; 
	 HttpURLConnection urlConn = null;  
	 
	 boolean isPost = true;  
	 
 public class checkupdate extends Thread
 {

	@Override
	public void run()
	{
		 String resultData=""; 
		 
		 try
		{
			Thread.sleep(12000);
		} catch (InterruptedException e2)
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		  if ( HttpURLConnection_update()==200)
			{
				try
				{
					InputStreamReader in;
					in = new InputStreamReader(urlConn.getInputStream());
					BufferedReader buffer = new BufferedReader(in);
					String inputLine = null;
					while (((inputLine = buffer.readLine()) != null))
					{
						resultData += inputLine;
					}
				} catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (TraceActivity.isdebug)
					Log.e("gps", "resultData:" + resultData);

				JSONObject mJsonObject;
				try
				{
					mJsonObject = new JSONObject(resultData);
					 apkname = mJsonObject.getString("apkname");
					 apkurl = mJsonObject.getString("apkurl");
					 apkversion = mJsonObject.getString("apkversion");
					 
					 try
					{
						int cunversion = getApplicationContext().getPackageManager().getPackageInfo(TraceActivity.this.getPackageName(), 0).versionCode;
						if (cunversion < Integer.valueOf(apkversion))
						{
							shownoticedialog();
						}
					} catch (NameNotFoundException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		super.run();
	}
	
 }
 
 private void shownoticedialog()
 {
	 final Builder adAlertDialog = new Builder(TraceActivity.this);
	 adAlertDialog.setMessage("当前软件版本太旧，需要更新");
	 adAlertDialog.setTitle("软件更新");
	 adAlertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
	{
		
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			dialog.dismiss();
			showdownloaddialog();
		}
	});
	 
	 runOnUiThread( new Runnable()
	{
		public void run()
		{
			 Dialog noticedialog = adAlertDialog.create();
			 noticedialog.show();
		}
	});
	
 }
 
 private void showdownloaddialog()
 {
	 Builder adAlertDialog = new Builder(TraceActivity.this);
	 final LayoutInflater mInflater  = LayoutInflater.from(getApplicationContext());
	View v = mInflater.inflate(R.layout.updatedialog, null);
	mProgressBar = (ProgressBar) v.findViewById(R.id.update_progressBar);
	
	 adAlertDialog.setView(v);
	 adAlertDialog.setTitle("下载进度");
	 adAlertDialog.setPositiveButton("取消", new DialogInterface.OnClickListener()
	{
		
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			dialog.dismiss();
			cancelupdate = true;
		}
	});
	 
	 Dialog downloaddialog = adAlertDialog.create();
	 downloaddialog.show();
	 
	 Downloadapk mdDownloadapk = new Downloadapk();
	 mdDownloadapk.setcontext(downloaddialog);
	 mdDownloadapk.start();
 }
	 
	 private class Downloadapk extends Thread
{
		 Dialog dialog;
		 public void setcontext(Dialog dialog)
		 {
			 this.dialog = dialog;
		 }
		 
		@Override
		public void run()
		{
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{
				String sdpath = Environment.getExternalStorageDirectory()+"/";
				mSavepath = sdpath +"download/";
				if (isdebug) Log.e("gps", mSavepath);
				
				try
				{
					URL downurl = new URL(apkurl);
					if (isdebug) Log.e("gps", downurl.toString());
					HttpURLConnection conn = (HttpURLConnection) downurl.openConnection();
					conn.connect();
					int apklength = conn.getContentLength();
					InputStream is = conn.getInputStream();
					
					File file = new File(mSavepath);
					if (!file.exists())
					{
						file.mkdir();
					}
					
					File apkfile = new File(apkname);
					FileOutputStream fos = new FileOutputStream(mSavepath+apkfile);
					int count = 0;
					byte buf[] = new byte[1024];
					do
					{
						int numred = is.read(buf);
						count += numred;
						progessperct =(int) (((float)count/apklength)*100);
						mHandler.sendEmptyMessage(DOWNLOAD_ING);
						if(numred<=0)
						{
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							dialog.dismiss();
							break;
						}
						
						fos.write(buf,0,numred);
						
					} while (!cancelupdate);
					if (fos!=null) fos.close();
					if (is!=null) is.close();
					if (conn!=null) conn = null;
					
				} catch (MalformedURLException e)
				{
					Toast.makeText(TraceActivity.this, "下载失败", Toast.LENGTH_LONG).show();
					dialog.dismiss();
					e.printStackTrace();
				} catch (IOException e)
				{
					Toast.makeText(TraceActivity.this, "下载失败", Toast.LENGTH_LONG).show();
					dialog.dismiss();
					e.printStackTrace();
				}
			}
				
				super.run();
		}
}

	private void updateprogessbar(int process)
	{
		mProgressBar.setProgress(process);
	}
	/**
		 * 比较传进来的时间与实际时间的大小，如果当前时间比参数大，返回true，小则返回false
		 * @param time
		 * @return boolean
		 */
		public boolean bijiaotime(String time1,String time2) //比较当前系统时间与传进来的时间大小
		{
			   
			   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    
			   Date dat1,dat2 = null;
			try
			{
				dat1 = df.parse(time1);
				dat2 = df.parse(time2);
				
				if ((dat1.getTime()-dat2.getTime())>0)
				   {
					   return true;
				   }
				   else {
					  return false;
				   }
			} catch (ParseException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			return false;
		}
	
	 private int HttpURLConnection_Post()
	 {  
		 int recode = 0;
	        try{  
	            //通过openConnection 连接  
	            URL url = new java.net.URL(URL_Post);  
	            urlConn=(HttpURLConnection)url.openConnection();  
	            //设置输入和输出流   
	            urlConn.setDoOutput(true);  
	            urlConn.setDoInput(true);  
	              
	            urlConn.setRequestMethod("POST");  
	            urlConn.setUseCaches(false);  
	            urlConn.setReadTimeout(3000);
	            urlConn.setConnectTimeout(3000);
	            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的    
	            urlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");    
	            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，  
	            // 要注意的是connection.getOutputStream会隐含的进行connect。    
	            urlConn.connect();  
	            //DataOutputStream流  
	            DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());  
	            //要上传的参数  
	            String content = "owner=" + URLEncoder.encode(owner, "GBK");   
	            //将要上传的内容写入流中  
	            out.writeBytes(content);     
	            //刷新、关闭  
	            out.flush();  
	            out.close();     

	            recode = urlConn.getResponseCode();
	            
	            if (TraceActivity.isdebug) Log.e("gps", String.valueOf(recode));
	        }catch(Exception e){  
	            
	            e.printStackTrace();  
	        }  
	        
	        return recode;
	    }  
	 
	 private int HttpURLConnection_update()
	 {  
		 int recode = 0;
	        try{  
	            //通过openConnection 连接  
	            URL url = new java.net.URL(getResources().getString(R.string.url)+"/about/updateversion.html");  
	            urlConn=(HttpURLConnection)url.openConnection();  
	            //设置输入和输出流   
	            urlConn.setDoOutput(true);  
	            urlConn.setDoInput(true);  
	              
	            urlConn.setRequestMethod("POST");  
	            urlConn.setUseCaches(false);  
	            urlConn.setReadTimeout(3000);
	            urlConn.setConnectTimeout(3000);
	            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的    
	            urlConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");    
	            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，  
	            // 要注意的是connection.getOutputStream会隐含的进行connect。    
	            urlConn.connect();  
	            //DataOutputStream流  
	            DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());  
	            //要上传的参数  
	            String content = "owner=" + URLEncoder.encode(owner, "GBK");   
	            //将要上传的内容写入流中  
	            out.writeBytes(content);     
	            //刷新、关闭  
	            out.flush();  
	            out.close();     

	            recode = urlConn.getResponseCode();
	            
	            if (TraceActivity.isdebug) Log.e("gps", String.valueOf(recode));
	        }catch(Exception e){  
	            
	            e.printStackTrace();  
	        }  
	        
	        return recode;
	    }  
	 
	private Boolean getbindphone_new()
	{
		 String resultData=""; 
		 try{  
            
         if ( HttpURLConnection_Post()==200)
           {   
             InputStreamReader in = new InputStreamReader(urlConn.getInputStream());    
             BufferedReader buffer = new BufferedReader(in);    
             String inputLine = null;    
              
             while (((inputLine = buffer.readLine()) != null)){  
                 resultData += inputLine ;    
             }  
             if (TraceActivity.isdebug) Log.e("gps","resultData:"+resultData);  
             
             int tolres = 0;
             
             try
				{
					JSONObject mJsonObject = new JSONObject(resultData);
					JSONArray jsonArray = mJsonObject.getJSONArray("imeis");
					
					 tolres = jsonArray.length();
					 if (tolres!=0)
					 {
						 imeidata.clear();
						 for (int i = 0; i < tolres; i++)
							{
								JSONObject dataobject = (JSONObject)jsonArray.opt(i);
								HashMap<String, String> mHashMap = new HashMap<String, String>();
							//	mHashMap.put("id", (String)dataobject.get("id"));
								mHashMap.put("serialno", (String)dataobject.get("serialno"));
								mHashMap.put("imei", (String)dataobject.get("imei"));
								mHashMap.put("isexp", (String)dataobject.get("isexp"));
								mHashMap.put("bieming", (String)dataobject.get("bieming"));
								mHashMap.put("pic", (String)dataobject.get("pic"));
								imeidata.add(mHashMap);
								
								imeilist = new String[tolres];
								seriallist = new String[tolres];
								bieming = new String[tolres];
								mlist = new String[tolres];
								isexplist = new String[tolres];
								piclist = new String[tolres];
							}
						 
							for (int j = 0; j < tolres; j++)
							{
								imeilist[j] = imeidata.get(j).get("imei");
								seriallist[j] = imeidata.get(j).get("serialno");
								bieming[j] = imeidata.get(j).get("bieming");
								isexplist[j] = imeidata.get(j).get("isexp");
								piclist[j] = imeidata.get(j).get("pic");
								mlist[j] = imeidata.get(j).get("bieming") + "【" + imeidata.get(j).get("serialno") + "】";
							}
					 }else
					 {
						/* HashMap<String, String> mHashMap = new HashMap<String, String>();
						 mHashMap.put("imei", "无设备");
						 mHashMap.put("bieming", "请添加设备或检查网络");
						 imeidata.add(mHashMap);*/
						    imeilist[0] = "无设备";
						    isexplist[0] = "n";
							seriallist[0] = "00000000000000000000";
							bieming[0] = "请添加设备或检查网络";
						 
					 }
				} catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
             
             in.close();   
           }
         }catch(Exception e){  
             resultData = "连接超时";  
             e.printStackTrace();  
             return false;
         }finally{  
             try{  
                 //关闭连接  
                 if(isPost)  
                 urlConn.disconnect();  
                   
             }catch(Exception e){  
                 e.printStackTrace();  
                 return false;
             }  
         }  
		return true;
	}
	
	public class Tea {
		//加密
		public byte[] encrypt(byte[] content, int offset, int[] key, int times){//times为加密轮数
		int[] tempInt = byteToInt(content, offset);
		int y = tempInt[0], z = tempInt[1], sum = 0, i;
		int delta=0x9e3779b9; //这是算法标准给的值
		int a = key[0], b = key[1], c = key[2], d = key[3];

		for (i = 0; i < times; i++) {
		sum += delta;
		y += ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
		z += ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);
		}
		tempInt[0]=y;
		tempInt[1]=z;
		return intToByte(tempInt, 0);
		}
		//解密
		public byte[] decrypt(byte[] encryptContent, int offset, int[] key, int times){
		int[] tempInt = byteToInt(encryptContent, offset);
		int y = tempInt[0], z = tempInt[1], sum = 0xC6EF3720, i;
		int delta=0x9e3779b9; //这是算法标准给的值
		int a = key[0], b = key[1], c = key[2], d = key[3];

		for(i = 0; i < times; i++) {
		z -= ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);
		y -= ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
		sum -= delta;
		}
		tempInt[0] = y;
		tempInt[1] = z;

		return intToByte(tempInt, 0);
		}
		//byte[]型数据转成int[]型数据
		private int[] byteToInt(byte[] content, int offset){

		int[] result = new int[content.length >> 2]; //除以2的n次方 == 右移n位 即 content.length / 4 == content.length ＞＞ 2
		for(int i = 0, j = offset; j < content.length; i++, j += 4){
		result[i] = transform(content[j + 3]) | transform(content[j + 2]) <<  8 |
		transform(content[j + 1]) << 16 | (int)content[j] << 24;
		}
		return result;

		}
		//int[]型数据转成byte[]型数据
		private byte[] intToByte(int[] content, int offset){
		byte[] result = new byte[content.length << 2]; //乘以2的n次方 == 左移n位 即 content.length * 4 == content.length ＜＜ 2
		for(int i = 0, j = offset; j < result.length; i++, j += 4){
		result[j + 3] = (byte)(content[i] & 0xff);
		result[j + 2] = (byte)((content[i] >> 8) & 0xff);
		result[j + 1] = (byte)((content[i] >> 16) & 0xff);
		result[j] = (byte)((content[i] >> 24) & 0xff);
		}
		return result;
		}
		//若某字节被解释成负的则需将其转成无符号正数
		private  int transform(byte temp){
		int tempInt = (int)temp;
		if(tempInt < 0){
		tempInt += 256;
		}
		return tempInt;
		}

		}
	
   public boolean getgps()
   {
	   HttpClient mHttpClient = new DefaultHttpClient();
	   String serverurl = getResources().getString(R.string.url);
	   String uri = serverurl+"/Getgps";
		HttpPost mHttpPost = new HttpPost(uri);
		NameValuePair pair1 = new BasicNameValuePair("serialno",serialno);
		List<NameValuePair>  params = new ArrayList<NameValuePair>();
		params.add(pair1);
		pair1 = new BasicNameValuePair("wantnum",num);
		params.add(pair1);
		pair1 = new BasicNameValuePair("owner",owner);
		params.add(pair1);
		
		int tolres = 0;
		try
		{
			mHttpPost.setEntity(new UrlEncodedFormEntity(params, "GBK"));
			mHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
			HttpResponse mhttpResponse =  mHttpClient.execute(mHttpPost);
			if (mhttpResponse.getStatusLine().getStatusCode()==200)
			{
				String response = EntityUtils.toString(mhttpResponse.getEntity(), "utf-8");
				try
				{
					JSONObject mJsonObject = new JSONObject(response);
				    	
					JSONArray jsonArray = mJsonObject.getJSONArray("gps");
					String lastack = mJsonObject.getString("lastack");
					String batter = mJsonObject.getString("batter");
					String bieming = mJsonObject.getString("bieming");
				//	 mpic = Integer.valueOf(mJsonObject.getString("pic"));
					 mpic = mJsonObject.getString("pic");
					 
					 if (TraceActivity.isdebug) Log.e("loghere", "lastack:"+lastack+"batter:"+batter+"bieming"+bieming+"pic"+mpic);
					 
					// batterinfo.setTextSize(10);   //densityutil.dip2px(TraceActivity.this, 5)
					 batterinfo.setText("最近同步时间: "+lastack+"\n 电池电量: "+batter+"%"+"   设别名称:"+bieming);
					 batterinfo.setTextColor(0XB4FF0000);
					 
					 tolres = jsonArray.length();
					 if (TraceActivity.isdebug) Log.e("loghere", String.valueOf(tolres));
				 if (tolres!=0)
				  {
					  runOnUiThread(new Runnable()
						{
							public void run()
							{
							   ImageView img4 = (ImageView)findViewById(R.id.guijiplay);
							   ImageView img5 = (ImageView)findViewById(R.id.guijipause);
							   img4.setVisibility(View.VISIBLE);
							   img5.setVisibility(View.INVISIBLE);
							}
						});
					 nowplayid = tolres - 1;
					gpsdata.clear();
					for (int i = 0; i < tolres; i++)
					{
						JSONObject dataobject = (JSONObject)jsonArray.opt(i);
						HashMap<String, String> mHashMap = new HashMap<String, String>();
					//	mHashMap.put("id", (String)dataobject.get("id"));
						mHashMap.put("time", (String)dataobject.get("time"));
						mHashMap.put("lng", (String)dataobject.get("lng"));
						mHashMap.put("lat", (String)dataobject.get("lat"));
						mHashMap.put("address", (String)dataobject.get("address"));
						mHashMap.put("locationtype", (String)dataobject.get("locationtype"));
						mHashMap.put("accuracy", (String)dataobject.get("accuracy"));
						mHashMap.put("pos", String.valueOf(i+1));
						
						gpsdata.add(mHashMap);
						
					}
                    
                		
                		new BitmapDescriptorFactory();
                		Bitmap photo = BitmapFactory.decodeResource(getResources(),R.drawable.s_548380);
                		 int width = photo.getWidth(), hight = photo.getHeight();
                		 Bitmap  icon = Bitmap.createBitmap(width*2, hight*2, Bitmap.Config.ARGB_8888);
                		 
                		
                	//	 BitmapDescriptor bmpf = BitmapDescriptorFactory.fromBitmap(icon);
                		 Canvas canvas = new Canvas(icon);	
                		 Paint photoPaint = new Paint(); //建立画笔  
                	       photoPaint.setDither(true); //获取跟清晰的图像采样  
                	       photoPaint.setFilterBitmap(true);//过滤一些  
                	        
                	       Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());//创建一个指定的新矩形的坐标  
                	       Rect dst = new Rect(0, 0, width, hight);//创建一个指定的新矩形的坐标  
                	       canvas.drawBitmap(photo, src, dst, photoPaint);//将photo 缩放或则扩大到 dst使用的填充区photoPaint  
                	       
                		   Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//设置画笔  
                	       textPaint.setTextSize(10.0f);//字体大小  
                	       textPaint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度  
                	       textPaint.setColor(Color.BLUE);//采用的颜色 
                	       canvas.drawText("10", 13, 13, textPaint);
                	       canvas.save(Canvas.ALL_SAVE_FLAG); 
                	       canvas.restore(); 
                	       
                /*		 mo1 = new MarkerOptions();
                			
                		mo1.icon(bmpf);
                			
                		 mo2 = new MarkerOptions();
                			
                		mo2.icon(bmpf);*/
                		
                	       if	(mk.length!=0)
                	       {   for (int i = 0; i < mk.length; i++)
								{
									mk[i].remove();
								}
                	       
                	       }
                		if (tolres == 0)
                		 {gpsresult[0] = new LatLng(0.0, 0.0);}
                		else {
                		
                			gpsresult = new LatLng[tolres];
                			mk = new Marker[tolres];
                			mo = new MarkerOptions[tolres];
                			
						}
                		
                		BitmapDescriptor iBitmap = null;
                		
                	//	 iBitmap = BitmapFactory.decodeResource(getResources(),mpic);
                		 
                		 iBitmap = BitmapDescriptorFactory.fromAsset(mpic);
                		// 获得图片的宽高

                		 int mwidth = iBitmap.getWidth();

                		 int mheight = iBitmap.getHeight();

                		 // 设置想要的大小

                		 int newWidth = densityutil.dip2px(this,26);

                		 int newHeight = densityutil.dip2px(this,26);

                		 // 计算缩放比例

                		 float scaleWidth = ((float) newWidth) / mwidth;

                		 float scaleHeight = ((float) newHeight) / mheight;

                		 // 取得想要缩放的matrix参数

                		 Matrix matrix = new Matrix();

                		 matrix.postScale(scaleWidth, scaleHeight);

                		 // 得到新的图片

                		 Bitmap newbm = Bitmap.createBitmap(iBitmap.getBitmap(), 0, 0, mwidth, mheight, matrix,true);
            		
                		 BitmapDescriptor bmpf =  BitmapDescriptorFactory.fromBitmap(newbm);
                	// BitmapDescriptor bmpf =  BitmapDescriptorFactory.fromResource(mpic) ;    //BitmapDescriptorFactory.fromAsset("s_548366.png");
            		
            		
            		for (int i = tolres-1; i > -1; i--) //防止最新的点呗覆盖倒叙画
					{
						
                		gpsresult[i] = new LatLng( Double.valueOf(gpsdata.get(i).get("lat")), Double.valueOf(gpsdata.get(i).get("lng")));
                		mo[i] = new MarkerOptions();
                		mo[i].icon(bmpf);
                		mo[i].position(gpsresult[i]);
                		StringBuilder sb_time = null;
                		
                		if (i==0)
                		  {if (bijiaotime(lastack, gpsdata.get(i).get("time")))
                			 {
                			  sb_time = new StringBuilder().append("起: ")
								.append(gpsdata.get(i).get("time")).append("\n止: ")
								.append(lastack);
                			 }else
                			 {
                				 sb_time = new StringBuilder().append("起: ")
         								.append(gpsdata.get(i).get("time")).append("\n止:至今 ");
                			 }
                		  }else if(i==tolres-1){
                			  sb_time = new StringBuilder().append("起: --\n")
      								.append("止: ").append(gpsdata.get(i).get("time"));
                		  }else
                		  {
                			  sb_time = new StringBuilder().append("起: ")
      								.append(gpsdata.get(i).get("time")).append("\n止: ")
      								.append(gpsdata.get(i-1).get("time"));
                		  }
                		mo[i].title(sb_time.toString());
                		
                		String RegeocodeAddress = "";
       				
	       				 try
	       				{
	       				   if (gpsdata.get(i).get("address").equals("nogps"))	 
							 { RegeocodeQuery mRegeocodeQuery = new RegeocodeQuery(new LatLonPoint(Double.valueOf(gpsdata.get(i).get("lat")), Double.valueOf(gpsdata.get(i).get("lng"))), 1000, "AMAP");
	           				 RegeocodeAddress mRegeocodeAddress = new RegeocodeAddress();
	           				
	           				 GeocodeSearch mGeocodeSearch = new GeocodeSearch(this);
	       					mRegeocodeAddress =  mGeocodeSearch.getFromLocation(mRegeocodeQuery);
	       					 RegeocodeAddress = mRegeocodeAddress.getFormatAddress();
							 }else {
							   RegeocodeAddress = gpsdata.get(i).get("address");
							}
	       					
	       					 mapview.getMap().addText(new TextOptions().text("位置"+(i+1)).position(new LatLng(gpsresult[i].latitude-0.0002,gpsresult[i].longitude))
	       								.backgroundColor(0x7d000001).fontColor(0xffffffff).fontSize(densityutil.dip2px(this,10)));
	       					 
	       					StringBuilder sb_Address = new StringBuilder("位置:").append(gpsdata.get(i).get("locationtype"))
	       							                        .append(gpsdata.get(i).get("accuracy")).append("\n");
	       					
							
	       					if (RegeocodeAddress.length()>13 && !RegeocodeAddress.equals(""))
	       					 { int cutlen = 13;
	       					   int startpos = 0;
	       					   for (int j = 0; j < RegeocodeAddress.length()/cutlen; j++)
							{
	       						sb_Address.append(RegeocodeAddress, startpos, startpos + cutlen)
	       						           .append("\n");
	       						startpos = startpos + cutlen;
							}
	       					    sb_Address.append(RegeocodeAddress, startpos,RegeocodeAddress.length());
	       					 }else
	       					  {
	       						sb_Address.append("\n"+RegeocodeAddress);
	       					  }
	       					 
	       					 mo[i].snippet(sb_Address.toString());
	       				} catch (AMapException e)
	       				{
	       					e.printStackTrace();
	       					return false;
	       				}
       				 mk[i] = mapview.getMap().addMarker(mo[i]);	
             		 mk[i].showInfoWindow();
             		
                	//	getIntFromColor(255,0,255)
					}
                	  mapview.getMap().setOnInfoWindowClickListener(new OnInfoWindowClickListener()
					{
						
						@Override
						public void onInfoWindowClick(Marker arg0)
						{
							arg0.hideInfoWindow();
						}
					});
				  }else {
					  runOnUiThread(new Runnable()
						{
							public void run()
							{
							   ImageView img4 = (ImageView)findViewById(R.id.guijiplay);
							   ImageView img5 = (ImageView)findViewById(R.id.guijipause);
							   img4.setVisibility(View.INVISIBLE);
							   img5.setVisibility(View.INVISIBLE);
							}
						});
					  Toast.makeText(TraceActivity.this, "无数据", Toast.LENGTH_LONG).show();
				    }
				} catch (JSONException e)
				{
					e.printStackTrace();
					Toast.makeText(TraceActivity.this, "无数据", Toast.LENGTH_LONG).show();
					return false;
				}
			  	
				if (TraceActivity.isdebug) Log.e("loghere", response);
			}
				
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
			return false;
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
   }
	
	private class dorefreshgps extends Thread
	{

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			super.run();
			
			
			isrefreshgps = getgps();
			if (isrefreshgps)
			{
				mHandler.sendEmptyMessage(REFRESHGPS);
				if (TraceActivity.isdebug) Log.e("gps", "getrefreshgpssessc");
			}
		}
		
	}
	
	
	
	private class dogetbindphone extends Thread
	{

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			super.run();
			getbindphone = getbindphone_new();
			if (getbindphone)
			{
				mHandler.sendEmptyMessage(BINDPHONE);
				if (TraceActivity.isdebug) Log.e("gps", "getbindphonesessc");
			}
		}
		
	}
	
	private class re_bindphone extends Thread
	{

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			super.run();
			try
			{
				Thread.sleep(2000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				mHandler.sendEmptyMessage(BINDPHONE);
				if (TraceActivity.isdebug) Log.e("gps", "getbindphonesessc");
			
		}
		
	}
	
	private void updatemap()
	{
		 if (gpsresult!=null)	
				if ( gpsresult[0].latitude!=0.0 )	
				 {	mapview = (MapView)findViewById(R.id.main_mapView);
					mapview.getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(gpsresult[0], 15f));
				 }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gaode_demo);
		
		if (savedInstanceState!=null )
		{
			   Toast.makeText(TraceActivity.this, "load savedInstanceState ok", Toast.LENGTH_LONG).show();
			   
			if (savedInstanceState.containsKey("select_serial"))
			 {
			    select_serial = savedInstanceState.getString("select_serial");
			    Toast.makeText(TraceActivity.this, select_serial, Toast.LENGTH_LONG).show();
				
			 }
			
			if (savedInstanceState.containsKey("select_pos"))
			 {
				select_pos = savedInstanceState.getInt("select_pos");
			 }
						
			 if (savedInstanceState.containsKey("seriallist"))
				 {
				   seriallist = savedInstanceState.getStringArray("seriallist");
				   String tmp = seriallist[0];
				   seriallist[0] = seriallist[select_pos];
				   seriallist[select_pos] = tmp;
				 }
			 
			 if (savedInstanceState.containsKey("bieming"))
			 {
				 bieming = savedInstanceState.getStringArray("bieming");
				 String tmp = bieming[0];
				 bieming[0] = bieming[select_pos];
				 bieming[select_pos] = tmp;
			 }
			 
			 if (savedInstanceState.containsKey("imeilist"))
			 {
				 imeilist = savedInstanceState.getStringArray("imeilist");
				 String tmp = imeilist[0];
				 imeilist[0] = imeilist[select_pos];
				 imeilist[select_pos] = tmp;
			 }
			 
			 if (savedInstanceState.containsKey("piclist"))
			 {
				 piclist = savedInstanceState.getStringArray("piclist");
			 }
			 
			 if (savedInstanceState.containsKey("isexplist"))
			 {
				 isexplist = savedInstanceState.getStringArray("isexplist");
			 }
			 
			 if (savedInstanceState.containsKey("mlist"))
			 {
				 mlist = savedInstanceState.getStringArray("mlist");
				 String tmp = mlist[0];
				 mlist[0] = mlist[select_pos];
				 mlist[select_pos] = tmp;
			 }
			 
			 getbindphone = true;
			 
		}
		
		URL_Post = getResources().getString(R.string.url)+"/Getbindphone"; 
		
		 owner = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		
		    sp1 = (Spinner)findViewById(R.id.spinner1);
			ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mlist);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp1.setAdapter(adapter);
			if (TraceActivity.isdebug) Log.e("loghere", ""+Thread.currentThread().getId());
		
		Spinner sp2 = (Spinner)findViewById(R.id.spin_num);
		ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, numlist);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp2.setAdapter(adapter2);
		sp2.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
			//	Toast.makeText(TraceActivity.this, "请选择轨迹显示数量", Toast.LENGTH_LONG).show();
				num = numlist[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				
				
			}
		});
		
		Button b_about = (Button)findViewById(R.id.menu_about);
		b_about.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent web = new Intent();
				web.setClass(TraceActivity.this, WebActivity.class);
				String urls = getResources().getString(R.string.url)+getResources().getString(R.string.about);
				web.putExtra("urls", urls);
				startActivity(web);
			}
		});
		
		Button b_thisid = (Button)findViewById(R.id.menu_thisid);
		b_thisid.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent web = new Intent();
				web.setClass(TraceActivity.this, ShowseriActivity.class);
				
				startActivity(web);
			}
		});
		
		Button b_chuji = (Button)findViewById(R.id.menu_chuji);
		b_chuji.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent web = new Intent();
				web.setClass(TraceActivity.this, WebActivity.class);
				String urls = getResources().getString(R.string.url)+getResources().getString(R.string.chuji);
				web.putExtra("urls", urls);
				startActivity(web);
			}
		});
		
		Button b_zhongji = (Button)findViewById(R.id.menu_qna);
		b_zhongji.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent web = new Intent();
				web.setClass(TraceActivity.this, WebActivity.class);
				String urls = getResources().getString(R.string.url)+getResources().getString(R.string.qna);
				web.putExtra("urls", urls);
				startActivity(web);
			}
		});
		
		Button b_gaoji = (Button)findViewById(R.id.menu_gaoji);
		b_gaoji.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent web = new Intent();
				web.setClass(TraceActivity.this, WebActivity.class);
				String urls = getResources().getString(R.string.url)+getResources().getString(R.string.gaoji);
				web.putExtra("urls", urls);
				startActivity(web);
			}
		});
		
		Button b_downapp = (Button)findViewById(R.id.menu_downloadapp);
		b_downapp.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				/*Intent web = new Intent();
				web.setClass(TraceActivity.this, WebActivity.class);
				String urls = getResources().getString(R.string.url)+getResources().getString(R.string.downapp);
				web.putExtra("urls", urls);
				startActivity(web);*/
				
				Intent intent = new Intent();       
		        intent.setAction("android.intent.action.VIEW");   
		        Uri content_url = Uri.parse(getResources().getString(R.string.url)+getResources().getString(R.string.downapp));  
		        intent.setData(content_url); 
		        startActivity(intent);
			}
		});
		
	      ImageView help = (ImageView)findViewById(R.id.help);
		final LinearLayout menu_v = (LinearLayout)findViewById(R.id.menu_v);
		
		help.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				
				if(menu_v.getVisibility()==View.INVISIBLE) 
				  {
					menu_v.setVisibility(View.VISIBLE);
				  }else
				  {
					  menu_v.setVisibility(View.INVISIBLE);
				  }
			}
		});
		
		final ImageView img1 = (ImageView)findViewById(R.id.addphone);
		
		img1.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent mintent2 = new Intent();
				mintent2.setClass(TraceActivity.this, BindActivity.class);
				startActivity(mintent2);
			}
		});
		
         final ImageView img2 = (ImageView)findViewById(R.id.editbindphone);
		
		img2.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if (getbindphone==true)
				{	Intent mintent2 = new Intent();
					mintent2.setClass(TraceActivity.this, ChangebindnameActivity.class);
					startActivity(mintent2);
				}else
				{
					Toast.makeText(TraceActivity.this, "无设备，请先绑定！", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		 final ImageView img4 = (ImageView)findViewById(R.id.guijiplay);
			
			img4.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					if (TraceActivity.isdebug) Log.e("loghere", "play--->click");
					Toast.makeText(TraceActivity.this, "轨迹播放", Toast.LENGTH_SHORT).show();
				  if(mguijiplay==null ) 	
				  { 
					 isstart = true; 
					 mguijiplay = new guijiplay();
					 if (TraceActivity.isdebug)  Log.e("loghere", "play--->new play");
					mguijiplay.start();
				  }
				  
				  ImageView img5 = (ImageView)findViewById(R.id.guijipause);
					ImageView img4 = (ImageView)findViewById(R.id.guijiplay);
					img5.setVisibility(View.VISIBLE);
					img4.setVisibility(View.INVISIBLE);
				}
			});
			
		 final ImageView img5 = (ImageView)findViewById(R.id.guijipause);
				
				img5.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
					
						if (TraceActivity.isdebug) Log.e("loghere", "pause--->click");
						Toast.makeText(TraceActivity.this, "轨迹暂停", Toast.LENGTH_SHORT).show();
					  if(mguijiplay!=null && mguijiplay.isAlive()) 	 
						{ 
						  isstart = false;
						  
						  mguijiplay = null;
						}
					  
					    ImageView img4 = (ImageView)findViewById(R.id.guijiplay);
						ImageView img5 = (ImageView)findViewById(R.id.guijipause);
						img4.setVisibility(View.VISIBLE);
						img5.setVisibility(View.INVISIBLE);
					}
				});
		
		ImageView img3 = (ImageView)findViewById(R.id.mylocation);
		img3.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
			  if(img1.getVisibility()==View.INVISIBLE) 
			  {
				  img1.setVisibility(View.VISIBLE);
			  }else
			  {
				  img1.setVisibility(View.INVISIBLE);
			  }
			  
			  if(img2.getVisibility()==View.INVISIBLE) 
			  {
				  img2.setVisibility(View.VISIBLE);
			  }else
			  {
				  img2.setVisibility(View.INVISIBLE);
			  }
			}
		});
		
		TextView tv_liebiao = (TextView)findViewById(R.id.liebiao);
		tv_liebiao.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent mintent2 = new Intent();
				mintent2.setClass(TraceActivity.this, LiebiaoActivity.class);
				startActivity(mintent2);
			}
		});
		
		mapview = (MapView)findViewById(R.id.main_mapView);
		
		 batterinfo = (TextView)findViewById(R.id.batterinfo);
	
	  if(!(savedInstanceState!=null && savedInstanceState.containsKey("seriallist")))	 
	  {	 dogetbindphone mdogetbindphone = new dogetbindphone();
			mdogetbindphone.start();
	  }else
	  {
		 // Toast.makeText(TraceActivity.this, "re_bindphone now", Toast.LENGTH_LONG).show();
		  new re_bindphone().start();
	  }
		
		Button img_freshgps = (Button)findViewById(R.id.freshgps);
		img_freshgps.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
			  if (!getbindphone)
				{
				  dogetbindphone mdogetbindphone = new dogetbindphone();
				  mdogetbindphone.start();
				}else
				{
					 mapview.getMap().clear();
							 
					 runOnUiThread(new Runnable()
					{
						
						@Override
						public void run()
						{
							isrefreshgps = getgps();
							if (isrefreshgps)
							{
								mHandler.sendEmptyMessage(REFRESHGPS);
								if (TraceActivity.isdebug) Log.e("gps", "getrefreshgpssessc");
							}
						}
					});
				}
			  }	
		});
		
		Button bt_loadserail = (Button)findViewById(R.id.but1);
		bt_loadserail.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				dogetbindphone mdogetbindphone = new dogetbindphone();
				mdogetbindphone.start();
			}
		});
		
		mapview.onCreate(savedInstanceState);
		
		TileOverlay tiOverlay = new TileOverlay(null);
		
		
	//	CameraPosition.builder(s)
		
		mapview.getMap().setOnMapLoadedListener(this);
		mapview.getMap().setOnMarkerClickListener( this);
		
		
	/*	list = new ArrayList<LatLng>();
		for(int i=0;i<trace.length-1;i++){
			LatLng  latlng = new LatLng(trace[i],trace[++i]);
			list.add(latlng);
		}*/
		
		checkupdate t_checkupdate = new checkupdate();
		
		t_checkupdate.start();
		
	}
	
	private Handler mHandler = new Handler()
	  {

		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg!=null)
			{
				switch (msg.what)
				{
				case BINDPHONE:
					setserial();
					break;
				case REFRESHGPS:
					updatemap();
					break;
				case doplay:
					doplay(msg.arg1);
					break;
				case DOWNLOAD_ING:
					updateprogessbar(TraceActivity.progessperct);
					break;
				case DOWNLOAD_FINISH:
					installapk();
					break;
				default:
					break;
				}
			}
		}

	   };
	   
		private void installapk()
		{
			File apkfile = new File(mSavepath,apkname);
			if (!apkfile.exists())
			{
				return;
			}
			Intent ins = new Intent(Intent.ACTION_VIEW);
			ins.setDataAndType(Uri.parse("file://"+apkfile), "application/vnd.android.package-archive");
			startActivity(ins);
		}
	   
	private   class  guijiplay extends Thread
	{   
	
		@Override
		public void run()
		{
			super.run();
			if (TraceActivity.isdebug) Log.e("loghere", "play--->"+nowplayid);
			for (int i = nowplayid; i > -1; i--)
			{
			  if (isstart == true)	
			  {	
				nowplayid = i;
				Message msg = new Message();
				msg.what = doplay;
				msg.arg1 = i;
				mHandler.sendMessage(msg);
				if (TraceActivity.isdebug) Log.e("loghere", "play--->start");

				   try
					{
						Thread.sleep(3000);
						if (TraceActivity.isdebug) Log.e("loghere", "play--->sleep");
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				   
				   if (i == 0)   
					{
						isstart = false;
						mguijiplay = null;
						
					  runOnUiThread(new Runnable()
					{
						public void run()
						{
						   ImageView img4 = (ImageView)findViewById(R.id.guijiplay);
						   ImageView img5 = (ImageView)findViewById(R.id.guijipause);
						   img4.setVisibility(View.VISIBLE);
						   img5.setVisibility(View.INVISIBLE);
						}
					});
						
					  if (gpsdata!=null )
					   if (gpsdata.size() > 0)  
						    nowplayid = gpsdata.size() - 1;
					}
			  }
				
			} 
		}
	}
	
	private void doplay(int pos)
	{
		if (gpsresult!=null)	
			if ( gpsresult[0].latitude!=0.0 )	
			 {	
								
				mapview = (MapView)findViewById(R.id.main_mapView);
				
				mk[pos].showInfoWindow();
				mapview.getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(gpsresult[pos], 15f));
			//	mapview.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(gpsresult[pos], 15f));
			 }
	}
	
	private void setserial()
	{
		// TODO Auto-generated method stub
		 sp1 = (Spinner)findViewById(R.id.spinner1);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mlist);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp1.setAdapter(adapter);
		
		sp1.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				
				 if (mlist[0].equals("无设备列表"))	
					{
					 	return;
					 }
				 
			 if (isexplist[position].equals("n"))
			 {
				 serialno = imeilist[position];
				 
				 select_serial = serialno;
				 
				 select_pos = position;
				 
				 mapview.getMap().clear();
				 
				 runOnUiThread(new Runnable()
				  {
					
					@Override
					public void run()
					{
						isrefreshgps = getgps();
						if (isrefreshgps)
						{
							mHandler.sendEmptyMessage(REFRESHGPS);
							if (TraceActivity.isdebug) Log.e("gps", "getrefreshgpssessc");
						}
					}
				  });
				}else
				{
					Intent web = new Intent();
					web.setClass(TraceActivity.this, ExpWebActivity.class);
					String urls = getResources().getString(R.string.url)+getResources().getString(R.string.isexp) + "?serial=" + serialno;
					web.putExtra("urls", urls);
					startActivity(web);
				}
		      }

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapview.onResume();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		
	}
	
	public int getIntFromColor(int Red, int Green, int Blue){
	    Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
	    Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
	    Blue = Blue & 0x000000FF; //Mask out anything not blue.

	    return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
	}
	
	

	@Override
	public void onMapLoaded() {
		
		CameraPosition cp1 =  mapview.getMap().getCameraPosition();
		mapview.getMap().setTrafficEnabled(false);
		mapview.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(org1,(float) 16.0));
	
	//	mapview.getMap().addText(new TextOptions().text("高德你好").position(org1)
	//			.backgroundColor(getIntFromColor(255,0,255)));
		
		
	//	mapview.getMap().addPolyline(new PolylineOptions().addAll(list));
	//	float zoom =   cp1.zoom;
	//	Toast.makeText(this, String.valueOf(zoom), Toast.LENGTH_LONG).show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// 过滤按键动作
		/*if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

			moveTaskToBack(true);
		}
		return super.onKeyDown(keyCode, event);*/
		 if( keyCode == KeyEvent.KEYCODE_MENU){  
			 final LinearLayout menu_v = (LinearLayout)findViewById(R.id.menu_v);
			 if(menu_v.getVisibility()==View.INVISIBLE) 
			 	 {
			 		menu_v.setVisibility(View.VISIBLE);
			 	  }else
			 	  {
			 	   menu_v.setVisibility(View.INVISIBLE);
			 	  }
	                return true;  
	         }  
	         return super.onKeyDown(keyCode, event);  
	}
	
	

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putStringArray("seriallist", seriallist);
		outState.putStringArray("bieming", bieming);
		outState.putStringArray("imeilist", imeilist);
		outState.putStringArray("piclist", piclist);
		outState.putStringArray("isexplist", isexplist);
		outState.putStringArray("mlist", mlist);
		outState.putString("select_serial", select_serial);
		outState.putInt("select_pos", select_pos);
	}

	@Override
	public void onBackPressed() {

		moveTaskToBack(true);
		super.onBackPressed();
	}

	@Override
	public boolean onMarkerClick(Marker mark)
	{
		
		return false;
		
	}
}
