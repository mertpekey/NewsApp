package com.example.mertpekeycs310homework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mertpekeycs310homework3.model.NewsItem;

import java.text.SimpleDateFormat;



public class NewsDetailsActivity extends AppCompatActivity {

    TextView txtDateDetails;
    TextView txtHeaderDetails;
    TextView txtDescriptionDetails;
    ImageView imgDetails;

    NewsItem selectedNewsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        setTitle("News Details");

        txtDateDetails = findViewById(R.id.txtDateDetails);
        txtHeaderDetails = findViewById(R.id.txtHeaderDetails);
        txtDescriptionDetails = findViewById(R.id.txtDescriptionDetails);
        imgDetails = findViewById(R.id.imgDetailsx);

        selectedNewsItem = (NewsItem)getIntent().getSerializableExtra("selectednews");

       // imgDetails.setImageBitmap(selectedNewsItem.getBitmap());
        if(selectedNewsItem.getBitmap() == null){

            new ImageDownloadTask(imgDetails).execute(selectedNewsItem);

        } else{

            imgDetails.setImageBitmap(selectedNewsItem.getBitmap());

        }
        txtDateDetails.setText(new SimpleDateFormat("dd/MM/yyy").format(selectedNewsItem.getNewsDate()));
        txtHeaderDetails.setText(selectedNewsItem.getTitle());
        txtDescriptionDetails.setText(selectedNewsItem.getText());


        ActionBar currbar = getSupportActionBar();
        currbar.setHomeButtonEnabled(true);
        currbar.setDisplayHomeAsUpEnabled(true);
        currbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_18dp);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_details_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mn_comment){
            Intent i = new Intent(NewsDetailsActivity.this, CommentsActivity.class);
            i.putExtra("selectednews", selectedNewsItem);
            startActivity(i);
        }
        else if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}
