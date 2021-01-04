package com.example.newsmanagerproject.model;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.newsmanagerproject.MyArticleModel;
import com.example.newsmanagerproject.R;
import com.example.newsmanagerproject.database.ArticleDB;
import com.example.newsmanagerproject.network.errors.ServerComnmunicationError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class NewsAdapter extends ArrayAdapter<Article> {


    private CardView myCard;
    private Dialog dialog;
    private Context mContext;
    private Button mapLocation;
    private List<Article> articles = new ArrayList<>();
    private FrameLayout frameLayout;
    public static FloatingActionButton deleteButton;
    public static FloatingActionButton modifyButton;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public NewsAdapter(@NonNull Context context, List<Article> list) {
        super(context, 0, list);
        this.mContext = context;
        articles = list;
        dialog = new Dialog(mContext);
    }


    @SuppressLint("RestrictedApi")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);

        myCard = listItem.findViewById(R.id.myCard);

        deleteButton= listItem.findViewById(R.id.deleteButton);
        deleteButton.setEnabled(false);
        modifyButton= listItem.findViewById(R.id.modifyButton);
        modifyButton.setEnabled(false);
       if(!Shared.checkLogin){
            deleteButton.setVisibility(View.GONE);
            modifyButton.setVisibility(View.GONE);
        }else{
           deleteButton.setVisibility(View.VISIBLE);
           modifyButton.setVisibility(View.VISIBLE);
       }

        final Article article = articles.get(position);

        ImageView image = listItem.findViewById(R.id.image);

        //Array of bytes
        byte[] decodedString = new byte[0];

        //Decode array of character to store on byte format
        try {
            if(article.getImage()!=null) {
                try {
                    decodedString = Base64.decode(article.getImage().getImage(), Base64.DEFAULT);
                } catch (ServerComnmunicationError serverComnmunicationError) {
                    serverComnmunicationError.printStackTrace();
                }

                //Use bitmap object to show the images for every article
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                image.setImageBitmap(decodedByte);
            } else if(article.getImageByte()!=null){
                decodedString = article.getImageByte();
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


                image.setImageBitmap(bitmap);
            }
        } catch (ServerComnmunicationError serverComnmunicationError) {
            serverComnmunicationError.printStackTrace();
        }

        TextView category = (TextView) listItem.findViewById(R.id.newsCategory);
        category.setText(article.getCategory());

        TextView title = (TextView) listItem.findViewById(R.id.newsTitle);
        title.setText(article.getTitleText());

        TextView Abstract = (TextView) listItem.findViewById(R.id.newsAbstract);
        Abstract.setText(article.getAbstractText());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String dateArticle=java.text.DateFormat.getDateTimeInstance().format(article.getLastUpdate())  ;
        TextView date = (TextView) listItem.findViewById(R.id.date_and_time);
        date.setText( dateArticle);

        //onClick method
        if (article.getIdUser() == ArticleDB.getUserId(firebaseAuth.getCurrentUser().getEmail())) {
           deleteButton.setEnabled(true);
           modifyButton.setEnabled(true);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopUp(v, article);
            }
        });

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(modifyButton.getContext(), CreatArticle.class);
                intent.putExtra("ModifiedArticle", article);
                intent.putExtra("ModArticleID",article.getId());
                mContext.startActivity(intent);
            }
        });

        myCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewsArticle = new Intent(myCard.getContext(), NewsArticle.class);
                intentNewsArticle.putExtra("Article", article);
                mContext.startActivity(intentNewsArticle);
            }
        });



        return listItem;

    }

    private void ShowPopUp(View v, Article article){

        dialog.setContentView(R.layout.activity_pop_delete);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        FloatingActionButton close = dialog.findViewById(R.id.closeButton) ;
        FloatingActionButton accept = dialog.findViewById(R.id.acceptButton);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //here if article is a user one it will be deleted
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    RetrieveArticleTask delete = new RetrieveArticleTask(getContext(), article);
                    delete.execute();
                    ArticleDB.deleteArticle(article);
                    MyArticleModel.deleteArticle(article);
                    articles.remove(article);
                    notifyNewsAdapter();
                    Snackbar.make(v.getRootView(), "Deleted with success!", Snackbar.LENGTH_LONG).setActionTextColor(Color.MAGENTA).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    }, 2000);

            }
        });

    }


    //This method add items to the arrayList
    public void addArticlesList(List<Article> listArticles) {
        articles.addAll(listArticles);
        //To refresh the list of articles
        this.notifyDataSetChanged();
    }
    private void notifyNewsAdapter(){
        this.notifyDataSetChanged();
    }

}
