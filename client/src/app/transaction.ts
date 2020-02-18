import { Money } from './money';

export class Transaction {
  date: Date;
  amount: Money;
  description: string;
  source: string;
}
