package in.mohammed.billingsoftware.controller;

import in.mohammed.billingsoftware.io.AuthRequest;
import in.mohammed.billingsoftware.io.AuthResponse;
import in.mohammed.billingsoftware.service.UserService;
import in.mohammed.billingsoftware.service.impl.AppUserDetailsService;
import in.mohammed.billingsoftware.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final AppUserDetailsService appUserDetailsService;

    private final JwtUtil jwtUtil;

    private final UserService userService;

    @PostMapping("/login")
    private AuthResponse login(@RequestBody AuthRequest request) throws Exception {
        authenticate(request.getEmail(), request.getPassword());
        final UserDetails userDetails = appUserDetailsService.loadUserByUsername(request.getEmail());
        final String jwtToken = jwtUtil.generateToken(userDetails);
        String userRole = userService.getUserRole(request.getEmail());
        return new AuthResponse(request.getEmail(), jwtToken , userRole);
    }

    private void authenticate(String email, String password) throws Exception {
        try{
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (DisabledException e) {
            throw new Exception("User is disabled", e);
        } catch (BadCredentialsException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials", e);
        }
    }

    @PostMapping("/encode")
    public String encodePassword(@RequestBody Map<String,String> request){
        return passwordEncoder.encode(request.get("password"));
    }

}
