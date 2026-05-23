package com.example.springboot.common.config;

public interface ApiRoutes {
  String VERSION = "/api/v1";

  interface Auth {
    String BASE = VERSION + "/auth";
    String REGISTER = BASE + "/register";
    String LOGIN = BASE + "/login";
  }

  interface Users {
    String BASE = VERSION + "/users";
    String CHANGE_PASSWORD = BASE + "/change-password";
    String CHANGE_EMAIL = BASE + "/change-email";
    String DELETE = BASE + "/delete";
  }

  interface Portfolio {
    String BASE = VERSION + "/portfolio";
    String CREATE_PORTFOLIO = BASE + "/create";

    // PortfolioAsset row
    interface PortfolioAsset {
      String BASE = Portfolio.BASE + "/portfolio-asset";
      String CREATE_PORTFOLIO_ASSET = BASE + "/create";
      String UPDATE = BASE + "/update";
    }
  }
}
