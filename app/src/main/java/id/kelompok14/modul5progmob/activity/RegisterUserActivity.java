package id.kelompok14.modul5progmob.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import id.kelompok14.modul5progmob.R;

public class RegisterUserActivity extends AppCompatActivity {

    EditText name, phonenumber, address, username, password;
    TextView infoSeek;
    Button cancel, save;
    SeekBar hour;
    CheckBox guitarChk, drumsetChk, violinChk;
    RadioGroup groupGender;
    RadioButton maleGdr, femaleGdr;

//    AlertDialog.Builder aDialog;
//    LayoutInflater inflater;
//    View dialogView;

    Button cancelDialog, submitDialog;

    TextView showname, shownumber, showaddress, showuser, showpass, showgender, showskill, showtime;

    String name_in, phonenumber_in,address_in, username_in, password_in, gender, skill, time;
    int statusForm, pval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        name = (EditText) findViewById(R.id.inputnama);
        phonenumber = (EditText) findViewById(R.id.inputnomor);
        address = (EditText) findViewById(R.id.inputalamat);
        username = (EditText) findViewById(R.id.inputusername);
        password = (EditText) findViewById(R.id.inputpassword);

        guitarChk = (CheckBox) findViewById(R.id.guitarCheckbox);
        drumsetChk = (CheckBox) findViewById(R.id.drumCheckbox);
        violinChk = (CheckBox) findViewById(R.id.violinCheckbox);

        cancel = (Button) findViewById(R.id.tombolbatalbatal);
        save = (Button) findViewById(R.id.tombolkirimkirim);

        groupGender = (RadioGroup) findViewById(R.id.gender_group);
        femaleGdr = (RadioButton) findViewById(R.id.femaleGender);
        maleGdr = (RadioButton) findViewById(R.id.maleGender);

        hour = (SeekBar) findViewById(R.id.seekHour);
        hour.setProgress(0);
        infoSeek = (TextView) findViewById(R.id.textView17);
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
                Intent intent = new Intent(RegisterUserActivity.this, StartMenuActivity.class);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                statusForm=0;
                bacaNama();
                bacaNomor();
                bacaAlamat();
                bacaUsername();
                bacaPassword();
                bacaGender();
                bacaSkill();

                if(statusForm==7) {
                    DialogForm();
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

    //Baca Username
    public void bacaUsername() {
        username_in = username.getText().toString();
        if(TextUtils.isEmpty(username_in)) {
            username.setError("Please enter your username!");

        }else{
            statusForm=statusForm+1;
            username.setError(null);
        }
    }

    //Baca Password
    public void bacaPassword() {
        password_in = password.getText().toString();
        if(TextUtils.isEmpty(password_in)) {
            password.setError("Please enter your password!");

        }else{
            statusForm=statusForm+1;
            password.setError(null);
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
    }

    private void DialogForm() {
//        aDialog = new AlertDialog.Builder(RegisterUser.this);
//        inflater = getLayoutInflater();
//        dialogView = inflater.inflate(R.layout.dialog_register, null);
//        aDialog.setView(dialogView);
//        aDialog.setCancelable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_register,null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        showname= (TextView) dialogView.findViewById(R.id.isinama);
        shownumber= (TextView) dialogView.findViewById(R.id.isinomor);
        showaddress = (TextView) dialogView.findViewById(R.id.isialamat);
        showuser= (TextView) dialogView.findViewById(R.id.isiuser);
        showpass= (TextView) dialogView.findViewById(R.id.isipass);
        showgender= (TextView) dialogView.findViewById(R.id.isigender);
        showskill= (TextView) dialogView.findViewById(R.id.isiskill);
        showtime= (TextView) dialogView.findViewById(R.id.isiwaktu);

        showname.setText(name_in);
        shownumber.setText(phonenumber_in);
        showaddress.setText(address_in);
        showuser.setText(username_in);
        showpass.setText(password_in);
        showgender.setText(gender);
        showskill.setText(skill);
        showtime.setText(time);

        cancelDialog= (Button) dialogView.findViewById(R.id.tombolbatalbatal);
        submitDialog= (Button) dialogView.findViewById(R.id.tombolkirimkirim);

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        submitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(RegisterUserActivity.this, ProfilUserConfirmationActivity.class);
                intent.putExtra("nama", name_in);
                intent.putExtra("nomor", phonenumber_in);
                intent.putExtra("alamat", address_in);
                intent.putExtra("gender", gender);
                intent.putExtra("username", username_in);
                intent.putExtra("password", password_in);
                intent.putExtra("instrument", skill);
                intent.putExtra("waktu", pval);
                startActivity(intent);
            }
        });
//        aDialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        aDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });

//        aDialog.show();
        dialog.show();
    }
}