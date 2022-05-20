import { Injectable } from '@angular/core';
import {Observable, of} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {Purchase} from "./purchase.model";
import {Customer} from "./customer.model";
import {Pet} from "./pet.model";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {PurchaseWithId} from "./purchase-with-id.model";
import {CustomerSpent} from "./customer-spent.model";

@Injectable({
  providedIn: 'root'
})
export class PurchaseService {

  private purchaseUrl = 'http://localhost:8081/api/purchases';
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private httpClient: HttpClient) {
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    }
  }

  getPurchases(): Observable<any> {
    return this.httpClient.get<Array<Purchase>>(this.purchaseUrl).pipe(
      catchError(this.handleError<Pet[]>('getPurchases', []))
    );
  }

  getCatFoodWithId(id: number): Observable<Purchase> {
    return this.getPurchases().pipe(
      map(purchases => purchases.purchases.find(cf => cf.id === id))
    );
  }

  update(purchase: Purchase): Observable<Purchase> {
    const url = `${this.purchaseUrl}/${purchase.pet.id}&${purchase.customer.id}`;
    return this.httpClient.put<Purchase>(url, purchase).pipe(
      catchError(this.handleError<Purchase>('updatePurchase'))
    );
  }

  add(purchase: Purchase): Observable<any> {
    console.log(purchase);
    return this.httpClient.post<any>(this.purchaseUrl, {
      petId: purchase.pet.id,
      customerId: purchase.customer.id,
      price: purchase.price,
      dateAcquired: purchase.dateAcquired,
      review: purchase.review
    } as PurchaseWithId).pipe(
      catchError(this.handleError<any>('addPurchase'))
    );
  }

  getCustomersThatBoughtBreed(breed: string): Observable<any> {
    return this.httpClient.get<Array<Customer>>(`${this.purchaseUrl}/breed=${breed}`)
      .pipe(catchError(this.handleError('getCustomerThatBoughtBreed')))
  }

  getSortedCustomers(): Observable<any> {
    return this.httpClient.get<Array<CustomerSpent>>(`http://localhost:8080/api/sortedCustomers`)
      .pipe(catchError(this.handleError('getSortedCustomers')))
  }

  getPurchasesWithMinimumReview(review: number): Observable<any> {
    return this.httpClient.get<Array<Purchase>>(`${this.purchaseUrl}/minReview=${review}`)
      .pipe(catchError(this.handleError('getPurchasesWithMinimumReview')))

  }
  delete(petId: number, customerId: number): Observable<any> {
    const url = `${this.purchaseUrl}/${petId}&${customerId}`;
    return this.httpClient.request('DELETE', url, this.httpOptions).pipe(
      catchError(this.handleError('removePurchase'))
    );
  }
}
