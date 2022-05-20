import {Customer} from "./customer.model";

export interface CustomerSpent {
  customer: Customer;
  totalSpent: number;
}
