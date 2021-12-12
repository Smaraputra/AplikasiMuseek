package id.kelompok14.modul5progmob.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.kelompok14.modul5progmob.API.Constant;
import id.kelompok14.modul5progmob.R;
import id.kelompok14.modul5progmob.fragment.HomeFragment;
import id.kelompok14.modul5progmob.fragment.ProfilUserFragment;
import id.kelompok14.modul5progmob.fragment.TransactionFragment;

public class DashboardAppActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "DashboardManagerActivity";

    SharedPreferences sharedPreferences;
    DrawerLayout drawerLayout;
    TextView viewname;
    TextView viewgender;
    String nama, gender, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        drawerLayout = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences = getSharedPreferences("loginsession", Context.MODE_PRIVATE);
        nama = sharedPreferences.getString("name","defaultValue");
        gender = sharedPreferences.getString("gender","defaultValue");
        token = sharedPreferences.getString("token", "defaultValues");

        viewname = headerView.findViewById(R.id.viewnama);
        viewgender = headerView.findViewById(R.id.viewgender);

        viewname.setText(nama);
        viewgender.setText(gender);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(DashboardAppActivity.this, drawerLayout,
                toolbar, R.string.buka, R.string.tutup);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.tempatmunculfragment, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.homemenu);
        }

    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else if((drawerLayout.isDrawerOpen(GravityCompat.START)!=true)){
            showDialogKeluar();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.homemenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.tempatmunculfragment, new HomeFragment(), "HomeFragment").commit();
                break;
            case R.id.profilmenu:
                getSupportFragmentManager().beginTransaction().replace(R.id.tempatmunculfragment, new ProfilUserFragment(), "ProfilFragment").commit();
                break;
            case R.id.transaksi:
                getSupportFragmentManager().beginTransaction().replace(R.id.tempatmunculfragment, new TransactionFragment(), "TransactionFragment").commit();
                break;
            case R.id.logout:
                showDialogLogout();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDialogKeluar(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set title dialog
        alertDialogBuilder.setTitle("You want to exit the app?");

        // Set pesan dari dialog
        alertDialogBuilder
                .setMessage("Choose 'Exit' to close MuSeek.")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Exit",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // Jika tombol diklik, maka akan menutup activity ini
                        DashboardAppActivity.this.finishAffinity();
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Jika tombol ini diklik, akan menutup dialog
                        dialog.cancel();
                    }
                });

        // Membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Menampilkan alert dialog
        alertDialog.show();
    }

    private void showDialogLogout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set title dialog
        alertDialogBuilder.setTitle("You want to logout?");

        // Set pesan dari dialog
        alertDialogBuilder
                .setMessage("Choose 'Logout' to logout your account.")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Logout",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // Jika tombol diklik, maka akan menutup activity ini
                        logout();
                        SharedPreferences sharedPreferences = getSharedPreferences("loginsession", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(DashboardAppActivity.this, StartMenuActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Jika tombol ini diklik, akan menutup dialog
                        dialog.cancel();
                    }
                });

        // Membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Menampilkan alert dialog
        alertDialog.show();
    }

    private void logout(){
        RequestQueue queue = Volley.newRequestQueue(DashboardAppActivity.this);
        String postUrl = Constant.LOGOUT;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        queue.add(request);
    }
}