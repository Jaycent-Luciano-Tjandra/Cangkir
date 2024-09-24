package application;


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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.CupList;
import model.User;

public class HomeAdmin extends Application {
	
	private MenuBar menubar;
	private Menu menu;
	private MenuItem menu1;
	private MenuItem menu2;
	private BorderPane bp;
	private BorderPane bp1;
	private FlowPane fp;
	private GridPane gp;
	private GridPane gp1;
	private GridPane gp2;
	private Label cup;
	private Label price;
	private Label list;
	private TextField priceTF;
	private TextField cupTF;
	private Button add;
	private Button update;
	private Button remove;
	private TableView<CupList> cupList;
	private TableColumn<CupList, String> nameCup;
	private TableColumn<CupList, Integer> priceCup;
	private Integer priceInt;
	private ObservableList<CupList> dataCupList;
	
	
	private void initialize() {
		menubar = new MenuBar();
		menu = new Menu("Menu");
		menu1 = new MenuItem("Cup Management");
		menu2 = new MenuItem("Log Out");
		bp = new BorderPane();
		bp1 = new BorderPane();
		cupList = new TableView<>();
		cupList.setPrefSize(350, 365);
		fp = new FlowPane();
		gp = new GridPane();
		gp1 = new GridPane();
		gp2 = new GridPane();
		cup = new Label("Cup Name");
		price = new Label("Cup Price");
		list = new Label("Cup Management");
		list.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
		cupTF = new TextField();
		cupTF.setPrefWidth(230);
		cupTF.setPromptText("Input cup name here");
		priceTF = new TextField();
		priceTF.setPrefWidth(230);
		priceTF.setPromptText("Input cup price here");
		add = new Button("Add Cup");
		add.setPrefSize(160, 45);
		update = new Button("Update Price");
		update.setPrefSize(160, 45);
		remove = new Button("Remove Cup");
		remove.setPrefSize(160, 45);
		dataCupList = FXCollections.observableArrayList();
		
		nameCup = new TableColumn<CupList, String>("Cup Name");
		nameCup.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCupName()));
		nameCup.setPrefWidth(175);
		priceCup = new TableColumn<CupList, Integer>("Cup Price");
		priceCup.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCupPrice()).asObject());
		priceCup.setPrefWidth(175);
		cupList.getColumns().addAll(nameCup, priceCup);
		dataCupList = FXCollections.observableArrayList();
		cupList.setItems(dataCupList);
		
	}
	
	private void layout() {
		bp.setTop(menubar);
		bp.setCenter(bp1);
		menubar.getMenus().addAll(menu);
		menu.getItems().addAll(menu1, menu2);
		gp2.add(add, 0, 0);
		gp2.add(update, 0, 1);
		gp2.add(remove, 0, 2);
		gp.add(cup, 0, 0);
		gp.add(cupTF, 0, 1);
		gp.add(price, 0, 2);
		gp.add(priceTF, 0, 3);
		gp.add(gp2, 0, 4);
		gp.setVgap(20);
		gp2.setVgap(10);
		gp.setPadding(new Insets(30, 20, 20, 20));
		gp1.add(list, 0, 0);
		gp1.add(cupList, 0, 1);
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

	public void action(Stage primaryStage) {
		
		
		menu2.setOnAction(e -> {
			try {
				openLoginPage(primaryStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		
		cupList.setOnMouseClicked(e->{
			CupList selectedCup = cupList.getSelectionModel().getSelectedItem();
			cupTF.setText(selectedCup.getCupName());
			priceTF.setText(selectedCup.getCupPrice().toString());
		});

		remove.setOnAction(e -> {

			CupList selectedCup = cupList.getSelectionModel().getSelectedItem();

			if (selectedCup != null) {
				
				DatabaseManager db = new DatabaseManager();
				db.removeCup(selectedCup.getCupID());
			    dataCupList.clear();
			    getCupList(); 
			    cupTF.clear();
			    priceTF.clear();
			    showInfoAlert("Cup Successfully Removed");

			}else if (selectedCup == null) {
				showErrorAlert("Please select a cup from the table to be removed");
			}

		});
		
		update.setOnAction(e -> {
			
			try {
				CupList selectedCup = cupList.getSelectionModel().getSelectedItem();

				if (selectedCup != null) {
					
					if(Integer.valueOf(priceTF.getText())<5000||Integer.valueOf(priceTF.getText())>1000000) {
						showErrorAlert("Cup price must be 5000-1000000");
					}else {
						DatabaseManager db = new DatabaseManager();
						db.updateCup(Integer.valueOf(priceTF.getText()), selectedCup.getCupID());
						dataCupList.clear();
						getCupList(); 	
					    cupTF.clear();
					    priceTF.clear();
						showInfoAlert("Cup Successfully Updated");
					}
				}else if (selectedCup == null) {
					showErrorAlert("Please select a cup from the table to be updated");
				}

				
			} catch (Exception e2) {
				showErrorAlert("Please fill out the cup price");
			}

		});
		
		add.setOnAction(e -> {
				
			try {
				Boolean cek = true;
				if (cupTF.getText().isEmpty()==true) {
					showErrorAlert("Please fill out the cup name");
					cek = false;
				}else if (checkCupName(cupTF.getText())) {
					showErrorAlert("Cup Already Exists");
					cek = false;
				}else if(priceTF.getText().isEmpty()==true) {
					showErrorAlert("Please fill out the cup price");
					cek = false;
				}else if (Integer.valueOf(priceTF.getText())<5000||Integer.valueOf(priceTF.getText())>1000000) {
					showErrorAlert("Cup price must be 5000-1000000");
					cek = false;
				}
				
				if (cek==true) {
						DatabaseManager db = new DatabaseManager();
						String id = db.generateCupId().toString();
						db.addCup(id, cupTF.getText(),Integer.valueOf(priceTF.getText()));
						dataCupList.clear();
						getCupList();
						cupTF.clear();
						priceTF.clear();
						showInfoAlert("Cup Successfully Added");					
				}
			} catch (Exception e2) {
				showErrorAlert("Please fill out the valid cup price");
			}


		});

	}
	
	public boolean checkCupName(String cupname) {
		boolean cek = false;

		try (Connection connection = DatabaseManager.getConnection()) {

			String query = "SELECT * FROM mscup WHERE cupname = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, cupname);

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
    public static void main(String[] args) {
        launch(args);
    }

    
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		initialize();
		layout();
		action(primaryStage);
		getCupList();
		Scene scene = new Scene (bp, 700, 550);
		primaryStage.setTitle("cangkIR");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void openLoginPage(Stage primaryStage) throws Exception {
		Login login = new Login();
		login.start(primaryStage);
	}
	
	private void showErrorAlert(String errorMessage) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Cup Management");
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}
	
	private void showInfoAlert(String infoMessage) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Message");
		alert.setHeaderText("Cup Management");
		alert.setContentText(infoMessage);
		alert.showAndWait();
	}
}

