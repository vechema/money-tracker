export class Money {
  cents: number;
  constructor(dollar: string) {
    this.cents = parseFloat(dollar) * 100;
  }

  getAmount() {
    return this.cents / 100;
  }

  toString = (): string => {
    return "" + this.cents / 100;
  }
}
