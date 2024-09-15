package vn.hoidanit.jobhunter.domain.response.user;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

import java.time.Instant;

@Getter
@Setter
public class ResCreateUserDTO {
    private Long id;
    private String email;
    private String name;
    private GenderEnum gender;
    private String address;
    private Integer age;
    private Instant createdAt;
    private CompanyUser company;

    @Getter
    @Setter
    public static class CompanyUser {
        private long id;
        private String name;
    }
}