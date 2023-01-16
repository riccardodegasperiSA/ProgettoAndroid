package it.itsar.progettocorso;

import static it.itsar.progettocorso.LeggiScrivi.leggiLocale;
import static it.itsar.progettocorso.LeggiScrivi.scriviLocale;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class DettaglioStoria extends AppCompatActivity {
    private TextView summary;
    private Button scarica;
    private Button back;
    private Storia storia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_storia);

        storia = (Storia) getIntent().getSerializableExtra("storia");

        this.setTitle(storia.getTitle());

        bindElements();
        initializeElements();
        setListeners();

    }


    private void bindElements() {
        summary = findViewById(R.id.summary);
        scarica = findViewById(R.id.scarica);
        back = findViewById(R.id.back);
    }

    private void initializeElements() {
        summary.setText(storia.getSummary());
    }

    private void setListeners() {
        scarica.setOnClickListener(view -> {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    salvaStoria();
                    goBack(false);
                }
            });
        });
        back.setOnClickListener(view -> {
            goBack(true);
        });
    }

    private void goBack(Boolean online) {
        Intent intent = new Intent();
        if (online){
            setResult(Activity.RESULT_CANCELED,intent);
        } else {
            setResult(Activity.RESULT_OK,intent);
        }
        finish();
    }

    private void salvaStoria() {
        String storieFile;
        JSONArray jsonArray;
        try {
            storieFile = leggiLocale(getFilesDir(), "storie.json");
            jsonArray = new JSONArray(storieFile);
            for (int i = 0; i < jsonArray.length(); i++) {
                Storia test = new Storia(jsonArray.getJSONObject(i));
                if (storia.equals(test)) {
                    return;
                }
            }
            jsonArray.put(storia.toJsonObject());
            scriviLocale(getFilesDir(),"storie.json",jsonArray.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            jsonArray = new JSONArray();
            jsonArray.put(storia.toJsonObject());
            try {
                scriviLocale(getFilesDir(),"storie.json",jsonArray.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}