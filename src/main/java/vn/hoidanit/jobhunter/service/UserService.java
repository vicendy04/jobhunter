package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.PaginatedResultDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResUserDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);

    }

    public ResUpdateUserDTO handleUpdateUser(User reqUser) throws IdInvalidException {
        User existingUser = this.fetchUserById(reqUser.getId()).orElseThrow(() -> new IdInvalidException("User với id " + reqUser.getId() + " không tồn tại"));

        // update
        if (reqUser.getCompany() != null) {
            Company company = this.companyRepository.findById(reqUser.getCompany().getId()).orElseThrow(() -> new IdInvalidException("Company id không tồn tại"));
            reqUser.setCompany(company);
        }
        if (reqUser.getRole() != null) {
            Role role = this.roleRepository.findById(reqUser.getRole().getId()).orElseThrow(() -> new IdInvalidException("Role id không tồn tại"));
            reqUser.setRole(role);
        }
        existingUser.setName(reqUser.getName());
        existingUser.setGender(reqUser.getGender());
        existingUser.setAddress(reqUser.getAddress());
        existingUser.setAge(reqUser.getAge());
        existingUser.setCompany(reqUser.getCompany());
        existingUser.setRole(reqUser.getRole());
        existingUser = this.userRepository.save(existingUser);

        return this.convertToUserUpdateResponse(existingUser);
    }

    public ResCreateUserDTO handleCreateUser(User reqUser) throws IdInvalidException {
        if (this.userRepository.existsByEmail(reqUser.getEmail())) {
            throw new IdInvalidException("Email đã tồn tại");
        }
        if (reqUser.getCompany() != null) {
            Company company = this.companyRepository.findById(reqUser.getCompany().getId()).orElseThrow(() -> new IdInvalidException("Company id không tồn tại"));
            reqUser.setCompany(company);
        }
        if (reqUser.getRole() != null) {
            Role role = this.roleRepository.findById(reqUser.getRole().getId()).orElseThrow(() -> new IdInvalidException("Role id không tồn tại"));
            reqUser.setRole(role);
        }
        reqUser.setPassword(passwordEncoder.encode(reqUser.getPassword()));
        User user = this.userRepository.save(reqUser);
        return this.convertToUserCreateResponse(user);
    }

    public ResCreateUserDTO convertToUserCreateResponse(User user) {
        ResCreateUserDTO dto = new ResCreateUserDTO();
        ResCreateUserDTO.CompanyUser companyUser = new ResCreateUserDTO.CompanyUser();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setAge(user.getAge());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setGender(user.getGender());
        dto.setAddress(user.getAddress());
        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            dto.setCompany(companyUser);
        }
        return dto;
    }

    public ResUpdateUserDTO convertToUserUpdateResponse(User user) {
        ResUpdateUserDTO dto = new ResUpdateUserDTO();
        ResUpdateUserDTO.CompanyUser companyUser = new ResUpdateUserDTO.CompanyUser();
        ResUpdateUserDTO.RoleUser roleUser = new ResUpdateUserDTO.RoleUser();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setAge(user.getAge());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setGender(user.getGender());
        dto.setAddress(user.getAddress());
        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            dto.setCompany(companyUser);
        }
        if (user.getRole() != null) {
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            dto.setRole(roleUser);
        }
        return dto;
    }

    public ResUserDTO convertToUserResponse(User user) {
        ResUserDTO dto = new ResUserDTO();
        ResUserDTO.CompanyUser companyUser = new ResUserDTO.CompanyUser();
        ResUserDTO.RoleUser roleUser = new ResUserDTO.RoleUser();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setAge(user.getAge());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setGender(user.getGender());
        dto.setAddress(user.getAddress());
        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            dto.setCompany(companyUser);
        }
        if (user.getRole() != null) {
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            dto.setRole(roleUser);
        }
        return dto;
    }


    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

    public ResUserDTO handleGetUserById(Long id) throws IdInvalidException {
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

        PaginatedResultDTO.Meta meta = new PaginatedResultDTO.Meta();
        meta.setPage(userPage.getNumber() + 1);
        meta.setPageSize(userPage.getSize());
        meta.setPages(userPage.getTotalPages());
        meta.setTotal(userPage.getTotalElements());

        List<ResUserDTO> resUserDTOS = userPage.getContent().stream().map(this::convertToUserResponse).toList();

        PaginatedResultDTO paginatedResultDTO = new PaginatedResultDTO();
        paginatedResultDTO.setMeta(meta);
        paginatedResultDTO.setResult(resUserDTOS);

        return paginatedResultDTO;
    }


    private Optional<User> fetchUserById(Long id) {
        return this.userRepository.findById(id);
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }
}
