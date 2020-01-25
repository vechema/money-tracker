import { Component, OnInit } from '@angular/core';
import { Transaction } from '../transaction';
import { Money } from '../money';

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.css']
})
export class TransactionsComponent implements OnInit {

  transaction: Transaction = {
    date: new Date('12/31/2019'),
    amount: new Money('-5.54'),
    location: 'home',
  }

  constructor() { }

  ngOnInit() {
  }

}
