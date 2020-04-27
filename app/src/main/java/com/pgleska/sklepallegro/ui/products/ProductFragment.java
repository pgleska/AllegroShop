package com.pgleska.sklepallegro.ui.products;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.pgleska.sklepallegro.R;
import com.pgleska.sklepallegro.connector.api.controller.ApiController;
import com.pgleska.sklepallegro.connector.api.model.ProductDTO;
import com.pgleska.sklepallegro.model.ProductViewModel;

import java.util.Objects;

public class ProductFragment extends Fragment {

    private View root;

    private ProductViewModel productViewModel;
    private ProductDTO productDTO;
    private ApiController apiController;

    private ImageView imageView;
    private TextView nameTextView, descriptionTextView, priceTextView;
    private ImageButton backButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_product, container, false);

        productViewModel =
                ViewModelProviders.of(getActivity()).get(ProductViewModel.class);

        productDTO = productViewModel.getProduct().getValue();

        initComponents();

        apiController = ApiController.getInstance(Objects.requireNonNull(getActivity()).getApplicationContext());
        apiController.getImageLoader().get(productDTO.getThumbnailUrl(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                imageView.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        return root;
    }

    private void initComponents() {
        imageView = root.findViewById(R.id.product_image);
        nameTextView = root.findViewById(R.id.product_name);
        priceTextView = root.findViewById(R.id.product_price);
        descriptionTextView = root.findViewById(R.id.product_description);

        backButton = root.findViewById(R.id.product_back_to_menu);

        backButton.setOnClickListener(view -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                .navigate(R.id.action_nav_product_to_nav_list_of_products));

        nameTextView.setText(productDTO.getName());

        String amount = productDTO.getPrice().getAmount();
        String currency = productDTO.getPrice().getCurrency();

        if(currency.equals("PLN")) {
            amount = amount.replace(".", ",");
        }
        priceTextView.setText(getResources().getString(R.string.product_price, amount, currency));

        descriptionTextView.setText(Html.fromHtml(productDTO.getDescription()).toString());
    }
}
