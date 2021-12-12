package id.kelompok14.modul5progmob.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import id.kelompok14.modul5progmob.API.Constant;
import id.kelompok14.modul5progmob.database.DBHandler;
import id.kelompok14.modul5progmob.R;

public class ProfilUserConfirmationActivity extends AppCompatActivity {

    TextView kon_nama, kon_nomor, kon_alamat, kon_gender, kon_skill, kon_time, kon_user, kon_pass;
    Button finishReg;
    DBHandler dbHandler;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_user_confirmation);
        dialog = new ProgressDialog(ProfilUserConfirmationActivity.this);

        dbHandler = new DBHandler(ProfilUserConfirmationActivity.this);

        Intent intent = getIntent();

        String namaUser = intent.getStringExtra("nama");
        String nomortelpUser = intent.getStringExtra("nomor");
        String alamatUser = intent.getStringExtra("alamat");
        String genderUser = intent.getStringExtra("gender");
        String usernameUser = intent.getStringExtra("username");
        String passwordUser = intent.getStringExtra("password");
        String instrumentUser = intent.getStringExtra("instrument");
        int waktuUser = intent.getIntExtra("waktu", 1);

        kon_nama = (TextView) findViewById(R.id.profilnama);
        kon_nomor = (TextView) findViewById(R.id.profilnomor);
        kon_alamat = (TextView) findViewById(R.id.profilalamat);
        kon_gender = (TextView) findViewById(R.id.profilkelamin);
        kon_skill = (TextView) findViewById(R.id.profilskill);
        kon_time = (TextView) findViewById(R.id.profilwaktu);
        kon_user = (TextView) findViewById(R.id.profilusername);
        kon_pass = (TextView) findViewById(R.id.profilpass);

        finishReg = (Button) findViewById(R.id.buttonFinish);

        kon_nama.setText(namaUser);
        kon_nomor.setText(nomortelpUser);
        kon_alamat.setText(alamatUser);
        kon_gender.setText(genderUser);
        kon_skill.setText(instrumentUser);
        kon_time.setText(String.valueOf(waktuUser));
        kon_user.setText(usernameUser);
        kon_pass.setText(passwordUser);

        finishReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(namaUser, nomortelpUser, alamatUser, usernameUser,
                        passwordUser, genderUser,instrumentUser,waktuUser);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast confirmAsk = Toast.makeText(context, "You almost done!", duration);
        confirmAsk.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast confirmBackAsk = Toast.makeText(context, "You have not completed the registration!", duration);
        confirmBackAsk.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set title dialog
        alertDialogBuilder.setTitle("You paused the registration.");

        // Set pesan dari dialog
        alertDialogBuilder
                .setMessage("Choose 'Continue' to proceed with the registration. Choose 'Cancel to go back to login page.'")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Continue",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(ProfilUserConfirmationActivity.this, StartMenuActivity.class);
                        startActivity(intent);
                    }
                });

        // Membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Menampilkan alert dialog
        alertDialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast successReg = Toast.makeText(context, "Registration Completed.", duration);
        successReg.show();
    }

    private void register(String namaUser,String nomortelpUser, String alamatUser, String usernameUser,
                          String passwordUser, String genderUser, String instrumentUser, int waktuUser){
        dialog.setMessage("Registering.");
        dialog.show();
        String postUrl = Constant.REGISTER;
        RequestQueue requestQueue = Volley.newRequestQueue(ProfilUserConfirmationActivity.this);
        JSONObject postData = new JSONObject();
        try {
            postData.put("name", namaUser);
            postData.put("username", usernameUser);
            postData.put("password", passwordUser);
            postData.put("phone", nomortelpUser);
            postData.put("address", alamatUser);
            postData.put("gender", genderUser);
            postData.put("skill", instrumentUser);
            postData.put("waktu", waktuUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject object = response;
                try {
                    if(object.getBoolean("success")){
                        Context context = ProfilUserConfirmationActivity.this;
                        int duration = Toast.LENGTH_SHORT;
                        Toast sukses = Toast.makeText(context, "Register Success.", duration);
                        sukses.show();

                        Intent intent = new Intent(ProfilUserConfirmationActivity.this, StartMenuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Context context = ProfilUserConfirmationActivity.this;
                    int duration = Toast.LENGTH_SHORT;
                    Toast sukses = Toast.makeText(context, "Register Failed.", duration);
                    sukses.show();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Context context = ProfilUserConfirmationActivity.this;
                int duration = Toast.LENGTH_SHORT;
                Toast sukses = Toast.makeText(context, "Register Failed.", duration);
                sukses.show();
                dialog.dismiss();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}