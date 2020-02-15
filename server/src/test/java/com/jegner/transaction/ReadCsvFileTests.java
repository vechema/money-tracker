package com.jegner.transaction;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.testng.annotations.Test;

import com.jegner.transaction.controller.TransactionController;
import com.jegner.transaction.model.Transaction;


public class ReadCsvFileTests {
	
	@Test
	public void readTransactionFiles() {
		TransactionController controller = new TransactionController();
		
		List<Transaction> transactions = controller.getAllTransactions();
		
		for(Transaction trans : transactions) {
		//	System.out.println(trans);
		}
		
		assertThat(true, is(true));
	}

}
