package id.kelompok14.modul5progmob.API;

public class Constant {
    public static final String URL="http://10.0.2.2:8000/";
    public static final String HOME=URL+"api/auth";
    public static final String LOGIN=HOME+"/login";
    public static final String LOGOUT=HOME+"/logout";
    public static final String REGISTER=HOME+"/register";
    public static final String PROFIL=HOME+"/user-profile";
    public static final String EDIT_USER=HOME+"/edit/user";
    public static final String DELETE_USER=HOME+"/delete/user";
    public static final String GET_PRODUCT=HOME+"/product";
    public static final String GET_CATEGORY=HOME+"/category";
    public static final String GET_CATEGORY_DETAIL=HOME+"/categorydetail";
    public static final String ADD_TRANSACTION=HOME+"/addtransaction";
    public static final String GET_TRANSACTION=HOME+"/gettransaction";
    public static final String DELETE_TRANSACTION=HOME+"/delete/transaction";
    public static final String EDIT_PAYMENT=HOME+"/edit/payment";
    public static final String GET_PAYMENT=HOME+"/getbukti";
    public static final String GET_PRODUCT_IMAGE=HOME+"/getproductimage";
    public static final String GET_ALL_PRODUCT_IMAGE=HOME+"/getallproductimage";
}
