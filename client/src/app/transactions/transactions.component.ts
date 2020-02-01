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

  // Filter variables
  filterString: string;
  startDate: Date;
  endDate: Date;

  constructor(private transactionService: TransactionService) { }

  ngOnInit() {
    this.transactions = new MatTableDataSource<Transaction>(this.getTransactions());
    this.transactions.sort = this.sort;
    this.transactions.paginator = this.paginator;
  }

  getTransactions(): Transaction[] {
    return this.transactionService.getTransactions();
  }

  // Need to just do what is being displayed...
  getTotalCost() {
    return this.getTransactions().map(t => t.amount).reduce((acc, value) => acc + value.getAmount(), 0);
  }

  events: string[] = [];

  private applyAllFilters() {

    this.transactions.filterPredicate = (transaction, filter) => {

      let wordResult: boolean = true;
      if (this.filterString && !transaction.location.toLowerCase().includes(this.filterString)) {
        wordResult = false;
      }

      let afterStartDate: boolean = true;
      if (this.startDate && !(this.startDate <= transaction.date)) {
        afterStartDate = false;
      }

      let beforeEndDate: boolean = true;
      if (this.endDate && !(this.endDate >= transaction.date)) {
        beforeEndDate = false;
      }

      this.events.push(``)

      return wordResult && afterStartDate && beforeEndDate;
    }
    //this.transactions.filter = this.filterString;
    this.transactions.filter = '' + Math.random();
    if (this.transactions.paginator) {
      this.transactions.paginator.firstPage();
    }
  }

  applyGeneralFilter(filterValue: string) {
    this.filterString = filterValue.trim().toLowerCase();
    this.applyAllFilters();
  }

  applyStartDateFilter(event: MatDatepickerInputEvent<Date>) {
    this.startDate = event.value;
    this.applyAllFilters();
  }

  applyEndDateFilter(event: MatDatepickerInputEvent<Date>) {
    this.endDate = event.value;
    this.applyAllFilters();
  }

}
