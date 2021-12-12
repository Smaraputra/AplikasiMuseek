package id.kelompok14.modul5progmob.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import id.kelompok14.modul5progmob.API.Constant;
import id.kelompok14.modul5progmob.R;
import id.kelompok14.modul5progmob.database.DBHandler;
import id.kelompok14.modul5progmob.model.ProductsModel;

public class CheckoutActivity extends AppCompatActivity {

    TextView start, end, total, total_instrument, total_day, total_price;
    ImageView startB, endB, add, remove;
    Button back, rent;

    private int mYear, mMonth, mDay;
    long endMS, startMS;
    int statusDate, idprod, maxstock, curstock=1, totalday, harga, totalharga, iduser;
    String startDate, endDate, curdate, deadline;
    ArrayList<ProductsModel> product = new ArrayList<>();
    Date startFinal, endFinal;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    DBHandler dbHandler;
    String token;
    SharedPreferences sharedPreferences;

    ProgressDialog dialog;

    //Format Integer Nilai Jadi Rupiah
    private Locale localeID = new Locale("in", "ID");
    private NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        sharedPreferences = getApplicationContext().getSharedPreferences("loginsession", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "defaultValues");
        iduser = sharedPreferences.getInt("iduser",0);
        dialog = new ProgressDialog(CheckoutActivity.this);

        Intent intent = getIntent();
        idprod = intent.getIntExtra("idprodukpilihan", 1);
        dbHandler = new DBHandler(CheckoutActivity.this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getProduct();
            }
        }, 0);

        product = dbHandler.getProductsOnID(idprod);
        maxstock = product.get(0).getStock_product();
        harga = product.get(0).getPrice_product();

        start = (TextView) findViewById(R.id.kolomStartDate);
        end = (TextView)  findViewById(R.id.kolomEndDate);
        total = (TextView)  findViewById(R.id.kolomTotal);
        total_instrument = (TextView)  findViewById(R.id.kolomTotalInstrument);
        total_day = (TextView)  findViewById(R.id.kolomTotalDay);
        total_price = (TextView)  findViewById(R.id.kolomTotalPrice);
        back = (Button) findViewById(R.id.backButton);
        rent = (Button) findViewById(R.id.finishRentButton);

        add = (ImageView) findViewById(R.id.tombolTambah);
        remove = (ImageView) findViewById(R.id.tombolKurang);
        startB = (ImageView)  findViewById(R.id.tombolStartDate);
        endB = (ImageView)  findViewById(R.id.tombolEndDate);

        startB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CheckoutActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDate = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1) + "/" + String.valueOf(year);
                        try {
                            startFinal = sdf.parse(startDate);
                            startMS = startFinal.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        statusDate = statusDate + 1;
                        if (statusDate >= 2){
                            long diff = endFinal.getTime() - startFinal.getTime();
                            total_day.setText(String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
                            totalday = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                            totalharga = harga * totalday * curstock;
                            total_price.setText(formatRupiah.format(totalharga));
                        }
                        startDate = sdf.format(startFinal);
                        start.setText(startDate);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 86400000);
                if(startMS!=0){
                    datePickerDialog.getDatePicker().setMaxDate(endMS - 86400000);
                }
                datePickerDialog.show();
            }
        });

        endB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CheckoutActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDate = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1) + "/" + String.valueOf(year);
                        try {
                            endFinal = sdf.parse(endDate);
                            endMS = endFinal.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        statusDate = statusDate + 1;
                        if (statusDate >= 2){
                            long diff = endFinal.getTime() - startFinal.getTime();
                            total_day.setText(String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
                            totalday = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                            totalharga = harga * totalday * curstock;
                            total_price.setText(formatRupiah.format(totalharga));
                        }
                        endDate = sdf.format(endFinal);
                        end.setText(endDate);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 86400000*2);
                if(startMS!=0){
                    datePickerDialog.getDatePicker().setMinDate(startMS + 86400000);
                }
                datePickerDialog.show();
            }
        });

        total.setText(String.valueOf(curstock));
        total_instrument.setText(String.valueOf(curstock));
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curstock-1!=0){
                    curstock--;
                }
                total.setText(String.valueOf(curstock));
                total_instrument.setText(String.valueOf(curstock));
                if (statusDate >= 2){
                    totalharga = harga * totalday * curstock;
                    total_price.setText(formatRupiah.format(totalharga));
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curstock+1<=maxstock){
                    curstock++;
                }
                total.setText(String.valueOf(curstock));
                total_instrument.setText(String.valueOf(curstock));
                if (statusDate >= 2){
                    totalharga = harga * totalday * curstock;
                    total_price.setText(formatRupiah.format(totalharga));
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(statusDate>=2){
                    showDialog();
                }
            }
        });
    }

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set title dialog
        alertDialogBuilder.setTitle("You want to rent this instrument?");

        // Set pesan dari dialog
        alertDialogBuilder
                .setMessage("Choose 'Yes' to confirm.")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // Jika tombol diklik, maka akan menutup activity ini
                        curdate = sdf.format(System.currentTimeMillis() - 1000);
                        deadline = sdf.format(System.currentTimeMillis() + 86400000);

//                        long idtrans = dbHandler.addNewTransaction(iduser, idprod, startDate, endDate, curstock, totalharga
//                                , "Awaiting Payment", 0, "Not Yet", curdate, "Not Yet", deadline, "");
                        addTransaction();
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

    private void addTransaction(){
        dialog.setMessage("Creating Transaction.");
        dialog.show();
        String postUrl = Constant.ADD_TRANSACTION;
        RequestQueue requestQueue = Volley.newRequestQueue(CheckoutActivity.this);
        JSONObject postData = new JSONObject();
        try {
            postData.put("id_user_transaction", iduser);
            postData.put("id_product_transaction", idprod);
            postData.put("start_transaction", startDate);
            postData.put("end_transaction", endDate);
            postData.put("total_product", curstock);
            postData.put("total_transaction", totalharga);
            postData.put("rating", 0);
            postData.put("status_rating_transaction", "Not Yet");
            postData.put("status_transaction", "Awaiting Payment");
            postData.put("date_transaction", curdate);
            postData.put("status_payment", "Not Yet");
            postData.put("deadline_payment", deadline);
            postData.put("proof", null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject object = response;
                try {
                    if(object.getBoolean("success")){
                        Context context = CheckoutActivity.this;
                        int duration = Toast.LENGTH_SHORT;
                        Toast sukses = Toast.makeText(context, "Add Transaction Success.", duration);
                        sukses.show();

                        Intent intent1 = new Intent(CheckoutActivity.this, FinishRentActivity.class);
                        startActivity(intent1);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Context context = CheckoutActivity.this;
                    int duration = Toast.LENGTH_SHORT;
                    Toast sukses = Toast.makeText(context, "Add Transaction Failed.", duration);
                    sukses.show();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Context context = CheckoutActivity.this;
                int duration = Toast.LENGTH_SHORT;
                Toast sukses = Toast.makeText(context, "Add Transaction Failed.", duration);
                sukses.show();
                dialog.dismiss();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+ token);
                return map;
            }
        };;

        requestQueue.add(jsonObjectRequest);
    }

    private void getProduct() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String postUrl = Constant.GET_PRODUCT;
        StringRequest request = new StringRequest(Request.Method.GET, postUrl, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    dbHandler.deleteProduct();
                    JSONArray array = new JSONArray(object.getString("hasil"));
                    for(int i=0;i<array.length();i++){
                        JSONObject prodObject = array.getJSONObject(i);
                        dbHandler.addNewProduct
                                (prodObject.getString("name_product"),
                                        (prodObject.getString("desc_product")),
                                        (prodObject.getInt("stock_product")),
                                        (prodObject.getInt("price_product")),
                                        (prodObject.getInt("rating_product")));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+ token);
                return map;
            }
        };
        queue.add(request);
    }
}