import { Component, OnInit } from '@angular/core';
import { Transaction } from '../transaction';
import { Money } from '../money';
import { TRANSACTIONS } from '../mock-transactions';

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.css']
})
export class TransactionsComponent implements OnInit {

  transactions = TRANSACTIONS;

  constructor() { }

  ngOnInit() {
  }

}
