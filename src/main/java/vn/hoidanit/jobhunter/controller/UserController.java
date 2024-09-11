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
import vn.hoidanit.jobhunter.domain.dto.UserCreateResponse;
import vn.hoidanit.jobhunter.domain.dto.UserResponse;
import vn.hoidanit.jobhunter.domain.dto.UserUpdateResponse;
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
    @ApiMessage(value = "fetch user by id")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") Long id) throws IdInvalidException {
        UserResponse user = this.userService.handleGetUser(id);

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
    @ApiMessage(value = "fetch all users")
    public ResponseEntity<PaginatedResultDTO> getUsers(
            @Filter Specification<User> spec,
            Pageable pageable
//            @RequestParam(value = "current", defaultValue = "1") int current,
//            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize
    ) {
//        Pageable pageable = PaginationHandler.getPageableObject(current, pageSize);

//        var users = this.userService.fetchAllUsers(pageable);
        var users = this.userService.fetchAllUsers(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping("/users")
    @ApiMessage(value = "Create a new user")
    public ResponseEntity<UserCreateResponse> createNewUser(@Valid @RequestBody User reqUser) throws IdInvalidException {
        UserCreateResponse createdUser = this.userService.handleCreateUser(reqUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/users")
    @ApiMessage(value = "update a user")
    public ResponseEntity<UserUpdateResponse> updateUser(@RequestBody User reqUser) throws IdInvalidException {
        UserUpdateResponse updatedUser = this.userService.handleUpdateUser(reqUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage(value = "Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) throws IdInvalidException {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
