package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.PaginatedResultDTO;
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

    @GetMapping("/roles/{id}")
    @ApiMessage("Fetch role by Id")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") long id) throws IdInvalidException {
        Role role = roleService.getRoleById(id);
        return ResponseEntity.ok().body(role);
    }

    @GetMapping("/roles")
    @ApiMessage("Fetch roles")
    public ResponseEntity<PaginatedResultDTO> getRoles(
            @Filter Specification<Role> spec, Pageable pageable) {

        return ResponseEntity.ok(this.roleService.getRoles(spec, pageable));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Role deleted successfully")
    public ResponseEntity<Void> deleteRole(@PathVariable long id) throws IdInvalidException {
        roleService.deleteRole(id);
        return ResponseEntity.ok().body(null);
    }
}
