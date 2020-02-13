import { Injectable } from '@angular/core';
import { Transaction } from './transaction';
import { Money } from './money';

import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  constructor(private http: HttpClient) { }

  transactions$: Observable<Transaction[]>;

  getTransactions(): Observable<Transaction[]> {
    if (!this.transactions$) {
      this.transactions$ = this.http.get<Transaction[]>('http://localhost:8080/transaction')
        .pipe(
          tap(transactions => transactions.forEach(trans => {
            trans.date = new Date(trans.date);
            trans.amount = new Money("" + trans.amount);
          })),
          catchError(this.handleError<Transaction[]>('getTransactions', []))
        );
    }
    return this.transactions$;
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      console.error(error); // log to console instead

      console.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
