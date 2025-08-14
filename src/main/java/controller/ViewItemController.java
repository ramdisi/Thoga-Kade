package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.dto.Item;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ViewItemController implements Initializable {

    @FXML
    private TableColumn<?, ?> column_description;

    @FXML
    private TableColumn<?, ?> column_itemCode;

    @FXML
    private TableColumn<?, ?> column_pkgSize;

    @FXML
    private TableColumn<?, ?> column_qtyOnHand;

    @FXML
    private TableColumn<?, ?> column_unitPrice;

    @FXML
    private TableView<Item> table_item;

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

    private ObservableList<Item> observableItemList = FXCollections.observableArrayList();

    private Item selectedItem;

    @FXML
    void btn_OnAction_deleteItem(ActionEvent event) {
        try {
            con.prepareStatement("delete from item where itemCode='"+selectedItem.getItemCode()+"'").execute();
            loadTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btn_OnAction_editItem(ActionEvent event) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("Update item set description=?,unitPrice=?,qtyOnHand=?,packSize=? where itemCode=?");
            preparedStatement.setString(1,txt_description.getText());
            preparedStatement.setDouble(2,Double.parseDouble(txt_unitPrice.getText()));
            preparedStatement.setInt(3,Integer.parseInt(txt_qtyOnHand.getText()));
            preparedStatement.setString(4,txt_pkgSize.getText());
            preparedStatement.setString(5,txt_itemCode.getText());
            preparedStatement.execute();
            loadTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            con= DriverManager.getConnection("jdbc:mysql://localhost/thogakade","root","1234");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loadTable();
    }

    private void loadTable(){
        try {
            table_item.getItems().clear();
            ResultSet resultSet = con.prepareStatement("select * from item").executeQuery();
            while(resultSet.next()){
                observableItemList.add(new Item(
                        resultSet.getString("itemCode"),
                        resultSet.getString("description"),
                        resultSet.getString("packSize"),
                        resultSet.getDouble("unitPrice"),
                        resultSet.getInt("qtyOnHand")
                ));
            }
            column_itemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
            column_description.setCellValueFactory(new PropertyValueFactory<>("description"));
            column_pkgSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
            column_unitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            column_qtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
            table_item.setItems(observableItemList);
            txt_unitPrice.setText(null);
            txt_description.setText(null);
            txt_pkgSize.setText(null);
            txt_itemCode.setText(null);
            txt_qtyOnHand.setText(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void OnClick_itemTable(MouseEvent mouseEvent) {
        selectedItem = table_item.getSelectionModel().getSelectedItem();
        txt_itemCode.setText(selectedItem.getItemCode());
        txt_description.setText(selectedItem.getDescription());
        txt_pkgSize.setText(selectedItem.getPackSize());
        txt_qtyOnHand.setText(selectedItem.getQtyOnHand().toString());
        txt_unitPrice.setText(selectedItem.getUnitPrice().toString());
    }
}
