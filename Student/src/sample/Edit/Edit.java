package sample.Edit;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Controller;
import sample.Database.Student;
import sample.Database.StudentDAO;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class Edit implements Initializable{

    public TextField nameField;
    public TextField emailField;
    public RadioButton maleRadio;
    public RadioButton femaleRadio;
    public ToggleGroup gender;
    public DatePicker dobPicker;
    public Button updateButton;
    public Button cancelButton;

    private int studentId;
    StudentDAO studentDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        maleRadio.setUserData("Male");
        femaleRadio.setUserData("Female");
        studentDAO = new StudentDAO();
    }

    public void updateAction(ActionEvent event)  {
        String name = nameField.getText();
        String email = emailField.getText();
        RadioButton selectedRadio = (RadioButton) gender.getSelectedToggle();
        String genderInfo = (String) selectedRadio.getUserData();
        LocalDate localDate = dobPicker.getValue();
        if (name.isEmpty() || email.isEmpty() || localDate == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill all forms.");
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.show();
            return;
        }
        Date date = Date.valueOf(localDate);
        Student student = new Student(studentId,name,email,genderInfo,date); //to update data.

        try {
            studentDAO.update(student);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setStudentData(Student selectedStudent){
        studentId = selectedStudent.getId();
        nameField.setText(selectedStudent.getName());
        emailField.setText(selectedStudent.getEmail());
        if (selectedStudent.getGender().equals("Male")){
            maleRadio.setSelected(true);
        }else {
            femaleRadio.setSelected(true);
        }
        Date date = selectedStudent.getDob();
        dobPicker.setValue(date.toLocalDate());
    }

    public void cancelAction(ActionEvent event) {
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();
    }
}
