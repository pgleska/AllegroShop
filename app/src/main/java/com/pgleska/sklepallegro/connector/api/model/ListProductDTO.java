package com.pgleska.sklepallegro.connector.api.model;

import java.util.ArrayList;

public class ListProductDTO extends ApiDTO {

    private ArrayList<ProductDTO> offers;

    public ArrayList<ProductDTO> getOffers() {
        return offers;
    }

    public void setOffers(ArrayList<ProductDTO> offers) {
        this.offers = offers;
    }
}
