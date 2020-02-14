package com.jegner.transaction.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

		return extractCitiTransactions().stream().filter(trans -> !trans.getAmount().contains("-"))
				.collect(Collectors.toList());
	}

	private List<Transaction> extractCitiTransactions() {
		List<Transaction> transactions = new ArrayList<>();

		String citiCsvPath = "C:\\Users\\Jo\\Life\\Finance\\citi_2back_2019.CSV";
		File citiFile = new File(citiCsvPath);
		final String source = citiFile.getName().substring(0, citiFile.getName().indexOf('_'));
		String line = "";
		String cvsSplitBy = ",";

		try (BufferedReader br = new BufferedReader(new FileReader(citiFile))) {

			// Get rid of header line
			String[] headers = br.readLine().split(cvsSplitBy);
			ArrayList<Integer> amountIndices = new ArrayList<>();
			int locationIndex = -1, dateIndex = -1;
			for (int i = 0; i < headers.length; i++) {
				String header = headers[i];
				// Date
				if (isDate(header) && dateIndex == -1) {
					dateIndex = i;
				}
				// Amount
				if (isAmount(header)) {
					amountIndices.add(i);
				}
				// Location
				if (isLocation(header)) {
					locationIndex = i;
				}
			}

			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] transactionRaw = line.split(cvsSplitBy);
				for (int i = 0; i < transactionRaw.length; i++) {
					String transField = transactionRaw[i];
					if (transField.startsWith("\"")) {
						transactionRaw[i] = transField.substring(1, transField.length() - 1);
					}

				}
				String date = transactionRaw[dateIndex];
				String location = transactionRaw[locationIndex];
				String amount = transactionRaw[amountIndices.get(0)];

				if (amount.isEmpty()) {
					amount = transactionRaw[amountIndices.get(1)];
				}

				Transaction transaction = Transaction.builder().setDate(date).setAmount(amount).setLocation(location)
						.setSource(source).build();
				transactions.add(transaction);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return transactions;
	}

	private boolean isDate(String header) {
		return header.toLowerCase().contains("date");
	}

	private boolean isAmount(String amount) {
		return amount.toLowerCase().contains("credit") || amount.toLowerCase().contains("debit")
				|| amount.toLowerCase().contains("amount");
	}

	private boolean isLocation(String location) {
		return location.toLowerCase().contains("description");
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
		String location = getRandomLocation();
		String amount = (((double) createRandomIntBetween(-10000, 10000)) / 100) + "";

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
