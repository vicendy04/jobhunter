package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return this.userService.fetchUserById(id);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return this.userService.fetchAllUsers();
    }

    @PostMapping("/users")
    public User createNewUser(@RequestBody User user) {
        return this.userService.handleCreateUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        return this.userService.handleUpdateUser(user);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        this.userService.handleDeleteUser(id);
        return "Deleted";
    }
}
