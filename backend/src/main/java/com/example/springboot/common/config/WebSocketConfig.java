package com.example.springboot.common.config;

import com.example.springboot.security.JwtHandshakeInterceptor;
import com.example.springboot.web_socket.PriceWebSocketHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@Profile("!test")
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

  private final PriceWebSocketHandler priceWebSocketHandler;
  private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

  @Value("#{'${app.security.allowed-origins}'.split(',')}")
  private List<String> allowedOrigins;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry
        .addHandler(priceWebSocketHandler, "/ws/prices")
        .addInterceptors(jwtHandshakeInterceptor)
        .setAllowedOrigins(allowedOrigins.toArray(new String[0]));
  }
}
