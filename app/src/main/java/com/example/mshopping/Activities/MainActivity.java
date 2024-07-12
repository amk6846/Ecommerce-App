package com.example.mshopping.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mshopping.Adapters.CategoryAdapter;
import com.example.mshopping.Adapters.ProductAdapter;
import com.example.mshopping.Model.Category;
import com.example.mshopping.Model.Product;
import com.example.mshopping.R;
import com.example.mshopping.databinding.ActivityMainBinding;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categories;

    ProductAdapter productAdapter;
    ArrayList<Product> products;

    private final String[] imageUrls = {
            "https://img.freepik.com/free-psd/special-deal-super-offer-upto-60-parcent-off-isolated-3d-render-with-editable-text_47987-15330.jpg",
            "https://orient.com.pk/cdn/shop/articles/Blog_Cover_31914c9e-72e8-4852-ae24-f549493d5d18.jpg?v=1542801283",
            "https://www.siriusjewels.com/uploads/blogs/copy_1WhatsApp%20Image%202022-04-23%20at%204.49.54%20PM.jpeg?v=2",
            "https://us-wd.gr-cdn.com/blog/sites/5/2022/05/0954/discount-strategy-guide-15.png"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initCategories();
        initProducts();
        initSlider();
    }

    private void initSlider() {

          getRecentOffers();
    }
    private void getRecentOffers() {

        ImageCarousel carousel = findViewById(R.id.carousel);

        List<CarouselItem> carouselItems = new ArrayList<>();
        for (String url : imageUrls) {
            carouselItems.add(new CarouselItem(url));
        }

        carousel.addData(carouselItems);
    }

    private void initCategories() {
        categories = new ArrayList<>();

        getCategories();
        categoryAdapter = new CategoryAdapter(this, categories);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        binding.categorieslist.setLayoutManager(layoutManager);
        binding.categorieslist.setAdapter(categoryAdapter);
    }

    private void getCategories() {
        String url = "https://fakestoreapi.com/products/categories";
        RequestQueue queue = Volley.newRequestQueue(this);

        // Array of URLs for each category
        String[] categoryUrls = {
                "https://cdn-icons-png.flaticon.com/128/2777/2777142.png",
                "https://cdn0.iconfinder.com/data/icons/online-shop-categories-vol-1/64/SHOPPING_CAT-PRIMARY-LINE-64_jewerly-512.png",
                "https://cdn3.iconfinder.com/data/icons/men-s-clothing-3-line/64/male_clothes-12-512.png",
                "https://cdn-icons-png.flaticon.com/512/83/83214.png"
        };

        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // List of required categories
                        ArrayList<String> requiredCategories = new ArrayList<>();
                        requiredCategories.add("electronics");
                        requiredCategories.add("jewelery");
                        requiredCategories.add("men's clothing");
                        requiredCategories.add("women's clothing");

                        for (int i = 0; i < response.length(); i++) {
                            String categoryName = response.getString(i);
                            if (requiredCategories.contains(categoryName)) {
                                // Create a Category object with id and corresponding URL from the array
                                String imageUrl = categoryUrls[i % categoryUrls.length];  // Cycle through the URLs
                                Category category = new Category(
                                        i,  // Use the index as a dummy ID
                                        categoryName,
                                        imageUrl
                                );
                                categories.add(category);
                            }
                        }
                        categoryAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("Volley Error", "Error: " + error.getMessage()));
        queue.add(request);
    }

    private void initProducts() {
        products = new ArrayList<>();
        getRecentProducts();

        productAdapter = new ProductAdapter(this, products);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.productlist.setLayoutManager(layoutManager);
        binding.productlist.setAdapter(productAdapter);
    }

    private void getRecentProducts() {
        String url = "https://dummyjson.com/products";
        RequestQueue queue = Volley.newRequestQueue(this);

        @SuppressLint("NotifyDataSetChanged") StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                JSONArray productsArray = object.getJSONArray("products");
                for (int i = 0; i < productsArray.length(); i++) {
                    JSONObject childObj = productsArray.getJSONObject(i);
                    Product product = new Product(
                            childObj.getString("title"),
                            childObj.getJSONArray("images").getString(0), // Assuming the first image
                            childObj.getString("category"),
                            childObj.getString("description"),
                            childObj.getDouble("price"),
                            childObj.getDouble("discountPercentage"),
                            childObj.getInt("stock"),
                            childObj.getInt("id")
                    );
                    products.add(product);
                }
                productAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("Volley Error", "Error: " + error.getMessage());
        });

        queue.add(request);
    }
}
