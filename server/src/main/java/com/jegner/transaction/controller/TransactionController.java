package com.jegner.transaction.controller;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jegner.transaction.model.Transaction;
import com.jegner.transaction.model.TransactionFile;

@RestController
@RequestMapping("/transaction")
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

	@GetMapping("")
	public List<Transaction> getAllTransactions() {

		List<Transaction> transactions = new ArrayList<>();

		File csvDirectory = new File("C:\\Users\\Jo\\Life\\Finance\\");
		File[] csvDirListing = csvDirectory.listFiles((dir, file) -> file.toLowerCase().endsWith(".csv"));
		for (File file : csvDirListing) {
			System.out.println(file.getName());
			TransactionFile transFile = new TransactionFile(file);
			transactions.addAll(transFile.getTransactions());
		}

		return transactions;
		// return transactions.stream().filter(trans ->
		// !trans.getAmount().contains("-")).collect(Collectors.toList());
	}

	private static List<Transaction> getTestTransactions() {
		List<Transaction> result = new ArrayList<>();
		int sum = 0;
		for (int i = 0; i < 100; i++) {
			Transaction transaction = makeRandomTransaction();
			result.add(transaction);
			sum += Double.parseDouble(transaction.getAmount()) * 100;
		}
		System.out.println(sum);

		return result;
	}

	private static Transaction makeRandomTransaction() {
		String date = createRandomDate(2015, 2021);
		String description = getRandomDescription();
		String amount = (((double) createRandomIntBetween(-10000, 10000)) / 100) + "";

		return Transaction.builder().setDate(date).setDescription(description).setAmount(amount).build();
	}

	private static int createRandomIntBetween(int start, int end) {
		return (int) (start + Math.round(Math.random() * (end - start)));
	}

	private static final String[] DESCRIPTIONS = { "HEB", "Walmart", "Amazon", "Home Depot", "Shell", "Joann Fabric",
			"PayPal" };

	private static String getRandomDescription() {
		int index = (int) createRandomIntBetween(0, DESCRIPTIONS.length - 1);

		return DESCRIPTIONS[index];
	}

	private static String createRandomDate(int startYear, int endYear) {
		int day = createRandomIntBetween(1, 28);
		int month = createRandomIntBetween(1, 12);
		int year = createRandomIntBetween(startYear, endYear);
		LocalDate date = LocalDate.of(year, month, day);

		return date.toString();
	}
}
