import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { Transaction } from '../transaction';
import { TransactionService } from '../transaction.service';

import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';

import { MatDatepickerInputEvent } from '@angular/material/datepicker';

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.css']
})
export class TransactionsComponent implements OnInit {

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  transactions: MatTableDataSource<Transaction>;

  tableColumns: string[] = ['date', 'amount', 'location'];

  constructor(private transactionService: TransactionService) { }

  ngOnInit() {
    this.transactions = new MatTableDataSource<Transaction>(this.getTransactions());
    this.transactions.sort = this.sort;
    this.transactions.paginator = this.paginator;
  }

  applyFilter(filterValue: string) {
    this.transactions.filter = filterValue.trim().toLowerCase();
    if (this.transactions.paginator) {
      this.transactions.paginator.firstPage();
    }
  }

  getTransactions(): Transaction[] {
    return this.transactionService.getTransactions();
  }

  getTotalCost() {
    return this.getTransactions().map(t => t.amount).reduce((acc, value) => acc + value.getAmount(), 0);
  }

  events: string[] = [];

  addEvent(type: string, event: MatDatepickerInputEvent<Date>) {
    this.events.push(`${type}: ${event.value}`);
  }

}
