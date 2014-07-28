package com.example.sdnusdk;
import cn.edu.sdnu.i.util.oauth.AppSDNU;
import cn.edu.sdnu.i.util.oauth.Constants;
import cn.edu.sdnu.i.util.oauth.Oauth;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdnusdk.Utils;


public class WelcomeActivity extends Activity   {
	private String tokenKey ;
	private String tokenSecret;
	private TextView textDisp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		//第一步,设置 key 
		String consumerKey = "key";//
		String consumerSecret = "secret";//		                                           
		AppSDNU.setAppKey(consumerKey, consumerSecret);				
		autoLogin();	
		textDisp = (TextView)findViewById(R.id.textView1);
		((Button)findViewById(R.id.start)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//request token 操作
				  Oauth.startThread(mHandler, null,Oauth.REQ_TOKEN ,WelcomeActivity.this); 
			}
	    	 
	     });

	}	
	private void autoLogin() {
		// TODO Auto-generated method stub	
		//获取上次保存token
		String tokenValue = Utils.getTokenValue(WelcomeActivity.this,Utils.TOKENVALUE,Context.MODE_PRIVATE);
		if(tokenValue.length()>1){
			 tokenKey = tokenValue.substring(0,tokenValue.indexOf("&"));
			 tokenSecret = tokenValue.substring(tokenValue.indexOf("&")+1);
			Oauth.setToken(tokenKey,tokenSecret);
			//做一个刷新操作.
			Oauth.startThread(mHandler,AppSDNU.get(Constants.BASE_URL) + AppSDNU.get(Constants.REF_URL),
																																			Oauth.METHOD,WelcomeActivity.this);
		}
	}
	private  Handler mHandler =new Handler(new Handler.Callback(){		
		@Override
		public boolean  handleMessage(Message msg){
			textDisp.setText((String)msg.obj);
			switch (msg.what) {  			
			 case Oauth.ERROR:
            	Toast.makeText(WelcomeActivity.this, "网络无连接,请检查网络!", Toast.LENGTH_SHORT).show();
            	break;	
			case Oauth.REQ_TOKEN:
				requestToken((String)msg.obj);
				break;
			case Oauth.METHOD:
				doMethod((String)msg.obj);
				break;
            default: 	
                break;  
            } 			
			return false;
		}
	
	}); 
	private void requestToken(String result ) {
		//是否强制显示登录页面,  切换用户或注销时设置为 true 即会重新显示登录页面.
		String url = Oauth.requestTokenUrl(true);
		//能获取到 oauthToken 便表明 request token 成功
		if(url != null)
			openBrowser(url);
	}
	//跳转到  浏览器控件(登录界面.)
		private void openBrowser(String url){		
			Intent intent = new Intent();
			intent.setClass(WelcomeActivity.this, LoginActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("url", url);
			intent.putExtras(bundle);
			startActivity(intent);
			WelcomeActivity.this.finish();
		}
		
		private void doMethod(String result) {
			// TODO Auto-generated method stub
			if(result.contains("11103")){    //11003
				Toast.makeText(WelcomeActivity.this, "密码已过期,请重新登录!",Toast.LENGTH_SHORT).show();
				return;
			}else if(result.contains("10002")){
				Toast.makeText(WelcomeActivity.this, "手机时间错误,请重新校正为北京时间!",Toast.LENGTH_SHORT).show();
				return;
			}else if(result.equals("error")){
				Toast.makeText(WelcomeActivity.this, "网络或服务器无连接!",Toast.LENGTH_SHORT).show();
				return;
			}else if(result.length()>350){
				Toast.makeText(this, "网络接入点异常,请检查网络!", Toast.LENGTH_SHORT).show();
				return;
			}
			if(result.contains(tokenKey))
				//不因切换太快,影响视觉效果,建议加个不影响主线程的延时.不推荐我这个方法.
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				goMainActivity();
		}
		
		
		private void goMainActivity(){
			//直接跳转到 主页面.
			Intent intent = new Intent();
			intent.setClass(WelcomeActivity.this, MainActivity.class);
			startActivity(intent);
			WelcomeActivity.this.finish();	
			Toast.makeText(getApplication(), "自动登录成功", Toast.LENGTH_SHORT).show();
		}

	/*-------------------------------------------下面这部分系统自动生成----------------------------------*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}



}
