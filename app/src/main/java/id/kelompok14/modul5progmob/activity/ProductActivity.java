package id.kelompok14.modul5progmob.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
import id.kelompok14.modul5progmob.model.ProductImagesModel;
import id.kelompok14.modul5progmob.model.ProductsModel;

public class ProductActivity extends AppCompatActivity {

    RatingBar ratingBar;
    TextView harga, stok, desc, nama, cat;
    Button homeButton, rentButton;
    ViewFlipper imageFlipper;

    DBHandler dbHandler;
    RecyclerView recyclerViews;
    ArrayList<Integer> mImages = new ArrayList<>();
    ArrayList<ProductsModel> product = new ArrayList<>();
    ArrayList<ProductsModel> products = new ArrayList<>();
    ArrayList<CategoriesDetailModel> categorydetail = new ArrayList<>();
    ArrayList<CategoriesModel> category = new ArrayList<>();
    ArrayList<ProductImagesModel> productImages = new ArrayList<>();
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
        imageFlipper = (ViewFlipper) findViewById(R.id.image_flipper);
        dbHandler = new DBHandler(getApplicationContext());

        ratingBar.setFocusable(false);
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        getCategory();
        getProduct();
        getCategoryDetail();
        getAllImage();

        product = dbHandler.getProductsOnID(idproduk);
        categorydetail = dbHandler.getProdCategoryOnID(product.get(0).getId_product());
        category = dbHandler.getCategoriesOnID(categorydetail.get(0).getId_category());

        ratingBar.setRating(product.get(0).getRating_product());
        harga.setText(formatRupiah.format(product.get(0).getPrice_product()));
        stok.setText(String.valueOf(product.get(0).getStock_product()));
        desc.setText(product.get(0).getDesc_product());
        nama.setText(product.get(0).getName_product());
        cat.setText(category.get(0).getName_category());

        recyclerViews = (RecyclerView) findViewById(R.id.recyclerSmallProduct);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViews.setLayoutManager(linearLayoutManager);
        recyclerViews.setHasFixedSize(true);

        products = dbHandler.getProducts();
        productsLongAdapter = new ProductsLongAdapter(ProductActivity.this, products, productImages);
        recyclerViews.setAdapter(productsLongAdapter);

//        mImages.add(R.drawable.ic_logo_menu_02);
//        mImages.add(R.drawable.ic_logo_menu_02);
//        mImages.add(R.drawable.ic_logo_menu_02);
//        mImages.add(R.drawable.ic_logo_menu_02);
//        mImages.add(R.drawable.ic_logo_menu_02);

//        CarouselView carouselView = findViewById(R.id.carousel);
//        if(mImages.size()>5){
//            jumlahGambar=5;
//        }else{
//            jumlahGambar=mImages.size();
//        }
//        carouselView.setPageCount(jumlahGambar);
//        carouselView.setImageListener(new ImageListener() {
//            @Override
//            public void setImageForPosition(int position, ImageView imageView) {
//                imageView.setImageResource(mImages.get(position));
//            }
//        });

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

        imageFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });

        getImage();
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

    private void getImage() {
        String postUrl = Constant.GET_PRODUCT_IMAGE;
        RequestQueue requestQueue = Volley.newRequestQueue(ProductActivity.this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("id_products", idproduk);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject object = response;
                try {
                    if (object.getBoolean("success")) {
                        JSONArray array = new JSONArray(object.getString("hasil"));
                        for(int i=0;i<array.length();i++) {
                            String gambarIn = (array.getString(i));
                            Bitmap a = stringToBitmap(gambarIn);
                            if(a!=null){
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ImageView image = new ImageView (ProductActivity.this);
                                        image.setImageBitmap(a);
                                        imageFlipper.addView(image);
                                        imageFlipper.setFlipInterval( 3000 ); //5s intervals
                                        imageFlipper.startFlipping();
                                    }
                                }, 1000);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
//                    Context context = ProductActivity.this;
//                    int duration = Toast.LENGTH_SHORT;
//                    Toast gagal = Toast.makeText(context, "Product Image Not Loaded.", duration);
//                    gagal.show();
                    getImage();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                Context context = ProductActivity.this;
//                int duration = Toast.LENGTH_SHORT;
//                Toast gagal = Toast.makeText(context, "Product Image Not Loaded.", duration);
//                gagal.show();
                getImage();
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

    private Bitmap stringToBitmap(String string) {
        try{
            byte[] byteArray1;
            byteArray1 = Base64.decode(string, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray1, 0, byteArray1.length);
            return bmp;
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        Bitmap bmp = null;
        return bmp;
    }

    private void getAllImage() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String postUrl = Constant.GET_ALL_PRODUCT_IMAGE;
        StringRequest request = new StringRequest(Request.Method.GET, postUrl, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    JSONArray arrayID = new JSONArray(object.getString("id"));
                    JSONArray arrayHasil = new JSONArray(object.getString("hasil"));
                    for(int i=0;i<arrayID.length();i++) {
                        int id = (arrayID.getInt(i));
                        String gambarIn = (arrayHasil.getString(i));
                        productImages.add(new ProductImagesModel(id, gambarIn));
                    }
                    products = dbHandler.getProducts();
                    productsLongAdapter = new ProductsLongAdapter(ProductActivity.this, products, productImages);
                    recyclerViews.setAdapter(productsLongAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
//                Context context = ProductActivity.this;
//                int duration = Toast.LENGTH_SHORT;
//                Toast gagal = Toast.makeText(context, "Images Not Loaded.", duration);
//                gagal.show();
                getAllImage();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Context context = ProductActivity.this;
//                int duration = Toast.LENGTH_SHORT;
//                Toast gagal = Toast.makeText(context, "Images Not Loaded.", duration);
//                gagal.show();
                getAllImage();
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