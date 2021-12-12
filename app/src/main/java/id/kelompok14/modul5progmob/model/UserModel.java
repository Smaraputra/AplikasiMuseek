package id.kelompok14.modul5progmob.model;

public class UserModel {
    private String name, phone, address, username, password, gender, skill;
    private int id_user, waktu;

    public UserModel(int id, String namein, String phonein,
                     String addressin, String usernamein, String passwordin,
                     String genderin, String skillin, int waktuin) {
        this.id_user = id;
        this.name = namein;
        this.phone = phonein;
        this.address = addressin;
        this.username = usernamein;
        this.password = passwordin;
        this.gender = genderin;
        this.skill = skillin;
        this.waktu = waktuin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getWaktu() {
        return waktu;
    }

    public void setWaktu(int waktu) {
        this.waktu = waktu;
    }
}
