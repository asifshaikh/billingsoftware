package in.mohammed.billingsoftware.io;

import lombok.*;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String email;
    private String token;
    private String role;


}
