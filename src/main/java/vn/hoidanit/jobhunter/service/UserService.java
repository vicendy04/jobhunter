package vn.hoidanit.jobhunter.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

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
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public void handleDeleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public List<User> fetchAllUsers() {
        return this.userRepository.findAll();
    }
}
