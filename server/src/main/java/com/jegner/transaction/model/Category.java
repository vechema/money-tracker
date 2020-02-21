package com.jegner.transaction.model;

import java.util.Arrays;

public enum Category {
	// expenses
	MORTGAGE(new String[] { "direct debit jpmorgan chasechase ach" }),
	BILLS(new String[] { "city of austin", "gas texas", "avail rent", "att*bill", "grande communi" }),
	GROCERY(
			new String[] { "supermarkets", "groceries", "h-e-b #", "central market", "mt supermarket", "farmers mark",
					"99 ranch", "bazar" }),
	RESTAURANT(
			new String[] { "restaurants", "ice crea", "phils ice", "taco", "ramen", " thai", "threadgills",
					"happy chicks", "noodle", "fruitealicious", "eurest arm", "wendy's", "pizza", "whataburger",
					"barbecue", "grill", "wok'n", "star of india", "be more pacific", "sq *", "dirty martins", "fooda",
					"poke-poke", "burger", "snowy willage", "waffle", "oyster", "hoovers", "hunan lion",
					"snowy village", "sushi", "chuy's", "arbys", "trudys", "tous les jours", "serranos", "eatery",
					"bistro", "cheddar" }),
	TRANSPORTATION(
			new String[] { "gasoline", ",gas,", "h-e-b gas", "fuel", "2222 market", "shell oil", "chevron", "u-haul",
					"parking", "wheels", "vehreg", "automotive", "oreilly" }),
	HEALTH(new String[] { "pharmacy", "bodyspec" }),
	HOBBY(new String[] { " sewing", "joann", "fry's" }),
	HOUSE(new String[] { "plumbing", "home warran", "housecheck", "service wizard" }),
	HOUSEHOLD(new String[] { "home depot", "new woolie", "wal-mart", "restaurant sup", "ikea", "wm supercent" }),
	PERSONAL(new String[] { "etronics", "swappa", "forlessfone" }),
	ENTERTAINMENT(new String[] { "valve corp", "the hideout", "nintendo", "steam games", "novel escape", "sherwood" }),
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
	CASHBACK(new String[] { "redemption credit", "cashback", "cash rewards" }),
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
