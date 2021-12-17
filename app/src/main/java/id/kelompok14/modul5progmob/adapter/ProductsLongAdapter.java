package id.kelompok14.modul5progmob.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import id.kelompok14.modul5progmob.R;
import id.kelompok14.modul5progmob.activity.ProductActivity;
import id.kelompok14.modul5progmob.database.DBHandler;
import id.kelompok14.modul5progmob.model.ProductImagesModel;
import id.kelompok14.modul5progmob.model.ProductsModel;

public class ProductsLongAdapter extends RecyclerView.Adapter<ProductsLongAdapter.MyViewHolderLong>{

    private Context context;
    private ArrayList<ProductsModel> products;
    private ArrayList<ProductsModel> mproducts;
    private ArrayList<ProductImagesModel> productImages;
    private DBHandler dbHandler;
    private Locale localeID = new Locale("in", "ID");
    private NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public ProductsLongAdapter(Context ct, ArrayList<ProductsModel> products, ArrayList<ProductImagesModel> productImages){
        this.context = ct;
        this.products = products;
        this.mproducts = products;
        this.productImages = productImages;
        dbHandler = new DBHandler(context);
    }

    @NonNull
    @Override
    public MyViewHolderLong onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_products_long, parent, false);
        return new MyViewHolderLong(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderLong holder, int position) {
        final ProductsModel product = mproducts.get(position);
        holder.nama.setText(product.getName_product());
        holder.harga.setText(formatRupiah.format(product.getPrice_product()));
        holder.ratingBar.setRating(product.getRating_product());
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
        return 4;
    }

    public class MyViewHolderLong extends RecyclerView.ViewHolder{
        TextView nama, harga, statusstok;
        CardView cardView;
        RatingBar ratingBar;
        ImageView imageView;

        public MyViewHolderLong(@NonNull View itemView) {
            super(itemView);
            nama = (TextView) itemView.findViewById(R.id.namaProductLong);
            harga = (TextView) itemView.findViewById(R.id.hargaProductLong);
            cardView = (CardView) itemView.findViewById(R.id.cardProdLong);
            statusstok = (TextView)  itemView.findViewById(R.id.statusStock2Long);
            ratingBar = (RatingBar) itemView.findViewById(R.id.myRatingBarLong);
            imageView = (ImageView) itemView.findViewById(R.id.gambarProdukLong);
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
                    ((ProductActivity)context).finish();
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