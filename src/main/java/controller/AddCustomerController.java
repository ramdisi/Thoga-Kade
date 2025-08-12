package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
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
        String title,name,address,city,province,postalCode;
        LocalDate dob;
        Double salary;
        title = combobox_title.getValue();
        name = txt_name.getText();
        address = txt_address.getText();
        city = txt_city.getText();
        province = txt_province.getText();
        postalCode = txt_postalCode.getText();
        dob = date_dob.getValue();
        salary = Double.parseDouble(txt_salary.getText());
        try {
            PreparedStatement preparedStatement = con.prepareStatement("Insert into customer values (?,?,?,?,?,?,?,?,?)");
            preparedStatement.setObject(1,custID);
            preparedStatement.setObject(2,title);
            preparedStatement.setObject(3,name);
            preparedStatement.setObject(4,dob);
            preparedStatement.setObject(5,salary);
            preparedStatement.setObject(6,address);
            preparedStatement.setObject(7,city);
            preparedStatement.setObject(8,province);
            preparedStatement.setObject(9,postalCode);
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
