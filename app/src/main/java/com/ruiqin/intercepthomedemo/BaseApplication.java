package com.ruiqin.intercepthomedemo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * @version 1.0
 * @author：ruiqin.shen
 * @since：2018/2/27
 */

public class BaseApplication extends Application {

    private Context context;

    public static int activityAount;

    /**
     * onCreate是一个回调接口，android系统会在应用程序启动的时候，在任何应用程序组件（activity、服务、
     * 广播接收器和内容提供者）被创建之前调用这个接口。
     * 需要注意的是，这个方法的执行效率会直接影响到启动Activity等的性能，因此此方法应尽快完成。
     * 最后在该方法中，一定要记得调用super.onCreate()，否则应用程序将会报错。
     */
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //注册自己的Activity的生命周期回调接口。
//        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }


    @Override
    public void onTerminate() {
//        //注销这个接口。
//        unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        super.onTerminate();
    }

    private static final String TAG = "MyApplication";

    private ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        /**
         * application下的每个Activity声明周期改变时，都会触发以下的函数。
         */
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.e(TAG, "onActivityStarted.");

            activityAount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.e(TAG, "onActivityResumed.");
        }

        long pauseTime;

        @Override
        public void onActivityPaused(Activity activity) {

            pauseTime = System.currentTimeMillis();
            Log.e(TAG, "onActivityPaused.");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            activityAount--;
            if (activityAount == 0) {
                Toast.makeText(getApplicationContext(), "切入后台", Toast.LENGTH_SHORT).show();
                backForeground();
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.e(TAG, "onActivityDestroyed.");
        }
    };


    public void backForeground() {
        //获取ActivityManager
        ActivityManager mAm = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获得当前运行的task
        List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo rti : taskList) {
            //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
            if (rti.topActivity.getPackageName().equals(context.getPackageName())) {
                try {
                    Intent resultIntent = new Intent(context, Class.forName(rti.topActivity.getClassName()));
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT);
                    pendingIntent.send();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        //若没有找到运行的task，用户结束了task或被系统释放，则重新启动mainactivity

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }


    public void backForeground2() {
        //获取ActivityManager
        ActivityManager mAm = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获得当前运行的task
        List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo rti : taskList) {
            //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
            if (rti.topActivity.getPackageName().equals(context.getPackageName())) {
                mAm.moveTaskToFront(rti.id, ActivityManager.MOVE_TASK_WITH_HOME);
                return;
            }
        }
        //若没有找到运行的task，用户结束了task或被系统释放，则重新启动mainactivity
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(resultIntent);
    }
}
