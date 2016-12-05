package com.sprocomm.utils;

import java.lang.reflect.Method;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;

public class ApkRunInstallUtil {
	
	private static final String TAG = "InstallPlay3DApp";
	private static final String MTAG = "AgingTest";
	
	public ApkRunInstallUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
     * 启动App
     * @param context
     */
    public static void RunApp(Context context,String packageName,String packageActivity) {
    	
    	Log.d(MTAG, TAG + "---RunApp start---");
    	
    	//无法启动去掉"android.intent.category.LAUNCHER"的apk
      // context.startActivity(context.getPackageManager().getLaunchIntentForPackage(packageName));
    	Intent intent = new Intent();  
        ComponentName comp = new ComponentName(packageName,packageActivity);  
        intent.setComponent(comp);  
        intent.setAction("android.intent.action.MAIN");  
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        context.startActivity(intent);
    }

    /**
     * 检测某个应用是否安装
     * 
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
    	Log.d(MTAG, TAG + "---isAppInstalled packageName---" +packageName );
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

   
    /** 
     * 获取文件安装的Intent 
     *  
     * @param file 
     * @return 
     */  
    public static void MakeApk(Context context,String packagePath) {  
       // Uri uri = Uri.fromFile(file);  
    	Uri uri = Uri.parse(packagePath);
        String type = "application/vnd.android.package-archive";  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.addCategory("android.intent.category.DEFAULT");  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        intent.setDataAndType(uri, type);  
        context.startActivity(intent); 
    }  
    
   
    /**
    *强制停止应用程序
    * @param pkgName
    * 需要添加---android:sharedUserId="android.uid.system"
    * <uses-permission android:name="android.permission.FORCE_STOP_PACKAGES"/>
    * 
    */
	public static void forceStopPackage(Context context, String pkgName) throws Exception {
		Log.d(MTAG, TAG + "---forceStopPackage---");
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
		method.invoke(am, pkgName);
	}
    
    /** 
     * 判断应用是否正在运行 
     *  
     * @param context 
     * @param packageName 
     * @return 
     */  
	public static boolean isRunning(Context context, String packageName) {
		Log.d(MTAG, TAG + "--- isRunning---");
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
		Log.d(MTAG, TAG + "---  list--->");
		for (RunningAppProcessInfo appProcess : list) {
			String processName = appProcess.processName;
			if (processName != null && processName.equals(packageName)) {
				return true;
			}
		}
		return false;
		
		/*ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		for (RunningTaskInfo info : list){
			if (info.topActivity.getPackageName().equals(packageName) || info.baseActivity.getPackageName().equals(packageName)){
				return true;
			} 
		}
		return false;*/
	}
	
	 /** 
     * 判断应用是否已安装 
     *  
     * @param context 
     * @param packageName 
     * @return 
     */  
    private static boolean isInstalled(Context context, String packageName) {  
        boolean hasInstalled = false;  
        PackageManager pm = context.getPackageManager();  
        List<PackageInfo> list = pm  
                .getInstalledPackages(PackageManager.PERMISSION_GRANTED);  
        for (PackageInfo p : list) {  
            if (packageName != null && packageName.equals(p.packageName)) {  
                hasInstalled = true;  
                break;  
            }  
        }  
        return hasInstalled;  
    }  

}
