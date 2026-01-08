package org.trung.tickethub.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.trung.tickethub.dto.role.RoleRequest;
import org.trung.tickethub.entity.Role;
import org.trung.tickethub.repository.RoleRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;

    public RoleRequest createRole(RoleRequest roleRequest) {
        Role role = Role.builder()
                .name(roleRequest.getName())
                .description(roleRequest.getDescription())
                .build();
        if (roleRepository.existsById(role.getName())) {
            log.warn("Role with name {} already exists", role.getName());
            return roleRequest;
        }
        roleRepository.save(role);
        return roleRequest;
    }
}
