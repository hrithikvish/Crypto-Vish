package com.example.cryptovish;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.viewHolder> {

    Context context;
    ArrayList<CryptoModal> arrCrypto;
    CryptoAdapter(Context context, ArrayList<CryptoModal> arrCrypto) {
        this.context = context;
        this.arrCrypto =  arrCrypto;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.crypto_rv_item, parent, false);
        viewHolder viewholder = new viewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        String name = arrCrypto.get(position).cyptoName;
        Double price = arrCrypto.get(position).price;
        String formattedPrice = String.format("%.2f", price) + "$";
        Double change24h = arrCrypto.get(position).change24h;
        String formattedChange24h = String.format("%.2f", change24h);

        String change1h = arrCrypto.get(position).change1h.toString();
        String change24hStr = change24h.toString();
        String change7d = arrCrypto.get(position).change7d.toString();
        String change30d = arrCrypto.get(position).change30d.toString();
        String change60d = arrCrypto.get(position).change60d.toString();
        String change90d = arrCrypto.get(position).change90d.toString();

        holder.cryptoName.setText(name);
        holder.nameSymbol.setText(arrCrypto.get(position).nameSymbol);
        holder.price.setText(formattedPrice);
        if (change24h < 0) holder.change24h.setTextColor(Color.RED);
        else holder.change24h.setTextColor(Color.parseColor("#00FF2A"));
        holder.change24h.setText(formattedChange24h + "%");

        holder.price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CryptoDialog cryptoDialog = new CryptoDialog(String.valueOf(price) + "$");
                cryptoDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "PriceDialog");
            }
        });

        holder.change24h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CryptoDialog cryptoDialog = new CryptoDialog(change1h, change24hStr, change7d, change30d, change60d, change90d);
                cryptoDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "ChangePercentDialog");
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrCrypto.size();
    }

    public void filterList(ArrayList<CryptoModal> filteredList) {
        arrCrypto = filteredList;
        notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView cryptoName, nameSymbol, price, change24h;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            cryptoName = itemView.findViewById(R.id.name);
            nameSymbol = itemView.findViewById(R.id.namesymbol);
            price = itemView.findViewById(R.id.price);
            change24h = itemView.findViewById(R.id.change24h);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
//            Toast.makeText(context, "Position: " + position, Toast.LENGTH_SHORT).show();
            CryptoModal selectedCrypto = arrCrypto.get(position);

            Intent intent = new Intent(context, SelectedCrypto.class);
            intent.putExtra("selectedCrypto", selectedCrypto);

            context.startActivity(intent);
        }
    }

}
