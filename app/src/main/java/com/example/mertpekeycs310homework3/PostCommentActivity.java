package com.example.mertpekeycs310homework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.mertpekeycs310homework3.model.NewsItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;





public class PostCommentActivity extends AppCompatActivity {

    EditText postName;
    EditText postMessage;
    int newsId;
    NewsItem selectedNewsItem;
    ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);
        setTitle("Post Comment");

        ActionBar currbar = getSupportActionBar();
        currbar.setHomeButtonEnabled(true);
        currbar.setDisplayHomeAsUpEnabled(true);
        currbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_18dp);


        selectedNewsItem = (NewsItem)getIntent().getSerializableExtra("selectednewsitem");
        postName = findViewById(R.id.postname);
        postMessage = findViewById(R.id.postmessage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_comment_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       // super.onOptionsItemSelected(item);

        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return true;
    }

    public void postButtonClicked(View v){

        JSONTask tsk = new JSONTask();
        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/savecomment",
                postName.getText().toString(), postMessage.getText().toString(),String.valueOf(selectedNewsItem.getId()));

    }


    class JSONTask extends AsyncTask<String, Void, String>{


        @Override
        protected void onPreExecute() {
            prgDialog = new ProgressDialog(PostCommentActivity.this);
            prgDialog.setTitle("Loading");
            prgDialog.setMessage("Please Wait...");
            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            StringBuilder strBuilder = new StringBuilder();
            String urlStr = strings[0];
            String name = strings[1];
            String text = strings[2];
            String news_id = strings[3];

            JSONObject obj = new JSONObject();

            try {
                obj.put("name", name);
                obj.put("text", text);
                obj.put("news_id", news_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.connect();

                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(obj.toString());

                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = "";

                    while((line = reader.readLine()) != null){

                        strBuilder.append(line);

                    }
                }


            } catch (MalformedURLException | ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return strBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {

            JSONObject obj = null;
            try {

                obj = new JSONObject(s);
                if(obj.getInt("serviceMessageCode") == 0) {
                    postName.setText("");
                    postMessage.setText("");
                    AlertDialog alertDialog = new AlertDialog.Builder(PostCommentActivity.this).create();
                    alertDialog.setTitle("Invalid Comment");
                    alertDialog.setMessage("Please enter a valid comment");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                } else {
                    Intent i = new Intent(PostCommentActivity.this, CommentsActivity.class);
                    i.putExtra("selectednews", selectedNewsItem);
                    startActivity(i);
                    finish();
                }
                prgDialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
