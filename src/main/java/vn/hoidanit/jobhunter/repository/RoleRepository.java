package vn.hoidanit.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.hoidanit.jobhunter.domain.Role;

public interface RoleRepository extends JpaSpecificationExecutor<Role>, JpaRepository<Role, Long> {
    boolean existsByName(String name);
}
