package id.kelompok14.modul5progmob.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.kelompok14.modul5progmob.API.Constant;
import id.kelompok14.modul5progmob.R;
import id.kelompok14.modul5progmob.adapter.ProductsLongAdapter;
import id.kelompok14.modul5progmob.database.DBHandler;
import id.kelompok14.modul5progmob.model.CategoriesDetailModel;
import id.kelompok14.modul5progmob.model.CategoriesModel;
import id.kelompok14.modul5progmob.model.ProductsModel;

public class ProductActivity extends AppCompatActivity {

    RatingBar ratingBar;
    TextView harga, stok, desc, nama, cat;
    Button homeButton, rentButton;

    DBHandler dbHandler;
    RecyclerView recyclerViews;
    ArrayList<Integer> mImages = new ArrayList<Integer>();
    ArrayList<ProductsModel> product = new ArrayList<>();
    ArrayList<ProductsModel> products = new ArrayList<>();
    ArrayList<CategoriesDetailModel> categorydetail = new ArrayList<>();
    ArrayList<CategoriesModel> category = new ArrayList<>();
    int jumlahGambar;
    int idproduk;
    String token;
    SharedPreferences sharedPreferences;

    ProductsLongAdapter productsLongAdapter;

    //Format Integer Nilai Jadi Rupiah
    private Locale localeID = new Locale("in", "ID");
    private NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        sharedPreferences = getApplicationContext().getSharedPreferences("loginsession", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "defaultValues");

        Intent intent = getIntent();
        idproduk = intent.getIntExtra("idproduk", 1);

        homeButton = (Button) findViewById(R.id.homeButton);
        rentButton = (Button) findViewById(R.id.rentButton);
        ratingBar = (RatingBar) findViewById(R.id.myRatingBar);
        harga = (TextView) findViewById(R.id.hargaProduct);
        stok = (TextView) findViewById(R.id.stockProduk);
        desc = (TextView) findViewById(R.id.prodDesc);
        cat = (TextView) findViewById(R.id.categoryProduct);
        nama = (TextView) findViewById(R.id.namaProduk);
        dbHandler = new DBHandler(getApplicationContext());

        ratingBar.setFocusable(false);
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getCategory();
                getProduct();
                getCategoryDetail();
            }
        }, 0);

        product = dbHandler.getProductsOnID(idproduk);
        categorydetail = dbHandler.getProdCategoryOnID(product.get(0).getId_product());
        category = dbHandler.getCategoriesOnID(categorydetail.get(0).getId_category());

        ratingBar.setRating(product.get(0).getRating_product());
        harga.setText(formatRupiah.format(product.get(0).getPrice_product()));
        stok.setText(String.valueOf(product.get(0).getStock_product()));
        desc.setText(product.get(0).getDesc_product());
        nama.setText(product.get(0).getName_product());
        cat.setText(category.get(0).getName_category());

        mImages.add(R.drawable.ic_logo_menu_02);
        mImages.add(R.drawable.ic_logo_menu_02);
        mImages.add(R.drawable.ic_logo_menu_02);
        mImages.add(R.drawable.ic_logo_menu_02);
        mImages.add(R.drawable.ic_logo_menu_02);

        CarouselView carouselView = findViewById(R.id.carousel);
        if(mImages.size()>5){
            jumlahGambar=5;
        }else{
            jumlahGambar=mImages.size();
        }
        carouselView.setPageCount(jumlahGambar);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(mImages.get(position));
            }
        });

        if(product.get(0).getStock_product()==0){
//            rentButton.setFocusable(View.NOT_FOCUSABLE);
            rentButton.setBackgroundColor(rentButton.getContext().getResources().getColor(R.color.abusoft));
            rentButton.setText("Instrument Not Available.");
        }else{
            rentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent checkout = new Intent(ProductActivity.this, CheckoutActivity.class);
                    checkout.putExtra("idprodukpilihan", idproduk);
                    startActivity(checkout);
                }
            });
        }

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerViews = (RecyclerView) findViewById(R.id.recyclerSmallProduct);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViews.setLayoutManager(linearLayoutManager);
        recyclerViews.setHasFixedSize(true);

        products = dbHandler.getProducts();
        productsLongAdapter = new ProductsLongAdapter(ProductActivity.this, products);
        recyclerViews.setAdapter(productsLongAdapter);
    }

    private void getCategory() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String postUrl = Constant.GET_CATEGORY;
        StringRequest request = new StringRequest(Request.Method.GET, postUrl, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    dbHandler.deleteCategories();
                    JSONArray array = new JSONArray(object.getString("hasil"));
                    for(int i=0;i<array.length();i++){
                        JSONObject catObject = array.getJSONObject(i);;
                        dbHandler.addNewCategory(catObject.getString("name_category"));
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

    private void getCategoryDetail() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String postUrl = Constant.GET_CATEGORY_DETAIL;
        StringRequest request = new StringRequest(Request.Method.GET, postUrl, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    dbHandler.deleteCategoriesDetail();
                    JSONArray array = new JSONArray(object.getString("hasil"));
                    for(int i=0;i<array.length();i++){
                        JSONObject catDetObject = array.getJSONObject(i);;
                        dbHandler.addNewCategoryDetail(catDetObject.getInt("id_category"), catDetObject.getInt("id_products"));
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
                        JSONObject prodObject = array.getJSONObject(i);;
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