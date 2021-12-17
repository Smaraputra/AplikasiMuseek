package id.kelompok14.modul5progmob.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.kelompok14.modul5progmob.API.Constant;
import id.kelompok14.modul5progmob.activity.ProductActivity;
import id.kelompok14.modul5progmob.activity.TransactionDetailActivity;
import id.kelompok14.modul5progmob.model.ProductImagesModel;
import id.kelompok14.modul5progmob.model.ProductsModel;
import id.kelompok14.modul5progmob.database.DBHandler;
import id.kelompok14.modul5progmob.R;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<ProductsModel> products;
    private ArrayList<ProductsModel> mproducts;
    private ArrayList<ProductImagesModel> productImages;
    ProgressDialog dialog;
    SharedPreferences sharedPreferences;
    String token;
    Bitmap a;
    private DBHandler dbHandler;
    private Locale localeID = new Locale("in", "ID");
    private NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public ProductsAdapter(Context ct, ArrayList<ProductsModel> products, ArrayList<ProductImagesModel> productImages){
        this.context = ct;
        this.products = products;
        this.mproducts = products;
        this.productImages = productImages;
        dbHandler = new DBHandler(context);
        dialog = new ProgressDialog(context);

        sharedPreferences = context.getSharedPreferences("loginsession", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "defaultValues");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_products, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ProductsModel product = mproducts.get(position);
        holder.nama.setText(product.getName_product());
        holder.harga.setText(formatRupiah.format(product.getPrice_product()));
        holder.ratingBar.setRating(product.getRating_product());

        int idprod = products.get(position).getId_product();
        if(productImages.size()>0){
            holder.imageView.setImageBitmap(stringToBitmap(productImages.get(position).getGambar()));
//            for(int i = 0; i <= productImages.size(); i++){
//                if(idprod==productImages.get(i).getId_product()){
//
//                }
//            }
        }

        if(product.getStock_product()==0){
            holder.statusstok.setText("Out of Stock");
            holder.statusstok.setTextColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nama, harga, statusstok;
        CardView cardView;
        RatingBar ratingBar;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = (TextView) itemView.findViewById(R.id.namaProduct);
            harga = (TextView) itemView.findViewById(R.id.hargaProduct);
            cardView = (CardView) itemView.findViewById(R.id.cardviewProd);
            statusstok = (TextView)  itemView.findViewById(R.id.statusStock);
            ratingBar = (RatingBar) itemView.findViewById(R.id.myRatingBar);
            imageView = (ImageView) itemView.findViewById(R.id.gambarProdukBiasa);
            ratingBar.setFocusable(false);
            ratingBar.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int idprod = products.get(getAdapterPosition()).getId_product();
                    Intent intent = new Intent(context, ProductActivity.class);
                    intent.putExtra("idproduk",idprod);
                    context.startActivity(intent);
                }
            });
        }
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
}