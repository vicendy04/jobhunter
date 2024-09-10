package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.PaginatedResultDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;

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

    public User handleUpdateUser(User requestUser) {
        User user = this.fetchUserById(requestUser.getId());
        if (user != null) {
            user.setName(requestUser.getName());
            user.setEmail(requestUser.getEmail());
            user.setPassword(requestUser.getPassword());
            user = this.userRepository.save(user);
        }
        return user;
    }

    public User handleCreateUser(User reqUser) {
        reqUser.setPassword(passwordEncoder.encode(reqUser.getPassword()));
        return this.userRepository.save(reqUser);
    }

    public User fetchUserById(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        return userOptional.orElse(null);
    }

    public void handleDeleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public PaginatedResultDTO fetchAllUsers(Pageable pageable) {
        Page<User> userPage = this.userRepository.findAll(pageable);

        Meta meta = new Meta();
        meta.setPage(userPage.getTotalPages());
        meta.setPageSize(userPage.getSize());
        meta.setPages(userPage.getTotalPages());
        meta.setTotal(userPage.getTotalElements());

        PaginatedResultDTO paginatedResultDTO = new PaginatedResultDTO();
        paginatedResultDTO.setMeta(meta);
        paginatedResultDTO.setResult(userPage.getContent());

        return paginatedResultDTO;
    }
}
