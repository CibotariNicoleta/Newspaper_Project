package com.example.newsmanagerproject.model;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.newsmanagerproject.R;
import com.example.newsmanagerproject.database.ArticleDB;
import com.example.newsmanagerproject.database.ArticleDatabaseHelper;
import com.example.newsmanagerproject.network.LoginTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Shared shared;
    public static FloatingActionButton loginButton;


    ArticleDatabaseHelper dbHelper = new ArticleDatabaseHelper(getBaseContext());

    private ArticleDB dbArticle;
    //Variables for sideBar
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ArticleDB articleDB = new ArticleDB(getApplicationContext());
        //shared preferences
        shared = new Shared(getApplicationContext());
        shared.firstTime();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UserPage.onBack = false;
                //Create FragmentManager
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                dbArticle = new ArticleDB(getApplicationContext());

                //SideBar
                Toolbar toolbar = findViewById(R.id.toolbarPerfect);
                setSupportActionBar(toolbar);

                // DrawerLayOut get the xml object
                drawerLayout = findViewById(R.id.drawer_layout);

                //To set the menu in the sidebar
                navigationView = findViewById(R.id.nav_controller_view_tag);
                navigationView.setNavigationItemSelectedListener(MainActivity.this);

                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawerLayout.addDrawerListener(toggle);
                toggle.syncState();


                if (savedInstanceState == null) {
                    transaction.add(R.id.fragment, new AllFragment()).commit();
                    transaction.addToBackStack(null);
                    navigationView.setCheckedItem(R.id.category_all);
                }

                loginButton = findViewById(R.id.loginButton);
                if (shared.login()) {
                    LoginTask loginTask= new LoginTask();
                    loginTask.execute();
                    loginButton.setVisibility(View.INVISIBLE);
                    Menu navMenus=navigationView.getMenu();
                    navMenus.findItem(R.id.nav_logout).setVisible(true);
                    navMenus.findItem(R.id.nav_create).setVisible(true);
                     navMenus.findItem(R.id.nav_userpage).setVisible(true);
                }
                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), Login.class);
                        startActivity(intent);
                    }
                });
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment f = new Fragment();
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intentHome = new Intent(this, MainActivity.class);
                startActivity(intentHome);
                break;
            case R.id.nav_create:
                Intent intentAddArticle = new Intent(this, CreatArticle.class);
                startActivity(intentAddArticle);
                break;
            case R.id.nav_userpage:
                Intent intentuser = new Intent(this, UserPage.class);
                startActivity(intentuser);
                break;
            case R.id.category_national:
                f = new NationalFragment();
                break;
            case R.id.category_economy:
                f = new EconomyFragment();
                break;
            case R.id.category_sports:
                f = new SportsFragment();
                break;
            case R.id.category_technology:
                f = new TechnologyFragment();
                break;
            case R.id.category_all:
                f = new AllFragment();
                break;
            case R.id.nav_logout:
                shared.getEditor().putBoolean("b", false).apply();
                shared.firstTime();
                Shared.checkLogin = false;
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                break;
        }
        if (f != null) {
            transaction.replace(R.id.fragment, f);
            transaction.addToBackStack(null);
            transaction.commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        //To select item when is triggered
        return false;

    }

    //This method allow manage the actions whenever any menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // Abrir menu
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}