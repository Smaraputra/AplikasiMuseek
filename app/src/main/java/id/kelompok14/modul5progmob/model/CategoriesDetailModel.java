package id.kelompok14.modul5progmob.model;

public class CategoriesDetailModel {
    private int id_category_detail, id_category, id_product;

        public CategoriesDetailModel(int id_category_detail, int id_category, int id_product) {
            this.id_category_detail = id_category_detail;
            this.id_category = id_category;
            this.id_product = id_product;
        }

    public int getId_category_detail() {
        return id_category_detail;
    }

    public void setId_category_detail(int id_category_detail) {
        this.id_category_detail = id_category_detail;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }
}
