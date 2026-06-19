package com.example.springboot.common.config;

import com.example.springboot.web_socket.PriceWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  private final PriceWebSocketHandler priceWebSocketHandler;

  public WebSocketConfig(PriceWebSocketHandler priceWebSocketHandler) {
    this.priceWebSocketHandler = priceWebSocketHandler;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(priceWebSocketHandler, "/ws/prices").setAllowedOrigins("*");
  }
}
