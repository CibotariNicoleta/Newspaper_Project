package com.example.newsmanagerproject.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newsmanagerproject.MyArticleModel;
import com.example.newsmanagerproject.R;
import com.example.newsmanagerproject.database.ArticleDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UserFragment extends Fragment {
    private List<Article> listRes;
    private ListView listView;
    private FloatingActionButton loginButon;
    private NewsAdapter userAdapter;
    private View root;

    public View footerView;
    public Handler mhandler;
    public boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_user, container, false);
       // listRes = ArticleDB.getUserArticles(ArticleDB.getUserId(FirebaseAuth.getInstance().getCurrentUser().getEmail()));

            listRes = ArticleDB.getUserArticles(ArticleDB.getUserId(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
            Collections.reverse(listRes);



        //Set the footer
        LayoutInflater li = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = li.inflate(R.layout.footer_view, null);
        initListView();
        //Create handler
        mhandler = new MyHandler();
        return root;
    }

    private void initListView() {
        // This part will show a list of articles
        listView = root.findViewById(R.id.list_userArticles);
        userAdapter = new NewsAdapter(Objects.requireNonNull(getContext()), listRes);
       // userAdapter = new NewsAdapter(Objects.requireNonNull(getContext()), MyArticleModel.getListFilter(listRes,2));
        listView.setAdapter(userAdapter);

        //This let us set every item clickable
        listView.setClickable(true);

      /*  listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

           /* @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getLastVisiblePosition()==totalItemCount-1 && !isLoading){
                    isLoading=true;
                    Thread thread = new UserFragment.ThreadGetMoreArticles();
                    thread.start();
                }
            }*/
        //)};
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    listView.addFooterView(footerView);
                    break;
                case 1:
                    //Update data adapater and UI
                    userAdapter.addArticlesList((List<Article>) msg.obj);

                    //To remove footer View
                    listView.removeFooterView(footerView);
                    isLoading = false;
                    break;
                default:
                    break;
            }
        }
    }

    public class ThreadGetMoreArticles extends Thread {
        @Override
        public void run() {
            //Add footer view
            mhandler.sendEmptyMessage(0);

            //Look for more data
            List<Article> getList= null;
            try {
                getList = new ArrayList<Article>(ArticleDB.loadArticles());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            getList= MyArticleModel.getListFilter(getList,2);
           // getList = new ArrayList<Article>(ArticleDB.getUserArticles(ArticleDB.getUserId(FirebaseAuth.getInstance().getCurrentUser().getEmail())));

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Sending the result
            Message msgRes = mhandler.obtainMessage(1, getList);
            mhandler.sendMessage(msgRes);

        }
    }
}
