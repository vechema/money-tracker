export class Money {
  cents: number;

  constructor(cents: number) {
    this.cents = cents;
  }

  getAmount() {
    return this.cents / 100;
  }

  toString = (): string => {
    return "" + this.cents / 100;
  }
}
