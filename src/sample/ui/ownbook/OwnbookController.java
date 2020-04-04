package sample.ui.ownbook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.classes.Book;
import sample.classes.User;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OwnbookController implements Initializable {

    ObservableList<Book> list = FXCollections.observableArrayList();

    DatabaseHandler handler;

    @FXML
    private TextField bookName;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Book> TableView;
    @FXML
    private TableColumn<Book, String> TitleCol;
    @FXML
    private TableColumn<Book, String> TeacherCol;
    @FXML
    private TableColumn<Book, String> TimeCol;
    @FXML
    private ChoiceBox<String> Rating;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DatabaseHandler.getInstance();
        Rating.getItems().addAll("1","2","3","4","5","6","7","8","9","10");
        Rating.setValue("10");
        TableView.getItems().removeAll(list);
        list = FXCollections.observableArrayList();
        initCol();
        loadData();
    }

    private void loadData() {
        String qu = "SELECT title,teacher,time FROM lessons";
        ResultSet rs = handler.execQuery(qu);
        try{
            while (rs.next()){
                String title = rs.getString("title");
                String teacher = rs.getString("teacher");
                String time = rs.getString("time");
                list.add(new Book(title, teacher, time, "2"));
            }
        } catch (SQLException ex){
            Logger.getLogger(OwnbookController.class.getName()).log(Level.SEVERE, null, ex);
        }

        TableView.getItems().setAll(list);
    }

    private void initCol() {
        TitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TeacherCol.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        TimeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
    }

    public void search(ActionEvent actionEvent) {

    }

    public void ownBook(ActionEvent actionEvent) {
        String name = User.getUsername();
        Book book = TableView.getSelectionModel().getSelectedItem();
        if (book == null){
            return;
        }
        String time = book.getTime();
        String rating = Rating.getValue();

        String q = "SELECT * FROM rating";
        ResultSet r = handler.execQuery(q);
        try{
            while (r.next()){
                String DBtitle = r.getString("title");
                String DBuser = r.getString("username");
                String DBtime = r.getString("time");
                if (DBtitle.toLowerCase().equals(book.getTitle().toLowerCase()) && DBuser.toLowerCase().equals(name) && DBtime.toLowerCase().equals(time)){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setTitle("Error");
                    alert.setContentText("You already rated this lesson!");
                    alert.showAndWait();
                    return;
                }
            }
        }catch (SQLException ex){
            Logger.getLogger(OwnbookController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        String qu = "INSERT INTO rating VALUES(" +
                "'" + book.getTitle() + "'," +
                "'" + book.getTeacher() + "'," +
                "'" + book.getTime() + "'," +
                "'" + rating + "'," +
                "'" + User.getUsername() + "'" +
                ")";
        if(handler.execAction(qu)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Good job, boss!");
            alert.setContentText("You rated this lesson!");
            alert.showAndWait();

        } else //Error
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("We got a problem!");
            alert.setContentText("Something went wrong, boss!");
            alert.showAndWait();
        }
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}
