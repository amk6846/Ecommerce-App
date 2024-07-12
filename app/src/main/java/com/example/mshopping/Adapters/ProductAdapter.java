package com.example.mshopping.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mshopping.Activities.ProductDetailActivity;
import com.example.mshopping.Model.Product;
import com.example.mshopping.R;
import com.example.mshopping.databinding.ItemProductBinding;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    ArrayList<Product> products;

    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);

        // Use Glide to load the image
        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.image);

        // Set other product details
        holder.binding.label.setText(product.getName());
        holder.binding.price.setText("PKR " + product.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("name", product.getName());
                intent.putExtra("image", product.getImage());
                intent.putExtra("description", product.getDesc());
                intent.putExtra("id", product.getId());
                intent.putExtra("price", product.getPrice());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductBinding.bind(itemView);
        }
    }
}
