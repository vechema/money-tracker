import { Transaction } from './transaction';
import { Money } from './money';

export const TRANSACTIONS: Transaction[] = [
  { date: new Date('12/1/19'), amount: new Money('4.32'), location: 'Joann Fabric' },
  { date: new Date('2/1/19'), amount: new Money('-5.14'), location: 'HEB' },
  { date: new Date('3/25/19'), amount: new Money('6.55'), location: 'Walmart' },
  { date: new Date('4/20/19'), amount: new Money('9.04'), location: 'home depot' },
];