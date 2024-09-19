package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.PaginatedResultDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Role createRole(Role roleRequest) throws IdInvalidException {
        if (roleRepository.existsByName(roleRequest.getName())) {
            throw new IdInvalidException("Role with the same name already exists.");
        }

        List<Permission> dbPermissions = permissionRepository.findAllById(
                roleRequest.getPermissions().stream().map(Permission::getId).collect(Collectors.toList()));

//        if (dbPermissions.size() != roleRequest.getPermissions().size()) {
//            throw new IllegalArgumentException("Invalid permissions provided.");
//        }

        roleRequest.setPermissions(dbPermissions);
        return roleRepository.save(roleRequest);
    }

    public Role updateRole(Role reqRole) throws IdInvalidException {
        Role existingRole = roleRepository.findById(reqRole.getId())
                .orElseThrow(() -> new IdInvalidException("Role not found"));
        if (roleRepository.existsByName(reqRole.getName())) {
            throw new IdInvalidException("Role with the same name already exists.");
        }

        List<Permission> dbPermissions = null;
        if (reqRole.getPermissions() != null) {
            dbPermissions = permissionRepository.findAllById(
                    reqRole.getPermissions().stream().map(Permission::getId).collect(Collectors.toList()));

//        if (permissions.size() != reqRole.getPermissions().size()) {
//            throw new IllegalArgumentException("Invalid permissions provided.");
//        }

        }

        existingRole.setName(reqRole.getName());
        existingRole.setDescription(reqRole.getDescription());
        existingRole.setActive(reqRole.isActive());
        existingRole.setPermissions(dbPermissions);
        return roleRepository.save(existingRole);
    }

    public PaginatedResultDTO getRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> pRole = this.roleRepository.findAll(spec, pageable);
        PaginatedResultDTO rs = new PaginatedResultDTO();
        PaginatedResultDTO.Meta mt = new PaginatedResultDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pRole.getTotalPages());
        mt.setTotal(pRole.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pRole.getContent());
        return rs;
    }
    public void deleteRole(long id) throws IdInvalidException {
        if (!roleRepository.existsById(id)) {
            throw new IdInvalidException("Role not found");
        }
        roleRepository.deleteById(id);
    }

    public Role getRoleById(long id) throws IdInvalidException {
        return roleRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Role not found"));
    }
}
