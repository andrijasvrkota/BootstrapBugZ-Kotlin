package org.bootstrapbugz.api.auth.jwt.service.impl;

import com.auth0.jwt.JWT;
import java.time.Instant;
import java.util.Set;
import org.bootstrapbugz.api.auth.jwt.redis.model.AccessTokenBlacklist;
import org.bootstrapbugz.api.auth.jwt.redis.model.UserBlacklist;
import org.bootstrapbugz.api.auth.jwt.redis.repository.AccessTokenBlacklistRepository;
import org.bootstrapbugz.api.auth.jwt.redis.repository.UserBlacklistRepository;
import org.bootstrapbugz.api.auth.jwt.service.AccessTokenService;
import org.bootstrapbugz.api.auth.jwt.util.JwtUtil;
import org.bootstrapbugz.api.auth.jwt.util.JwtUtil.JwtPurpose;
import org.bootstrapbugz.api.shared.error.exception.UnauthorizedException;
import org.bootstrapbugz.api.shared.message.service.MessageService;
import org.bootstrapbugz.api.user.payload.dto.RoleDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {
  private static final JwtPurpose PURPOSE = JwtPurpose.ACCESS_TOKEN;

  private final AccessTokenBlacklistRepository accessTokenBlacklistRepository;
  private final UserBlacklistRepository userBlacklistRepository;
  private final MessageService messageService;

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.access-token.duration}")
  private int tokenDuration;

  public AccessTokenServiceImpl(
      AccessTokenBlacklistRepository accessTokenBlacklistRepository,
      UserBlacklistRepository userBlacklistRepository,
      MessageService messageService) {
    this.accessTokenBlacklistRepository = accessTokenBlacklistRepository;
    this.userBlacklistRepository = userBlacklistRepository;
    this.messageService = messageService;
  }

  @Override
  public String create(Long userId, Set<RoleDTO> roleDTOs) {
    return JWT.create()
        .withIssuer(userId.toString())
        .withClaim("issuedAt", Instant.now().toString())
        .withClaim("roles", roleDTOs.stream().map(RoleDTO::getName).toList())
        .withClaim("purpose", PURPOSE.name())
        .withExpiresAt(Instant.now().plusSeconds(tokenDuration))
        .sign(JwtUtil.getAlgorithm(secret));
  }

  @Override
  public void check(String token) {
    JwtUtil.verify(token, secret, PURPOSE);
    isInJwtBlacklist(token);
    isInUserBlacklist(token);
  }

  private void isInJwtBlacklist(String token) {
    if (accessTokenBlacklistRepository.existsById(token))
      throw new UnauthorizedException(messageService.getMessage("token.invalid"));
  }

  private void isInUserBlacklist(String token) {
    final var userInBlacklist = userBlacklistRepository.findById(JwtUtil.getUserId(token));
    if (userInBlacklist.isPresent()
        && JwtUtil.getIssuedAt(token).isBefore(userInBlacklist.get().getUpdatedAt()))
      throw new UnauthorizedException(messageService.getMessage("token.invalid"));
  }

  @Override
  public void invalidate(String token) {
    accessTokenBlacklistRepository.save(new AccessTokenBlacklist(token, tokenDuration));
  }

  @Override
  public void invalidateAllByUser(Long userId) {
    userBlacklistRepository.save(new UserBlacklist(userId, Instant.now(), tokenDuration));
  }
}
