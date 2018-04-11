package com.cc;

import java.io.File;

import com.cc.R;

import database.BaseDao;
import database.BaseDaoImpl;
import database.SqLiteControl;

import net.MSG;
import service.NetService;
import util.tools.MyJson;
import util.tools.Tools;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;




 
/**
 * @author Walker
 * @date 2017-2-18 下午1:06:34
 * Description: 程序入口，根据状态转发目标地点
 */
public class StartAc extends BaseAc {

	@Override
	public void OnCreate(Bundle savedInstanceState) {
		//setContentView(R.layout.ac_start);
		setContentView(R.layout.ac_start);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		Constant.screenH = dm.heightPixels;
		Constant.screenW = dm.widthPixels;
		  
		//初始化数据库表
		initDatabaseTable();
		initFileDir();
		//初始化时启动网络后台服务
		startService();
		Tools.out("StartAc.oncreate");
		//逻辑处理，若没有登陆账号则跳转到 登陆界面
	 
		
		startActivity(new Intent(StartAc.this, LoginAc.class));
		this.finish(); 
		
	} 
	public void initFileDir(){
//		public static final String root = Environment.getExternalStorageDirectory() + "/mycc/";  
//		public static final String dirVoice = root + "record/";  
//		public static final String dirPhoto =  root + "photo/";  
//		public static final String dirFile =  root + "file/";  
//		public static final String dirCamera = root +  "camera/";  
//		public static final String dirProfile = root +  "profile/";  
//		public static final String dirProfileWall = root +  "profilewall/";
		String dirs[] =  {Constant.dirVoice, Constant.dirPhoto, Constant.dirFile, 
				Constant.dirCamera, Constant.dirProfile, Constant.dirProfileWall  };
		for(String str: dirs){
			File file = new File(str);
			if(!file.exists()){
				file.mkdirs();
			}
		}
		
		
	}

	 
	
	//初始化数据库表
	public void initDatabaseTable(){
		 
		//sqlDao.execSQL("drop table login_user");
		sqlDao.execSQL("create table if not exists login_user (id varchar(30) primary key, pwd varchar(50), profilepath varchar(200) ) ");
		 
		
		
	}
	// 开启网络服务后台
	public void startService() {
		Intent i = new Intent(this,  NetService.class);
		startService(i);
	}
	
	@Override
	public void callback(String jsonstr) { 
		Tools.out("StartAc.callback."+jsonstr);
		
		//Tools.toast(this, MyJson.getValue0(jsonstr)); 
		
		switch (MyJson.getCmd(jsonstr)) {
		case MSG.OK: 
			startActivity(new Intent(StartAc.this, LoginAc.class));
			this.finish(); 
			break;
		
		}
		
		
	}


 

	@Override
	public boolean OnBackPressed() { 
		this.exitApp();
		return false;
	}



 
}