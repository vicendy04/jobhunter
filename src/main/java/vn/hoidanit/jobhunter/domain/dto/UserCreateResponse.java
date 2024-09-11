package vn.hoidanit.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

import java.time.Instant;

@Getter
@Setter
public class UserCreateResponse {
    private Long id;
    private String email;
    private String name;
    private GenderEnum gender;
    private String address;
    private Integer age;
    private Instant createdAt;
}