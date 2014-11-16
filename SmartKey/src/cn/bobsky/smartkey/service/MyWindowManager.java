package cn.bobsky.smartkey.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import cn.bobsky.smartkey.view.FloatKeyView;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;

public class MyWindowManager {  
  
    /** 
     * С������View��ʵ�� 
     */  
    private static FloatKeyView smallWindow;  
    /** 
     * С������View�Ĳ��� 
     */  
    private static LayoutParams smallWindowParams;  
  
    /** 
     * ��������View�Ĳ��� 
     */  
    private static LayoutParams bigWindowParams;  
  
    /** 
     * ���ڿ�������Ļ����ӻ��Ƴ������� 
     */  
    private static WindowManager mWindowManager;  
  
    /** 
     * ���ڻ�ȡ�ֻ������ڴ� 
     */  
    private static ActivityManager mActivityManager;  
  
    /** 
     * ����һ��С����������ʼλ��Ϊ��Ļ���Ҳ��м�λ�á� 
     *  
     * @param context 
     *            ����ΪӦ�ó����Context. 
     */  
    public static void createSmallWindow(Context context) {  
        WindowManager windowManager = getWindowManager(context);  
        int screenWidth = windowManager.getDefaultDisplay().getWidth();  
        int screenHeight = windowManager.getDefaultDisplay().getHeight();  
        if (smallWindow == null) {  
            smallWindow = new FloatKeyView(context);  
            if (smallWindowParams == null) {  
                smallWindowParams = new LayoutParams();  
                smallWindowParams.type = LayoutParams.TYPE_SYSTEM_ALERT;  
               // smallWindowParams.format = PixelFormat.RGBA_8888; 
                smallWindowParams.format = PixelFormat.TRANSLUCENT;
                smallWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE ;  
                smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;  
                smallWindowParams.width = FloatKeyView.viewWidth;  
                smallWindowParams.height = FloatKeyView.viewHeight;  
                smallWindowParams.x = screenWidth;  
                smallWindowParams.y = screenHeight / 2;  
            }  
            smallWindow.setParams(smallWindowParams);  
            windowManager.addView(smallWindow, smallWindowParams); 
         
        }  
    }  
  
    /** 
     * ��С����������Ļ���Ƴ��� 
     *  
     * @param context 
     *            ����ΪӦ�ó����Context. 
     */  
    public static void removeSmallWindow(Context context) {  
        if (smallWindow != null) {  
            WindowManager windowManager = getWindowManager(context);  
            windowManager.removeView(smallWindow);  
            smallWindow = null;  
        }  
    }  
  
    /** 
     * �Ƿ���������(����С�������ʹ�������)��ʾ����Ļ�ϡ� 
     *  
     * @return ����������ʾ�������Ϸ���true��û�еĻ�����false�� 
     */  
    public static boolean isWindowShowing() {  
        return smallWindow != null ;  
    }  
  
    /** 
     * ���WindowManager��δ�������򴴽�һ���µ�WindowManager���ء����򷵻ص�ǰ�Ѵ�����WindowManager�� 
     *  
     * @param context 
     *            ����ΪӦ�ó����Context. 
     * @return WindowManager��ʵ�������ڿ�������Ļ����ӻ��Ƴ��������� 
     */  
    private static WindowManager getWindowManager(Context context) {  
        if (mWindowManager == null) {  
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);  
        }  
        return mWindowManager;  
    }  
  
    /** 
     * ���ActivityManager��δ�������򴴽�һ���µ�ActivityManager���ء����򷵻ص�ǰ�Ѵ�����ActivityManager�� 
     *  
     * @param context 
     *            �ɴ���Ӧ�ó��������ġ� 
     * @return ActivityManager��ʵ�������ڻ�ȡ�ֻ������ڴ档 
     */  
    private static ActivityManager getActivityManager(Context context) {  
        if (mActivityManager == null) {  
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);  
        }  
        return mActivityManager;  
    }  
  
    /** 
     * ������ʹ���ڴ�İٷֱȣ������ء� 
     *  
     * @param context 
     *            �ɴ���Ӧ�ó��������ġ� 
     * @return ��ʹ���ڴ�İٷֱȣ����ַ�����ʽ���ء� 
     */  
    public static String getUsedPercentValue(Context context) {  
        String dir = "/proc/meminfo";  
        try {  
            FileReader fr = new FileReader(dir);  
            BufferedReader br = new BufferedReader(fr, 2048);  
            String memoryLine = br.readLine();  
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));  
            br.close();  
            long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));  
            long availableSize = getAvailableMemory(context) / 1024;  
            int percent = (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);  
            return percent + "%";  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return "������";  
    }  
  
    /** 
     * ��ȡ��ǰ�����ڴ棬�����������ֽ�Ϊ��λ�� 
     *  
     * @param context 
     *            �ɴ���Ӧ�ó��������ġ� 
     * @return ��ǰ�����ڴ档 
     */  
    private static long getAvailableMemory(Context context) {  
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();  
        getActivityManager(context).getMemoryInfo(mi);  
        return mi.availMem;  
    }  
    
    
  
}