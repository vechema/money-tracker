import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { Transaction } from '../transaction';
import { Money } from '../money';
import { TRANSACTIONS } from '../mock-transactions';

import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.css']
})
export class TransactionsComponent implements OnInit {

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  transactions = new MatTableDataSource<Transaction>(TRANSACTIONS);

  tableColumns: string[] = ['date', 'amount', 'location'];

  constructor() { }

  ngOnInit() {
    this.transactions.sort = this.sort;
    this.transactions.paginator = this.paginator;
  }

  applyFilter(filterValue: string) {
    this.transactions.filter = filterValue.trim().toLowerCase();
    if (this.transactions.paginator) {
      this.transactions.paginator.firstPage();
    }
  }

  getTotalCost() {
    return TRANSACTIONS.map(t => t.amount).reduce((acc, value) => acc + value.getAmount(), 0);
  }

}
