package com.example.springboot.security;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

  private final JwtService jwtService;

  @Override
  public boolean beforeHandshake(
      ServerHttpRequest request,
      ServerHttpResponse response,
      WebSocketHandler wsHandler,
      Map<String, Object> attributes) {

    String query = request.getURI().getQuery(); // token=xxx
    if (query == null || !query.startsWith("token=")) {
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
      return false; // reject connection
    }

    String token = query.replace("token=", "");
    String email = jwtService.extractEmail(token);
    if (!jwtService.isTokenValid(token, email)) {
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
      return false; // reject connection
    }

    return true; // allow connection
  }

  @Override
  public void afterHandshake(
      ServerHttpRequest request,
      ServerHttpResponse response,
      WebSocketHandler wsHandler,
      Exception exception) {}
}
