package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.PaginatedResultDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    private boolean isPermissionExist(Permission permission) {
        // Kiểm tra điều kiện apiPath, method, module không trùng
        return this.permissionRepository.existsByApiPathAndMethodAndModule(permission.getApiPath(), permission.getMethod(), permission.getModule());
    }

    private boolean isSameName(Permission p) throws IdInvalidException {
        Permission existingPermission = permissionRepository.findById(p.getId()).orElseThrow(() -> new IdInvalidException("Permission not found"));
        return p.getName().equals(existingPermission.getName());
    }

    public Permission create(Permission permission) throws IdInvalidException {
        if (this.isPermissionExist(permission)) {
            throw new IdInvalidException("Trùng rồi");
        }
        return this.permissionRepository.save(permission);
    }

    public Permission updatePermission(Permission reqPermission) throws IdInvalidException {

        if (this.isPermissionExist(reqPermission)) {
            if (this.isSameName(reqPermission)) {
                throw new IdInvalidException("Trùng rồi");
            }
        }

        Permission existingPermission = permissionRepository.findById(reqPermission.getId()).orElseThrow(() -> new IdInvalidException("Permission not found"));
        existingPermission.setName(reqPermission.getName());
        existingPermission.setApiPath(reqPermission.getApiPath());
        existingPermission.setMethod(reqPermission.getMethod());
        existingPermission.setModule(reqPermission.getModule());

        return this.permissionRepository.save(existingPermission);
    }

    public PaginatedResultDTO getPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pPermissions = this.permissionRepository.findAll(spec, pageable);
        PaginatedResultDTO rs = new PaginatedResultDTO();
        PaginatedResultDTO.Meta mt = new PaginatedResultDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pPermissions.getTotalPages());
        mt.setTotal(pPermissions.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pPermissions.getContent());
        return rs;
    }

    public void deletePermission(long id) throws IdInvalidException {
        Permission existingPermission = permissionRepository.findById(id).orElseThrow(() -> new IdInvalidException("Permission not found"));
        // Loại bỏ liên kết với các Role
        existingPermission.getRoles().forEach(role -> role.getPermissions().remove(existingPermission));
        this.permissionRepository.delete(existingPermission);
    }
}