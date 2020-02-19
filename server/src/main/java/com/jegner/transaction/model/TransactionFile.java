package com.jegner.transaction.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Data;

/**
 * Debit will be negative (money spent - HEB, paying a bill) Credit will be
 * positive (money received - salary, payment of credit card on credit card
 * statement)
 * 
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
			ArrayList<Integer> descriptionIndices = new ArrayList<>();
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
				// Description
				if (isDescription(header)) {
					descriptionIndices.add(i);
				}
			}

			if (descriptionIndices.size() == 0 || dateIndex == -1 || amountIndices.size() == 0) {
				System.out.println("Didn't find needed index");
			}

			System.out.println("Date index: " + dateIndex);
			System.out.println("Description index: " + descriptionIndices);
			System.out.println("Amount indices: " + amountIndices);

			String[] creditDescriptions = { "direct dep", "credit card deposit", "directpay", "automatic payment",
					"auto-pmt" };
			boolean creditIsPos = true;

			while ((line = br.readLine()) != null && !line.isEmpty()) {

				// use comma as separator
				String[] transactionRaw = line.split(cvsSplitBy);
				// System.out.println(line + " " + transactionRaw.length);

				// Check to make sure transaction has amount
				if (transactionRaw.length < amountIndices.get(0)) {
					System.out.println("There's possibly no amount in this transaction - weird");
					System.out.println("\t" + line);
					continue;
				}

				for (int i = 0; i < transactionRaw.length; i++) {
					String transField = transactionRaw[i];
					if (transField.startsWith("\"")) {
						transactionRaw[i] = transField.substring(1, transField.length() - 1);
					}

				}

				String date = transactionRaw[dateIndex].trim();
				String description = transactionRaw[descriptionIndices.get(0)].trim();
				String amount = transactionRaw[amountIndices.get(0)].trim();

				if (amount.isEmpty() && amountIndices.size() > 1) {
					amount = transactionRaw[amountIndices.get(1)].trim();
				}

				if (description.isEmpty() && descriptionIndices.size() > 1) {
					description = transactionRaw[descriptionIndices.get(1)].trim();
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

				if (description.isEmpty()) {
					System.out.println("Transaction has no description entry");
					System.out.println("\t" + line);
					continue;
				}

				Category category = Category.determineCategory(line);
				if (category.equals(Category.MISCELLANEOUS)) {
					System.out.println(category + " : " + line);
				}

				Transaction transaction = Transaction.builder().setDate(date).setAmount(amount)
						.setDescription(description).setSource(source).setCategory(category).build();

				if (Arrays.stream(creditDescriptions).parallel().anyMatch(description.toLowerCase()::contains)) {
					if (amount.startsWith("-")) {
						creditIsPos = false;
					}
				}
				// System.out.println("\t" + transaction);
				transactions.add(transaction);
			}

			if (!creditIsPos) {
				List<Transaction> tempTransactions = new ArrayList<>(transactions);
				transactions.clear();
				for (Transaction trans : tempTransactions) {
					String newAmount = trans.getAmount().startsWith("-") ? trans.getAmount().substring(1)
							: "-" + trans.getAmount();
					Transaction flippedTrans = Transaction.builder().setDate(trans.getDate()).setAmount(newAmount)
							.setDescription(trans.getDescription()).setSource(trans.getSource()).build();
					transactions.add(flippedTrans);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("\n\n");
		return transactions;
	}

	private static boolean isDate(String header) {
		return header.toLowerCase().contains("date");
	}

	private static boolean isAmount(String amount) {
		return amount.toLowerCase().contains("credit") || amount.toLowerCase().contains("debit")
				|| amount.toLowerCase().contains("amount");
	}

	private static boolean isDescription(String description) {
		return description.toLowerCase().contains("description") || description.toLowerCase().contains("name")
				|| description.toLowerCase().contains("type") || description.toLowerCase().startsWith("action");
	}
}
