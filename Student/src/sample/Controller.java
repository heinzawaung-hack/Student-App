package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Database.Student;
import sample.Database.StudentDAO;
import sample.Edit.Edit;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public Button saveButton;
    public ToggleGroup gender;
    public TextField nameField;
    public TextField emailField;
    public RadioButton maleRadio;
    public RadioButton femaleRadio;
    public DatePicker dobPicker;
    public TableView<Student> studentTable;
    public TableColumn<Student, Integer> idColumn;
    public TableColumn<Student, String> nameColumn;
    public TableColumn<Student, String> emailColumn;
    public TableColumn<Student, String> genderColumn;
    public TableColumn<Student, Date> dobColumn;
    public MenuItem editMenu;
    public MenuItem deleteMenu;
    public MenuItem versionInfo;
    public MenuItem checkUpdate;
    public MenuItem exitItem;
    public MenuItem deleteItem;


    private StudentDAO studentDAO;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        maleRadio.setUserData("Male");
        femaleRadio.setUserData("Female");
        initial();
        studentDAO = new StudentDAO();
        loadTableData();
    }

    public void loadTableData() {
        try {
            List<Student> studentList = studentDAO.getStudent();
            studentTable.getItems().setAll(studentList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initial() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
    }


    public void saveInfo(ActionEvent event) {
        String name = nameField.getText();
        String email = emailField.getText();
        RadioButton selectedRadio = (RadioButton) gender.getSelectedToggle();
        String genderInfo = (String) selectedRadio.getUserData();
        LocalDate localDate =  dobPicker.getValue();

        if (name.isEmpty() || email.isEmpty() || localDate == null){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Please fill all forms");
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Error Occurred");
            errorAlert.show();
            return;
        }
        Date dobSql = Date.valueOf(localDate);

        Student student = new Student(name,email,genderInfo,dobSql);
        try {
            studentDAO.saveStudent(student);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Saved data to Database.");
            alert.setHeaderText(null);
            alert.setTitle("Success");
            alert.show();
            clearFields();
            loadTableData();
        } catch (SQLException e) {
            System.out.println("Error Occurred");
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        maleRadio.setSelected(true);
        dobPicker.setValue(null);
    }

    public void deleteInfo(ActionEvent event) {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select one row to delete.");
            alert.setTitle("Error");
            alert.show();
            return;
        }

        try {
            studentDAO.delete(selectedStudent.getId());
            studentTable.getItems().remove(selectedStudent);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadEditWindow(ActionEvent event) throws IOException {
        Student getStudent = studentTable.getSelectionModel().getSelectedItem();
        if (getStudent == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select one row to edit.");
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.show();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/Edit/Edit.fxml"));
        Parent root = loader.load();
        Edit editController  = loader.getController();
        editController.setStudentData(getStudent); //to connect with edit controller

        Scene scene = new Scene(root);
        Stage updateStage = new Stage();
        updateStage.setScene(scene);
        updateStage.setTitle("Update Info");
        updateStage.initModality(Modality.WINDOW_MODAL);
        updateStage.initOwner(studentTable.getScene().getWindow());
        updateStage.showAndWait();
        loadTableData();
    }

    public void versionWindowReal(ActionEvent event) throws IOException {
        Parent version = FXMLLoader.load(getClass().getResource("Version.fxml"));
        Scene scene = new Scene(version);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Version");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(femaleRadio.getScene().getWindow());
        stage.show();
    }

    public void exitAction(ActionEvent event) {
        Stage exitStage = (Stage) saveButton.getScene().getWindow();
        exitStage.close();
    }

    public void deleteAction(ActionEvent event) {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select one row to delete.");
            alert.setTitle("Error");
            alert.show();
            return;
        }

        try {
            studentDAO.delete(selectedStudent.getId());
            studentTable.getItems().remove(selectedStudent);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkForUpdate(ActionEvent event) {
    }
}
