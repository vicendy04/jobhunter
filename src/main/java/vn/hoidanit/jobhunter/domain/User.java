package vn.hoidanit.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @NotBlank(message = "email không được bỏ trống")
    private String email;
    @NotBlank(message = "mật khẩu không được bỏ trống")
    private String password;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private String address;
    @Column(columnDefinition = "TEXT")
    private String refreshToken;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Resume> resumes;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @PrePersist
    public void handleBeforeCreate() {
        Optional<String> currentUserLogin = SecurityUtil.getCurrentUserLogin();
        String emailCurrentUser = currentUserLogin.orElse("");
        this.setCreatedBy(emailCurrentUser);
        this.setCreatedAt(Instant.now());
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        Optional<String> currentUserLogin = SecurityUtil.getCurrentUserLogin();
        String emailCurrentUser = currentUserLogin.orElse("");
        this.setUpdatedBy(emailCurrentUser);
        this.setUpdatedAt(Instant.now());
    }
}
