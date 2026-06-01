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

  interface Portfolios {
    String BASE = VERSION + "/portfolios";
    // POST
    String CREATE = BASE;
    // GET
    String LIST = BASE;
    String DELETE = BASE + "/delete";

    // PortfolioAsset row
    interface PortfolioAssets {
      String BASE = VERSION + "/{portfolioName}";
      // GET request
      String LIST = BASE;
      // POST request
      String CREATE = BASE;
      // PATCH request
      String UPDATE = BASE + "/{assetName}";
      // DELETE request
      String DELETE = BASE + "/{assetName}";
    }
  }
}
