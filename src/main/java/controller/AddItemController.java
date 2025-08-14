package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AddItemController implements Initializable {

    @FXML
    private TextArea txt_description;

    @FXML
    private TextField txt_itemCode;

    @FXML
    private TextField txt_pkgSize;

    @FXML
    private TextField txt_qtyOnHand;

    @FXML
    private TextField txt_unitPrice;

    private Connection con;

    private String itemCode;

    @FXML
    void addItem_btn_OnAction(ActionEvent event) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("insert into item values (?,?,?,?,?)");
            preparedStatement.setObject(1,itemCode);
            preparedStatement.setObject(2,txt_description.getText());
            preparedStatement.setObject(3,txt_pkgSize.getText());
            preparedStatement.setObject(4,Integer.parseInt(txt_qtyOnHand.getText()));
            preparedStatement.setObject(5,Double.parseDouble(txt_unitPrice.getText()));
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setItemCode(itemCode);
        txt_description.setText("");
        txt_pkgSize.setText("");
        txt_unitPrice.setText("");
        txt_qtyOnHand.setText("");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/thogakade","root","1234");
            ResultSet resultSet = con.prepareStatement("select itemCode from item order by itemCode desc limit 1").executeQuery();
            if (resultSet.next()){
                setItemCode(resultSet.getString("itemCode"));
            }else{
                setItemCode("P000");//if no id available
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setItemCode(String previousCode){
        itemCode = "P"+String.format("%03d",Integer.parseInt(previousCode.substring(1))+1);
        txt_itemCode.setText(itemCode);
    }
}
