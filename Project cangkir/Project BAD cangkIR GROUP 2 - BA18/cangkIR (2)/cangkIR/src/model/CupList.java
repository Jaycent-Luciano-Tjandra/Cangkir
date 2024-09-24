package model;

public class CupList {

	private String CupID;
	private String CupName;
	private Integer CupPrice;

	public CupList(String cupID, String cupName, Integer cupPrice) {
		super();
		CupID = cupID;
		CupName = cupName;
		CupPrice = cupPrice;
	}

	public  String getCupID() {
		return CupID;
	}

	public void setCupID(String cupID) {
		CupID = cupID;
	}

	public String getCupName() {
		return CupName;
	}

	public void setCupName(String cupName) {
		CupName = cupName;
	}

	public Integer getCupPrice() {
		return CupPrice;
	}

	public void setCupPrice(Integer cupPrice) {
		CupPrice = cupPrice;
	}

}