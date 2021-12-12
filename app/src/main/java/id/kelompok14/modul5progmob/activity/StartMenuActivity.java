package id.kelompok14.modul5progmob.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.kelompok14.modul5progmob.API.Constant;
import id.kelompok14.modul5progmob.R;
import id.kelompok14.modul5progmob.database.DBHandler;

public class StartMenuActivity extends AppCompatActivity {

    TextView tblregister;
    Button loginBtn;
    SharedPreferences sharedPreferences;

    EditText username, password;
    String username_in;
    String password_in;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tblregister = (TextView) findViewById(R.id.tombolregister);
        loginBtn = (Button) findViewById(R.id.tombollogin);
        username = (EditText) findViewById(R.id.inusername);
        password = (EditText) findViewById(R.id.inpassword);
        dialog = new ProgressDialog(StartMenuActivity.this);
        dialog.setCancelable(false);

        sharedPreferences = getSharedPreferences("loginsession", Context.MODE_PRIVATE);

        //Login Automatis Ketika Buka Aplikasi (Apabila Sudah Login Sebelumnya dan Tidak Melakukan Logout)
        if(!sharedPreferences.getString("username","defaultValue").equals("defaultValue")
                && !sharedPreferences.getString("token","defaultValue").equals("defaultValue")){
            Intent intent = new Intent(StartMenuActivity.this, DashboardAppActivity.class);
            startActivity(intent);
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username_in = username.getText().toString();
                password_in = password.getText().toString();
                if(TextUtils.isEmpty(username_in) && TextUtils.isEmpty(password_in)) {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast kurangusernamepassword = Toast.makeText(context, "Username and Password are not inputted.", duration);
                    kurangusernamepassword.show();
                }else if (TextUtils.isEmpty(username_in)){
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast kurangusername = Toast.makeText(context, "Username is not inputted.", duration);
                    kurangusername.show();
                }else if(TextUtils.isEmpty(password_in)){
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast kurangpassword = Toast.makeText(context, "Password is not inputted.", duration);
                    kurangpassword.show();
                }else{
                    Log.d("AAAAAA", Constant.LOGIN);
                    login();
                }
            }
        });
        tblregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartMenuActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set title dialog
        alertDialogBuilder.setTitle("You want to close the app?");

        // Set pesan dari dialog
        alertDialogBuilder
                .setMessage("Choose 'Exit' to close the app.")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Exit",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // Jika tombol diklik, maka akan menutup activity ini
                        StartMenuActivity.this.finishAffinity();
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

    private void login(){
        dialog.setMessage("Logging In.");
        dialog.show();
        String postUrl = Constant.LOGIN;
        RequestQueue requestQueue = Volley.newRequestQueue(StartMenuActivity.this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("username", username_in);
            postData.put("password", password_in);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject object = response;
                try {
                    if(!object.getString("access_token").isEmpty()){
                        JSONObject user = object.getJSONObject("user");
                        SharedPreferences userPref = StartMenuActivity.this.getSharedPreferences("loginsession", StartMenuActivity.this.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userPref.edit();
                        editor.putString("token",object.getString("access_token"));
                        editor.putInt("iduser",user.getInt("id_user"));
                        editor.putString("name",user.getString("name"));
                        editor.putString("username",user.getString("username"));
                        editor.putString("gender",user.getString("gender"));
                        editor.apply();

                        Context context = StartMenuActivity.this;
                        int duration = Toast.LENGTH_SHORT;
                        Toast sukses = Toast.makeText(context, "Login Success. Welcome " + user.getString("username") + ".", duration);
                        sukses.show();

                        Intent intent = new Intent(StartMenuActivity.this, DashboardAppActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Context context = StartMenuActivity.this;
                    int duration = Toast.LENGTH_SHORT;
                    Toast gagal = Toast.makeText(context, "Account is not valid. Please wait for admin validation.", duration);
                    gagal.show();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Context context = StartMenuActivity.this;
                int duration = Toast.LENGTH_SHORT;
                Toast gagal = Toast.makeText(context, "Login Failed.", duration);
                gagal.show();
                dialog.dismiss();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}