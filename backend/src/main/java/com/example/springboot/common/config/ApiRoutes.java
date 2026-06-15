package com.example.springboot.common.config;

public interface ApiRoutes {
  String VERSION = "/api/v1";

  interface Currency {
    String BASE = VERSION + "/currencies";
  }

  interface Auth {
    String BASE = VERSION + "/auth";
    // POST
    String REGISTER = BASE + "/register";
    // POST
    String LOGIN = BASE + "/login";
  }

  interface UserBalance {
    String BASE = VERSION + "/balance";
    // POST
    String INCREASE = BASE + "/increase";
    // GET
    String GET = BASE;
  }

  interface Users {
    String BASE = VERSION + "/users";
    // POST
    String CHANGE_PASSWORD = BASE + "/change-password";
    // PATCH
    String CHANGE_EMAIL = BASE + "/change-email";
    // PATCH
    String CHANGE_CURRENCY = BASE + "/change-currency";
    // DELETE
    String DELETE = BASE + "/delete";
  }

  interface Assets {
    String BASE = VERSION + "/assets";
    // GET
    String GET = BASE;
  }

  interface Portfolios {
    String BASE = VERSION + "/portfolios";
    // POST
    String CREATE = BASE;
    // GET
    String LIST = BASE;
    // DELETE
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
