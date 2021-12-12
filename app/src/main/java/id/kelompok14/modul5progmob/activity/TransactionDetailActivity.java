package id.kelompok14.modul5progmob.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.kelompok14.modul5progmob.API.Constant;
import id.kelompok14.modul5progmob.R;
import id.kelompok14.modul5progmob.database.DBHandler;
import id.kelompok14.modul5progmob.model.ProductsModel;
import id.kelompok14.modul5progmob.model.TransactionsModel;

public class TransactionDetailActivity extends AppCompatActivity {

    Button backToTrans, addRating, addPayment;
    TextView namaproduk, start, end, totalIns, total, status,
            date, rating, status_rating, deadline, status_payment;
    int idtrans, iduser;
    int selectPayment = 1;
    String token;
    ImageView buktiBayar;
    ProgressDialog dialog;

    Uri uri;
    SharedPreferences sharedPreferences;

    DBHandler dbHandler;
    ArrayList<TransactionsModel> transaction = new ArrayList<>();
    ArrayList<ProductsModel> product = new ArrayList<>();

    //Format Integer Nilai Jadi Rupiah
    private Locale localeID = new Locale("in", "ID");
    private NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        Intent intent = getIntent();
        idtrans = intent.getIntExtra("idtrans", 1);
        dbHandler = new DBHandler(TransactionDetailActivity.this);
        sharedPreferences = getSharedPreferences("loginsession", CheckoutActivity.MODE_PRIVATE);
        iduser = sharedPreferences.getInt("iduser",0);
        token = sharedPreferences.getString("token", "defaultValues");
        dialog = new ProgressDialog(TransactionDetailActivity.this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getTransaction();
            }
        }, 0);

        transaction = dbHandler.getTransactionOnIDTRANS(idtrans);
        product = dbHandler.getProductsOnID(transaction.get(0).getId_product_transaction());

        addRating = (Button) findViewById(R.id.tombolTambahRatingTrans);
        addPayment = (Button) findViewById(R.id.tambahBayarTrans);
        backToTrans = (Button) findViewById(R.id.backTrans);
        namaproduk = (TextView) findViewById(R.id.kolomProdTrans);
        start = (TextView) findViewById(R.id.kolomStartTrans);
        end = (TextView) findViewById(R.id.kolomEndTrans);
        totalIns = (TextView) findViewById(R.id.kolomTotalInstrumentTrans);
        total = (TextView) findViewById(R.id.kolomTotalHargaTrans);
        status = (TextView) findViewById(R.id.kolomStatusTrans);
        date = (TextView) findViewById(R.id.kolomTanggalTrans);
        rating = (TextView) findViewById(R.id.kolomRatingTrans);
        status_rating = (TextView) findViewById(R.id.kolomStatusRatingTrans);
        deadline = (TextView) findViewById(R.id.kolomPaymentDeadlineTrans);
        status_payment = (TextView) findViewById(R.id.kolomStatusPaymentTrans);
        buktiBayar = (ImageView) findViewById(R.id.buktiBayarTrans);

        namaproduk.setText(product.get(0).getName_product());
        start.setText(transaction.get(0).getStart_transaction());
        end.setText(transaction.get(0).getEnd_transaction());
        totalIns.setText(String.valueOf(transaction.get(0).getTotal_product()));
        total.setText(formatRupiah.format(transaction.get(0).getTotal_transaction()));
        status.setText(transaction.get(0).getStatus_transaction());
        date.setText(transaction.get(0).getDate_transaction());
        rating.setText(String.valueOf(transaction.get(0).getRating()));
        status_rating.setText(transaction.get(0).getStatus_rating_transaction());

        deadline.setText(transaction.get(0).getDeadline_payment());
        status_payment.setText(transaction.get(0).getStatus_payment());

        backToTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(transaction.get(0).getStatus_transaction().equals("Success")){
            addRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }else{
            addRating.setClickable(false);
            addRating.setBackgroundColor(addRating.getContext().getResources().getColor(R.color.abusoft));
        }

        addPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGetPayment = new Intent(Intent.ACTION_PICK);
                intentGetPayment.setType("image/*");
                startActivityForResult(intentGetPayment, selectPayment);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == selectPayment && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                buktiBayar.setImageBitmap(bitmap);
            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void getTransaction() {
        dialog.setMessage("Loading Transaction Data.");
        dialog.show();
        String postUrl = Constant.GET_TRANSACTION;
        RequestQueue requestQueue = Volley.newRequestQueue(TransactionDetailActivity.this);

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
                                    transObject.getString("status_rating_transaction"),
                                    transObject.getInt("rating"),
                                    transObject.getString("status_transaction"),
                                    transObject.getString("date_transaction"),
                                    transObject.getString("status_payment"),
                                    transObject.getString("deadline_payment"),
                                    transObject.getString("proof"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Context context = TransactionDetailActivity.this;
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
                Context context = TransactionDetailActivity.this;
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