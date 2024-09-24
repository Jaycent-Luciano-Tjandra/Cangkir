package model;

public class Courier {

	private String courierId;
	private String courierName ;
	private Integer courierPrice;

	public Courier(String courierId, String courierName, Integer courierPrice) {
		super();
		this.courierId = courierId;
		this.courierName = courierName;
		this.courierPrice = courierPrice;
	}

	public String getCourierId() {
		return courierId;
	}

	public void setCourierId(String courierId) {
		this.courierId = courierId;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public Integer getCourierPrice() {
		return courierPrice;
	}

	public void setCourierPrice(Integer courierPrice) {
		this.courierPrice = courierPrice;
	}

}
