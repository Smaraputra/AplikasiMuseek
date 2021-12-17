package id.kelompok14.modul5progmob.model;

public class ProductImagesModel {
    private int id_product;
    private String gambar;

    public ProductImagesModel(int id_product, String gambar) {
        this.id_product = id_product;
        this.gambar = gambar;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
