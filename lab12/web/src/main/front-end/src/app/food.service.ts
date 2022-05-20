import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {Pet} from "./pet.model";
import {catchError, map} from "rxjs/operators";
import {Food} from "./food.model";

@Injectable({
  providedIn: 'root'
})
export class FoodService {
  private foodUrl = "http://localhost:8081/api/food";
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

  getFood(): Observable<any> {
    return this.httpClient.get<Array<Food>>(this.foodUrl).pipe(
      catchError(this.handleError<Food[]>('getFood', []))
    );
  }

  getFoodWithId(id: number): Observable<Food> {
    return this.getFood().pipe(
      map(food => food.foods.find((f: { id: number; }) => f.id === id))
    );
  }

  update(food: Food): Observable<Food> {
    const url = `${this.foodUrl}/${food.id}`;
    return this.httpClient.put<Food>(url, food).pipe(
      catchError(this.handleError<Food>('updateFood'))
    );
  }

  add(food: Food): Observable<any> {
    return this.httpClient.post<any>(this.foodUrl, food).pipe(
      catchError(this.handleError<any>('addFood'))
    );
  }

  delete(foodId: number): Observable<any> {
    const url = `${this.foodUrl}/${foodId}`;
    console.log(url);
    return this.httpClient.request('DELETE', url, this.httpOptions).pipe(
      catchError(this.handleError('removeFood'))
    );
  }

  getValidFood(): Observable<any> {
    const url = `${this.foodUrl}/valid`;
    return this.httpClient.get<any>(url).pipe(
      catchError(this.handleError<Food[]>('getValidFood', []))
    );
  }
}
