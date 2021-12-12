package id.kelompok14.modul5progmob.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import id.kelompok14.modul5progmob.activity.TransactionDetailActivity;
import id.kelompok14.modul5progmob.database.DBHandler;
import id.kelompok14.modul5progmob.model.ProductsModel;
import id.kelompok14.modul5progmob.model.TransactionsModel;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<TransactionsModel> transactions;
    private ArrayList<ProductsModel> product;
    private ArrayList<TransactionsModel> mtrasns;
    private DBHandler dbHandler;


    public TransactionAdapter(Context ct, ArrayList<TransactionsModel> transactions){
        this.context = ct;
        this.transactions = transactions;
        this.mtrasns = transactions;
        dbHandler = new DBHandler(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_transaction, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final TransactionsModel tran = mtrasns.get(position);
        holder.nomor.setText(String.valueOf(position+1));
        holder.nama.setText(dbHandler.getProductsOnID(tran.getId_product_transaction()).get(0).getName_product());
        holder.start.setText(tran.getStart_transaction());
        holder.end.setText(tran.getEnd_transaction());
        holder.ratingBar.setRating(tran.getRating());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nama, start, end, nomor;
        CardView cardView;
        RatingBar ratingBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nomor = (TextView) itemView.findViewById(R.id.nomorTrans);
            nama = (TextView) itemView.findViewById(R.id.namaProduct);
            start = (TextView) itemView.findViewById(R.id.tempatStartRecycler);
            end = (TextView)  itemView.findViewById(R.id.tempatEndRecycler);
            cardView = (CardView) itemView.findViewById(R.id.cardTrans);
            ratingBar = (RatingBar) itemView.findViewById(R.id.myRatingBarTrans);

            ratingBar.setFocusable(false);
            ratingBar.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int idtrans = mtrasns.get(getAdapterPosition()).getId_transaction();
                    Intent intent = new Intent(context, TransactionDetailActivity.class);
                    intent.putExtra("idtrans",idtrans);
                    context.startActivity(intent);
                }
            });

        }
    }
}