package com.pgleska.sklepallegro.connector.api.model;

public class ProductDTO extends ApiDTO implements Comparable<ProductDTO> {

    private String id;
    private String name;
    private String thumbnailUrl;
    private PriceDTO price;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public PriceDTO getPrice() {
        return price;
    }

    public void setPrice(PriceDTO price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(ProductDTO productDTO) {
        float thisPrice, thatPrice;
        thisPrice = Float.parseFloat(this.getPrice().getAmount());
        thatPrice = Float.parseFloat(productDTO.getPrice().getAmount());

        return Float.compare(thisPrice, thatPrice);
    }
}
