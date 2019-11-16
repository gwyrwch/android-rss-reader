package com.example.rssreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    public static final String URL = "rss_url";
    private EditText editRSSUrlView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editRSSUrlView = findViewById(R.id.edit_rss_url);

        final Button btnSave = findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();

                String newUrl = editRSSUrlView.getText().toString();
                replyIntent.putExtra(URL, newUrl);

                setResult(RESULT_OK, replyIntent);
                finish();

            }
        });


        Intent intent = getIntent();
        String currentUrl = intent.getStringExtra(URL);

        editRSSUrlView.setText(currentUrl);

    }
}
