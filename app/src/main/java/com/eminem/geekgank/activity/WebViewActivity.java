package com.eminem.geekgank.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.eminem.geekgank.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    @Bind(R.id.tv_toolbar)
    TextView tvToolbar;
    @Bind(R.id.toolbar_web)
    Toolbar toolbarWeb;
    @Bind(R.id.wv_content)
    WebView wvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        ButterKnife.bind(this);
        setSupportActionBar(toolbarWeb);

        Intent intent=getIntent();
        String url = intent.getStringExtra("url");

        //WebView设置
        wvContent.getSettings().setJavaScriptEnabled(true);
        wvContent.getSettings().setSupportZoom(true);
        wvContent.getSettings().setBuiltInZoomControls(true);
//      wvContent.setWebChromeClient(new WebViewClient());
        wvContent.loadUrl(url);
    }

//    private class WebViewClient extends WebChromeClient {
//        @Override
//        public void onProgressChanged(WebView view, int newProgress) {
////            pb.setProgress(newProgress);
////            if(newProgress==100){
////                pb.setVisibility(View.GONE);
////            }
//            super.onProgressChanged(view, newProgress);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            changeTheme();
            return true;
        }else if (id == R.id.action_exit){
            finish();
        }else if(id==R.id.item1){
            Toast.makeText(WebViewActivity.this, "分享", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
