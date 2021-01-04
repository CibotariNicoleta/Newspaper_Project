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
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.newsmanagerproject.MyArticleModel;
import com.example.newsmanagerproject.R;
import com.example.newsmanagerproject.database.ArticleDB;
import com.example.newsmanagerproject.network.SaveArticleTask;
import com.example.newsmanagerproject.network.UpdateArticleTask;
import com.example.newsmanagerproject.network.errors.ServerComnmunicationError;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;



public class CreatArticle extends AppCompatActivity {
    private Spinner spinner;
    private FloatingActionButton saveButton, cancelButton;
    private EditText text_title, text_abstract, text_subtitle, text_body;
    private String categoryST;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private Article articleCreated;
    private Dialog myDialog, dialog;
    private Date date;
    private String titleST, abstractST, subtitleST, bodyST;
    private TextView category_text, title_text, abstract_text, subtitle_text, body_text;
    private boolean save = true;
    private int id;
    public static int articleId;
    private ImageView image;
    private FirebaseAuth auth;
    private int userId;
    byte[] imageData;
    //Image articleImage;
    private Mapp map;
    private static int RESULT_LOAD_IMG = 1000;
    private static int RESULT_TAKE_PICTURE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        auth = FirebaseAuth.getInstance();
        userId = ArticleDB.getUserId(auth.getCurrentUser().getEmail());
        //For PopUp
        myDialog = new Dialog(this);
        dialog = new Dialog(this);
        //Code to get the data of the spinner
        spinner = findViewById(R.id.spinner_categories);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryST = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        text_title = findViewById(R.id.text_create_title);
        text_abstract = findViewById(R.id.text_create_abstract);
        text_subtitle = findViewById(R.id.text_create_subtitle);
        text_body = findViewById(R.id.text_create_body);
        image = findViewById(R.id.imageView);
        date = GregorianCalendar.getInstance().getTime();

        //update article

        if (getIntent().hasExtra("ModifiedArticle")) {
            //String jsonArticle = extras.getString("Article");

            Article article = (Article) getIntent().getSerializableExtra("ModifiedArticle");
            text_title.setText(article.getTitleText());
            text_abstract.setText(article.getAbstractText());
            text_subtitle.setText(article.getSubtitleText());
            text_body.setText(article.getBodyText());
            id = (int) getIntent().getSerializableExtra("ModArticleID");
            save = false;
            byte[] decodedString = new byte[0];
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
        }

        Article ex = (Article) getIntent().getSerializableExtra("Article");
        if (ex != null) {
            articleCreated = ex;
        }



        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PicturePopUp(v);
            }
        });

        titleST = text_title.getText().toString();
        text_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                titleST = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                titleST = s.toString();
            }
        });


        abstractST = text_abstract.getText().toString();
        text_abstract.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                abstractST = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                abstractST = s.toString();
            }
        });


        subtitleST = text_subtitle.getText().toString();
        text_subtitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                subtitleST = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                subtitleST = s.toString();
            }
        });


        bodyST = text_body.getText().toString();
        text_body.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bodyST = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                bodyST = s.toString();
            }
        });

        //Buttons Action
        saveButton = findViewById(R.id.save_article_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSave(v);
            }
        });

        cancelButton = findViewById(R.id.cancel_article_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Cancelling Creating Article Operation", Snackbar.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }
                }, 1000);
            }
        });

    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CreatArticle.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            getCurrentLocation();
        } else {
            Toast.makeText(this, "Permission", Toast.LENGTH_SHORT).show();
        }

    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(CreatArticle.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(CreatArticle.this).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    double lat = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    double lng = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    map = new Mapp(lng, lat);
                  //  articleId = ArticleDB.getArticleId(userId, titleST);
                   map.setArticleId(articleId);
                    ArticleDB.saveMap(map);
                }
            }
        }, Looper.getMainLooper());

    };

    private void goSave(View v) {
        //articleImage = new Image(imageData.toString());
        //String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (isValidated()) {
            articleCreated = new Article(categoryST, titleST, abstractST, bodyST, subtitleST, "" + userId);
            articleCreated.setLastUpdate(date);
            articleCreated.setImageByte(imageData);

            if (save) {
                SaveArticleTask saveArticleTask = new SaveArticleTask(getApplicationContext(), articleCreated);
                saveArticleTask.execute();
                MyArticleModel.articles.add(articleCreated);

            } else {
                articleCreated.setId(id);
                UpdateArticleTask updateArticleTask = new UpdateArticleTask(getApplicationContext(), articleCreated);
                updateArticleTask.execute();
            }
            ShowPopUp(v);

        } else {
            Snackbar.make(v, "Please, complete all the fields", Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean isValidated() {

        if (!titleST.matches("") && !abstractST.matches("") && !subtitleST.matches("") && !bodyST.matches("")) {
            Log.i("TaG", "Validates:  FULL");
            return true;
        }
        Log.i("TaG", "EMPTY");
        return false;
    }

    public void ShowPopUp(View v) {

        myDialog.setContentView(R.layout.activity_popup);
        FloatingActionButton buttonPopUpArticle = myDialog.findViewById(R.id.checkButton);
        category_text = myDialog.findViewById(R.id.category_created);
        category_text.setText(articleCreated.getCategory());

        title_text = myDialog.findViewById(R.id.title_created);
        title_text.setText(articleCreated.getTitleText());

        abstract_text = myDialog.findViewById(R.id.abstract_created);
        abstract_text.setText(articleCreated.getAbstractText());

        subtitle_text = myDialog.findViewById(R.id.subtitle_created);
        subtitle_text.setText(articleCreated.getSubtitleText());

        body_text = myDialog.findViewById(R.id.body_created);
        body_text.setText(articleCreated.getBodyText());



        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        buttonPopUpArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                UserPage.number_of_articles++;
                //We have to send create Request To API
                Snackbar.make(v, "Success in executed action!", Snackbar.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      //  getLocation();
                        myDialog.dismiss();
                    }
                }, 2000);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        Uri selectImageUri = null;
        if (requestCode == RESULT_TAKE_PICTURE || requestCode == RESULT_LOAD_IMG) {
            selectImageUri = data.getData();
            if(selectImageUri != null){
                image.setImageURI(selectImageUri);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                bitmap = (Bitmap) data.getExtras().get("data");
                image.setImageBitmap(bitmap);
            }
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        imageData = byteArray;

    }


    public void PicturePopUp(View v) {
        dialog.setContentView(R.layout.change_picture_pop_up);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Button take_picture = dialog.findViewById(R.id.take_picture);
        Button upload_picture = dialog.findViewById(R.id.upload);
        Button cancel = dialog.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreatArticle.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreatArticle.this,
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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
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
