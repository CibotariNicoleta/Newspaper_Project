package com.example.newsmanagerproject.model;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.newsmanagerproject.R;
import com.example.newsmanagerproject.database.ArticleDB;
import com.example.newsmanagerproject.services.AppContainer;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UserPage extends AppCompatActivity {
    private TextView username;
    private String name;
    private byte[] image;
    private TextView nr_articles;
    public static int number_of_articles = 0;
    private Fragment fragment;
    private ImageView imageview;
    private int userID;
    private User user;
    private Dialog myDialog;
    public static boolean onBack = false;

    //private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    private static int RESULT_LOAD_IMG = 1000;
    private static int RESULT_TAKE_PICTURE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppContainer.goForward(this.getClass().getSimpleName());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        onBack = true;
        userID = ArticleDB.getUserId(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        myDialog = new Dialog(UserPage.this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment = new UserFragment();
       if (fragment != null) {
            transaction.replace(R.id.fragmentUser, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        user = ArticleDB.getUser(userID);
       // ArticleDB.updateUser();
        name = user.getUserName();
        image = user.getImage();
        number_of_articles = ArticleDB.getNumberOfArticles(userID);
        byte[] decodedString = new byte[0];
        username = findViewById(R.id.user_name);
        nr_articles = findViewById(R.id.nr_of_articles);
        setNumberOfArticles();
        username.setText(name);
        imageview = findViewById(R.id.profile_image);

        if(image!=null) {

                //decodedString = Base64.encode(image.getBytes(), Base64.DEFAULT);
                decodedString = image;
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                //  Bitmap bmp = Bitmap.createBitmap(1500,1700,Bitmap.Config.ARGB_8888);
                  //ByteBuffer buffer = ByteBuffer.wrap(decodedString);
                 // buffer.rewind();
                 // bmp.copyPixelsFromBuffer(buffer);
                //Use bitmap object to show the images for every article
               // Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageview.setImageBitmap(bitmap);

        }


        //name = ArticleDB.getUserName(ArticleDB.getUserId(FirebaseAuth.getInstance().getCurrentUser().getEmail()));




        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopUp(v);
            }
        });
    }

    public void setNumberOfArticles() {
        nr_articles.setText(""+number_of_articles);
    }

    @Override
        protected void onActivityResult (int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);

            Bitmap captureImage = null;
            Uri selectImageUri = null;
            if (requestCode == RESULT_TAKE_PICTURE || requestCode == RESULT_LOAD_IMG) {
                selectImageUri = data.getData();
                if(selectImageUri != null) {
                    imageview.setImageURI(selectImageUri);
                    try {
                        captureImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    captureImage = (Bitmap) data.getExtras().get("data");
                    imageview.setImageBitmap(captureImage);
                }

            }


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        captureImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();


        user.setImage( byteArray);
        ArticleDB.saveUpdateUser(user, userID);


    }

    public void ShowPopUp(View v){
        myDialog.setContentView(R.layout.change_picture_pop_up);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        Button take_picture = myDialog.findViewById(R.id.take_picture);
        Button upload_picture = myDialog.findViewById(R.id.upload);
        Button cancel = myDialog.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(UserPage.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(UserPage.this,
                            new String[]{
                                    Manifest.permission.CAMERA
                            }, RESULT_TAKE_PICTURE);
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_TAKE_PICTURE);
            }
        });

        upload_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        // requestPermissions(permissions, PERMISSION_CODE);
                    }
                }
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

    }

}