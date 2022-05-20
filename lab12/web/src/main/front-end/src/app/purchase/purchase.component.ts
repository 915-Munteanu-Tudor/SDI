import {Component, OnInit} from '@angular/core';
import {Customer} from "../customer.model";
import {Purchase} from "../purchase.model"
import {PurchaseService} from "../purchase.service";
import {CustomerService} from "../customer.service";
import {Pet} from "../pet.model";
import {PetsService} from "../pets.service";

@Component({
  selector: 'app-purchase',
  templateUrl: './purchase.component.html',
  styleUrls: ['./purchase.component.css']
})
export class PurchaseComponent implements OnInit {

  pets: Pet[];
  customers: Customer[];
  selectedPet: Pet;
  selectedCustomer: Customer;


  purchases: Purchase[];
  selectedPurchase: Purchase;

  constructor(private purchaseService: PurchaseService,
              private petsService: PetsService,
              private customerService: CustomerService) {
  }

  makePurchase(price: string, dateAcquired: string, review: string) {
    console.log(price)
    console.log(dateAcquired)
    console.log(review)
    this.purchaseService.add({
      pet: this.selectedPet,
      customer: this.selectedCustomer,
      price: Number(price),
      dateAcquired: new Date(dateAcquired),
      review: Number(review)
    } as Purchase).subscribe(
      _ => this.getPurchases()
    )
  }

  filterWithReview(review: string) {
    this.purchaseService.getPurchasesWithMinimumReview(Number(review)).subscribe(purchases => this.purchases = purchases.purchases);
  }
  onSelect(purchase: Purchase) {
    this.selectedPurchase = purchase;
    console.log(purchase)
  }

  onSelectPet(pet: Pet) {
    this.selectedPet = pet;
  }

  onSelectCustomer(customer: Customer) {
    this.selectedCustomer = customer;
  }

  delete(purchase: Purchase) {
    this.purchaseService.delete(purchase.pet.id, purchase.customer.id).subscribe(
      _ => this.getPurchases()
    )
  }

  getPurchases() {
    this.purchaseService.getPurchases().subscribe(
      purchases => this.purchases = purchases.purchases
    )
  }

  getPets() {
    this.petsService.getPets().subscribe(
      pets => this.pets = pets.pets
    )
  }

  getCustomers() {
    this.customerService.getCustomers().subscribe(
      customers => this.customers = customers.customers
    )
  }

  ngOnInit(): void {
    this.getPurchases();
    this.getPets();
    this.getCustomers();
  }

}
