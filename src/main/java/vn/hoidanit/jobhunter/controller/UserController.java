package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.PaginatedResultDTO;
import vn.hoidanit.jobhunter.domain.dto.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUserDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users/{id}")
    @ApiMessage(value = "Fetch user by ID")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") Long id) throws IdInvalidException {
        ResUserDTO user = this.userService.handleGetUserById(id);

//        if (userOptional.isPresent()) {
//            return ResponseEntity.ok(userOptional.get());
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//
//        map ánh xạ optional hiện tại thành optional bọc giá trị đầu ra của hàm truyền vào
//        Optional<User> thành Optional<ResponseEntity<User>>
//        return userOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    @ApiMessage(value = "Fetch all users with pagination")
    public ResponseEntity<PaginatedResultDTO> getAllUsers(@Filter Specification<User> spec, Pageable pageable) {
//        Pageable pageable = PaginationHandler.getPageableObject(current, pageSize);
//        var users = this.userService.fetchAllUsers(pageable);
        PaginatedResultDTO users = this.userService.handleGetAllUsers(spec, pageable);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    @ApiMessage(value = "Create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User reqUser) throws IdInvalidException {
        ResCreateUserDTO createdUser = this.userService.handleCreateUser(reqUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/users")
    @ApiMessage(value = "Update an existing user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User reqUser) throws IdInvalidException {
        ResUpdateUserDTO updatedUser = this.userService.handleUpdateUser(reqUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage(value = "Delete a user by ID")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) throws IdInvalidException {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
