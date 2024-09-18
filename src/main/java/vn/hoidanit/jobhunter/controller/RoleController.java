package vn.hoidanit.jobhunter.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;


@RestController
@RequestMapping("/api/v1")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Role created successfully")
    public ResponseEntity<Role> createRole(@RequestBody @Valid Role role) throws IdInvalidException {
        Role createdRole = roleService.createRole(role);
        return ResponseEntity.status(201).body(createdRole);  // HTTP 201 Created
    }

    @PutMapping("/roles")
    @ApiMessage("Role updated successfully")
    public ResponseEntity<Role> updateRole(@RequestBody @Valid Role updatedRole) throws IdInvalidException {
        Role updated = roleService.updateRole(updatedRole);
        return ResponseEntity.ok(updated);  // HTTP 200 OK
    }

    @GetMapping("/roles")
    @ApiMessage("Roles fetched successfully")
    public ResponseEntity<Page<Role>> getRoles(Pageable pageable) {
        Page<Role> roles = roleService.getRoles(pageable);
        return ResponseEntity.ok(roles);  // HTTP 200 OK
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Role deleted successfully")
    public ResponseEntity<Void> deleteRole(@PathVariable long id) throws IdInvalidException {
        roleService.deleteRole(id);
        return ResponseEntity.ok().body(null);
    }
}
