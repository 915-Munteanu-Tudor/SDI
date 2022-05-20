import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {Food} from "./food.model";
import {catchError, map} from "rxjs/operators";
import {Customer} from "./customer.model";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private customerUrl = "http://localhost:8081/api/customers";
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  constructor(private httpClient: HttpClient) {
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    }
  }

  getCustomers(): Observable<any> {
    return this.httpClient.get<Array<Customer>>(this.customerUrl).pipe(
      catchError(this.handleError<Customer[]>('getCustomer', []))
    );
  }

  getCustomerWithId(id: number): Observable<Customer> {
    return this.getCustomers().pipe(
      map(customers => customers.customers.find((c: { id: number; }) => c.id === id))
    );
  }

  update(customer: Customer): Observable<Customer> {
    const url = `${this.customerUrl}/${customer.id}`;
    return this.httpClient.put<Customer>(url, customer).pipe(
      catchError(this.handleError<Customer>('updateCustomer'))
    );
  }

  add(customer: Customer): Observable<any> {
    console.log(customer)
    return this.httpClient.post<any>(this.customerUrl, customer).pipe(
      catchError(this.handleError<any>('addCustomer'))
    );
  }

  delete(customerId: number): Observable<any> {
    const url = `${this.customerUrl}/${customerId}`;
    return this.httpClient.request('DELETE', url, this.httpOptions).pipe(
      catchError(this.handleError('removeCustomer'))
    );
  }
}
