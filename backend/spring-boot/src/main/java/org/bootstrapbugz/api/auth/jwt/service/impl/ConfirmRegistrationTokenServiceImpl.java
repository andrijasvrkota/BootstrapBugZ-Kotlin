package org.bootstrapbugz.api.auth.jwt.service.impl;

import com.auth0.jwt.JWT;
import java.time.Instant;
import org.bootstrapbugz.api.auth.jwt.event.OnSendJwtEmail;
import org.bootstrapbugz.api.auth.jwt.service.ConfirmRegistrationTokenService;
import org.bootstrapbugz.api.auth.jwt.util.JwtUtil;
import org.bootstrapbugz.api.auth.jwt.util.JwtUtil.JwtPurpose;
import org.bootstrapbugz.api.user.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ConfirmRegistrationTokenServiceImpl implements ConfirmRegistrationTokenService {
  private static final JwtPurpose PURPOSE = JwtPurpose.CONFIRM_REGISTRATION_TOKEN;

  private final ApplicationEventPublisher eventPublisher;

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.confirm-registration-token.duration}")
  private int tokenDuration;

  public ConfirmRegistrationTokenServiceImpl(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Override
  public String create(Long userId) {
    return JWT.create()
        .withIssuer(userId.toString())
        .withClaim("issuedAt", Instant.now().toString())
        .withClaim("purpose", PURPOSE.name())
        .withExpiresAt(Instant.now().plusSeconds(tokenDuration))
        .sign(JwtUtil.getAlgorithm(secret));
  }

  @Override
  public void check(String token) {
    JwtUtil.verify(token, secret, PURPOSE);
  }

  @Override
  public void sendToEmail(User user, String token) {
    eventPublisher.publishEvent(new OnSendJwtEmail(user, token, PURPOSE));
  }
}
