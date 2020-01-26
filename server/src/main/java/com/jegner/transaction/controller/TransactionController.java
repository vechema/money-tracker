package com.jegner.transaction.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
		String date = createRandomDate(2015, 2019);
		String location = getRandomLocation();
		String amount = String.format("%.2f", createRandomDoubleBetween(-10000, 10000) / 100);

		return Transaction.builder().setDate(date).setLocation(location).setAmount(amount).build();
	}

	private static double createRandomDoubleBetween(int start, int end) {
		return start + Math.round(Math.random() * (end - start));
	}

	private static final String[] LOCATIONS = { "HEB", "Walmart", "Amazon", "Home Depot", "Shell", "Joann Fabric",
			"PayPal" };

	private static String getRandomLocation() {
		int index = (int) createRandomDoubleBetween(0, LOCATIONS.length - 1);

		return LOCATIONS[index];
	}

	private static String createRandomDate(int startYear, int endYear) {
		int day = (int) createRandomDoubleBetween(1, 28);
		int month = (int) createRandomDoubleBetween(1, 12);
		int year = (int) createRandomDoubleBetween(startYear, endYear);
		LocalDate date = LocalDate.of(year, month, day);

		String dateFormat = "MM/dd/yyyy";
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat);

		return dateFormatter.format(date);
	}
}
