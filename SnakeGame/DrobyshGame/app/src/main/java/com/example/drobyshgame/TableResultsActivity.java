package com.example.drobyshgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.ArrayList;

public class TableResultsActivity extends AppCompatActivity {

    ArrayList<Result> resultArrayList = new ArrayList<>();

    AppCompatImageButton backButton;
    RecyclerView recyclerView;
    AppDataBase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_table_results);

        backButton = findViewById(R.id.returnToGame);

        recyclerView = findViewById(R.id.recyclerView);
        setResultArrayList();

        CustomAdapter adapter = new CustomAdapter(this, resultArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TableResultsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void setResultArrayList(){

        AppDataBase db = DataApp.getApp().getDatabase();
        ResultDao resultDao = db.resultDao();

        resultArrayList.addAll(resultDao.orderByScore());
    }
}