package errai.rest.login.server;

import errai.rest.login.shared.LoginService;

public class LoginServiceImpl implements LoginService {

  @Override
  public boolean login(String user, String pass) {
    return user.equals(pass);
  }

}
