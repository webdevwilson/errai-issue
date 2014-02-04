package errai.login.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import errai.rest.login.shared.LoginService;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;

public class Login implements EntryPoint {

  @Override
  public void onModuleLoad() {
    call(new RemoteCallback<Boolean>() {
      @Override
      public void callback(Boolean response) {
        GWT.log("response:" + response);
      }
    }).login("user", "user");
  }

  private LoginService call(RemoteCallback cb) {
    return (LoginService)RestClient.create(LoginService.class, cb, 200);
  }

}
