package com.jegner.transaction.model;

import lombok.Data;

@Data
public class Money {
	private final int cents;

	public Money(int cents) {
		this.cents = cents;
	}
}
