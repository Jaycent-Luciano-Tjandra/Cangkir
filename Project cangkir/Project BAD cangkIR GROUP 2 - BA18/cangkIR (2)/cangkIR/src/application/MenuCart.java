package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Vector;

import database.DatabaseManager;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;
import model.Cart;
import model.Courier;
import model.CupList;
import model.User;
import model.UserCart;

public class MenuCart extends Application {

	private MenuBar menubar;
	private Menu menu;
	private MenuItem menu1;
	private MenuItem menu2;
	private MenuItem menu3;
	private BorderPane bp, bp1, bp2, bp3;
	private GridPane gp, gp1, gp2, gp3;
	private FlowPane fp;
	private Label list, delete, courier, price, total, confirm;
	private String name;
	private Button delBut, coBut, yesBut, noBut;
	private CheckBox checkbox;
	private ComboBox<String> combobox;
	private TableView<UserCart> tableview;
	private TableColumn<UserCart, String> cupName;
	private TableColumn<UserCart, Integer> cupPrice, quantity, totalColumn;
	private Window window;
	private ObservableList<UserCart> dataCupList;
	private LocalDate currentDate;
	Courier courier1;
	private Vector<String> listCupId;
	private Vector<Integer> listQuantity;
	private Stage popupStage;

	private void initialize() {
		User user = new User();
		menubar = new MenuBar();
		menu = new Menu("Menu");
		menu1 = new MenuItem("Home");
		menu2 = new MenuItem("Cart");
		menu3 = new MenuItem("Log Out");
		bp = new BorderPane();
		bp1 = new BorderPane();
		bp2 = new BorderPane();
		bp3 = new BorderPane();
		fp = new FlowPane();
		gp = new GridPane();
		gp1 = new GridPane();
		gp2 = new GridPane();
		gp3 = new GridPane();
		name = user.getUsername();
		list = new Label(name + "'s Cart");
		list.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
		delete = new Label("Delete Item");
		delete.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
		courier = new Label("Courier");
		courier.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
		price = new Label("Courier price: ");
		price.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
		total = new Label("Total Price: ");
		total.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
		delBut = new Button("Delete Item");
		delBut.setPrefSize(100, 35);
		coBut = new Button("Checkout");
		coBut.setPrefSize(100, 35);
		checkbox = new CheckBox("Use Delivery Insurance");
		combobox = new ComboBox<>();
		combobox.getItems().addAll("JNA", "TAKA", "LoinParcel", "IRX", "JINJA");
		tableview = new TableView<>();
		tableview.setPrefSize(350, 365);
		confirm = new Label("Are you sure want to purchase?");
		confirm.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
		yesBut = new Button("Yes");
		yesBut.setPrefSize(70, 30);
		noBut = new Button("No");
		noBut.setPrefSize(70, 30);
		popupStage = new Stage();

		dataCupList = FXCollections.observableArrayList();
		cupName = new TableColumn<UserCart, String>("Cup Name");
		cupName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCupName()));
		cupName.setPrefWidth(75);
		cupPrice = new TableColumn<UserCart, Integer>("Cup Price");
		cupPrice.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getCupPrice()).asObject());
		cupPrice.setPrefWidth(75);
		quantity = new TableColumn<UserCart, Integer>("Quantity");
		quantity.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
		quantity.setPrefWidth(75);
		totalColumn = new TableColumn<UserCart, Integer>("Total");
		totalColumn
				.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTotal()).asObject());
		totalColumn.setPrefWidth(75);
		tableview.getColumns().addAll(cupName, cupPrice, quantity, totalColumn);
		tableview.setItems(dataCupList);

		currentDate = LocalDate.now();
		listCupId = new Vector<>();
		listQuantity = new Vector<>();
	}

	private void layout() {
		bp.setTop(menubar);
		bp.setCenter(bp1);
		menubar.getMenus().addAll(menu);
		menu.getItems().addAll(menu1, menu2, menu3);
		gp.add(delete, 0, 0);
		gp.add(delBut, 0, 1);
		gp.add(courier, 0, 2);
		gp.add(combobox, 0, 3);
		gp.add(price, 0, 4);
		gp.add(checkbox, 0, 5);
		gp.add(total, 0, 6);
		gp.add(coBut, 0, 7);
		gp.setVgap(20);
		gp.setPadding(new Insets(10, 10, 10, 10));
		gp1.add(list, 0, 0);
		gp1.add(tableview, 0, 1);
		fp.getChildren().addAll(gp1, gp);
		fp.setAlignment(Pos.BOTTOM_LEFT);
		bp1.setCenter(fp);
		bp1.setPadding(new Insets(0, 0, 10, 10));
	}

	private void confirmation() {
		gp3.add(yesBut, 0, 0);
		gp3.add(noBut, 1, 0);
		gp3.setHgap(25);
		gp3.setPadding(new Insets(0, 30, 0, 30));
		gp2.add(confirm, 0, 0);
		gp2.add(gp3, 0, 1);
		gp2.setVgap(20);
		bp2.setCenter(gp2);
		gp2.setAlignment(Pos.CENTER);

		window = new Window("Checkout confirmation");
		window.getContentPane().getChildren().addAll(bp2);
		bp3.setCenter(window);

		Scene scene = new Scene(bp3, 700, 550);
		popupStage.setScene(scene);

	}

	public static void main(String[] args) {
		launch(args);
	}

	private Integer count() {

		Integer countTotal = 0;
		for (UserCart userCart : dataCupList) {
			if (userCart.getTotal() != null) {

				countTotal = countTotal + userCart.getTotal();

			}
		}
		return countTotal;
	}

	private Integer cekCekbox() {
		int cek = 0;
		if (checkbox.isSelected() == true) {
			cek = 1;
		} else {
			cek = 0;
		}
		return cek;
	}

	private void kalkulasi() {
		DatabaseManager db = new DatabaseManager();
		String selectedItem = combobox.getValue();
		
		Integer courierPrice, courierTotal;
		courierPrice = Integer.parseInt(db.getCourierPrice(selectedItem));

		price.setText("Courier Price: " + courierPrice.toString());

		courierTotal = courierPrice;

		Integer hitung = count();

		if (checkbox.isSelected()) {
			courierTotal += 2000;
		}

		courierTotal += hitung;

		total.setText("Total Price: " + courierTotal.toString());
	}

	public void Action(Stage primaryStage) {

		menu1.setOnAction(e -> {

			try {
				openHomePage(primaryStage);
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

		delBut.setOnAction(e -> {

			
			try {
				UserCart selectedCart = tableview.getSelectionModel().getSelectedItem();
				User user = new User();
				String userid = user.getUserID();
				DatabaseManager db = new DatabaseManager();

				if (selectedCart != null) {
					db.deleteCartItem(selectedCart.getCupID(), userid, selectedCart.getQuantity());

					tableview.getItems().remove(selectedCart);
					tableview.refresh();
					kalkulasi();
					
					showSuccessAlert("Cart Deleted Successfully");
				} else {
					showErrorAlert("Please select the item you want to delete");
				}
			} catch (Exception e2) {
				showSuccessAlert("Cart Deleted Successfully");
			}
			

		});

		combobox.setOnAction(e -> {

			kalkulasi();

		});

		checkbox.setOnAction(e -> {

			try {
				kalkulasi();
			} catch (Exception e2) {
				// TODO: handle exception
			}

		});

		coBut.setOnAction(e -> {

			if (tableview.getItems().size() == 0) {
				showErrorCart("Cart cannot be empty");
			} else if (combobox.getSelectionModel().isEmpty()) {
				showErrorCart("Please select the courier");
			}else {
				popupStage.showAndWait();
			}

		});

		yesBut.setOnAction(e -> {

			getCourierId();
			Integer insurance = cekCekbox();
			User user = new User();

			DatabaseManager db = new DatabaseManager();
			String transactionid = db.generateTransactionId().toString();
			db.addTransaction(transactionid, user.getUserID(), courier1.getCourierId(), currentDate.toString(),
					insurance);

			getCup();

			for (int i = 0; i < listCupId.size(); i++) {
				db.addTransactionDetail(transactionid, listCupId.get(i), listQuantity.get(i));
				db.deleteCartItem(listCupId.get(i), user.getUserID(), listQuantity.get(i));
			}

			dataCupList.clear();
			listCupId.clear();
			listQuantity.clear();
			popupStage.close();
			showInfoAlert("Checkout successful");
		});

		noBut.setOnAction(e -> {

			popupStage.close();

		});

	}

	private void getCup() {
		String cupid;
		Integer quantityCup;
		for (UserCart userCart : dataCupList) {
			if (userCart.getCupID() != null) {
				cupid = userCart.getCupID();
				quantityCup = userCart.getQuantity();
				listCupId.add(cupid);
				listQuantity.add(quantityCup);
			}
		}
	}

	private void getCourierId() {

		String name = combobox.getValue();
		try (Connection connection = DatabaseManager.getConnection()) {
			String query = "SELECT * FROM mscourier WHERE couriername = " + "'" + name + "'";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				String courierId = resultSet.getString("courierid");
				String courierName = resultSet.getString("couriername");
				int courierPrice = resultSet.getInt("courierprice");

				courier1 = new Courier(courierId, courierName, courierPrice);

			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void getCartList() {
		User user = new User();
		String id = user.getUserID();
		try (Connection connection = DatabaseManager.getConnection()) {
			String query = "SELECT * FROM cart JOIN mscup ON mscup.cupid = cart.cupid WHERE userid = " + "'" + id + "'";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				String userId = resultSet.getString("userId");
				String cupId = resultSet.getString("cupid");
				int quantity = resultSet.getInt("quantity");
				String cupName = resultSet.getString("cupname");
				int cupPrice = resultSet.getInt("cupprice");

				UserCart userCart = new UserCart(cupId, userId, cupName, cupPrice, quantity, (cupPrice * quantity));
				dataCupList.add(userCart);

			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		initialize();
		layout();
		confirmation();
		getCartList();
		Action(primaryStage);
		Scene scene = new Scene(bp, 700, 550);
		primaryStage.setTitle("cangkIR");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void openHomePage(Stage primaryStage) throws Exception {
		Home home = new Home();
		home.start(primaryStage);

	}

	private void openLoginPage(Stage primaryStage) throws Exception {
		Login login = new Login();
		login.start(primaryStage);
	}

	private void showSuccessAlert(String successMessage) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Message");
		alert.setHeaderText("Deletion Information");
		alert.setContentText(successMessage);
		alert.showAndWait();
	}

	private void showErrorAlert(String errorMessage) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Deletion Error");
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}

	private void showInfoAlert(String infoMessage) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Message");
		alert.setHeaderText("Checkout Information");
		alert.setContentText(infoMessage);
		alert.showAndWait();
	}
	
	private void showErrorCart(String errorMessage) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Cart Error");
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}
	
	

}
