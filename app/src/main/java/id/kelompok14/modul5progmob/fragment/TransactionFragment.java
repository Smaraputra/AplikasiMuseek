package id.kelompok14.modul5progmob.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.kelompok14.modul5progmob.API.Constant;
import id.kelompok14.modul5progmob.R;
import id.kelompok14.modul5progmob.activity.CheckoutActivity;
import id.kelompok14.modul5progmob.activity.DashboardAppActivity;
import id.kelompok14.modul5progmob.activity.StartMenuActivity;
import id.kelompok14.modul5progmob.adapter.ProductsAdapter;
import id.kelompok14.modul5progmob.adapter.TransactionAdapter;
import id.kelompok14.modul5progmob.database.DBHandler;
import id.kelompok14.modul5progmob.model.CategoriesModel;
import id.kelompok14.modul5progmob.model.ProductsModel;
import id.kelompok14.modul5progmob.model.TransactionsModel;

public class TransactionFragment extends Fragment {

    TransactionAdapter transactionAdapter;
    RecyclerView recyclerView;
    DBHandler dbHandler;
    SharedPreferences sharedPreferences;
    String token;
    int iduser;
    ProgressDialog dialog;

    ArrayList<TransactionsModel> transactions = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        sharedPreferences = getContext().getSharedPreferences("loginsession", CheckoutActivity.MODE_PRIVATE);
        iduser = sharedPreferences.getInt("iduser",0);
        token = sharedPreferences.getString("token", "defaultValues");
        dialog = new ProgressDialog(getContext());
        getTransaction();
        dbHandler = new DBHandler(getContext());

        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview_trans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                transactions = dbHandler.getTransactionOnIDUSER(iduser);
                transactionAdapter = new TransactionAdapter(getContext(), transactions);
                recyclerView.setAdapter(transactionAdapter);
            }
        }, 1000);
    }

    private void getTransaction() {
        dialog.setMessage("Loading Transaction Data.");
        dialog.show();
        String postUrl = Constant.GET_TRANSACTION;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JSONObject postData = new JSONObject();
        try {
            postData.put("id_user_transaction", iduser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject object = response;
                try {
                    if (object.getBoolean("success")){
                        dbHandler.deleteTransaction();
                        JSONArray array = new JSONArray(object.getString("hasil"));
                        for(int i=0;i<array.length();i++){
                            JSONObject transObject = array.getJSONObject(i);
                            dbHandler.addNewTransaction(
                                    transObject.getInt("id_transaction"),
                                    transObject.getInt("id_user_transaction"),
                                    transObject.getInt("id_product_transaction"),
                                    transObject.getString("start_transaction"),
                                    transObject.getString("end_transaction"),
                                    transObject.getInt("total_product"),
                                    transObject.getInt("total_transaction"),
                                    transObject.getString("status_transaction"),
                                    transObject.getInt("rating"),
                                    transObject.getString("status_rating_transaction"),
                                    transObject.getString("date_transaction"),
                                    transObject.getString("deadline_payment"),
                                    transObject.getString("status_payment"),
                                    transObject.getString("proof"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Context context = getContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast gagal = Toast.makeText(context, "Loading Transaction Failed.", duration);
                    gagal.show();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Context context = getContext();
                int duration = Toast.LENGTH_SHORT;
                Toast gagal = Toast.makeText(context, "Loading Transaction Failed.", duration);
                gagal.show();
                dialog.dismiss();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+ token);
                return map;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
}