package id.kelompok14.modul5progmob.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import id.kelompok14.modul5progmob.R;
import id.kelompok14.modul5progmob.activity.DashboardAppActivity;
import id.kelompok14.modul5progmob.database.DBHandler;
import id.kelompok14.modul5progmob.activity.EditUserActivity;
import id.kelompok14.modul5progmob.activity.StartMenuActivity;
import id.kelompok14.modul5progmob.model.UserModel;

public class ProfilUserFragment extends Fragment {

    DBHandler dbHandler;
    ArrayList<UserModel> userData=new ArrayList<UserModel>();
    SharedPreferences sharedPreferences;
    Button edit, hapus;
    TextView nama, nomor, alamat, username, password, gender, instrument, time;
    String nama_in, nomor_in, alamat_in, username_in, password_in, gender_in, instrument_in;
    int idlogin, time_in;
    String token;
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profiluser, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("loginsession", Context.MODE_PRIVATE);
        idlogin = sharedPreferences.getInt("iduser",1);
        token = sharedPreferences.getString("token", "defaultValues");
        dialog = new ProgressDialog(getContext());

        getUser();

        nama = (TextView) view.findViewById(R.id.profilnama);
        nomor = (TextView) view.findViewById(R.id.profilnomor);
        alamat = (TextView) view.findViewById(R.id.profilalamat);
        gender = (TextView) view.findViewById(R.id.profilkelamin);
        instrument = (TextView) view.findViewById(R.id.profilskill);
        time = (TextView) view.findViewById(R.id.profilwaktu);
        edit = (Button) view.findViewById(R.id.button3);
        hapus = (Button) view.findViewById(R.id.button);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditUserActivity.class);
                startActivity(intent);
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        // Set title dialog
        alertDialogBuilder.setTitle("You want to delete your account?");

        // Set pesan dari dialog
        alertDialogBuilder
                .setMessage("Choose 'Yes' to delete your account?.")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // Jika tombol diklik, maka akan menutup activity ini
                        deleteUser();
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

    private void getUser(){
        dialog.setMessage("Loading User Profile.");
        dialog.show();
        RequestQueue queue = Volley.newRequestQueue(getContext());
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

                    nama.setText(namein);
                    nomor.setText(phonein);
                    alamat.setText(addressin);
                    gender.setText(genderin);
                    instrument.setText(skillin);
                    time.setText(String.valueOf(waktuin));
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

    private void deleteUser(){
        dialog.setMessage("Deleting User.");
        dialog.show();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String postUrl = Constant.DELETE_USER;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, postUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("loginsession", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getActivity(), StartMenuActivity.class);
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