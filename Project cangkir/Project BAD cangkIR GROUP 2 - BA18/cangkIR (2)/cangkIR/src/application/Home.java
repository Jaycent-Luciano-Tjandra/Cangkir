package application;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.DatabaseManager;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.CupList;
import model.User;

public class Home extends Application {

	private MenuBar menubar;
	private Menu menu;
	private MenuItem menu1;
	private MenuItem menu2;
	private MenuItem menu3;
	private Scene scene;
	private Label title;
	private Label cupName;
	private Label cupPrice;
	private BorderPane bp, bp1;
	private GridPane gp, gp1;
	private FlowPane fp;
	private Spinner<Integer> spinner;
	private ObservableList<CupList> dataCupList;
	private TableView<CupList> tableView;
	private TableColumn<CupList, String> ccup;
	private TableColumn<CupList, Integer> cprice;
	private Button addButton;
	private User currentUser;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public void init() {
		menubar = new MenuBar();
		menu = new Menu("Menu");
		menu1 = new MenuItem("Home");
		menu2 = new MenuItem("Cart");
		menu3 = new MenuItem("Log Out");
		bp = new BorderPane();
		bp1 = new BorderPane();
		fp = new FlowPane();
		gp = new GridPane();
		gp1 = new GridPane();

		spinner = new Spinner<>(1, 20, 0, 1);
		tableView = new TableView();
		ccup = new TableColumn<CupList, String>("Cup Name");
		ccup.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCupName()));
		ccup.setPrefWidth(175);

		cprice = new TableColumn<CupList, Integer>("Cup Price");
		cprice.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCupPrice()).asObject());
		tableView.getColumns().addAll(ccup, cprice);
		tableView.setPrefSize(350, 365);
		cprice.setPrefWidth(175);

		dataCupList = FXCollections.observableArrayList();
		tableView.setItems(dataCupList);

		title = new Label("Cup List");
		title.setStyle("-fx-font-size: 20; -fx-font-weight: bold; ");

		cupName = new Label("Cup Name");
		cupName.setStyle("-fx-font-size: 20; -fx-font-weight: bold; ");

		cupPrice = new Label("Price");
		cupPrice.setStyle("-fx-font-size: 20; -fx-font-weight: bold; ");

		addButton = new Button("Add to Cart");
		addButton.setPrefSize(130, 40);

	}

	public void setPos() {

		bp.setTop(menubar);
		bp.setCenter(bp1);
		menubar.getMenus().addAll(menu);
		menu.getItems().addAll(menu1, menu2, menu3);
		gp.add(cupName, 0, 0);
		gp.add(spinner, 0, 1);
		gp.add(cupPrice, 0, 2);
		gp.add(addButton, 0, 3);
		gp.setVgap(20);
		gp.setPadding(new Insets(90, 10, 10, 10));
		gp1.add(title, 0, 0);
		gp1.add(tableView, 0, 1);
		fp.getChildren().addAll(gp1, gp);
		fp.setAlignment(Pos.BOTTOM_LEFT);
		bp1.setCenter(fp);
		bp1.setPadding(new Insets(0, 0, 10, 10));

	}

	private void getCupList() {

		try (Connection connection = DatabaseManager.getConnection()) {
			String query = "SELECT * FROM mscup ";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				String cupId = resultSet.getString("cupid");
				String cupName = resultSet.getString("cupname");
				int cupPrice = resultSet.getInt("cupprice");

				CupList cupList = new CupList(cupId, cupName, cupPrice);
				dataCupList.add(cupList);

			}

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean cekCart(String cupid, String userid) {
		try (Connection connection = DatabaseManager.getConnection()) {
			String selectQuery = "SELECT * FROM cart WHERE cupid = ? AND userid = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
				preparedStatement.setString(1, cupid);
				preparedStatement.setString(2, userid);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {

					return resultSet.next();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Integer getCupQuantityCart(String userid, String cupid) {
		String query = "SELECT * FROM cart WHERE userid = ? AND cupid = ?";
		try (Connection connection = DatabaseManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, userid);
			preparedStatement.setString(2, cupid);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt("quantity");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void Action(Stage primaryStage) {

		tableView.setOnMouseClicked(e -> {
			Integer quantity = spinner.getValue();

			CupList selectedCup = tableView.getSelectionModel().getSelectedItem();
			cupName.setText(selectedCup.getCupName().toString());
			cupPrice.setText(String.valueOf(selectedCup.getCupPrice() * quantity));

		});
		
		 spinner.valueProperty().addListener((observable,  oldValue,  newValue) -> {
			 Integer quantity = spinner.getValue();

				CupList selectedCup = tableView.getSelectionModel().getSelectedItem();
			 cupName.setText(selectedCup.getCupName().toString());
			 cupPrice.setText(String.valueOf(selectedCup.getCupPrice() * quantity));
			 
		 });
		 
		addButton.setOnAction(e -> {
			Integer quantity = spinner.getValue();

			CupList selectedCup = tableView.getSelectionModel().getSelectedItem();
			User user = new User();

			if (selectedCup != null) {

				String userid = user.getUserID();

				DatabaseManager db = new DatabaseManager();

				if (cekCart(selectedCup.getCupID(), userid)) {
					Integer jumlah = getCupQuantityCart(userid, selectedCup.getCupID());
					db.updateQuantity(selectedCup.getCupID(), (jumlah + quantity));

				} else {
					db.insertCup(userid, selectedCup.getCupID(), quantity);
				}

				showSuccessAlert("Item successfully inserted the item !");
				spinner.getValueFactory().setValue(1);
			} else {
				showErrorAlert("Please select a cup to be added");
			}

		});

		menu2.setOnAction(e -> {

			try {
				openMenuCart(primaryStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});

		menu3.setOnAction(e -> {

			try {
				openLoginPage(primaryStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		init();
		setPos();
		getCupList();
		Action(primaryStage);

		primaryStage.setTitle("cangkIR");
		scene = new Scene(bp, 700, 550);
		primaryStage.setScene(scene);

		primaryStage.show();

	}

	private void openMenuCart(Stage primaryStage) throws Exception {
		MenuCart menuCart = new MenuCart();
		menuCart.start(primaryStage);
	}

	private void openLoginPage(Stage primaryStage) throws Exception {
		Login login = new Login();
		login.start(primaryStage);
	}

	private void showSuccessAlert(String successMessage) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Message");
		alert.setHeaderText("Cart Info");
		alert.setContentText(successMessage);
		alert.showAndWait();
	}

	private void showErrorAlert(String errorMessage) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Cart Error");
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}

}
