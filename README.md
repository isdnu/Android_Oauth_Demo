智慧山师 Android Oauth认证Demo
===================================
###Demo简要说明:
Demo实现 oauth认证过程,实现自动登录和注销功能,认证部分说明   http://i.sdnu.edu.cn/open/oauth.aspx
###Sdnuapi_1.1 接口说明:
		1
		startThread(Handler mHandler, String url, int flag, Activity activity)
		请求token , 获取数据的统一方法.
		Parameters:
		mHandler ,操作回调的 handler
		url ,操作的url ,请求 token时可以设置为null
		flag ,标识操作类型. Oauth.REQ_TOKEN,Oauth.METHOD,Oauth.ACC_TOKEN,
		activity ,
		2
		setOauthVerifier(String value)   
		从外部设置OauthVerifier 
		Parameters: 
		value  为callback url
		@true 
		为request token成功.
		3
		requestTokenUrl(boolean forcelogin)
		返回 request token url 
		Parameters: 
		forcelogin 是否强制登录 
		Returns: 
		返回智慧山师的用户授权地址
		4
		setToken(String tokenKey, String tokenSecret)
		将储存器保存的 token 使用该方法对Oauth进行设置
		Parameters: 
		tokenKey 
		tokenSecret 
		5
		getToken()
		返回内容格式 tokenKey&tokenSecret. 
		Returns: 
		tokenKey&tokenSecret格式
###程序过程简要说明:
首先程序autoLogin(),查看是否用存储的 token ,如果有<br/>
//设置token<br/>
Oauth.setToken(tokenKey,tokenSecret);<br/>
//做一个刷新操作<br />
Oauth.startThread(mHandler,AppSDNU.get(Constants.BASE_URL) + AppSDNU.get(Constants.REF_URL),Oauth.METHOD,WelcomeActivity.this);<br/>
如果刷新返回的token与请求时token一致,则认为token有效,则直接登录.点击进入登录按钮页,进行 request token操作.<br/>
//request token 操作<br/>
Oauth.startThread(mHandler, null,Oauth.REQ_TOKEN ,WelcomeActivity.this);<br/>
此时会获得 oauth_token,调用requestTokenUrl(boolean forcelogin) 方法,返回用户授权地址形如:<br />
		http://i.sdnu.edu.cn/oauth/authorize?oauth_token=11111111111111111111111111111111<br/>
调用 webview 访问该地址.登录成功会返回 callback地址,形如:<br />
		http://fakeurl.com/callback?from=isdnu#oauth_token=11111111111111111111111111111111&oauth_verifier<br/>
		=aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa<br/>
调用方法 Oauth.setOauthVerifier(url)  ,设置 OauthVerifier.
接着执行Oauth.startThread(mHandler, null,Oauth.ACC_TOKEN,LoginActivity.this);	
用  Request token 换取 Access Token , 认证成功之后 ,保存 token到储存器.用于自动登录.<br />
String token = Oauth.getToken();
Utils.saveTokenValue(LoginActivity.this, token, Utils.TOKENVALUE, Context.MODE_PRIVATE);


注销操作:
//置空操作.注销其实就是 清空  token对应的值.
Oauth.setToken("", "");
//清空储存密钥
Utils.saveTokenValue(MainActivity.this, "", Utils.TOKENVALUE, Context.MODE_PRIVATE);

	