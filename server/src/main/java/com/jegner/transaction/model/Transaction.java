package com.jegner.transaction.model;

import lombok.Data;

@Data
public class Transaction {

	private final String date;
	private final String amount;
	private final String location;

	private Transaction(TransactionBuilder builder) {
		this.date = builder.date;
		this.amount = builder.amount;
		this.location = builder.location;
	}

	public static TransactionBuilder builder() {
		return new TransactionBuilder();
	}

	public static class TransactionBuilder {
		private String date;
		private String amount;
		private String location;

		private TransactionBuilder() {

		}

		public TransactionBuilder setDate(String date) {
			this.date = date;
			return this;
		}

		public TransactionBuilder setAmount(String amount) {
			this.amount = amount;
			return this;
		}

		public TransactionBuilder setLocation(String location) {
			this.location = location;
			return this;
		}

		public Transaction build() {
			return new Transaction(this);
		}
	}

}
