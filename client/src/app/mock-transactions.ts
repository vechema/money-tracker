import { Transaction } from './transaction';
import { Money } from './money';

export const TRANSACTIONS: Transaction[] = [
  { date: new Date('01/01/2020'), amount: new Money('4.32'), location: 'Joann Fabric' },
  { date: new Date('03/25/2020'), amount: new Money('6.55'), location: 'Walmart' },
  { date: new Date('05/30/2020'), amount: new Money('20.65'), location: 'Lowes' },
  { date: new Date('04/20/2020'), amount: new Money('9.04'), location: 'Home depot' },
  { date: new Date('02/15/2020'), amount: new Money('-5.14'), location: 'HEB' },
];