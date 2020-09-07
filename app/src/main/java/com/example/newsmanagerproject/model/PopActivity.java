package com.example.newsmanagerproject.model;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.newsmanagerproject.R;

public class PopActivity extends Activity {

    Dialog myDialog;
    private Article article_created;
    private TextView category_text, title_text, abstract_text, subtitle_text, body_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        //To Display the window
        myDialog=new Dialog(this);
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//
//        getWindow().setLayout((int)(width*8),(int)(height*0.6));

        Log.i("TAG", "THe category is ->" + article_created.getCategory());

        article_created = (Article) getIntent().getSerializableExtra("ArticleCreated");


        category_text = findViewById(R.id.category_created);
        category_text.setText(article_created.getCategory());

        title_text = findViewById(R.id.title_created);
        title_text.setText(article_created.getTitleText());

        abstract_text = findViewById(R.id.abstract_created);
        abstract_text.setText(article_created.getAbstractText());

        subtitle_text = findViewById(R.id.subtitle_created);
        subtitle_text.setText(article_created.getSubtitleText());

        body_text = findViewById(R.id.body_created);
        body_text.setText(article_created.getBodyText());
    }
    public void ShowPopUp(View v){
        myDialog.setContentView(R.layout.activity_popup);
    }
}
