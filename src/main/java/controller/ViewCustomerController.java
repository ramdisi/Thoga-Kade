package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.dto.Customer;

import java.net.URL;
import java.sql.*;
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

    @FXML
    private ComboBox<String> combobox_title;

    @FXML
    private DatePicker date_dob;

    @FXML
    private TextField txt_custID;

    @FXML
    private TextField txt_name;

    @FXML
    private TextField txt_salary;

    @FXML
    private TextField txt_address;

    @FXML
    private TextField txt_city;

    @FXML
    private TextField txt_postalCode;

    @FXML
    private TextField txt_province;

    private ObservableList<Customer> observableCustomerList = FXCollections.observableArrayList();

    private Stage stage = new Stage();

    private Connection con;

    private Customer selectedCustomer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
        combobox_title.getItems().addAll("Mr","Mrs","Ms","Thero");
    }

    public void deleteSelectedCustomer_btn_OnAction(ActionEvent actionEvent) {
        try {
            con.prepareStatement("delete from customer where custID='"+selectedCustomer.getCustID()+"'").execute();
            loadTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editSelectedCustomer_btn_OnAction(ActionEvent actionEvent) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("UPDATE customer SET title=?,name=?,salary=?,dob=?,city=?,province=?,address=?,postalCode=? WHERE custID=?");
            preparedStatement.setString(1,combobox_title.getValue());
            preparedStatement.setString(2,txt_name.getText());
            preparedStatement.setDouble(3,Double.parseDouble(txt_salary.getText()));
            preparedStatement.setObject(4,date_dob.getValue());
            preparedStatement.setString(5,txt_city.getText());
            preparedStatement.setString(6,txt_province.getText());
            preparedStatement.setString(7,txt_address.getText());
            preparedStatement.setString(8,txt_postalCode.getText());
            preparedStatement.setString(9,txt_custID.getText());
            preparedStatement.execute();
            loadTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadTable (){
        try {
            customerDetailTable.getItems().clear();
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
        txt_custID.setText(null);
        txt_address.setText(null);
        txt_city.setText(null);
        txt_province.setText(null);
        txt_postalCode.setText(null);
        txt_salary.setText(null);
        txt_name.setText(null);
        date_dob.setValue(null);
        combobox_title.setValue(null);
    }

    public void table_OnMouseClick(MouseEvent mouseEvent) {
        selectedCustomer = customerDetailTable.getSelectionModel().getSelectedItem();
        txt_custID.setText(selectedCustomer.getCustID());
        txt_address.setText(selectedCustomer.getAddress());
        txt_city.setText(selectedCustomer.getCity());
        txt_province.setText(selectedCustomer.getProvince());
        txt_postalCode.setText(selectedCustomer.getPostalCode());
        txt_salary.setText(selectedCustomer.getSalary().toString());
        txt_name.setText(selectedCustomer.getName());
        date_dob.setValue(selectedCustomer.getDob());
        combobox_title.setValue(selectedCustomer.getTitle());
    }
}
