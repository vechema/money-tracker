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

		List<Transaction> transactions = new ArrayList<>();

		File csvDirectory = new File("C:\\Users\\Jo\\Life\\Finance\\");
		File[] csvDirListing = csvDirectory.listFiles((dir, file) -> file.toLowerCase().endsWith(".csv"));
		for (File file : csvDirListing) {
			System.out.println(file.getName());
			transactions.addAll(getTransactions(file));
		}

		return transactions;
		// return transactions.stream().filter(trans ->
		// !trans.getAmount().contains("-")).collect(Collectors.toList());
	}

	private List<Transaction> getTransactions(File transactionFile) {

		List<Transaction> transactions = new ArrayList<>();

		final String source = transactionFile.getName().substring(0, transactionFile.getName().indexOf('_'));
		String line = "";
		String cvsSplitBy = ",";

		try (BufferedReader br = new BufferedReader(new FileReader(transactionFile))) {

			// Get rid of header line
			String[] headers = br.readLine().split(cvsSplitBy);
			while (headers.length == 1) {
				headers = br.readLine().split(cvsSplitBy);
			}

			if (headers[0].endsWith("\"")) {
				cvsSplitBy = "\",\"";
			}

			ArrayList<Integer> amountIndices = new ArrayList<>();
			ArrayList<Integer> locationIndices = new ArrayList<>();
			int dateIndex = -1;
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
					locationIndices.add(i);
				}
			}

			if (locationIndices.size() == 0 || dateIndex == -1 || amountIndices.size() == 0) {
				System.out.println("Didn't find needed index");
			}

			System.out.println("Date index: " + dateIndex);
			System.out.println("Location index: " + locationIndices);
			System.out.println("Amount indices: " + amountIndices);

			while ((line = br.readLine()) != null && !line.isEmpty()) {

				// use comma as separator
				String[] transactionRaw = line.split(cvsSplitBy);
				System.out.println(line + " " + transactionRaw.length);

				// Check to make sure transaction has amount
				if (transactionRaw.length < amountIndices.get(0)) {
					System.out.println("There's possibly no amount in this transaction - weird");
					System.out.println("\t" + line);
					continue;
				}

				if (line.contains("Egner") && line.contains("2,265")) {
					System.out.println();
				}
				for (int i = 0; i < transactionRaw.length; i++) {
					String transField = transactionRaw[i];
					if (transField.startsWith("\"")) {
						transactionRaw[i] = transField.substring(1, transField.length() - 1);
					}

				}

				String date = transactionRaw[dateIndex].trim();
				String location = transactionRaw[locationIndices.get(0)].trim();
				String amount = transactionRaw[amountIndices.get(0)].trim();

				if (amount.isEmpty() && amountIndices.size() > 1) {
					amount = transactionRaw[amountIndices.get(1)].trim();
				}

				if (location.isEmpty() && locationIndices.size() > 1) {
					location = transactionRaw[locationIndices.get(1)].trim();
				}

				if (amount.isEmpty()) {
					System.out.println("Transaction has no amount entry");
					System.out.println("\t" + line);
					continue;
				}

				if (date.isEmpty()) {
					System.out.println("Transaction has no date entry");
					System.out.println("\t" + line);
					continue;
				}

				if (location.isEmpty()) {
					System.out.println("Transaction has no location entry");
					System.out.println("\t" + line);
					continue;
				}

				Transaction transaction = Transaction.builder().setDate(date).setAmount(amount).setLocation(location)
						.setSource(source).build();
				System.out.println("\t" + transaction);
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
		return location.toLowerCase().contains("description") || location.toLowerCase().contains("name")
				|| location.toLowerCase().contains("type");
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
