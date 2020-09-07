package com.example.newsmanagerproject.model;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsmanagerproject.R;
import com.example.newsmanagerproject.network.errors.ServerComnmunicationError;
import com.google.android.material.snackbar.Snackbar;

public class NewsArticle extends AppCompatActivity {
    private Article articleNews;
    private TextView category;
    private TextView subtitle;
    private TextView textAbstract;
    private TextView textBody;
    private TextView textTitle;
    private ImageView articleImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);

        articleNews=(Article)getIntent().getSerializableExtra("Article");

        textTitle=(TextView) findViewById(R.id.text_title);
        textTitle.setText(articleNews.getTitleText());

        category=(TextView) findViewById(R.id.text_category);
        category.setText(articleNews.getCategory());

        subtitle=(TextView) findViewById(R.id.text_subtitle);
        subtitle.setText(articleNews.getSubtitleText());

        textAbstract=(TextView) findViewById(R.id.text_abstract);
        textAbstract.setText(articleNews.getAbstractText());

        textBody=(TextView) findViewById(R.id.text_body);
        textBody.setText(articleNews.getBodyText());

        articleImage=(ImageView) findViewById(R.id.imageView);

        //To show the image we have to convert String -> BitMap
        //Array of bytes
        byte[] decodedString = new byte[0];

        //Decode array of character to store on byte format
        try {
            if(articleNews.getImage() != null) {
                try {
                    decodedString = Base64.decode(articleNews.getImage().getImage(), Base64.DEFAULT);
                } catch (ServerComnmunicationError serverComnmunicationError) {
                    serverComnmunicationError.printStackTrace();
                }

                //Use bitmap object to show the images for every article
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                articleImage.setImageBitmap(decodedByte);

                articleImage.setImageBitmap(decodedByte);
            }
        } catch (ServerComnmunicationError serverComnmunicationError) {
            serverComnmunicationError.printStackTrace();
        }
    }
}
