package com.jegner.transaction.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Debit will be negative (money spent - HEB, paying a bill)
 * Credit will be positive (money received - salary, payment of credit card on credit card statement)
 * @author Jo
 *
 */
@Data
public class TransactionFile {
	private final File file;
	private List<Transaction> transactions;
	
	public TransactionFile(File file) {
		this.file = file;
		this.transactions = extractTransactions(this.file);
	}
	
	private static List<Transaction> extractTransactions(File transactionFile) {

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

	private static boolean isDate(String header) {
		return header.toLowerCase().contains("date");
	}

	private static boolean isAmount(String amount) {
		return amount.toLowerCase().contains("credit") || amount.toLowerCase().contains("debit")
				|| amount.toLowerCase().contains("amount");
	}

	private static boolean isLocation(String location) {
		return location.toLowerCase().contains("description") || location.toLowerCase().contains("name")
				|| location.toLowerCase().contains("type");
	}
}
