import {Pet} from "./pet.model";
import {Customer} from "./customer.model";

export interface Purchase {
  pet: Pet;
  customer: Customer;
  price: number;
  dateAcquired: Date;
  review: number;
}
