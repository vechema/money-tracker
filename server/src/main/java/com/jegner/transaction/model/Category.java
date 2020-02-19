package com.jegner.transaction.model;

import java.util.Arrays;

public enum Category {
	// expenses
	MORTGAGE(new String[] { "direct debit jpmorgan chasechase ach" }),
	BILLS(new String[] { "city of austin", "gas texas", "avail rent", "att*bill" }),
	GROCERY(new String[] { "supermarkets", "groceries", "h-e-b #", "central market", "mt supermarket" }),
	RESTAURANT(
			new String[] { "restaurants", "ice crea", "phils ice", "taco", "ramen", "saam thai", "threadgills",
					"happy chicks", "noodle", "fruitealicious", "eurest arm", "wendy's", "pizza", "whataburger",
					"barbecue" }),
	TRANSPORTATION(new String[] { "gasoline", ",gas,", "h-e-b gas", "fuel", "2222 market", "shell oil", "chevron" }),
	HEALTH(new String[] { "pharmacy" }),
	HOBBY(new String[] { " sewing", "joann" }),
	HOUSEHOLD(new String[] { "home depot", "new woolie", "wal-mart", "restarant sup" }),
	PERSONAL(new String[] { "etronics", "swappa", "forlessfones" }),
	ENTERTAINMENT(new String[] { "valve corp", "hideout theatre", "nintendo", "steam games", "novel escape", }),
	VACATION(new String[] { "emirates" }),
	GIFT(new String[] {}),
	PET(new String[] { "petco" }),
	AMAZON(new String[] { "amazon.com", "amzn" }),
	// income
	SALARY(new String[] { "pay data", "pay ddirect", "inc direct dep", "tedirect dep" }),
	BONUS(new String[] {}),
	DIVIDENDS(new String[] { "dividend received" }),
	INTEREST(new String[] { "interest earned" }),
	INVESTMENT(new String[] { "reinvestment", "roth ira" }),
	// other
	TRANSFER(new String[] { "transfer", "fid bkg svc llc", "general credit card deposit", "general withdrawal" }),
	PAYMENT(new String[] { "automatic payment", "autopay", "pay full balance", "e-payment" }),
	CASHBACK(new String[] { "redemption credit", "cashback" }),
	MISCELLANEOUS(new String[] {});

	private String[] descriptions;

	private Category(String[] descriptions) {
		this.descriptions = descriptions;
	}

	public static Category determineCategory(String line) {
		for (Category category : Category.values()) {
			if (Arrays.stream(category.descriptions).parallel().anyMatch(line.toLowerCase()::contains)) {
				return category;
			}
		}
		return MISCELLANEOUS;
	}
}
