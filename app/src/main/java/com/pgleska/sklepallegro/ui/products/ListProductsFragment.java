package com.pgleska.sklepallegro.ui.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.pgleska.sklepallegro.R;
import com.pgleska.sklepallegro.adapter.ProductsAdapter;
import com.pgleska.sklepallegro.connector.api.controller.ApiController;
import com.pgleska.sklepallegro.connector.api.controller.ApiRequest;
import com.pgleska.sklepallegro.connector.api.model.ListProductDTO;
import com.pgleska.sklepallegro.connector.api.model.ProductDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class ListProductsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = ListProductsFragment.class.getName();
    private View root;

    private String url;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ApiController apiController;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_list_products, container, false);

        url = getString(R.string.allegro_url);

        recyclerView = root.findViewById(R.id.list_product_recycler_view);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());

        apiController = ApiController.getInstance(Objects.requireNonNull(getActivity()).getApplicationContext());

        sendRequest();

        mSwipeRefreshLayout = root.findViewById(R.id.list_product_swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        return root;
    }

    private void setMappings(ListProductDTO list) {

        float price;

        recyclerView.setLayoutManager(layoutManager);

        mSwipeRefreshLayout.setRefreshing(false);

        ArrayList<ProductDTO> products = new ArrayList<>();

        for(ProductDTO p : list.getOffers()) {
            price = Float.parseFloat(p.getPrice().getAmount());
            if (price >= 50 && price <= 1000) {
                products.add(p);
            }
        }

        Collections.sort(products);

        adapter = new ProductsAdapter(getActivity(), Navigation.findNavController(root), products);
        recyclerView.setAdapter(adapter);
    }

    private void sendRequest() {
        apiController.addToRequestQueue(new ApiRequest<>(
                Request.Method.GET, url, null, ListProductDTO.class,
                this::setMappings,
                error -> onError()));
    }

    @Override
    public void onRefresh() {
        sendRequest();
    }

    private void onError() {
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getContext(), "Check your internet connection.", Toast.LENGTH_LONG).show();
    }
}
