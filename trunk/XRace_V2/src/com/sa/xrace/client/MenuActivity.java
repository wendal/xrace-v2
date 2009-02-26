///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id$
 * Last Commit:  $Author$
 * @version $Revision: $
 * 
 */
package com.sa.xrace.client;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sa.xrace.client.toolkit.NetworkToolKit;

/**
 * @author jlin
 * @version $Id$
 */
/*
 * This class is to show the Menu interface, and add the Animation in the ImageView
 * And in the interface we can choose different option. 
 * 
 */
public class MenuActivity extends Activity implements OnClickListener,MediaPlayer.OnPreparedListener{

   
//    private Socket connection;
	private ImageView single,multiple,setting;
	private LinearLayout layout;
	private EditText edit_Name,edit_IP;
	private TextView text_Name,text_IP;
	 
	private TranslateAnimation  animation;
	private int src_width,src_height,b_width = 140,b_height=40;
//	private ProgressBar progressbar;
//	private int mProgressStatus=0;
//	private Handler mHandler;
//	private boolean isChange;
//	public MainPlayer player;
//	public MediaPlayer mp, mp2;
	
	
//	private InforPoolClient  inPool;
//	public ServerListenerImp listener;
//	private PostManagerClient poster;
	
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		src_width = getWindowManager().getDefaultDisplay().getWidth();
		src_height = getWindowManager().getDefaultDisplay().getHeight();
//		mHandler = new Handler();
		
		
		single = (ImageView)findViewById(R.id.single);
		single.setEnabled(false);
		multiple = (ImageView)findViewById(R.id.multiple);
		multiple.setEnabled(false);
		setting = (ImageView)findViewById(R.id.setting);
		setting.setEnabled(false);
		
		single.setOnClickListener(this);
		multiple.setOnClickListener(this);
		setting.setOnClickListener(this);
		

		edit_Name = new EditText(this);
		edit_IP = new EditText(this);
		edit_Name.setText(NetworkToolKit.NAME);
		edit_IP.setText(NetworkToolKit.SERVERIP);
		
		text_Name = new TextView(this);
		text_Name.setText("Login Name");
		text_IP = new TextView(this);
		text_IP.setText("Connect IP");
		
		
		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(text_Name);
		layout.addView(edit_Name);
		layout.addView(text_IP);
		layout.addView(edit_IP);
		Log.e("-----------Menu","Here");
//		DataUnti.init(this);
		startMove(single,0,src_width,src_width-150,src_height/3,src_height/3);
		
		//释放全部对象
//		releaseAllObject();
	}
	
	private void releaseAllObject(){
	    edit_IP =null;
	    text_IP = null;
	    edit_Name = null;
	    text_Name = null;
	    layout = null;
	    animation = null;
	    setting = null;
	    single = null;
	    multiple= null;
	}
	
	
/*
 *  @param  ImageView <p> receive a ImageView  </p>
 *  
 *  set the ImageView initial value and the Animation initial value
 */
	private void startMove(ImageView b,int n,float startX, float toX, float startY,float toY){
	    animation = new TranslateAnimation(startX,toX,startY,startY);
		animation.setDuration(300);
//		animation.setFillAfter(true);
		animation.setAnimationListener(new DisplayNextButton(n));
		b.setAnimation(animation);
		
		animation.startNow();
	}
	
/*
 * The class is used to implements Animation.AnimationListener,that when the Animation
 * move end the class will run
 */
	private class DisplayNextButton implements Animation.AnimationListener {
		int index;
		public DisplayNextButton(int index){
			this.index = index;
		}
		public void onAnimationEnd(Animation animation) {
			switch(index){
				case 0:{
					single.setLayoutParams(new AbsoluteLayout.LayoutParams(b_width, b_height, src_width-150,src_height/3));
					single.setVisibility(View.VISIBLE);
					startMove(multiple,1,src_width,src_width-170,src_height/3+60,src_height/3+60);
					break;
				}
				case 1:{
					multiple.setLayoutParams(new AbsoluteLayout.LayoutParams(b_width, b_height, src_width-170,src_height/3+60));
					multiple.setVisibility(View.VISIBLE);
					startMove(setting,2,src_width,src_width-190,src_height/3+120,src_height/3+120);
					break;
				}
				case 2:{
					setting.setLayoutParams(new AbsoluteLayout.LayoutParams(b_width, b_height, src_width-190,src_height/3+120));
					setting.setVisibility(View.VISIBLE);
					single.setEnabled(true);
					multiple.setEnabled(true);
					setting.setEnabled(true);
					break;
				}
				
			}
		}

		public void onAnimationRepeat(Animation animation) {
		}

		public void onAnimationStart(Animation arg0) {
		}
		
	}
	public void onClick(View arg0) {
		
		if(arg0==single){
//			Intent intent = new Intent();
//			intent.setClass(MenuActivity.this, SingleGame.class);
//			startActivity(intent);
//			finish();
			
		}
		else if(arg0==multiple){
			showDialog(0);
		}
		else if(arg0==setting){
			 
		}
	}
	private String cutNames(String name){
		String tem = "";
		tem = name.replaceAll("-","");
		if(tem.length()==0){
			tem="a";
		}
		else if(tem.length()>10){
			tem = tem.substring(0, 10);
		}
		return tem;
	}
		
	
	protected Dialog onCreateDialog(int id) {
		return new AlertDialog.Builder(MenuActivity.this).setIcon(
				R.drawable.alert_dialog_icon).setTitle("Connect Setting").setView(layout)
				.setPositiveButton(R.string.alert_dialog_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							    NetworkToolKit.NAME = cutNames(edit_Name.getText().toString());
							    NetworkToolKit.SERVERIP = edit_IP.getText().toString();
								
							    //释放全部对象
							    releaseAllObject();
							    
								Intent intent = new Intent();
								intent.setClass(MenuActivity.this, GameActivity.class);
//								Uri n = Uri.parse(Uri.encode(NAME+"@&"+SERVERIP));
//								intent.setData(n);
								startActivity(intent);
								finish();
								
								
							}
						}).setNegativeButton(R.string.alert_dialog_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						}).create();
	}

	

	
	protected void onPause() {
//		if (!isChange) {
//			MainPlayer.mp.pause();
//			MainPlayer.isBegin = true;
//			finish();
//		}
		super.onPause();
	}
	
	/**
	 * Called when the activity is no longer visible to the user. Stop the MainPlayer
	 */


	public void onPrepared(MediaPlayer arg0) {
//		Log.v("onPrepared= ","------");
//		if (MainPlayer.isBegin || MainPlayer.music) {
//			MainPlayer.mp.start();
//			MainPlayer.isBegin = false;
//		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#getRequestedOrientation()
	 */
	@Override
	public int getRequestedOrientation() {
		// TODO Auto-generated method stub
		return super.getRequestedOrientation();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#setRequestedOrientation(int)
	 */
	@Override
	public void setRequestedOrientation(int requestedOrientation) {
		// TODO Auto-generated method stub
		super.setRequestedOrientation(requestedOrientation);
	}
	
	
	


}