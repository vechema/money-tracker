import { Injectable } from '@angular/core';
import { TRANSACTIONS } from './mock-transactions';
import { Transaction } from './transaction';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  constructor() { }

  getTransactions(): Transaction[] {
    return TRANSACTIONS;
  }
}
