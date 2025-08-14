package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {


    @FXML
    private ComboBox<String> combobox_title;

    @FXML
    private DatePicker date_dob;

    @FXML
    private TextField txt_address;

    @FXML
    private TextField txt_city;

    @FXML
    private TextField txt_cusID;

    @FXML
    private TextField txt_name;

    @FXML
    private TextField txt_postalCode;

    @FXML
    private TextField txt_province;

    @FXML
    private TextField txt_salary;

    private Connection con;

    private String custID;

    @FXML
    void addCustomer_btn_OnAction(ActionEvent event) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("Insert into customer values (?,?,?,?,?,?,?,?,?)");
            preparedStatement.setObject(1,custID);
            preparedStatement.setObject(2,combobox_title.getValue());
            preparedStatement.setObject(3,txt_name.getText());
            preparedStatement.setObject(4,date_dob.getValue());
            preparedStatement.setObject(5,Double.parseDouble(txt_salary.getText()));
            preparedStatement.setObject(6,txt_address.getText());
            preparedStatement.setObject(7,txt_city.getText());
            preparedStatement.setObject(8,txt_province.getText());
            preparedStatement.setObject(9,txt_postalCode.getText());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setCustomerID(custID);
        txt_address.setText("");
        txt_city.setText("");
        txt_name.setText("");
        txt_province.setText("");
        txt_salary.setText("");
        txt_postalCode.setText("");
        combobox_title.setValue("");
        date_dob.setValue(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        combobox_title.getItems().addAll("Mr","Mrs","Ms","Thero");
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/thogakade","root","1234");
            ResultSet resultSet = con.prepareStatement("select custID from customer order by custID desc limit 1").executeQuery();
            if (resultSet.next()){
                setCustomerID(resultSet.getString("custID"));
            }else{
                setCustomerID("C000");//if no id available
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private void setCustomerID(String previousID){
        custID = "C"+String.format("%03d",Integer.parseInt(previousID.substring(1))+1);
        txt_cusID.setText(custID);
    }
}
