package id.kelompok14.modul5progmob.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import id.kelompok14.modul5progmob.activity.ProductActivity;
import id.kelompok14.modul5progmob.adapter.ProductsAdapter;
import id.kelompok14.modul5progmob.database.DBHandler;
import id.kelompok14.modul5progmob.model.CategoriesDetailModel;
import id.kelompok14.modul5progmob.model.CategoriesModel;
import id.kelompok14.modul5progmob.model.ProductImagesModel;
import id.kelompok14.modul5progmob.model.ProductsModel;

public class HomeFragment extends Fragment {

    DBHandler dbHandler;
    ArrayList<ProductsModel> products=new ArrayList<>();
    ArrayList<ProductsModel> products1=new ArrayList<>();
    ArrayList<CategoriesModel> categories=new ArrayList<>();
    ArrayList<CategoriesModel> categories1=new ArrayList<>();
    ArrayList<CategoriesDetailModel> categoriesDetails = new ArrayList<>();
    ArrayList<ProductImagesModel> productImages = new ArrayList<>();
    Spinner spinnerCategory;
    ProductsAdapter productsAdapter;
    RecyclerView recyclerView;
    String pilihanCategoryStr;
    int status;
    String token;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("loginsession", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "defaultValues");

        categories.add(new CategoriesModel(0, "All Products"));
        getCategory();
        getProduct();
        getCategoryDetail();
        getImage();

        dbHandler = new DBHandler(getContext());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                categories1 = dbHandler.getCategories();
                categories.addAll(categories1);
            }
        }, 1000);

        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview_product);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setHasFixedSize(true);

        //Spinner
        spinnerCategory = (Spinner) view.findViewById(R.id.spinnerCategory);
        ArrayAdapter<CategoriesModel> spinneradapter = new ArrayAdapter<CategoriesModel>(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinneradapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Membaca pilihan periode
                pilihanCategoryStr = spinnerCategory.getSelectedItem().toString();

                if(pilihanCategoryStr.equals("All Products")){
                    allAdapter();
                }else{
                    catAdapter(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });


    }

    void allAdapter(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                products = dbHandler.getProducts();
                productsAdapter = new ProductsAdapter(getContext(), products, productImages);
                recyclerView.setAdapter(productsAdapter);
            }
        }, 2000);
    }

    void catAdapter(int id){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                products1.clear();
                categoriesDetails = dbHandler.getIDProductOnCategory(id);
                for (int counter = 0; counter < categoriesDetails.size(); counter++) {
                    int idin = categoriesDetails.get(counter).getId_product();
                    products = dbHandler.getProductsOnID(idin);
                    products1.addAll(products);
                }
                productsAdapter = new ProductsAdapter(getContext(), products1, productImages);
                recyclerView.setAdapter(productsAdapter);
            }
        }, 2000);
    }

    private void getCategory() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                Context context = getContext();
                int duration = Toast.LENGTH_SHORT;
                Toast gagal = Toast.makeText(context, "Details Not Loaded.", duration);
                gagal.show();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Context context = getContext();
                int duration = Toast.LENGTH_SHORT;
                Toast gagal = Toast.makeText(context, "Details Not Loaded.", duration);
                gagal.show();
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
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                Context context = getContext();
                int duration = Toast.LENGTH_SHORT;
                Toast gagal = Toast.makeText(context, "Category Not Loaded.", duration);
                gagal.show();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Context context = getContext();
                int duration = Toast.LENGTH_SHORT;
                Toast gagal = Toast.makeText(context, "Category Not Loaded.", duration);
                gagal.show();
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
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                Context context = getContext();
                int duration = Toast.LENGTH_SHORT;
                Toast gagal = Toast.makeText(context, "Product Not Loaded.", duration);
                gagal.show();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Context context = getContext();
                int duration = Toast.LENGTH_SHORT;
                Toast gagal = Toast.makeText(context, "Product Not Loaded.", duration);
                gagal.show();
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
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
//                Context context = getContext();
//                int duration = Toast.LENGTH_SHORT;
//                Toast gagal = Toast.makeText(context, "Images Not Loaded.", duration);
//                gagal.show();
                getImage();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Context context = getContext();
//                int duration = Toast.LENGTH_SHORT;
//                Toast gagal = Toast.makeText(context, "Images Not Loaded.", duration);
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
        queue.add(request);

    }
}