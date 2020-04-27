package com.pgleska.sklepallegro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.pgleska.sklepallegro.R;
import com.pgleska.sklepallegro.connector.api.controller.ApiController;
import com.pgleska.sklepallegro.connector.api.model.ProductDTO;
import com.pgleska.sklepallegro.model.ProductViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    private Context context;
    private NavController navController;
    private ArrayList<ProductDTO> products;
    private ApiController apiController;

    private ProductViewModel productViewModel;

    public ProductsAdapter(Context ctx, NavController nc, ArrayList<ProductDTO> productDTOS) {
        context = ctx;
        navController = nc;
        products = productDTOS;

        productViewModel =
                ViewModelProviders.of((FragmentActivity) context).get(ProductViewModel.class);
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product, parent, false);

        ProductsAdapter.ProductsViewHolder nvh = new ProductsAdapter.ProductsViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {

        holder.name.setText(products.get(position).getName());

        String amount = products.get(position).getPrice().getAmount();
        String currency = products.get(position).getPrice().getCurrency();

        if(currency.equals("PLN")) {
            amount = amount.replace(".", ",");
        }

        holder.price.setText(context.getString(R.string.amount, amount, currency));

        apiController = ApiController.getInstance(Objects.requireNonNull(context).getApplicationContext());
        apiController.getImageLoader().get(products.get(position).getThumbnailUrl(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.imageView.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productViewModel.setProduct(products.get(position));
                navController.navigate(R.id.action_nav_list_of_products_to_nav_product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView name, price;
        public LinearLayout item;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.list_product_item);
            imageView = itemView.findViewById(R.id.list_product_image);
            name = itemView.findViewById(R.id.list_product_name);
            price = itemView.findViewById(R.id.list_product_amount);
        }
    }

    private void setImage(ImageLoader image) {

    }
}
