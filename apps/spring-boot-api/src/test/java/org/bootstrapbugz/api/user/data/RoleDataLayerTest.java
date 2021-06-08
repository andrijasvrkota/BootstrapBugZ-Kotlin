package org.bootstrapbugz.api.user.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import org.bootstrapbugz.api.user.model.Role;
import org.bootstrapbugz.api.user.model.Role.RoleName;
import org.bootstrapbugz.api.user.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class RoleDataLayerTest {
  @Autowired private RoleRepository roleRepository;

  @Test
  void findAllRolesByNameIn() {
    Set<RoleName> names = Set.of(RoleName.USER, RoleName.ADMIN);
//    List<Role> roles = roleRepository.findAllByNameIn(names);
//    assertThat(roles).hasSize(2);
  }

  @Test
  void findRoleByName() {
//    Role role = roleRepository.findByName(RoleName.USER).orElseThrow();
//    assertEquals(1L, role.getId());
  }
}
