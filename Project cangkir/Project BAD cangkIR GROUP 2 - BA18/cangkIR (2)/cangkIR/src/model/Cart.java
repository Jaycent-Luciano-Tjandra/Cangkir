package model;

public class Cart {

	private String userID;
	private String cupID;
	private Integer quantity;
	
	public Cart(String userID, String cupID, Integer quantity) {
		super();
		this.userID = userID;
		this.cupID = cupID;
		this.quantity = quantity;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getCupID() {
		return cupID;
	}

	public void setCupID(String cupID) {
		this.cupID = cupID;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
	
}
