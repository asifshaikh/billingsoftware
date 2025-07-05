package in.mohammed.billingsoftware.controller;

import in.mohammed.billingsoftware.io.UserRequest;
import in.mohammed.billingsoftware.io.UserResponse;
import in.mohammed.billingsoftware.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@RequestBody UserRequest request){
        try {
            return userService.createUser(request);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }
    }
    @GetMapping("/users")
    public List<UserResponse> readUsers() {
        try {
            return userService.readUsers();
        } catch (Exception e) {
            throw new RuntimeException("Error reading users: " + e.getMessage(), e);
        }
    }
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }
}
