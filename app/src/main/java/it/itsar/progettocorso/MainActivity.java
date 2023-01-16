package it.itsar.progettocorso;

import static android.content.ContentValues.TAG;

import static it.itsar.progettocorso.LeggiScrivi.leggiLocale;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView tabMenu;
    private ListView storieView;
    private ArrayList<Storia> storie = new ArrayList<>();
    private ArrayAdapter<Storia> storieAdapter;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference storieRef = db.collection("storie");
    private Boolean isOnline = false;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                switch (result.getResultCode()) {
                    case Activity.RESULT_OK:
                        showLocale();
                        isOnline = false;
                        tabMenu.setSelectedItemId(R.id.locale);
                        break;
                    case Activity.RESULT_CANCELED:
                        showOnline();
                        isOnline = true;
                        break;
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindElements();
        setListeners();
        inizializeElements();
        
    }

    private void fetchStorie() {
        storie.clear();
        runOnUiThread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            public void run() {
                storieAdapter.notifyDataSetChanged();
            }
        });
        storieRef
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        storie.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Storia storia = document.toObject(Storia.class);
                            storia.setId(document.getId());
                            storie.add(storia);
                        }
                        runOnUiThread(new Runnable() {
                            @SuppressLint("NotifyDataSetChanged")
                            public void run() {
                                storieAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        Log.w(TAG,"Error getting documents",task.getException());
                    }
                });
    }

    private void showLocale() {
        storie.clear();
        runOnUiThread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            public void run() {
                storieAdapter.notifyDataSetChanged();
            }
        });
        String storieFile;
        JSONArray jsonArray;
        try {
            storieFile = leggiLocale(getFilesDir(), "storie.json");
            jsonArray = new JSONArray(storieFile);
            JSONObject json;
            for (int i = 0; i < jsonArray.length(); i++) {
                json = jsonArray.getJSONObject(i);
                storie.add(new Storia(json));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            public void run() {
                storieAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showOnline() {
        fetchStorie();
    }

    private void inizializeElements() {
        storieAdapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                storie
        );
        showLocale();
        storieView.setAdapter(storieAdapter);
        tabMenu.setSelectedItemId(R.id.locale);
    }

    private void setListeners() {
        tabMenu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.online:
                    showOnline();
                    isOnline = true;
                    return true;
                case R.id.locale:
                    showLocale();
                    isOnline = false;
                    return true;
                default:
                    return false;
            }
        });

        storieView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent;
            if (isOnline) {
                intent = new Intent(MainActivity.this,DettaglioStoria.class);
            } else {
                intent = new Intent(MainActivity.this,LeggiStoria.class);
            }
            intent.putExtra("storia",storie.get(i));
            activityResultLauncher.launch(intent);
        });
    }

    private void bindElements() {
        tabMenu = findViewById(R.id.tab_menu);
        storieView = findViewById(R.id.lista_storie);
    }
}