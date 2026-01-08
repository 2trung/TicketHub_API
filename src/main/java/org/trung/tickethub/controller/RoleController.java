package org.trung.tickethub.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trung.tickethub.dto.role.RoleRequest;
import org.trung.tickethub.service.RoleService;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    // todo: restrict this endpoint to admin only later
    @PostMapping()
    public RoleRequest createRole(@RequestBody RoleRequest roleRequest) {
        return roleService.createRole(roleRequest);
    }
}
