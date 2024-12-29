package ma.ensaj.staysafe10.model;

public class LoginResponse {
    private String token;

    // Constructor, getters, setters


    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
