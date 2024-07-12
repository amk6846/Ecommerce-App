package com.example.mshopping.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mshopping.Model.Product;
import com.example.mshopping.R;
import com.example.mshopping.databinding.ActivityProductDetailBinding;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ProductDetailActivity extends AppCompatActivity {
    ActivityProductDetailBinding binding;
    Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Disable the add to cart button until product details are loaded
        binding.addtocartbtn.setEnabled(false);

        // Get product details from the intent
        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        String description = getIntent().getStringExtra("description");
        int id = getIntent().getIntExtra("id", 0);
        double price = getIntent().getDoubleExtra("price", 0);

        // Set product details to views
        binding.productdesc.setText(Html.fromHtml(description));
        Glide.with(this).load(image).into(binding.productimg);
        Objects.requireNonNull(getSupportActionBar()).setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Fetch additional product details from the server
        getProductDetail(id);

        Cart cart = TinyCartHelper.getCart();

        binding.addtocartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentProduct != null) {
                    cart.addItem(currentProduct, 1);
                    binding.addtocartbtn.setEnabled(false);
                    binding.addtocartbtn.setText("Added to cart");
                }
            }
        });
    }

    private void getProductDetail(int productId) {
        String url = "https://dummyjson.com/products/" + productId; // Assuming the API URL uses product ID
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject child = new JSONObject(response);

                currentProduct = new Product(
                        child.getString("title"),
                        child.getJSONArray("images").getString(0), // Assuming the first image
                        child.getString("category"),
                        child.getString("description"),
                        child.getDouble("price"),
                        child.getDouble("discountPercentage"),
                        child.getInt("stock"),
                        child.getInt("id")
                );

                // Enable the add to cart button after product details are loaded
                binding.addtocartbtn.setEnabled(true);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("Volley Error", "Error: " + error.getMessage());
        });

        queue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cart) {
            startActivity(new Intent(this, CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
