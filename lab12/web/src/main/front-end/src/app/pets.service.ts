import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpRequest} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {Pet} from "./pet.model";
import {catchError, map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class PetsService {
  private petsUrl = 'http://localhost:8081/api/pets';
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
  getPets(): Observable<any> {
    return this.httpClient.get<Array<Pet>>(this.petsUrl).pipe(
      catchError(this.handleError<Pet[]>('getPets', []))
    );
  }

  getPet(id: number): Observable<Pet> {
    return this.getPets().pipe(
      map(pets => pets.pets.find((pet: { id: number; }) => pet.id === id))
    );
  }

  update(pet: Pet): Observable<Pet> {
    const url = `${this.petsUrl}/${pet.id}`;
    return this.httpClient.put<Pet>(url, pet).pipe(
      catchError(this.handleError<Pet>('updatePet'))
    );
  }

  add(pet: Pet): Observable<any> {
    return this.httpClient.post<any>(this.petsUrl, pet).pipe(
      catchError(this.handleError<any>('addPet'))
    );
  }

  delete(petId: number): Observable<any> {
    const url = `${this.petsUrl}/${petId}`;
    console.log(url);
    return this.httpClient.request('DELETE', url, this.httpOptions).pipe(
      catchError(this.handleError('removePet'))
    );
  }
}
