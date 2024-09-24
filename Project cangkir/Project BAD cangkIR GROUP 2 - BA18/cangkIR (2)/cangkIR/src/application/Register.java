package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseManager;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

public class Register extends Application {

	private static final String String = null;
	private Scene scene;
	private Label judul;
	private Label nameLabel;
	private Label space;
	private TextField nameTextField;
	private Label emailLabel;
	private TextField emailField;
	private Label passwordLabel;
	private PasswordField passwordField;
	private Label genderLabel;
	private RadioButton rbm;
	private RadioButton rbf;
	private Button registerButton;
	private GridPane gp;
	private BorderPane bp;
	private FlowPane fp;
	private Hyperlink hyperl;
	private VBox vbox;
	private ToggleGroup tg;

	public static void main(String[] args) {
		launch(args);
	}

	public void init() {

		bp = new BorderPane();
		gp = new GridPane();
		fp = new FlowPane();
		vbox = new VBox();
		judul = new Label("Register ");
		judul.setStyle("-fx-font-size: 32; -fx-font-weight: bold; ");
		registerButton = new Button("Register");
		registerButton.setPrefWidth(100); 
		registerButton.setPrefHeight(40);

		nameLabel = new Label("Username");
		nameTextField = new TextField();
		nameTextField.setPromptText("Input your username here");

		emailLabel = new Label("Email");
		emailField = new TextField();
		emailField.setPromptText("Input your email here");
		passwordLabel = new Label("Password");
		passwordField = new PasswordField();
		passwordField.setMinWidth(400);
		passwordField.setPromptText("Input your password here");

		space = new Label("");

		genderLabel = new Label("Gender");
		genderLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold; ");
		rbm = new RadioButton("Male");
		rbf = new RadioButton("Female");

		tg = new ToggleGroup();
		rbm.setToggleGroup(tg);
		rbf.setToggleGroup(tg);
		hyperl = new Hyperlink("Already have an account? click here to login!");
	}

	public void layouting() {
		bp.setTop(judul);
		bp.setCenter(gp);
		bp.setBottom(vbox);
		bp.setAlignment(judul, javafx.geometry.Pos.CENTER);
		bp.setAlignment(registerButton, javafx.geometry.Pos.CENTER);

		bp.setPadding(new Insets(30, 50, 100, 50));

		gp.setAlignment(javafx.geometry.Pos.CENTER);
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setPadding(new Insets(25, 25, 25, 25));

		gp.add(nameLabel, 0, 0);
		gp.add(nameTextField, 0, 1);
		gp.add(emailLabel, 0, 2);
		gp.add(emailField, 0, 3);
		gp.add(passwordLabel, 0, 4);
		gp.add(passwordField, 0, 5);
		gp.add(genderLabel, 0, 6);
		gp.add(fp, 0, 7);

		fp.getChildren().addAll(rbm, rbf);
		fp.setHgap(10);

		vbox.getChildren().addAll(registerButton, space, hyperl);

		vbox.setAlignment(javafx.geometry.Pos.CENTER);
	}

	public boolean checkPassword(String password) {
		boolean isNumeric = false;
		boolean isAlphabetic = false;

		for (int i = 0; i < password.length(); i++) {
			if (Character.isAlphabetic(password.charAt(i))) {
				isAlphabetic = true;
			}
			if (Character.isDigit(password.charAt(i))) {
				isNumeric = true;
			}
		}
		return isNumeric && isAlphabetic;
	}

	public boolean checkUsername(String username) {
		boolean cek = false;

		try (Connection connection = DatabaseManager.getConnection()) {

			String query = "SELECT * FROM msuser WHERE username = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				cek = true;
			}

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cek;
	}

	public boolean checkEmail(String useremail) {
		boolean cek = false;

		try (Connection connection = DatabaseManager.getConnection()) {

			String query = "SELECT * FROM msuser WHERE useremail = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, useremail);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				cek = true;
			}

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cek;
	}

	public void Action(Stage primaryStage) {

		registerButton.setOnAction(e -> {

			String username = nameTextField.getText();
			String email = emailField.getText();
			String password = passwordField.getText();
			String role;
			String gender = "";

			if (rbm.isSelected() == true) {
				gender = "Male";
			} else if (rbf.isSelected() == true) {
				gender = "Female";
			}

			boolean cek = true;

			if (username.isEmpty()) {
				cek = false;
				showErrorAlert("Please fill out your username");
			} else if (checkUsername(username)) {
				cek = false;
				showErrorAlert("Please choose a different username");
			} else if (email.isEmpty()) {
				cek = false;
				showErrorAlert("Please fill out your email");
			} else if (!email.endsWith("@gmail.com")) {
				cek = false;
				showErrorAlert("Make sure your email ends with @gmail.com ");
			} else if (checkEmail(email)) {
				cek = false;
				showErrorAlert("Please choose a different email");
			} else if (password.isEmpty()) {
				cek = false;
				showErrorAlert("Please fill out your password");
			} else if (!(password.length() >= 8 && password.length() <= 15)) {
				cek = false;
				showErrorAlert("Make sure your password has a length of 8 - 15 characters");
			} else if (checkPassword(password) != true) {
				cek = false;
				showErrorAlert("Password must be alphanumeric");
			}else if (tg.getSelectedToggle()==null) {
				cek = false;
				showErrorAlert("Please choose a gender");
			}

			if (username.toLowerCase().contains("admin")) {
				role = "Admin";
			} else {
				role = "User";
			}

			if (cek) {
				DatabaseManager db = new DatabaseManager();
				String id = db.generateUserId().toString();
				db.saveUserToDatabase(id, username, email, password, gender, role);

				try {
					openLoginPage(primaryStage);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});

		hyperl.setOnAction(e -> {
			try {
				openLoginPage(primaryStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});

	}
	
	private void openLoginPage(Stage primaryStage) throws Exception {
		Login login = new Login();
		login.start(primaryStage);
	}

	@Override
	public void start(Stage primaryStage) {
		init();
		layouting();
		Action(primaryStage);

		primaryStage.setTitle("CangkIR");
		scene = new Scene(bp, 700, 500);
		primaryStage.setScene(scene);

		primaryStage.show();
	}

	
	private void showErrorAlert(String errorMessage) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Register Error");
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}
}
