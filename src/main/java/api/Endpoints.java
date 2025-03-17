package api;

public class Endpoints {
    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    public static final String LOGIN_URL = BASE_URL + "/login";
    public static final String REGISTER_URL = BASE_URL + "/register";
    public static final String FORGOT_PASSWORD_URL = BASE_URL + "/forgot-password";
    public static final String ACCOUNT_URL = BASE_URL + "/account";
    public static final String CONSTRUCTOR_URL = BASE_URL + "/";
    public static final String API_USER_CREATE = BASE_URL + "/api/auth/register";
    public static final String API_USER_DELETE = BASE_URL + "/api/auth/user";
    public static final String API_USER_LOGIN = BASE_URL + "/api/auth/login";
    public static void deleteUser(String email) {
        System.out.println("Удалён пользователь: " + email);
    }

    public static void createUser(String email, String password) {
        System.out.println("Создан пользователь: " + email);
    }
}
