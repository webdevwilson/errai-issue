package errai.app.client;

import com.google.gwt.core.client.EntryPoint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.jboss.errai.enterprise.client.jaxrs.api.ResponseCallback;

import errai.rest.customers.shared.Customer;
import errai.rest.customers.shared.CustomerService;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;

public class App implements EntryPoint {

  final private FlexTable customersTable = new FlexTable();
  final private TextBox custFirstName = new TextBox();
  final private TextBox custLastName = new TextBox();
  final private TextBox custPostalCode = new TextBox();

  final Map<Long, Integer> rows = new HashMap<Long, Integer>();

  final RemoteCallback<Long> creationCallback = new RemoteCallback<Long>() {
    @Override
    public void callback(Long id) {
      call(new RemoteCallback<Customer>() {
        @Override
        public void callback(Customer customer) {
          addCustomerToTable(customer, customersTable.getRowCount() + 1);
        }
      })
      .retrieveCustomerById(id);
    }
  };

  final RemoteCallback<Customer> modificationCallback = new RemoteCallback<Customer>() {
    @Override
    public void callback(Customer customer) {
      addCustomerToTable(customer, rows.get(customer.getId()));
    }
  };

  final ResponseCallback deletionCallback = new ResponseCallback() {
    @Override
    public void callback(Response response) {
      if (response.getStatusCode() == Response.SC_NO_CONTENT) {
        customersTable.removeAllRows();
        populateCustomersTable();
      } else {
        Window.alert("Could not delete customer");
      }
    }
  };

  @Override
  public void onModuleLoad() {
    
    final Button create = new Button("Create", new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        Customer customer = new Customer(custFirstName.getText(), custLastName.getText(), custPostalCode.getText());
        call(creationCallback).createCustomer(customer);
      }
    });

    FlexTable newCustomerTable = new FlexTable();
    newCustomerTable.setWidget(0, 1, custFirstName);
    newCustomerTable.setWidget(0, 2, custLastName);
    newCustomerTable.setWidget(0, 3, custPostalCode);
    newCustomerTable.setWidget(0, 4, create);
    newCustomerTable.setStyleName("new-customer-table");

    VerticalPanel vPanel = new VerticalPanel();
    vPanel.add(customersTable);
    vPanel.add(new HTML("<hr>"));
    vPanel.add(newCustomerTable);
    vPanel.addStyleName("whole-customer-table");
    RootPanel.get().add(vPanel);

    populateCustomersTable();
  }

  void populateCustomersTable() {
    customersTable.setText(0, 0, "ID");
    customersTable.setText(0, 1, "First Name");
    customersTable.setText(0, 2, "Last Name");
    customersTable.setText(0, 3, "Postal Code");
    customersTable.setText(0, 4, "Date Changed");

    final RemoteCallback<List<Customer>> listCallback = new RemoteCallback<List<Customer>>() {
      @Override
      public void callback(List<Customer> customers) {
        for (final Customer customer : customers) {
          addCustomerToTable(customer, customersTable.getRowCount() + 1);
        }
      }
    };
    call(listCallback).listAllCustomers();
  }

  void addCustomerToTable(final Customer customer, int row) {
    final TextBox firstName = new TextBox();
    firstName.setText(customer.getFirstName());

    final TextBox lastName = new TextBox();
    lastName.setText(customer.getLastName());

    final TextBox postalCode = new TextBox();
    postalCode.setText(customer.getPostalCode());

    final Button update = new Button("Update", new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        customer.setFirstName(firstName.getText());
        customer.setLastName(lastName.getText());
        customer.setPostalCode(postalCode.getText());
        call(modificationCallback).updateCustomer(customer.getId(), customer);
      }
    });

    Button delete = new Button("Delete", new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        call(deletionCallback).deleteCustomer(customer.getId());
      }
    });

    customersTable.setText(row, 0, new Long(customer.getId()).toString());
    customersTable.setWidget(row, 1, firstName);
    customersTable.setWidget(row, 2, lastName);
    customersTable.setWidget(row, 3, postalCode);
    customersTable.setText(row, 4,DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(customer.getLastChanged()));
    customersTable.setWidget(row, 5, update);
    customersTable.setWidget(row, 6, delete);
    rows.put(customer.getId(), row);
  }
  
  private CustomerService call(RemoteCallback cb) {
    return RestClient.create(CustomerService.class, cb, 200);
  }
          
}