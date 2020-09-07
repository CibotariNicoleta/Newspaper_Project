package com.example.newsmanagerproject.model;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newsmanagerproject.MyArticleModel;
import com.example.newsmanagerproject.R;
import com.example.newsmanagerproject.database.ArticleDB;
import com.example.newsmanagerproject.network.ModelManager;
import com.example.newsmanagerproject.network.SaveArticleTask;
import com.example.newsmanagerproject.network.UpdateArticleTask;
import com.example.newsmanagerproject.network.errors.ServerComnmunicationError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class creatArticle extends AppCompatActivity {
    private Spinner spinner;
    private FloatingActionButton saveButton, cancelButton;
    private EditText text_title, text_abstract, text_subtitle, text_body;
    private String categoryST;
    private Article articleCreated;
    private Dialog myDialog;
    private Date date;
    private String titleST, abstractST, subtitleST, bodyST;
    private TextView category_text, title_text, abstract_text, subtitle_text, body_text;
    private boolean save = true;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        //For PopUp
        myDialog = new Dialog(this);
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


        //get current time
//        SimpleDateFormat formatter = new SimpleDateFormat(
//                "yyyy-MM-dd HH:mm:ss");
        date = new Date();
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(date);

        //update article

        if (getIntent().hasExtra("ModifiedArticle")) {
            //String jsonArticle = extras.getString("Article");
            Article article = (Article) getIntent().getSerializableExtra("ModifiedArticle");
            text_title.setText(article.getTitleText());
            text_abstract.setText(article.getAbstractText());
            text_subtitle.setText(article.getSubtitleText());
            text_body.setText(article.getBodyText());
            id=(int)getIntent().getSerializableExtra("ModArticleID");
            save = false;
            //The key argument here must match that used in the other activity
        }

        Article ex = (Article) getIntent().getSerializableExtra("Article");
        if (ex != null) {
            articleCreated = ex;
        }

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

    private void goSave(View v) {
        String user = "12";
        if (isValidated()) {
            articleCreated = new Article(categoryST, titleST, abstractST, bodyST, subtitleST, user);
            articleCreated.setLastUpdate(date);

            if (save) {
                SaveArticleTask saveArticleTask = new SaveArticleTask(getApplicationContext(), articleCreated);
                saveArticleTask.execute();

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
                //We have to send create Request To API
                Snackbar.make(v, "Success in executed action!", Snackbar.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myDialog.dismiss();
                    }
                }, 2000);

            }
        });
    }


}
