package com.eminem.geekgank.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eminem.geekgank.R;
import com.eminem.geekgank.app.App;

import uk.co.senab.photoview.PhotoViewAttacher;

public class PreviewActivity extends AppCompatActivity {



    ImageView mImageView;
    PhotoViewAttacher mAttacher;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
//        setSupportActionBar(toolbarWeb);
//        toolbarWeb.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
//        toolbarWeb.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                onBackPressed();
//                finish();
//            }
//        });

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        mImageView = (ImageView) findViewById(R.id.iv_photo);

        Glide.with(App.getContext())
                .load(url)
//                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);

        mAttacher = new PhotoViewAttacher(mImageView);


//        mAttacher.update();
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
            Toast.makeText(App.getContext(), "分享", Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }
}
