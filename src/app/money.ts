export class Money {
  amount: number;
  constructor(dollar: string) {
    this.amount = parseFloat(dollar) * 100;
  }

  getAmount() {
    return this.amount;
  }

  toString = (): string => {
    return "" + this.amount / 100;
  }
}