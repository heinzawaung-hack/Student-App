package sample.Database;

import javafx.scene.chart.PieChart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public void saveStudent(Student student) throws SQLException {
        Connection connection = Database.getInstance().getConnection();
        String save = "insert into user (name,email,gender,dob) values (?,?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(save);
        preparedStatement.setString(1,student.getName());
        preparedStatement.setString(2,student.getEmail());
        preparedStatement.setString(3,student.getGender());
        preparedStatement.setDate(4,student.getDob());
        preparedStatement.execute();
    }

    public List<Student> getStudent() throws SQLException {
        Connection connection = Database.getInstance().getConnection();
        String retrieve = "select * from user";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(retrieve);

        List<Student> studentList = new ArrayList<>();
        while (resultSet.next()){
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String gender = resultSet.getString("gender");
            Date date = resultSet.getDate("dob");
            Student student = new Student(id,name,email,gender,date);
            studentList.add(student);
        }
        return studentList;
    }

    public Student getStudentById(int id) throws SQLException {
        Connection connection = Database.getInstance().getConnection();
        String dataRetrieve = "select * from user where id=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(dataRetrieve);
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Student student = null;
        if (resultSet.next()){
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String gender = resultSet.getString("gender");
            Date dob = resultSet.getDate("dob");
            student = new Student(id,name,email,gender,dob);
        }
        return student;
    }

    public void update(Student student) throws SQLException {
        Connection connection = Database.getInstance().getConnection();
        int id = student.getId();
        String name = student.getName();
        String email = student.getEmail();
        String gender = student.getGender();
        Date dob = student.getDob();
        String updateData = "update user set name=?,email=?,gender=?,dob=? where id=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(updateData);
        preparedStatement.setString(1,name);
        preparedStatement.setString(2,email);
        preparedStatement.setString(3,gender);
        preparedStatement.setDate(4,dob);
        preparedStatement.setInt(5,id);
        preparedStatement.executeUpdate();
        System.out.println("Update Data Success");
    }

    public void delete(int id) throws SQLException {
        Connection connection = Database.getInstance().getConnection();
        String delete = "delete from user where id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(delete);
        preparedStatement.setInt(1,id);
        preparedStatement.executeUpdate();
    }
}
