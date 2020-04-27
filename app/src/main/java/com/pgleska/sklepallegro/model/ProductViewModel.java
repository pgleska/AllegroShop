package com.pgleska.sklepallegro.model;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pgleska.sklepallegro.connector.api.model.ProductDTO;

public class ProductViewModel extends ViewModel {

    private MutableLiveData<ProductDTO> product;

    public ProductViewModel() {
        product = new MutableLiveData<>();
    }

    public MutableLiveData<ProductDTO> getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product.setValue(product);
    }

}
