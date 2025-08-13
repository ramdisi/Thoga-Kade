package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.dto.Customer;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewCustomerController implements Initializable {

    @FXML
    private TableColumn<?, ?> column_address;

    @FXML
    private TableColumn<?, ?> column_city;

    @FXML
    private TableColumn<?, ?> column_custID;

    @FXML
    private TableColumn<?, ?> column_dob;

    @FXML
    private TableColumn<?, ?> column_name;

    @FXML
    private TableColumn<?, ?> column_postalCode;

    @FXML
    private TableColumn<?, ?> column_province;

    @FXML
    private TableColumn<?, ?> column_salary;

    @FXML
    private TableColumn<?, ?> column_title;

    @FXML
    private TableView<Customer> customerDetailTable;

    private ObservableList<Customer> observableCustomerList = FXCollections.observableArrayList();

    private Stage stage = new Stage();

    private Connection con;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
    }

    public void deleteSelectedCustomer_btn_OnAction(ActionEvent actionEvent) {
        Customer selectedCustomer = customerDetailTable.getSelectionModel().getSelectedItem();
        try {
            con.prepareStatement("delete from customer where custID='"+selectedCustomer.getCustID()+"'").execute();
            customerDetailTable.getItems().clear();
            loadTable();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void editSelectedCustomer_btn_OnAction(ActionEvent actionEvent) {
        Customer selectedCustomer = customerDetailTable.getSelectionModel().getSelectedItem();


    }

    public void loadTable (){
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/thogakade","root","1234");
            ResultSet resultSet = con.prepareStatement("select * from customer").executeQuery();
            while(resultSet.next()){
                observableCustomerList.add(new Customer(
                        resultSet.getString("custID"),
                        resultSet.getString("title"),
                        resultSet.getString("name"),
                        resultSet.getDate("dob").toLocalDate(),
                        resultSet.getDouble("salary"),
                        resultSet.getString("address"),
                        resultSet.getString("city"),
                        resultSet.getString("province"),
                        resultSet.getString("postalCode")
                ));
            }
            column_custID.setCellValueFactory(new PropertyValueFactory<>("custID"));
            column_address.setCellValueFactory(new PropertyValueFactory<>("address"));
            column_city.setCellValueFactory(new PropertyValueFactory<>("city"));
            column_dob.setCellValueFactory(new PropertyValueFactory<>("dob"));
            column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
            column_province.setCellValueFactory(new PropertyValueFactory<>("province"));
            column_title.setCellValueFactory(new PropertyValueFactory<>("title"));
            column_postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            column_salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
            customerDetailTable.setItems(observableCustomerList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
