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
      
    //引导图片资源  
    private static final int[] pics = { R.drawable.l_1,  
            R.drawable.l_2, R.drawable.l_3,  
            R.drawable.l_4 };  
      
    //底部小店图片  
    private ImageView[] dots ;  
      
    //记录当前选中位置  
    private int currentIndex;  
      
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        
        SharedPreferences preferences = getSharedPreferences("firstload",MODE_MULTI_PROCESS );   
        int count = preferences.getInt("firstload", 0);    
           
        //判断程序与第几次运行，如果是第一次运行则跳转到引导页面     
        if (count != 0) {    
        Intent intent = new Intent();   
        intent.setClass(getApplicationContext(),TraceActivity.class);    
        startActivity(intent);    
        this.finish();    
        }    
        
        Editor editor = preferences.edit();    
        //存入数据      
        editor.putInt("firstload", ++count);    
        //提交修改      
        editor.commit();
        
        setContentView(R.layout.launcherview);  
          
        views = new ArrayList<View>();  
         
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,  
                LinearLayout.LayoutParams.WRAP_CONTENT);  
          
        //初始化引导图片列表  
        for(int i=0; i<pics.length; i++) {  
            ImageView iv = new ImageView(this);  
            iv.setLayoutParams(mParams);  
            iv.setImageResource(pics[i]);  
            views.add(iv);  
        }  
        vp = (ViewPager) findViewById(R.id.viewpager);  
        //初始化Adapter  
        vpAdapter = new ViewPagerAdapter(views);  
        vp.setAdapter(vpAdapter);  
        //绑定回调  
        vp.setOnPageChangeListener(this);  
          
        //初始化底部小点  
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
  
        //循环取得小点图片  
        for (int i = 0; i < pics.length; i++) {  
            dots[i] = (ImageView) ll.getChildAt(i);  
            dots[i].setEnabled(true);//都设为灰色  
            dots[i].setOnClickListener(this);  
            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应  
        }  
  
        currentIndex = 0;  
        dots[currentIndex].setEnabled(false);//设置为白色，即选中状态  
    }  
      
    /** 
     *设置当前的引导页  
     */  
    private void setCurView(int position)  
    {  
        if (position < 0 || position >= pics.length) {  
            return;  
        }  
  
        vp.setCurrentItem(position);  
    }  
  
    /** 
     *这只当前引导小点的选中  
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
  
    //当滑动状态改变时调用  
    @Override  
    public void onPageScrollStateChanged(int arg0) {  
        // TODO Auto-generated method stub  
          
    }  
  
    //当当前页面被滑动时调用  
    @Override  
    public void onPageScrolled(int arg0, float arg1, int arg2) {  
        // TODO Auto-generated method stub  
          
    }  
  
    //当新的页面被选中时调用  
    @Override  
    public void onPageSelected(int arg0) {  
        //设置底部小点选中状态  
        setCurDot(arg0);  
    }  
  
    @Override  
    public void onClick(View v) {  
        int position = (Integer)v.getTag();  
        setCurView(position);  
        setCurDot(position);  
    }  
}  
