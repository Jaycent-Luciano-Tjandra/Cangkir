package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.DatabaseManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.CupList;
import model.User;
import application.Home;

public class Login extends Application {

	private Scene scene;
	private Label title;
	private Label name;
	private Label pass;
	private Label space;
	private TextField namefield;
	private PasswordField passfield;
	private Button button;
	private BorderPane bp;
	private GridPane gp;
	private VBox vbox;
	private Hyperlink hyper;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	private void initialize() {
		title = new Label("Login");
		title.setStyle("-fx-font-size: 32; -fx-font-weight: bold;");
		name = new Label("Username");
		pass = new Label("Password");
		space = new Label("");
		namefield = new TextField();
		namefield.setPromptText("Input your username here");
		passfield = new PasswordField();
		passfield.setPromptText("Input your password here");
		button = new Button("Login");
		button.setPrefSize(100, 40);
		hyper = new Hyperlink("Don't have an account yet? Register here!");
		bp = new BorderPane();
		gp = new GridPane();
		vbox = new VBox();
		scene = new Scene(bp, 700, 500);
	}

	private void layouting() {
		bp.setTop(title);
		bp.setAlignment(title, Pos.CENTER);
		bp.setCenter(gp);
		bp.setBottom(vbox);
		vbox.setAlignment(Pos.CENTER);
		gp.setAlignment(Pos.CENTER);
		gp.add(name, 0, 0);
		gp.add(namefield, 0, 1);
		gp.add(pass, 0, 2);
		gp.add(passfield, 0, 3);
		vbox.getChildren().addAll(button, space, hyper);
		namefield.setMinWidth(400);
		passfield.setMinWidth(400);
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setPadding(new Insets(25, 25, 25, 25));
		bp.setPadding(new Insets(100, 150, 150, 100));
	}

	private boolean validateLogin(String username, String userpassword) {

		try (Connection connection = DatabaseManager.getConnection()) {
			String query = "SELECT * FROM msuser WHERE username=?  AND userpassword = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

				preparedStatement.setString(1, username);
				preparedStatement.setString(2, userpassword);

				ResultSet resultSet = preparedStatement.executeQuery();

				return resultSet.next();

			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String getRole(String username, String password) {
		String role = null;

		try (Connection connection = DatabaseManager.getConnection()) {
			String query = "SELECT userrole FROM msuser WHERE username = ? AND userpassword = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, username);
				preparedStatement.setString(2, password);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						role = resultSet.getString("userrole");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return role;
	}

	private void action(Stage primaryStage) {

		button.setOnAction(e -> {

			String username = namefield.getText();
			String password = passfield.getText();
			String role = getRole(username, password);
			User.loggedUser = username;

			boolean cek = true;

			if (username.isEmpty()) {
				cek = false;
				showErrorAlert("Please fill out your username");
			} else if (password.isEmpty()) {
				cek = false;
				showErrorAlert("Please fill out your password");
			} else if (validateLogin(username, password)) {
				cek = true;

				if (role.contains("User")) {

					try {
						openHomePage(primaryStage);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				} else if (role.contains("Admin")) {

					try {
						openHomeAdmin(primaryStage);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

			} else if (validateLogin(username, password) == false) {
				cek = false;
				showErrorAlert("Username/password incorrect");
			}

		});

		hyper.setOnAction(e -> {
			try {
				openRegisterPage(primaryStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		initialize();
		layouting();
		action(primaryStage);

		primaryStage.setScene(scene);
		primaryStage.setTitle("cangkIR");
		primaryStage.show();

	}

	private void openRegisterPage(Stage primaryStage) throws Exception {

		Register register = new Register();
		register.start(primaryStage);
	}

	private void openHomePage(Stage primaryStage) throws Exception {
		Home home = new Home();
		home.start(primaryStage);

	}

	private void openHomeAdmin(Stage primaryStage) throws Exception {
		HomeAdmin homeAdmin = new HomeAdmin();
		homeAdmin.start(primaryStage);

	}

	private void showErrorAlert(String errorMessage) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Login Error");
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}

}
