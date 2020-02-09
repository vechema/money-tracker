package com.jegner.transaction.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jegner.transaction.model.Transaction;

@RestController
@RequestMapping("/transaction")
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

	@GetMapping("")
	public List<Transaction> getAllTransactions() {

		List<Transaction> result = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			result.add(makeRandomTransaction());
		}

		return result;
	}

	private static Transaction makeRandomTransaction() {
		String date = createRandomDate(2015, 2021);
		String location = getRandomLocation();
		int amount = createRandomIntBetween(-10000, 10000);

		return Transaction.builder().setDate(date).setLocation(location).setAmount(amount).build();
	}

	private static int createRandomIntBetween(int start, int end) {
		return (int) (start + Math.round(Math.random() * (end - start)));
	}

	private static final String[] LOCATIONS = { "HEB", "Walmart", "Amazon", "Home Depot", "Shell", "Joann Fabric",
			"PayPal" };

	private static String getRandomLocation() {
		int index = (int) createRandomIntBetween(0, LOCATIONS.length - 1);

		return LOCATIONS[index];
	}

	private static String createRandomDate(int startYear, int endYear) {
		int day = createRandomIntBetween(1, 28);
		int month = createRandomIntBetween(1, 12);
		int year = createRandomIntBetween(startYear, endYear);
		LocalDate date = LocalDate.of(year, month, day);

		return date.toString();
	}
}
