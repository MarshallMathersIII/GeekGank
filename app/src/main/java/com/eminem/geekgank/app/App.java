package com.eminem.geekgank.app;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.umeng.socialize.PlatformConfig;

/**z
 * Created by Eminem on 2016/6/24.
 *
 */
public class App extends Application {
    private static Context context;
    private RequestQueue mRequestQueue;
    private static  App mInstance;
    public static final String TAG = App.class.getName();

    @Override
    public void onCreate() {
        context=getApplicationContext();

        super.onCreate();
        mInstance=this;
        mRequestQueue= Volley.newRequestQueue(getApplicationContext());

        //umeng
        PlatformConfig.setWeixin("wx20622f8025d830ad", "333c572646620f74810b6e20c009728f");//微信 appid appsecret
        PlatformConfig.setSinaWeibo("740802164","0d78e97b6152bf59ab3aff28628e8e9e");//新浪微博 appkey appsecret
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba"); // QQ和Qzone appid appkey
    }

    /**
     * Volley的设置
     */
    public static synchronized App getInstance() {
        return mInstance;
    }
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancel() {
        mRequestQueue.cancelAll(TAG);
    }

    /**
     * 设置全局的Context
     */
    public static Context getContext(){return context;}
}
