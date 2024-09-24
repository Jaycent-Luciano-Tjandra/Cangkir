package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import model.Cart;
import model.User;
import model.UserCart;

public class DatabaseManager {

	private static Connection connect;
	private Statement state;
	private static DatabaseManager dbManager;
	private static final String HOST = "localhost:3306";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "";
	private static final String DATABASE = "cangkir";
	private static final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
	}

	private int getUserIndex() {
		int userIndex = 0;
		try (Connection connection = DatabaseManager.getConnection()) {
			String query = "SELECT MAX(CAST(SUBSTRING(UserId, 3) AS UNSIGNED)) FROM msuser WHERE userID LIKE 'US%'";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						userIndex = resultSet.getInt(1);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userIndex;
	}

	public String generateUserId() {
		int userIndex = getUserIndex();
		userIndex++; 
		String userId = String.format("US%03d", userIndex);

		return userId;
	}

	public void saveUserToDatabase(String userId, String username, String userEmail, String userPassword,
			String userGender, String userRole) {
		try (Connection connection = DatabaseManager.getConnection()) {
			String query = "INSERT INTO msuser (userid, username, useremail, userpassword, usergender, userrole) VALUES (?, ?, ?, ?, ?, ?)";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

				preparedStatement.setString(1, userId);
				preparedStatement.setString(2, username);
				preparedStatement.setString(3, userEmail);
				preparedStatement.setString(4, userPassword);
				preparedStatement.setString(5, userGender);
				preparedStatement.setString(6, userRole);
				preparedStatement.executeUpdate();
			}
			System.out.println("User registered successfully!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertCup(String userId, String cupId, Integer quantity) {
		try (Connection connection = DatabaseManager.getConnection()) {
			String query = "INSERT INTO cart (userid, cupid, quantity) VALUES (?, ?, ?)";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, userId);
				preparedStatement.setString(2, cupId);
				preparedStatement.setInt(3, quantity);
				preparedStatement.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String getIdUser(String userid) {
		String query = "SELECT userid FROM msuser WHERE username = ?";
		try (Connection connection = DatabaseManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, userid);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString("userid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userid;
	}

	public static String getUsername(String username) {
		String query = "SELECT username FROM msuser WHERE username = ?";
		try (Connection connection = DatabaseManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, username);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString("username");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return username;
	}

	public static String getCourierPrice(String couriername) {
		String query = "SELECT courierprice FROM mscourier WHERE couriername = ?";
		try (Connection connection = DatabaseManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, couriername);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString("courierprice");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return couriername;
	}

	public void updateQuantity(String cupid, int quantity) {
		try (Connection connection = DatabaseManager.getConnection()) {
			String query = "UPDATE cart SET quantity = ? WHERE cupid = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setInt(1, quantity);
				preparedStatement.setString(2, cupid);

				preparedStatement.executeUpdate();

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeCup(String cupId) {
		try (Connection connection = DatabaseManager.getConnection()) {
			String query = "DELETE FROM mscup WHERE CupID LIKE ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, cupId);
				preparedStatement.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateCup(Integer cupPrice, String cupId) {
		try (Connection connection = DatabaseManager.getConnection()) {
			String query = "UPDATE mscup SET cupprice = ? WHERE CupID = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setInt(1, cupPrice);
				preparedStatement.setString(2, cupId);
				preparedStatement.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addCup(String cupId, String cupName, Integer cupPrice) {
		try (Connection connection = DatabaseManager.getConnection()) {
			String query = "INSERT INTO mscup (cupid, cupname, cupprice) VALUES (?, ?, ?)";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, cupId);
				preparedStatement.setString(2, cupName);
				preparedStatement.setInt(3, cupPrice);
				preparedStatement.executeUpdate();
			}
			System.out.println("Cup registered successfully!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteCartItem(String cupid, String userid, int quantity) {
		String query = "DELETE FROM cart WHERE cupid = ? AND userid = ? AND quantity = ?";

		try (Connection connection = DatabaseManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, cupid);
			preparedStatement.setString(2, userid);
			preparedStatement.setInt(3, quantity);

			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private int getCupIndex() {
		int cupIndex = 0;
		try (Connection connection = DatabaseManager.getConnection()) {
			String query = "SELECT MAX(CAST(SUBSTRING(CupID, 3) AS UNSIGNED)) FROM mscup WHERE CupID LIKE 'CU%'";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						cupIndex = resultSet.getInt(1);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cupIndex;
	}

	public String generateCupId() {
		int cupIndex = getCupIndex();
		cupIndex++;

		String cupId = String.format("CU%03d", cupIndex);

		return cupId;
	}

	private int getTransactionIndex() {
		int transactionIndex = 0;
		try (Connection connection = DatabaseManager.getConnection()) {
			String query = "SELECT MAX(CAST(SUBSTRING(TransactionID, 3) AS UNSIGNED)) FROM TransactionHeader WHERE TransactionID LIKE 'TR%'";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						transactionIndex = resultSet.getInt(1);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return transactionIndex;
	}

	public String generateTransactionId() {
		int transactionIndex = getTransactionIndex();
		transactionIndex++;

		String transactionId = String.format("TR%03d", transactionIndex);

		return transactionId;
	}

	public void addTransaction(String transactionId, String userId, String courierId, String transactionDate,
			Integer useDeliveryInsurance) {
		try (Connection connection = DatabaseManager.getConnection()) {
			String query = "INSERT INTO transactionheader (transactionid, userid, courierid, transactiondate, usedeliveryinsurance) VALUES (?, ?, ?, ?, ?)";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, transactionId);
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, courierId);
				preparedStatement.setString(4, transactionDate);
				preparedStatement.setInt(5, useDeliveryInsurance);
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addTransactionDetail(String transactionId, String cupId, Integer quantity) {
		try (Connection connection = DatabaseManager.getConnection()) {
			String query = "INSERT INTO transactiondetail (transactionid, cupid, quantity) VALUES (?, ?, ?)";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, transactionId);
				preparedStatement.setString(2, cupId);
				preparedStatement.setInt(3, quantity);

				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	

}