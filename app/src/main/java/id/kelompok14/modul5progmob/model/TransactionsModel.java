package id.kelompok14.modul5progmob.model;

public class TransactionsModel {
    private String start_transaction, end_transaction, date_transaction
            , status_transaction,status_rating_transaction, deadline_payment, status_payment, proof;
    private int id_transaction, id_user_transaction,  id_product_transaction, total_transaction, total_product, rating;

    public TransactionsModel(int id_transaction, int id_user_transaction, int id_product_transaction
            , int total_transaction, int total_product, int rating
            , String start_transaction, String end_transaction
            , String date_transaction, String status_transaction, String status_rating_transaction
            , String deadline_payment, String status_payment, String proof) {
        this.id_transaction = id_transaction;
        this.id_user_transaction = id_user_transaction;
        this.id_product_transaction = id_product_transaction;
        this.rating = rating;
        this.start_transaction = start_transaction;
        this.end_transaction = end_transaction;
        this.total_transaction = total_transaction;
        this.date_transaction = date_transaction;
        this.status_transaction = status_transaction;
        this.status_rating_transaction = status_rating_transaction;
        this.total_product = total_product;
        this.deadline_payment = deadline_payment;
        this.status_payment = status_payment;
        this.proof = proof;
    }

    public String getStart_transaction() {
        return start_transaction;
    }

    public void setStart_transaction(String start_transaction) {
        this.start_transaction = start_transaction;
    }

    public String getEnd_transaction() {
        return end_transaction;
    }

    public void setEnd_transaction(String end_transaction) {
        this.end_transaction = end_transaction;
    }

    public String getDate_transaction() {
        return date_transaction;
    }

    public void setDate_transaction(String date_transaction) {
        this.date_transaction = date_transaction;
    }

    public String getStatus_transaction() {
        return status_transaction;
    }

    public void setStatus_transaction(String status_transaction) {
        this.status_transaction = status_transaction;
    }

    public String getStatus_rating_transaction() {
        return status_rating_transaction;
    }

    public void setStatus_rating_transaction(String status_rating_transaction) {
        this.status_rating_transaction = status_rating_transaction;
    }

    public String getDeadline_payment() {
        return deadline_payment;
    }

    public void setDeadline_payment(String deadline_payment) {
        this.deadline_payment = deadline_payment;
    }

    public String getStatus_payment() {
        return status_payment;
    }

    public void setStatus_payment(String status_payment) {
        this.status_payment = status_payment;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public int getId_transaction() {
        return id_transaction;
    }

    public void setId_transaction(int id_transaction) {
        this.id_transaction = id_transaction;
    }

    public int getId_user_transaction() {
        return id_user_transaction;
    }

    public void setId_user_transaction(int id_user_transaction) {
        this.id_user_transaction = id_user_transaction;
    }

    public int getId_product_transaction() {
        return id_product_transaction;
    }

    public void setId_product_transaction(int id_product_transaction) {
        this.id_product_transaction = id_product_transaction;
    }

    public int getTotal_transaction() {
        return total_transaction;
    }

    public void setTotal_transaction(int total_transaction) {
        this.total_transaction = total_transaction;
    }

    public int getTotal_product() {
        return total_product;
    }

    public void setTotal_product(int total_product) {
        this.total_product = total_product;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
