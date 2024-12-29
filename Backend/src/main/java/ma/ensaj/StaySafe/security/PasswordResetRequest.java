package ma.ensaj.StaySafe.security;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
