package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.*;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);

    }

    public UserUpdateResponse handleUpdateUser(User requestUser) throws IdInvalidException {
        User existingUser = this.fetchUserById(requestUser.getId()).orElseThrow(() -> new IdInvalidException("User với id " + requestUser.getId() + " không tồn tại"));
        // update
        existingUser.setName(requestUser.getName());
        existingUser.setGender(requestUser.getGender());
        existingUser.setAddress(requestUser.getAddress());
        existingUser.setAge(requestUser.getAge());
        existingUser = this.userRepository.save(existingUser);

        return this.convertToUserUpdateResponse(existingUser);
    }

    public UserCreateResponse handleCreateUser(User reqUser) throws IdInvalidException {
        if (this.userRepository.existsByEmail(reqUser.getEmail())) {
            throw new IdInvalidException("Email đã tồn tai");
        }
        reqUser.setPassword(passwordEncoder.encode(reqUser.getPassword()));
        User user = this.userRepository.save(reqUser);
        return this.convertToUserCreateResponse(user);
    }

    public UserCreateResponse convertToUserCreateResponse(User user) {
        UserCreateResponse dto = new UserCreateResponse();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setAge(user.getAge());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setGender(user.getGender());
        dto.setAddress(user.getAddress());
        return dto;
    }

    public UserUpdateResponse convertToUserUpdateResponse(User user) {
        UserUpdateResponse dto = new UserUpdateResponse();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setAge(user.getAge());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setGender(user.getGender());
        dto.setAddress(user.getAddress());
        return dto;
    }

    public UserResponse convertToUserResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setAge(user.getAge());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setGender(user.getGender());
        dto.setAddress(user.getAddress());
        return dto;
    }


    public UserResponse handleGetUserById(Long id) throws IdInvalidException {
//        Optional<User> userOptional = this.fetchUserById(id);
//        if (userOptional.isEmpty()) {
//            throw new IdInvalidException("User với id = " + id + " không tồn tai");
//        } else {
//            return this.convertToResCreateUserDTO(userOptional.get());
//        }
        User user = fetchUserById(id).orElseThrow(() -> new IdInvalidException("User with id " + id + " does not exist"));
        return convertToUserResponse(user);
    }

    public void handleDeleteUser(Long id) throws IdInvalidException {
        if (this.userRepository.existsById(id)) {
            this.userRepository.deleteById(id);
        } else {
            throw new IdInvalidException("User với id = " + id + " không tồn tại");
        }
    }

    public PaginatedResultDTO handleGetAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> userPage = this.userRepository.findAll(spec, pageable);

        Meta meta = new Meta();
        meta.setPage(userPage.getNumber() + 1);
        meta.setPageSize(userPage.getSize());
        meta.setPages(userPage.getTotalPages());
        meta.setTotal(userPage.getTotalElements());

        List<UserResponse> userResponseDTOS = userPage.getContent()
                .stream().map(this::convertToUserResponse
                ).toList();

        PaginatedResultDTO paginatedResultDTO = new PaginatedResultDTO();
        paginatedResultDTO.setMeta(meta);
        paginatedResultDTO.setResult(userResponseDTOS);

        return paginatedResultDTO;
    }

//    public User fetchUserById(Long id) {
//        Optional<User> userOptional = this.userRepository.findById(id);
//        return userOptional.orElse(null);
//    }

    private Optional<User> fetchUserById(Long id) {
        return this.userRepository.findById(id);
    }
}
