package in.mohammed.billingsoftware.service;

import in.mohammed.billingsoftware.io.UserRequest;
import in.mohammed.billingsoftware.io.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest request);
    String getUserRole(String email);
    List<UserResponse> readUsers();
    void deleteUser(String id);
}
