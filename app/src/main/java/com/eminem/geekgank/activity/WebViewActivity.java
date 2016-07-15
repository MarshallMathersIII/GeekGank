package com.eminem.geekgank.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eminem.geekgank.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {
    @Bind(R.id.pb)
    ProgressBar pb;
    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.tv_toolbar)
    TextView tvToolbar;
    @Bind(R.id.toolbar_web)
    Toolbar toolbarWeb;


    private String url;
    private String desc;
    final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        ButterKnife.bind(this);
        setSupportActionBar(toolbarWeb);
        pb.setMax(100);
//        toolbarWeb.setNavigationIcon(R.drawable.back1);
//        toolbarWeb.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//
//            }
//        });

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        desc = intent.getStringExtra("desc");


        //WebView设置
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setWebChromeClient(new WebViewClient());
        webview.loadUrl(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
//            changeTheme();
            return true;
        } else if (id == R.id.action_exit) {
            finish();
        } else if (id == R.id.item1) {
            Toast.makeText(WebViewActivity.this, "分享", Toast.LENGTH_SHORT).show();
            ShareAction action = new ShareAction(WebViewActivity.this);
            action.setDisplayList(displaylist);
            action.withTitle("分享标题");
            action.withText(desc);
            action.withTargetUrl(url);//点击分享内容打开的链接
//          action.withMedia(UMImage);//附带的图片，音乐，视频等多媒体对象
//          action.setShareboardclickCallback();//设置友盟集成的分享面板的点击监听回调
            action.open();//打开集成的分享面板
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 友盟分享面板PopupWindow监听器
     */
    private ShareBoardlistener mShareBoardlistener = new ShareBoardlistener() {

        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
            ShareAction shareAction = new ShareAction(WebViewActivity.this);
            shareAction.setPlatform(share_media);
            shareAction.setCallback(mUmShareListener);//设置每个平台的点击事件
            shareAction.withTitle("分享标题");
            shareAction.withText("分享文本内容");
            shareAction.withTargetUrl("http://blog.xiongit.com");//点击分享内容打开的链接
//          shareAction.withMedia(UMImage);//附带的图片，音乐，视频等多媒体对象
            shareAction.share();//发起分享，调起微信，QQ，微博客户端进行分享。
        }
    };
// title在微博中无用，text在朋友圈中无用。但是加上也不影响分享。

    /**
     * 友盟分享后事件监听器
     */
    private UMShareListener mUmShareListener = new UMShareListener() {

        @Override
        public void onResult(SHARE_MEDIA platform) {
//            mTempActivity = null;
            // TODO 分享成功
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            mTempActivity = null;
            // TODO 分享失败
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            mTempActivity = null;
            // TODO 分享取消
        }
    };

    /**
     * 自定义webview
     */
    class WebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            pb.setProgress(newProgress);
            if(newProgress==100){
                pb.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}
