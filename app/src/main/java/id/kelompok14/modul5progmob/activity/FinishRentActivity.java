package id.kelompok14.modul5progmob.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import id.kelompok14.modul5progmob.R;

public class FinishRentActivity extends AppCompatActivity {

    Button kecilhome, besarhome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_rent);

        kecilhome = (Button) findViewById(R.id.backToHomeButton);
        besarhome = (Button) findViewById(R.id.buttonBesarHome);

        kecilhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinishRentActivity.this, DashboardAppActivity.class);
                startActivity(intent);
            }
        });

        besarhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinishRentActivity.this, DashboardAppActivity.class);
                startActivity(intent);
            }
        });
    }
}