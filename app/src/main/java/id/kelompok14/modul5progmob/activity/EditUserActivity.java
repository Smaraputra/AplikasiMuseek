package id.kelompok14.modul5progmob.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.kelompok14.modul5progmob.API.Constant;
import id.kelompok14.modul5progmob.database.DBHandler;
import id.kelompok14.modul5progmob.R;
import id.kelompok14.modul5progmob.model.UserModel;

public class EditUserActivity extends AppCompatActivity {

    private DBHandler dbHandler;
    private ArrayList<UserModel> userData=new ArrayList<>();
    SharedPreferences sharedPreferences;
    EditText name, phonenumber, address, username, password;
    TextView infoSeek;
    Button cancel, save;
    SeekBar hour;
    CheckBox guitarChk, drumsetChk, violinChk;
    RadioGroup groupGender;
    RadioButton maleGdr, femaleGdr;
    ProgressDialog dialog;

    String name_in, phonenumber_in,address_in, username_in, password_in, gender, skill, time;
    int statusForm, pval, idlogin, waktu;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        sharedPreferences = getApplicationContext().getSharedPreferences("loginsession", Context.MODE_PRIVATE);
        idlogin = sharedPreferences.getInt("iduser", 1);
        token = sharedPreferences.getString("token", "defaultValues");
        dialog = new ProgressDialog(EditUserActivity.this);
        getUser();

        name = (EditText) findViewById(R.id.inputnama3);
        phonenumber = (EditText) findViewById(R.id.inputnomor3);
        address = (EditText) findViewById(R.id.inputalamat3);
        guitarChk = (CheckBox) findViewById(R.id.guitarCheckbox4);
        drumsetChk = (CheckBox) findViewById(R.id.drumCheckbox4);
        violinChk = (CheckBox) findViewById(R.id.violinCheckbox4);

        cancel = (Button) findViewById(R.id.tombolbatalbatal);
        save = (Button) findViewById(R.id.tombolkirimkirim);

        groupGender = (RadioGroup) findViewById(R.id.gender_group);
        femaleGdr = (RadioButton) findViewById(R.id.femaleGender);
        maleGdr = (RadioButton) findViewById(R.id.maleGender);

        hour = (SeekBar) findViewById(R.id.seekHour4);
        hour.setProgress(1);
        infoSeek = (TextView) findViewById(R.id.textView35);
        time = "1 Hour";
        pval = 1;

        hour.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                time = (String.valueOf(pval));
                time = time.concat(" Hour");
                infoSeek.setText(time);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                statusForm=0;
                bacaNama();
                bacaNomor();
                bacaAlamat();
                bacaGender();
                bacaSkill();

                if(statusForm==5) {
                    editUser();
                }else{
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast informasikurang = Toast.makeText(context, "Data is not valid.", duration);
                    informasikurang.show();
                }
            }
        });
    }

    //Baca Nama
    public void bacaNama(){
        name_in = name.getText().toString();
        if(TextUtils.isEmpty(name_in)){
            name.setError("Please enter your name!");
        }else{
            name.setError(null);
            statusForm=statusForm+1;
        }
    }

    public void bacaNomor(){
        phonenumber_in = phonenumber.getText().toString();
        if(TextUtils.isEmpty(phonenumber_in)) {
            phonenumber.setError("Please enter your phone number!");
        }else{
            phonenumber.setError(null);
            statusForm=statusForm+1;
        }
    }

    //Baca Alamat
    public void bacaAlamat(){
        address_in = address.getText().toString();
        if(TextUtils.isEmpty(address_in)){
            address.setError("Please enter your address!");

        }else{
            statusForm=statusForm+1;
            address.setError(null);
        }
    }

    //Baca Radio Button Gender
    public void bacaGender() {
        if(groupGender.getCheckedRadioButtonId() != -1){
            femaleGdr.setError(null);
            statusForm=statusForm+1;
            gender = ((RadioButton)findViewById(groupGender.getCheckedRadioButtonId())).getText().toString();
        }else{
            femaleGdr.setError("Please select your gender!");
        }
    }

    //Baca Checkbox
    public void bacaSkill() {
        skill="";
        if(violinChk.isChecked() || drumsetChk.isChecked() || guitarChk.isChecked()){
            guitarChk.setError(null);
            statusForm=statusForm+1;
        }else{
            guitarChk.setError("Please select your skill!");
        }
        if(violinChk.isChecked()){
            skill = skill + "Violin; ";
        }
        if(drumsetChk.isChecked()){
            skill = skill + "Drumset; ";
        }
        if(guitarChk.isChecked()){
            skill = skill + "Guitar; ";
        }
        skill=skill+".";
    }

    private void getUser(){
        dialog.setMessage("Loading User Profile.");
        dialog.show();
        RequestQueue queue = Volley.newRequestQueue(EditUserActivity.this);
        String postUrl = Constant.PROFIL;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, postUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject object = response;
                try {
                    int id = object.getInt("id_user");
                    String namein = object.getString("name");
                    String phonein = object.getString("phone");
                    String addressin = object.getString("address");
                    String genderin = object.getString("gender");
                    String skillin = object.getString("skill");
                    int waktuin = object.getInt("waktu");

                    name.setText(namein);
                    phonenumber.setText(phonein);
                    address.setText(addressin);

                    if(genderin.equals("Male")){
                        maleGdr.setChecked(true);
                    }else if(genderin.equals("Female")){
                        femaleGdr.setChecked(true);
                    }

                    String[] splitskill = skillin.split(";");
                    for(int i=0; i<=splitskill.length-2; i++) {
                        if (splitskill[i].replaceAll("\\s+", "").equals("Violin")) {
                            violinChk.setChecked(true);
                        } else if (splitskill[i].replaceAll("\\s+", "").equals("Drumset")) {
                            drumsetChk.setChecked(true);
                        } else if (splitskill[i].replaceAll("\\s+", "").equals("Guitar")) {
                            guitarChk.setChecked(true);
                        }
                    }

                    pval = waktuin;
                    time = (String.valueOf(pval));
                    time = time.concat(" Hour");
                    infoSeek.setText(time);
                    hour.setProgress(pval);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
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

    private void editUser(){
        dialog.setMessage("Updating User Profile.");
        dialog.show();
        RequestQueue queue = Volley.newRequestQueue(EditUserActivity.this);
        String postUrl = Constant.EDIT_USER;
        Log.d("AAAAAAA", postUrl);

        JSONObject postData = new JSONObject();
        try {
            postData.put("name", name_in);
            postData.put("phone", phonenumber_in);
            postData.put("address", address_in);
            postData.put("gender", gender);
            postData.put("skill", skill);
            postData.put("waktu", pval);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast profil = Toast.makeText(context, "Profile updated.", duration);
                profil.show();
                Intent intent = new Intent(EditUserActivity.this, DashboardAppActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
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