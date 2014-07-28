2014.7.26
使用 sdnuapi 库,只需要把 sdnuapi_1.1.jar 和     libappsdnu.so放到 libs文件夹下即可.
对于混淆操作,加入几两句

#山东师范大学认证相关
-libraryjars libs/sdnuapi_1.1.jar
-keepclassmembers class cn.edu.sdnu.i.util.oauth.Oauth{
   public void startThread(android.os.Handler,java.lang.String,android.app.Activity);
}   
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembers class * {
    native <methods>;
}											