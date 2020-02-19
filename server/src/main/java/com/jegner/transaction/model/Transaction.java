package com.jegner.transaction.model;

import lombok.Data;

@Data
public class Transaction {

	private final String date;
	private final String amount;
	private final String description;
	private final String source;
	private final Category category;

	private Transaction(TransactionBuilder builder) {
		this.date = builder.date;
		this.amount = builder.amount;
		this.description = builder.description;
		this.source = builder.source;
		this.category = builder.category;
	}

	public static TransactionBuilder builder() {
		return new TransactionBuilder();
	}

	public static class TransactionBuilder {
		private String date;
		private String amount;
		private String description;
		private String source;
		private Category category;

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

		public TransactionBuilder setDescription(String description) {
			this.description = description;
			return this;
		}

		public TransactionBuilder setSource(String source) {
			this.source = source;
			return this;
		}

		public TransactionBuilder setCategory(Category category) {
			this.category = category;
			return this;
		}

		public Transaction build() {
			return new Transaction(this);
		}
	}

	@Override
	public String toString() {
		return date + " " + description + " " + amount + " " + source;
	}

}
