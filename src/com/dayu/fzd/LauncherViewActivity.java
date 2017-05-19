package com.dayu.fzd;

import java.util.ArrayList;  
import java.util.List;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;  
import android.support.v4.view.ViewPager;  
import android.support.v4.view.ViewPager.OnPageChangeListener;  
import android.view.View;  
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;  
import android.widget.LinearLayout;  
  
public class LauncherViewActivity extends Activity implements OnClickListener, OnPageChangeListener{  
      
    private ViewPager vp;  
    private ViewPagerAdapter vpAdapter;  
    private List<View> views;  
      
    //����ͼƬ��Դ  
    private static final int[] pics = { R.drawable.l_1,  
            R.drawable.l_2, R.drawable.l_3,  
            R.drawable.l_4 };  
      
    //�ײ�С��ͼƬ  
    private ImageView[] dots ;  
      
    //��¼��ǰѡ��λ��  
    private int currentIndex;  
      
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        
        SharedPreferences preferences = getSharedPreferences("firstload",MODE_MULTI_PROCESS );   
        int count = preferences.getInt("firstload", 0);    
           
        //�жϳ�����ڼ������У�����ǵ�һ����������ת������ҳ��     
        if (count != 0) {    
        Intent intent = new Intent();   
        intent.setClass(getApplicationContext(),TraceActivity.class);    
        startActivity(intent);    
        this.finish();    
        }    
        
        Editor editor = preferences.edit();    
        //��������      
        editor.putInt("firstload", ++count);    
        //�ύ�޸�      
        editor.commit();
        
        setContentView(R.layout.launcherview);  
          
        views = new ArrayList<View>();  
         
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,  
                LinearLayout.LayoutParams.WRAP_CONTENT);  
          
        //��ʼ������ͼƬ�б�  
        for(int i=0; i<pics.length; i++) {  
            ImageView iv = new ImageView(this);  
            iv.setLayoutParams(mParams);  
            iv.setImageResource(pics[i]);  
            views.add(iv);  
        }  
        vp = (ViewPager) findViewById(R.id.viewpager);  
        //��ʼ��Adapter  
        vpAdapter = new ViewPagerAdapter(views);  
        vp.setAdapter(vpAdapter);  
        //�󶨻ص�  
        vp.setOnPageChangeListener(this);  
          
        //��ʼ���ײ�С��  
        initDots();  
        
        Button but1 = (Button)findViewById(R.id.btn_iknow);
        but1.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent mainactivity = new Intent();
				mainactivity.setClass(LauncherViewActivity.this, TraceActivity.class);
				startActivity(mainactivity);
			}
		});
          
    }  
      
    private void initDots() {  
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);  
  
        dots = new ImageView[pics.length];  
  
        //ѭ��ȡ��С��ͼƬ  
        for (int i = 0; i < pics.length; i++) {  
            dots[i] = (ImageView) ll.getChildAt(i);  
            dots[i].setEnabled(true);//����Ϊ��ɫ  
            dots[i].setOnClickListener(this);  
            dots[i].setTag(i);//����λ��tag������ȡ���뵱ǰλ�ö�Ӧ  
        }  
  
        currentIndex = 0;  
        dots[currentIndex].setEnabled(false);//����Ϊ��ɫ����ѡ��״̬  
    }  
      
    /** 
     *���õ�ǰ������ҳ  
     */  
    private void setCurView(int position)  
    {  
        if (position < 0 || position >= pics.length) {  
            return;  
        }  
  
        vp.setCurrentItem(position);  
    }  
  
    /** 
     *��ֻ��ǰ����С���ѡ��  
     */  
    private void setCurDot(int positon)  
    {  
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {  
            return;  
        }  
  
        dots[positon].setEnabled(false);  
        dots[currentIndex].setEnabled(true);  
  
        currentIndex = positon;  
    }  
  
    //������״̬�ı�ʱ����  
    @Override  
    public void onPageScrollStateChanged(int arg0) {  
        // TODO Auto-generated method stub  
          
    }  
  
    //����ǰҳ�汻����ʱ����  
    @Override  
    public void onPageScrolled(int arg0, float arg1, int arg2) {  
        // TODO Auto-generated method stub  
          
    }  
  
    //���µ�ҳ�汻ѡ��ʱ����  
    @Override  
    public void onPageSelected(int arg0) {  
        //���õײ�С��ѡ��״̬  
        setCurDot(arg0);  
    }  
  
    @Override  
    public void onClick(View v) {  
        int position = (Integer)v.getTag();  
        setCurView(position);  
        setCurDot(position);  
    }  
}  
