package com.example.springboot.common.config;

public interface ApiRoutes {
  String VERSION = "/api/v1";

  interface Auth {
    String BASE = VERSION + "/auth";
    String REGISTER = BASE + "/register";
    String LOGIN = BASE + "/login";
  }

  interface UserBalance {
    String BASE = VERSION + "/balance";
    String INCREASE = BASE + "/increase";
    String GET = BASE;
  }

  interface Users {
    String BASE = VERSION + "/users";
    String CHANGE_PASSWORD = BASE + "/change-password";
    String CHANGE_EMAIL = BASE + "/change-email";
    String CHANGE_CURRENCY = BASE + "/change-currency";
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
      String BASE = ApiRoutes.Portfolios.BASE;
      // GET request
      String LIST = BASE + "/{portfolioName}";
      // POST request
      String CREATE = BASE + "/{portfolioName}";
      // PATCH request - no need for assetName as it is in the request
      String UPDATE = BASE + "/{portfolioName}";
      // DELETE request
      String DELETE = BASE + "/{portfolioName}/{assetName}";
    }
  }
}
