package com.example.mertpekeycs310homework3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mertpekeycs310homework3.model.CategoryItem;
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




public class MainActivity extends AppCompatActivity {

    RecyclerView newsRcView;
    Spinner spCategories;
    NewsAdapter adp;
    List<NewsItem> newsData;
    List<CategoryItem> categoryData;
    ProgressDialog prgDialog;
    ArrayAdapter<CategoryItem> adpCat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("News");


        newsData = new ArrayList<>();
        categoryData = new ArrayList<>();
        newsRcView = findViewById(R.id.newsrc);

        adp = new NewsAdapter(newsData, this, new NewsAdapter.NewsItemClickListener() {
            @Override
            public void NewItemClicked(NewsItem selectedNewsItem) {
                Intent i = new Intent(MainActivity.this, NewsDetailsActivity.class);
                i.putExtra("selectednews", selectedNewsItem);
                startActivity(i);
            }
        });
        newsRcView.setLayoutManager(new LinearLayoutManager(this));
        newsRcView.setAdapter(adp);

        spCategories = findViewById(R.id.spinnercategory);

        NewsTask tsk = new NewsTask();
        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getallnewscategories");

        spCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    NewsTask tsk2 = new NewsTask();
                    tsk2.execute("http://94.138.207.51:8080/NewsApp/service/news/getall");
                }else if(position == 1){
                    NewsTask tsk2 = new NewsTask();
                    tsk2.execute("http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/4");
                }else if(position == 2){
                    NewsTask tsk2 = new NewsTask();
                    tsk2.execute("http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/6");
                }else if(position == 3){
                    NewsTask tsk2 = new NewsTask();
                    tsk2.execute("http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/5");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    class NewsTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            prgDialog = new ProgressDialog(MainActivity.this);
            prgDialog.setTitle("Loading");
            prgDialog.setMessage("Please Wait...");
            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String urlStr = strings[0];

            StringBuilder buffer = new StringBuilder();


            try {
                URL url = new URL(urlStr);
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

            newsData.clear();

            try {

                JSONObject obj = new JSONObject(s);

                if(obj.getInt("serviceMessageCode") == 1){

                    JSONArray arr = obj.getJSONArray("items");

                    if(arr.length() != 3) {
                        for (int i = 0; i < arr.length(); i++) {

                            JSONObject current = (JSONObject) arr.get(i);

                            long date = current.getLong("date");
                            Date objDate = new Date(date);

                            NewsItem item = new NewsItem(current.getInt("id"),
                                    current.getString("title"),
                                    current.getString("text"),
                                    current.getString("image"),
                                    current.getString("categoryName"),
                                    objDate);

                            newsData.add(item);

                        }
                        adp.notifyDataSetChanged();
                    }
                    else{
                        categoryData.clear();
                        CategoryItem cat = new CategoryItem(100, "All");
                        categoryData.add(cat);
                        for (int i = 0; i < arr.length(); i++) {

                            JSONObject current = (JSONObject) arr.get(i);

                            CategoryItem item = new CategoryItem(current.getInt("id"),
                                    current.getString("name"));

                            categoryData.add(item);

                        }

                        adpCat = new ArrayAdapter<CategoryItem>(MainActivity.this,android.R.layout.simple_list_item_1, categoryData);

                        spCategories.setAdapter(adpCat);
                    }

                } else{

                }

                prgDialog.dismiss();

            } catch (JSONException e) {

                Log.e("DEV",e.getMessage());

            }
        }
    }

}

