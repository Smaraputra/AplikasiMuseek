package id.kelompok14.modul5progmob.model;

public class ProductsModel {
    private String name_product, desc_product;
    private int id_product, rating_product, stock_product, price_product;

    public ProductsModel(int id_product, int rating_product, int stock_product, int price_product, String name_product, String desc_product) {
        this.id_product = id_product;
        this.rating_product = rating_product;
        this.name_product = name_product;
        this.stock_product = stock_product;
        this.price_product = price_product;
        this.desc_product = desc_product;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public String getDesc_product() {
        return desc_product;
    }

    public void setDesc_product(String desc_product) {
        this.desc_product = desc_product;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public int getRating_product() {
        return rating_product;
    }

    public void setRating_product(int rating_product) {
        this.rating_product = rating_product;
    }

    public int getStock_product() {
        return stock_product;
    }

    public void setStock_product(int stock_product) {
        this.stock_product = stock_product;
    }

    public int getPrice_product() {
        return price_product;
    }

    public void setPrice_product(int price_product) {
        this.price_product = price_product;
    }
}
