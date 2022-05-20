import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {Pet} from "./pet.model";
import {catchError, map} from "rxjs/operators";
import {PetFood} from "./pet-food.model";

@Injectable({
  providedIn: 'root'
})
export class PetFoodService {
  private petFoodUrl = 'http://localhost:8081/api/petFoods';
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
  getPetFoods(): Observable<any> {
    return this.httpClient.get<Array<PetFood>>(this.petFoodUrl).pipe(
      catchError(this.handleError<Pet[]>('getPetFoods', []))
    );
  }

  getPetFoodWithId(id: number): Observable<PetFood> {
    return this.getPetFoods().pipe(
      map(petFoods => petFoods.petFoods.find((pf: { id: number; }) => pf.id === id))
    );
  }

  update(petFood: PetFood): Observable<PetFood> {
    const url = `${this.petFoodUrl}/${petFood.petid}&${petFood.foodid}`;
    return this.httpClient.put<PetFood>(url, petFood).pipe(
      catchError(this.handleError<PetFood>('updatePetFood'))
    );
  }

  add(petFood: PetFood): Observable<any> {
    return this.httpClient.post<any>(this.petFoodUrl, petFood).pipe(
      catchError(this.handleError<any>('addPetFood'))
    );
  }

  delete(petFoodId: number): Observable<any> {
    const url = `${this.petFoodUrl}/${petFoodId}`;
    console.log(url);
    return this.httpClient.request('DELETE', url, this.httpOptions).pipe(
      catchError(this.handleError('removePetFood'))
    );
  }

  filterPetsThatEatCertainFood(foodId: number): Observable<any> {
    return this.httpClient.get<any>(`${this.petFoodUrl}/${foodId}`).pipe(
      catchError(this.handleError<any>('filterPetsThatEatCertainFood'))
    );
  }
}
