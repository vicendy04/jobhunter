package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.PaginatedResultDTO;
import vn.hoidanit.jobhunter.service.PermissionService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }


    @PostMapping("/permissions")
    @ApiMessage("Permission created successfully")
    public ResponseEntity<Permission> createPermission(@RequestBody @Valid Permission permission) throws IdInvalidException {
        Permission createdPermission = this.permissionService.create(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPermission);
    }

    @PutMapping("/permissions")
    @ApiMessage("Permission updated successfully")
    public ResponseEntity<Permission> updatePermission(@RequestBody @Valid Permission permission) throws IdInvalidException {
        Permission updated = permissionService.updatePermission(permission);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/permissions")
    @ApiMessage("Fetch permissions")
    public ResponseEntity<PaginatedResultDTO> getPermissions(
            @Filter Specification<Permission> spec,
            Pageable pageable) {

        return ResponseEntity.ok(this.permissionService.getPermissions(spec, pageable));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Permission deleted successfully")
    public ResponseEntity<Void> deletePermission(@PathVariable long id) throws IdInvalidException {
        permissionService.deletePermission(id);
        return ResponseEntity.ok().body(null);
    }
}
