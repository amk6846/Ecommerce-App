package com.example.mshopping.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mshopping.Model.Product;
import com.example.mshopping.R;
import com.example.mshopping.databinding.ItemCartBinding;
import com.example.mshopping.databinding.QuantityDialogBinding;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    ArrayList<Product> products;
    CartListener cartListener;
    Cart cart;

    public interface CartListener {
        void onQuantityChanged();
    }

    public CartAdapter(Context context, ArrayList<Product> products, CartListener cartListener) {
        this.context = context;
        this.products = products;
        this.cart = TinyCartHelper.getCart();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = products.get(position);
        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.image);

        holder.binding.name.setText(product.getName());
        holder.binding.price.setText("PKR " + product.getPrice());
        holder.binding.quantity.setText(product.getQuantity() + " item(s)");

        holder.itemView.setOnClickListener(view -> {
            QuantityDialogBinding quantityDialogBinding = QuantityDialogBinding.inflate(LayoutInflater.from(context));

            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setView(quantityDialogBinding.getRoot())
                    .create();

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
            }

            quantityDialogBinding.productName.setText(product.getName());
            quantityDialogBinding.productStock.setText("Stock: " + product.getStock());
            quantityDialogBinding.quantity.setText(String.valueOf(product.getQuantity()));
            int stock = product.getStock();

            quantityDialogBinding.plusBtn.setOnClickListener(view1 -> {
                int quantity = product.getQuantity();
                quantity++;

                if (quantity > stock) {
                    Toast.makeText(context, "Max stock available: " + stock, Toast.LENGTH_SHORT).show();
                } else {
                    product.setQuantity(quantity);
                    quantityDialogBinding.quantity.setText(String.valueOf(quantity));
                    notifyDataSetChanged();
                    cart.updateItem(product, product.getQuantity());
                    cartListener.onQuantityChanged();
                }
            });

            quantityDialogBinding.minusBtn.setOnClickListener(view12 -> {
                int quantity = product.getQuantity();
                if (quantity > 1) {
                    quantity--;
                    product.setQuantity(quantity);
                    quantityDialogBinding.quantity.setText(String.valueOf(quantity));
                    notifyDataSetChanged();
                    cart.updateItem(product, product.getQuantity());
                    cartListener.onQuantityChanged();
                }
            });

            quantityDialogBinding.saveBtn.setOnClickListener(view13 -> {
                dialog.dismiss();
                notifyDataSetChanged();
                cart.updateItem(product, product.getQuantity());
                cartListener.onQuantityChanged();
            });

            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        ItemCartBinding binding;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCartBinding.bind(itemView);
        }
    }
}
