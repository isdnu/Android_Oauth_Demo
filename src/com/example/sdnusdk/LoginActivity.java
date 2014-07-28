package com.example.sdnusdk;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import cn.edu.sdnu.i.util.oauth.Oauth;

import com.example.sdnusdk.R;

public class LoginActivity extends  Activity{
	private WebView show;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.loginactivity);
		
		Bundle bundle = this.getIntent().getExtras();
		String urlStr = bundle.getString("url");
		//初始化 webview
		initWebView(urlStr);	
		//恢复 上次的token	
	}

	private  Handler mHandler =new Handler(new Handler.Callback(){			
		@Override
		public boolean  handleMessage(Message msg){

			switch (msg.what) {  			
			 case  Oauth.ERROR:
            	Toast.makeText(LoginActivity.this, "网络无连接,请检查网络!", Toast.LENGTH_SHORT).show();
            	break;	
			case Oauth.ACC_TOKEN:
				decodeAccessToken();
            default: 	
                break;  
            } 			
			return false;
		}	
	}); 
	
	
	private void decodeAccessToken( ) {
		// TODO Auto-generated method stub
		//转到主页 .
		//保存token对应值 .用于自动登录.
		String token = Oauth.getToken();
		Utils.saveTokenValue(LoginActivity.this, token, Utils.TOKENVALUE, Context.MODE_PRIVATE);
		
		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, MainActivity.class);
		startActivity(intent);
		LoginActivity.this.finish();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView(String urlStr){
		// 获取页面中文本框、WebView组件
		show = (WebView) findViewById(R.id.show);		
		show.setWebViewClient(new WebViewClient(){	
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub		
				String subcallbackUrl = "login_custom.jsp?userip=";;
				if (url.contains(subcallbackUrl)) {
		            view.stopLoading();
		            view.loadUrl("about:blank");
					Toast.makeText(LoginActivity.this,"加载失败,请重试!", Toast.LENGTH_SHORT).show();
		            return true;
		        }else if (url.contains("http://fakeurl.com/callback?from=isdnu")){	        	      	
		    		if(Oauth.setOauthVerifier(url)){
		    			//Request token 换取 Access Token 
		    			//能执行到这里表明,可以正确执行 access token 方法.
		    			Oauth.startThread(mHandler, null,Oauth.ACC_TOKEN,LoginActivity.this);	
		    			Toast.makeText(LoginActivity.this, "登录成功,跳转到主页!", Toast.LENGTH_SHORT).show();	    					
		    		}
		        }
		        return false;
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub		
				super.onPageFinished(view, url);
			}		
		});
		//支持 js
		show.getSettings().setJavaScriptEnabled(true);		
		show.loadUrl(urlStr);	
	}
	
	
	@Override
	public void onResume() {
		super.onResume();		
	}

	public void onPause() {
		super.onPause();
		
	}		
}