package com.example.newsmanagerproject.model;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newsmanagerproject.R;
import com.example.newsmanagerproject.database.ArticleDB;
import com.example.newsmanagerproject.network.errors.ServerComnmunicationError;

public class NewsArticle extends AppCompatActivity {
    private Article articleNews;
    private TextView category;
    private TextView subtitle;
    private TextView textAbstract;
    private TextView textBody;
    private TextView textTitle;
    private ImageView articleImage;
    private ImageView articleLocation;
    private int ArticleId;

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

        articleLocation=(ImageView) findViewById(R.id.articleLocation);

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
            } else if(articleNews.getImageByte() != null){
                decodedString = articleNews.getImageByte();
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


                articleImage.setImageBitmap(bitmap);

            }
        } catch (ServerComnmunicationError serverComnmunicationError) {
            serverComnmunicationError.printStackTrace();
        }
        
        ArticleId = articleNews.getId();
        articleLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(articleNews.getIdUser()!=0){
                Mapp mapp = ArticleDB.getArticleLocation(ArticleId);

                if(mapp != null) {
                    Intent intent = new Intent(articleLocation.getContext(), MapsApp.class);
                    intent.putExtra("location", mapp);
                    startActivity(intent);
                    } else {
                      Toast.makeText(NewsArticle.this, "Sorry! Location is not available", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent;
        if(UserPage.onBack) {
             intent = new Intent(this, UserPage.class);
        } else{
             intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
