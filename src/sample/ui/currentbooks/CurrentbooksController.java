package sample.ui.currentbooks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.classes.Book;
import sample.classes.User;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CurrentbooksController implements Initializable {

    ObservableList<Book> list = FXCollections.observableArrayList();

    @FXML
    private TableView<Book> TableView;
    @FXML
    private TableColumn<Book, String> TitleCol;
    @FXML
    private TableColumn<Book, String> TeacherCol;
    @FXML
    private TableColumn<Book, String> TimeCol;
    @FXML
    private TableColumn<Book, String> RatingCol;
    @FXML
    private AnchorPane rootPane;

    DatabaseHandler handler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DatabaseHandler.getInstance();
        list = FXCollections.observableArrayList();
        initCol();
        loadData();
    }

    private void loadData() {
        ArrayList<String> listbook = new ArrayList<>();
        String qu = "SELECT * FROM rating WHERE username='" + User.getUsername() + "'";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()){
                listbook.add(rs.getString("title"));
            }
        } catch (SQLException ex) {
            System.out.println("BOOOM");
            Logger.getLogger(CurrentbooksController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (String s : listbook) {
            String q = "SELECT * FROM rating WHERE title='" + s + "'";
            ResultSet r = handler.execQuery(q);
            try {
                while (r.next()) {
                    String title = r.getString("title");
                    String teacher = r.getString("teacher");
                    String time = r.getString("time");
                    String rating = r.getString("rating");
                    list.add(new Book(title, teacher, time, rating));
                }
            } catch (SQLException ex) {
                Logger.getLogger(CurrentbooksController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        TableView.getItems().setAll(list);
    }

    private void initCol() {
        TitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TeacherCol.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        TimeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        RatingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
    }

//    public void remove(ActionEvent actionEvent) {
//        Book book = TableView.getSelectionModel().getSelectedItem();
//        String qu = "DELETE FROM current WHERE user='" + User.getUsername() + "' AND ISBN='" + book.getISBN() + "'";
//        if(handler.execAction(qu)){
//            TableView.getItems().removeAll(list);
//            list = FXCollections.observableArrayList();
//            loadData();
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setHeaderText(null);
//            alert.setTitle("Good job, boss!");
//            alert.setContentText("Book has been removed!");
//            alert.showAndWait();
//
//        } else //Error
//        {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText(null);
//            alert.setTitle("We got a problem!");
//            alert.setContentText("Something went wrong, boss!");
//            alert.showAndWait();
//        }
//    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}
