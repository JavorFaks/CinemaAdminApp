import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { PagedResponse } from '../models/paged-response';
import { Observable } from 'rxjs';
import { Rezervacija } from '../models/rezervacija';

@Injectable({
  providedIn: 'root',
})
export class RezervacijaService {

  private baseUrl = 'http://localhost:8080/rezervacija';

  constructor(private httpClient: HttpClient) {}

  //Get all rezervacije sa paginacijom i opcionalnim sortiranjem
  public getAll(page: number, size: number, sort?:string):Observable<PagedResponse<Rezervacija>> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);
    
    if(sort){
      params = params.set('sort', sort);
    }

    return this.httpClient.get<PagedResponse<Rezervacija>>(this.baseUrl, { params });

  }

  public getRezervacijaBySala(salaId: number, page: number, size: number, sort?:string):Observable<PagedResponse<Rezervacija>> {
    let params = new HttpParams()
      .set('salaId', salaId)
      .set('page', page)
      .set('size', size);
    
    if(sort){
      params = params.set('sort', sort);
    }

    return this.httpClient.get<PagedResponse<Rezervacija>>(`${this.baseUrl}/sala`, { params });
  }
  //Create nova rezervacija
  public createRezervacija(rezervacija: Rezervacija): Observable<Rezervacija> {
    return this.httpClient.post<Rezervacija>(this.baseUrl, rezervacija);
  }

  //Update rezervacija
  public updateRezervacija(rezervacija: Rezervacija):Observable<Rezervacija> {
    return this.httpClient.put<Rezervacija>(`${this.baseUrl}/${rezervacija.id}`, rezervacija);
  }

  public deleteRezervacija(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseUrl}/${id}`);
  }

}
