package id.kelompok14.modul5progmob.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import id.kelompok14.modul5progmob.activity.ProductActivity;
import id.kelompok14.modul5progmob.model.ProductsModel;
import id.kelompok14.modul5progmob.database.DBHandler;
import id.kelompok14.modul5progmob.R;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<ProductsModel> products;
    private ArrayList<ProductsModel> mproducts;

    private DBHandler dbHandler;
    private Locale localeID = new Locale("in", "ID");
    private NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public ProductsAdapter(Context ct, ArrayList<ProductsModel> products){
        this.context = ct;
        this.products = products;
        this.mproducts = products;
        dbHandler = new DBHandler(context);
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = (TextView) itemView.findViewById(R.id.namaProduct);
            harga = (TextView) itemView.findViewById(R.id.hargaProduct);
            cardView = (CardView) itemView.findViewById(R.id.cardviewProd);
            statusstok = (TextView)  itemView.findViewById(R.id.statusStock);
            ratingBar = (RatingBar) itemView.findViewById(R.id.myRatingBar);
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
}