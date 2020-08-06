package com.example.mertpekeycs310homework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mertpekeycs310homework3.model.CommentItem;
import com.example.mertpekeycs310homework3.model.NewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class CommentsActivity extends AppCompatActivity {

    RecyclerView commentsRcView;
    CommentsAdapter adp;
    List<CommentItem> commentData;
    ProgressDialog prgDialog;
    NewsItem selectedNewsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        setTitle("Comments");

        ActionBar currbar = getSupportActionBar();
        currbar.setHomeButtonEnabled(true);
        currbar.setDisplayHomeAsUpEnabled(true);
        currbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_18dp);

        commentData = new ArrayList<>();
        commentsRcView = findViewById(R.id.commentrcv);
        selectedNewsItem = (NewsItem)getIntent().getSerializableExtra("selectednews");

        adp = new CommentsAdapter(commentData, this, new CommentsAdapter.CommentsItemClickListener() {

            @Override
            public void CommentItemClicked(CommentItem selectedCommentItem) {

            }
        });
        commentsRcView.setLayoutManager(new LinearLayoutManager(this));
        commentsRcView.setAdapter(adp);

        CommentTask tsk = new CommentTask();
        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getcommentsbynewsid/" + selectedNewsItem.getId());


    }

    class CommentTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {

            prgDialog = new ProgressDialog(CommentsActivity.this);
            prgDialog.setTitle("Loading");
            prgDialog.setMessage("Please Wait...");
            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String urlStr = strings[0];
            StringBuilder buffer = new StringBuilder();

            URL url = null;
            try {
                url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line = "";
                while((line = reader.readLine()) != null){

                    buffer.append(line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            commentData.clear();

            try {

                JSONObject obj = new JSONObject(s);

                if(obj.getInt("serviceMessageCode") == 1){

                    JSONArray arr = obj.getJSONArray("items");

                    for(int i = 0; i < arr.length(); i++){

                        JSONObject current = (JSONObject) arr.get(i);

                        CommentItem item = new CommentItem(current.getInt("id"),
                                current.getInt("news_id"),
                                current.getString("text"),
                                current.getString("name"));

                        commentData.add(item);

                    }

                } else{


                }

                adp.notifyDataSetChanged();
                prgDialog.dismiss();



            } catch (JSONException e) {

                Log.e("DEV",e.getMessage());

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comments_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mn_addComment){
            Intent i = new Intent(CommentsActivity.this, PostCommentActivity.class);
            i.putExtra("selectednewsitem",selectedNewsItem);
            startActivity(i);
        }
        else if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        adp = new CommentsAdapter(commentData, this, new CommentsAdapter.CommentsItemClickListener() {

            @Override
            public void CommentItemClicked(CommentItem selectedCommentItem) {

            }
        });
        commentsRcView.setLayoutManager(new LinearLayoutManager(this));
        commentsRcView.setAdapter(adp);

        CommentTask tsk = new CommentTask();
        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getcommentsbynewsid/" + selectedNewsItem.getId());

    }
}
