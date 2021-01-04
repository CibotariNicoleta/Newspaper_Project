package com.example.newsmanagerproject.model;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.newsmanagerproject.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TakePicture extends Fragment {
    private Dialog myDialog;
    private  Button take_picture;
    private Button upload_picture;
    private Button cancel;
    private Bitmap bitmap;
    private  ImageView imageView;
    private Context context;
   // private View v;
    public static int RESULT_LOAD_IMG = 1000;
    public static int RESULT_TAKE_PICTURE = 100;

    public TakePicture(ImageView imageView, Context context){
        this.imageView = imageView;
        this.context = context;
        //this.v = context;
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.change_picture_pop_up);

        take_picture = myDialog.findViewById(R.id.take_picture);
        upload_picture = myDialog.findViewById(R.id.upload);
        cancel = myDialog.findViewById(R.id.cancel);
        showPopUp();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.change_picture_pop_up);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.change_picture_pop_up, container, false);
       // v = root;
        myDialog = new Dialog(root.getContext());
        take_picture = root.findViewById(R.id.take_picture);
        upload_picture = root.findViewById(R.id.upload);
        cancel = root.findViewById(R.id.cancel);
        //showPopUp(root);
        return root;
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = null;
        Uri selectImageUri = null;
        if (requestCode == RESULT_TAKE_PICTURE || requestCode == RESULT_LOAD_IMG) {
            selectImageUri = data.getData();
            imageView.setImageURI(selectImageUri);
        }

       try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectImageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public byte[] doStuff() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
        // user.setImage( byteArray);
        //ArticleDB.saveUpdateUser(user, ArticleDB.getUserId(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
    }

    public void showPopUp(){
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions((Activity)context,
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
                    if(ContextCompat.checkSelfPermission(view.getContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
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
