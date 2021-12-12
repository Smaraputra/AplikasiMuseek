package id.kelompok14.modul5progmob.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import id.kelompok14.modul5progmob.model.CategoriesDetailModel;
import id.kelompok14.modul5progmob.model.CategoriesModel;
import id.kelompok14.modul5progmob.model.ProductsModel;
import id.kelompok14.modul5progmob.model.TransactionsModel;
import id.kelompok14.modul5progmob.model.UserModel;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "museekdb";
    private static final int DB_VERSION = 1;

    private static final String TABEL_USERS = "users";
    private static final String ID_USER = "id_user";
    private static final String NAME_USER = "name_user";
    private static final String NO_USER = "number_user";
    private static final String ALAMAT_USER = "address_user";
    private static final String USERNAME_USER = "username_user";
    private static final String PASSWORD_USER = "password_user";
    private static final String KELAMIN_USER = "gender_user";
    private static final String SKILL_USER = "skill_user";
    private static final String WAKTU_USER = "time_user";
    public String tabelUsers(){
        String querys="";
        querys = querys + "CREATE TABLE " + TABEL_USERS + " ("
                + ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_USER + " TEXT, "
                + NO_USER + " TEXT,"
                + ALAMAT_USER + " TEXT,"
                + USERNAME_USER + " TEXT,"
                + PASSWORD_USER + " TEXT,"
                + KELAMIN_USER + " TEXT,"
                + SKILL_USER + " TEXT,"
                + WAKTU_USER + " INTEGER)";
        return querys;
    }

    public ArrayList<UserModel> getUserEdit (int idin) {
        String sql = " select * from " + TABEL_USERS + " where id_user = " + idin + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<UserModel> storeUser = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String namein = cursor.getString(1);
                String phonein = cursor.getString(2);
                String addressin = cursor.getString(3);
                String usernamein = cursor.getString(4);
                String passwordin = cursor.getString(5);
                String genderin = cursor.getString(6);
                String skillin = cursor.getString(7);
                int waktuin = cursor.getInt(8);
                storeUser.add(new UserModel(
                        id, namein, phonein,
                        addressin, usernamein, passwordin,
                        genderin, skillin, waktuin));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeUser;
    }

    public ArrayList<UserModel> loginUser (String username, String password) {
        String sql = " select * from " + TABEL_USERS + " where username_user = '" + username + "' and password_user = '" + password + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<UserModel> storeUser = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String namein = cursor.getString(1);
                String phonein = cursor.getString(2);
                String addressin = cursor.getString(3);
                String usernamein = cursor.getString(4);
                String passwordin = cursor.getString(5);
                String genderin = cursor.getString(6);
                String skillin = cursor.getString(7);
                int waktuin = cursor.getInt(8);
                storeUser.add(new UserModel(
                        id, namein, phonein,
                        addressin, usernamein, passwordin,
                        genderin, skillin, waktuin));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeUser;
    }

    public void addNewUser(String nama, String nomor, String alamat, String username,
                           String password, String kelamin, String skill, int waktu) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME_USER, nama);
        values.put(NO_USER, nomor);
        values.put(ALAMAT_USER, alamat);
        values.put(KELAMIN_USER, kelamin);
        values.put(USERNAME_USER, username);
        values.put(PASSWORD_USER, password);
        values.put(SKILL_USER, skill);
        values.put(WAKTU_USER, waktu);

        db.insert(TABEL_USERS, null, values);
    }

    public void deleteUser(int idin) {
        String idstr = String.valueOf(idin);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABEL_USERS, "id_user=?", new String[]{idstr});
        db.close();
    }

    public void updateUser(int idin, String nama, String nomor,
                           String alamat, String kelamin,
                           String username, String password,
                           String skill, int waktu) {
        String idstr = String.valueOf(idin);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME_USER, nama);
        values.put(NO_USER, nomor);
        values.put(ALAMAT_USER, alamat);
        values.put(KELAMIN_USER, kelamin);
        values.put(USERNAME_USER, username);
        values.put(PASSWORD_USER, password);
        values.put(SKILL_USER, skill);
        values.put(WAKTU_USER, waktu);

        db.update(TABEL_USERS, values, "id_user=?", new String[]{idstr});
        db.close();
    }

    private static final String TABEL_PRODUCT = "products";
    private static final String ID_PRODUCT = "id_product";
    private static final String NAME_PRODUCT = "name_product";
    private static final String DESC_PRODUCT = "desc_product";
    private static final String STOCK_PRODUCT = "stock_product";
    private static final String PRICE_PRODUCT = "price_product";
    private static final String RATING_PRODUCT = "rating_product";
    public String tabelProduct(){
        String querys="";
        querys = querys + "CREATE TABLE " + TABEL_PRODUCT + " ("
                + ID_PRODUCT + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_PRODUCT + " TEXT, "
                + DESC_PRODUCT + " TEXT, "
                + STOCK_PRODUCT + " INTEGER,"
                + PRICE_PRODUCT + " INTEGER,"
                + RATING_PRODUCT + " INTEGER)";
        return querys;
    }

    public void addNewProduct(String nama, String desc, int stock, int price, int rating) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME_PRODUCT, nama);
        values.put(DESC_PRODUCT, desc);
        values.put(STOCK_PRODUCT, stock);
        values.put(PRICE_PRODUCT, price);
        values.put(RATING_PRODUCT, rating);

        db.insert(TABEL_PRODUCT, null, values);
    }

    public ArrayList<ProductsModel> getProducts () {
        String sql = " select * from " + TABEL_PRODUCT + " ;";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ProductsModel> storeProduct = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String namein = cursor.getString(1);
                String desc = cursor.getString(2);
                int stock = Integer.parseInt(cursor.getString(3));
                int price = Integer.parseInt(cursor.getString(4));
                int rating = Integer.parseInt(cursor.getString(5));
                storeProduct.add(new ProductsModel(
                        id, rating, stock, price, namein, desc));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeProduct;
    }

    public ArrayList<ProductsModel> getProductsOnID (int idin) {
        String sql = " select * from " + TABEL_PRODUCT + " where " + ID_PRODUCT + " = " + idin + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ProductsModel> storeProduct = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String namein = cursor.getString(1);
                String desc = cursor.getString(2);
                int stock = Integer.parseInt(cursor.getString(3));
                int price = Integer.parseInt(cursor.getString(4));
                int rating = Integer.parseInt(cursor.getString(5));
                storeProduct.add(new ProductsModel(
                        id, rating, stock, price, namein, desc));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeProduct;
    }

    public void deleteProduct() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABEL_PRODUCT + ";");
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABEL_PRODUCT + "'");
        db.close();
    }

    private static final String TABEL_CATEGORIES = "categories";
    private static final String ID_CATEGORY = "id_category";
    private static final String NAME_CATEGORY = "name_category";
    public String tabelCategories(){
        String querys="";
        querys = querys + "CREATE TABLE " + TABEL_CATEGORIES + " ("
                + ID_CATEGORY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_CATEGORY + " TEXT)";
        return querys;
    }

    public void addNewCategory(String nama) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME_CATEGORY, nama);

        db.insert(TABEL_CATEGORIES, null, values);
    }

    public ArrayList<CategoriesModel> getCategories () {
        String sql = " select * from " + TABEL_CATEGORIES + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<CategoriesModel> storeCategories = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String namein = cursor.getString(1);
                storeCategories.add(new CategoriesModel(id, namein));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeCategories;
    }

    public ArrayList<CategoriesModel> getCategoriesOnID (int idin) {
        String sql = " select * from " + TABEL_CATEGORIES + " where " + ID_CATEGORY + " = " + idin + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<CategoriesModel> storeCategories = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String namein = cursor.getString(1);
                storeCategories.add(new CategoriesModel(id, namein));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeCategories;
    }

    public void deleteCategories() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABEL_CATEGORIES + ";");
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABEL_CATEGORIES + "'");
        db.close();
    }

    private static final String TABEL_CATEGORIES_DETAIL = "categories_detail";
    private static final String ID_CATEGORY_DETAIL = "id_category_detail";
    private static final String ID_CATEGORY_FK = "id_category";
    private static final String ID_PRODUCT_FK = "id_product";
    public String tabelCategoriesDetail(){
        String querys="";
        querys = querys + "CREATE TABLE " + TABEL_CATEGORIES_DETAIL + " ("
                + ID_CATEGORY_DETAIL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ID_CATEGORY_FK + " INTEGER, "
                + ID_PRODUCT_FK + " INTEGER)";
        return querys;
    }

    public void addNewCategoryDetail(int idcat, int idprod) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ID_CATEGORY_FK, idcat);
        values.put(ID_PRODUCT_FK, idprod);

        db.insert(TABEL_CATEGORIES_DETAIL, null, values);
    }

    public ArrayList<CategoriesDetailModel> getIDProductOnCategory (int idin) {
        String sql = " select * from " + TABEL_CATEGORIES_DETAIL + " where " + ID_CATEGORY_FK + " = " + idin + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<CategoriesDetailModel> storeCategoriesDetail = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                int idcat = Integer.parseInt(cursor.getString(1));
                int idprod = Integer.parseInt(cursor.getString(2));
                storeCategoriesDetail.add(new CategoriesDetailModel(id, idcat, idprod));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeCategoriesDetail;
    }

    public ArrayList<CategoriesDetailModel> getProdCategoryOnID (int idin) {
        String sql = " select * from " + TABEL_CATEGORIES_DETAIL + " where " + ID_PRODUCT_FK + " = " + idin + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<CategoriesDetailModel> storeCategoriesDetail = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                int idcat = Integer.parseInt(cursor.getString(1));
                int idprod = Integer.parseInt(cursor.getString(2));
                storeCategoriesDetail.add(new CategoriesDetailModel(id, idcat, idprod));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeCategoriesDetail;
    }

    public void deleteCategoriesDetail() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABEL_CATEGORIES_DETAIL + ";");
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABEL_CATEGORIES_DETAIL + "'");
        db.close();
    }

    private static final String TABEL_TRANSACTIONS = "transactions";
    private static final String ID_TRANSACTION = "id_transaction";
    private static final String ID_USER_TRANSACTION = "id_user_transaction";
    private static final String ID_PRODUCT_TRANSACTION = "id_product_transaction";
    private static final String START_TRANSACTION = "start_transaction";
    private static final String END_TRANSACTION = "end_transaction";
    private static final String TOTAL_PRODUCT = "product_total_transaction";
    private static final String TOTAL_TRANSACTION = "total_transaction";
    private static final String STATUS_TRANSACTION = "status_transaction";
    private static final String RATING_TRANSACTION = "rating_transaction";
    private static final String STATUS_RATING_TRANSACTION = "status_rating_transaction";
    private static final String DATE_TRANSACTION = "date_transaction";
    private static final String STATUS_PAYMENT = "status_payment";
    private static final String DEADLINE_PAYMENT = "deadline_payment";
    private static final String PROOF = "proof_payment";
    public String tabelTransations(){
        String querys="";
        querys = querys + "CREATE TABLE " + TABEL_TRANSACTIONS + " ("
                + ID_TRANSACTION + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ID_USER_TRANSACTION + " INTEGER, "
                + ID_PRODUCT_TRANSACTION + " INTEGER, "
                + START_TRANSACTION + " TEXT, "
                + END_TRANSACTION + " TEXT, "
                + TOTAL_PRODUCT + " INTEGER, "
                + TOTAL_TRANSACTION + " INTEGER, "
                + STATUS_TRANSACTION + " TEXT, "
                + RATING_TRANSACTION + " INTEGER, "
                + STATUS_RATING_TRANSACTION + " TEXT, "
                + DATE_TRANSACTION + " TEXT, "
                + STATUS_PAYMENT + " TEXT, "
                + DEADLINE_PAYMENT + " TEXT, "
                + PROOF + " TEXT)";
        return querys;
    }

    public void addNewTransaction(int id, int iduser, int idprod, String start, String end,
                                  int totalprod, int totaltrans, String status,
                                  int rating, String statusrating, String date,
                                  String datepay, String deadline, String proof) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ID_TRANSACTION, id);
        values.put(ID_USER_TRANSACTION, iduser);
        values.put(ID_PRODUCT_TRANSACTION, idprod);
        values.put(START_TRANSACTION, start);
        values.put(END_TRANSACTION, end);
        values.put(TOTAL_PRODUCT, totalprod);
        values.put(TOTAL_TRANSACTION, totaltrans);
        values.put(STATUS_TRANSACTION, status);
        values.put(RATING_TRANSACTION, rating);
        values.put(STATUS_RATING_TRANSACTION, statusrating);
        values.put(DATE_TRANSACTION, date);
        values.put(STATUS_PAYMENT, datepay);
        values.put(DEADLINE_PAYMENT, deadline);
        values.put(PROOF, proof);

        db.insert(TABEL_TRANSACTIONS, null, values);
    }

    public ArrayList<TransactionsModel> getTransactionOnIDUSER (int idin) {
        String sql = " select * from " + TABEL_TRANSACTIONS + " where " + ID_USER_TRANSACTION + " = " + idin + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<TransactionsModel> storeTransaction = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                int iduser = Integer.parseInt(cursor.getString(1));
                int idprod = Integer.parseInt(cursor.getString(2));
                String start = (cursor.getString(3));
                String end = (cursor.getString(4));
                int total = Integer.parseInt(cursor.getString(5));
                int totaltrans = Integer.parseInt(cursor.getString(6));
                String status = (cursor.getString(7));
                int rating = Integer.parseInt(cursor.getString(8));
                String statusrating = (cursor.getString(9));
                String date = (cursor.getString(10));
                String datepay = (cursor.getString(11));
                String deadline = (cursor.getString(12));
                String proof = (cursor.getString(13));
                storeTransaction.add(new TransactionsModel(
                        id, iduser, idprod, totaltrans, total, rating,
                        start, end, date, status, statusrating, datepay, deadline, proof));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeTransaction;
    }

    public ArrayList<TransactionsModel> getTransactionOnIDTRANS (int idin) {
        String sql = " select * from " + TABEL_TRANSACTIONS + " where " + ID_TRANSACTION + " = " + idin + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<TransactionsModel> storeTransaction = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                int iduser = Integer.parseInt(cursor.getString(1));
                int idprod = Integer.parseInt(cursor.getString(2));
                String start = (cursor.getString(3));
                String end = (cursor.getString(4));
                int total = Integer.parseInt(cursor.getString(5));
                int totaltrans = Integer.parseInt(cursor.getString(6));
                String status = (cursor.getString(7));
                int rating = Integer.parseInt(cursor.getString(8));
                String statusrating = (cursor.getString(9));
                String date = (cursor.getString(10));
                String datepay = (cursor.getString(11));
                String deadline = (cursor.getString(12));
                String proof = (cursor.getString(13));
                storeTransaction.add(new TransactionsModel(
                        id, iduser, idprod, totaltrans, total, rating,
                        start, end, date, status, statusrating, datepay, deadline, proof));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeTransaction;
    }

    public void deleteTransaction() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABEL_TRANSACTIONS + ";");
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABEL_TRANSACTIONS + "'");
        db.close();
    }

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = tabelUsers();
        db.execSQL(query1);
        String query2 = tabelProduct();
        db.execSQL(query2);
        String query3 = tabelCategories();
        db.execSQL(query3);
        String query4 = tabelCategoriesDetail();
        db.execSQL(query4);
        String query5 = tabelTransations();
        db.execSQL(query5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABEL_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABEL_CATEGORIES);
            db.execSQL("DROP TABLE IF EXISTS " + TABEL_CATEGORIES_DETAIL);
            db.execSQL("DROP TABLE IF EXISTS " + TABEL_PRODUCT);
            db.execSQL("DROP TABLE IF EXISTS " + TABEL_TRANSACTIONS);
            onCreate(db);
        }
    }
}