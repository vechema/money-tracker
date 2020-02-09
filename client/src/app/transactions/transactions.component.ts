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

  matTransactions: MatTableDataSource<Transaction>;
  transactions: Transaction[];

  tableColumns: string[] = ['date', 'amount', 'location'];

  // Filter variables
  filterString: string;
  startDate: Date;
  endDate: Date;

  constructor(private transactionService: TransactionService) { }

  ngOnInit() {
    this.getTransactions();
  }

  getTransactions() {
    this.transactionService.getTransactions().subscribe(trans => {
      this.transactions = trans
      this.matTransactions = new MatTableDataSource<Transaction>(this.transactions);
      this.matTransactions.sort = this.sort;
      this.matTransactions.paginator = this.paginator;
    });
  }

  getTotalCost() {
    if (this.matTransactions) {
      return this.matTransactions.filteredData.map(trans => trans.amount).reduce((acc, value) => acc + value.cents, 0) / 100;
    }
    return 0;
  }

  private applyAllFilters() {

    this.matTransactions.filterPredicate = (transaction, filter) => {

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

      return wordResult && afterStartDate && beforeEndDate;
    }
    //this.matTransactions.filter = this.filterString;
    this.matTransactions.filter = '' + Math.random();
    if (this.matTransactions.paginator) {
      this.matTransactions.paginator.firstPage();
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
