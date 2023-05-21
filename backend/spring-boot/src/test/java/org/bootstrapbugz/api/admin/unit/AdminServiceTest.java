package org.bootstrapbugz.api.admin.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.bootstrapbugz.api.admin.payload.request.AdminRequest;
import org.bootstrapbugz.api.admin.payload.request.UpdateRoleRequest;
import org.bootstrapbugz.api.admin.service.impl.AdminServiceImpl;
import org.bootstrapbugz.api.auth.jwt.service.AccessTokenService;
import org.bootstrapbugz.api.auth.jwt.service.RefreshTokenService;
import org.bootstrapbugz.api.shared.util.UnitTestUtil;
import org.bootstrapbugz.api.user.model.Role;
import org.bootstrapbugz.api.user.model.Role.RoleName;
import org.bootstrapbugz.api.user.model.User;
import org.bootstrapbugz.api.user.repository.RoleRepository;
import org.bootstrapbugz.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
  @Mock private UserRepository userRepository;
  @Mock private RoleRepository roleRepository;
  @Mock private AccessTokenService accessTokenService;
  @Mock private RefreshTokenService refreshTokenService;
  @InjectMocks private AdminServiceImpl adminService;
  @Captor private ArgumentCaptor<Iterable<User>> userArgumentCaptor;

  @Test
  void updateUsersRoles() {
    final var updateRolesRequest =
        new UpdateRoleRequest(Collections.singleton("test"), Set.of(RoleName.USER, RoleName.ADMIN));
    final var expectedUser =
        User.builder()
            .id(2L)
            .firstName("Test")
            .lastName("Test")
            .username("test")
            .email("test@localhost")
            .activated(true)
            .roles(Set.of(new Role(RoleName.USER), new Role(RoleName.ADMIN)))
            .build();
    when(userRepository.findAllByUsernameIn(updateRolesRequest.usernames()))
        .thenReturn(List.of(UnitTestUtil.getTestUser()));
    when(roleRepository.findAllByNameIn(updateRolesRequest.roleNames()))
        .thenReturn(List.of(new Role(RoleName.USER), new Role(RoleName.ADMIN)));
    adminService.updateRole(updateRolesRequest);
    verify(userRepository, times(1)).saveAll(userArgumentCaptor.capture());
    assertThat(userArgumentCaptor.getValue()).isEqualTo(List.of(expectedUser));
  }

  @Test
  void lockUsers() {
    final var adminRequest = new AdminRequest(Collections.singleton("test"));
    final var expectedUser =
        User.builder()
            .id(2L)
            .firstName("Test")
            .lastName("Test")
            .username("test")
            .email("test@localhost")
            .activated(true)
            .nonLocked(false)
            .roles(Set.of(new Role(RoleName.USER)))
            .build();
    when(userRepository.findAllByUsernameIn(adminRequest.usernames()))
        .thenReturn(List.of(UnitTestUtil.getTestUser()));
    adminService.lock(adminRequest);
    verify(userRepository, times(1)).saveAll(userArgumentCaptor.capture());
    assertThat(userArgumentCaptor.getValue()).isEqualTo(List.of(expectedUser));
  }

  @Test
  void unlockUsers() {
    final var adminRequest = new AdminRequest(Collections.singleton("test"));
    final var expectedUser =
        User.builder()
            .id(2L)
            .firstName("Test")
            .lastName("Test")
            .username("test")
            .email("test@localhost")
            .activated(true)
            .nonLocked(true)
            .roles(Set.of(new Role(RoleName.USER)))
            .build();
    when(userRepository.findAllByUsernameIn(adminRequest.usernames()))
        .thenReturn(List.of(UnitTestUtil.getTestUser()));
    adminService.unlock(adminRequest);
    verify(userRepository, times(1)).saveAll(userArgumentCaptor.capture());
    assertThat(userArgumentCaptor.getValue()).isEqualTo(List.of(expectedUser));
  }

  @Test
  void activateUsers() {
    final var adminRequest = new AdminRequest(Collections.singleton("test2"));
    final var expectedUser =
        User.builder()
            .id(2L)
            .firstName("Test")
            .lastName("Test")
            .username("test")
            .email("test@localhost")
            .activated(true)
            .roles(Set.of(new Role(RoleName.USER)))
            .build();
    when(userRepository.findAllByUsernameIn(adminRequest.usernames()))
        .thenReturn(List.of(UnitTestUtil.getTestUser()));
    adminService.activate(adminRequest);
    verify(userRepository, times(1)).saveAll(userArgumentCaptor.capture());
    assertThat(userArgumentCaptor.getValue()).isEqualTo(List.of(expectedUser));
  }

  @Test
  void deactivateUsers() {
    final var adminRequest = new AdminRequest(Collections.singleton("test"));
    final var expectedUser =
        User.builder()
            .id(2L)
            .firstName("Test")
            .lastName("Test")
            .username("test")
            .email("test@localhost")
            .activated(false)
            .roles(Set.of(new Role(RoleName.USER)))
            .build();
    when(userRepository.findAllByUsernameIn(adminRequest.usernames()))
        .thenReturn(List.of(UnitTestUtil.getTestUser()));
    adminService.deactivate(adminRequest);
    verify(userRepository, times(1)).saveAll(userArgumentCaptor.capture());
    assertThat(userArgumentCaptor.getValue()).isEqualTo(List.of(expectedUser));
  }

  @Test
  void deleteUsers() {
    final var adminRequest = new AdminRequest(Set.of("admin", "test"));
    when(userRepository.findAllByUsernameIn(adminRequest.usernames()))
        .thenReturn(List.of(UnitTestUtil.getTestUser(), UnitTestUtil.getAdminUser()));
    adminService.delete(adminRequest);
    verify(userRepository, times(2)).delete(any(User.class));
  }
}
