package it.itsar.progettocorso;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

public class LeggiStoria extends AppCompatActivity {
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leggi_storia);

        Storia storia = (Storia) getIntent().getSerializableExtra("storia");

        this.setTitle(storia.getTitle());

        content = findViewById(R.id.content);
        content.setText(storia.getContent());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}