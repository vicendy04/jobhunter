package vn.hoidanit.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.SecurityUtil;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "companies")
@Getter
@Setter
// không thích generate toString constructor
//@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "name không được bỏ trống")
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String address;
    private String logo;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    @JsonIgnore
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    List<User> users;

    @JsonIgnore
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    List<Job> jobs;

    @PrePersist
    public void handleBeforeCreate() {
//        this.setCreatedBy("hoangtran");
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
