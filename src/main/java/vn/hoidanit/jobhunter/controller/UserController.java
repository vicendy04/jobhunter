package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        User foundUser = this.userService.fetchUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(foundUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = this.userService.fetchAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User reqUser) {
        User createdUser = this.userService.handleCreateUser(reqUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User reqUser) {
        User updatedUser = this.userService.handleUpdateUser(reqUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) throws IdInvalidException {
        if (id > 1500) {
            throw new IdInvalidException("Id khong lon hon 1500");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully");
    }
}
