import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Bioskop } from '../models/bioskop';
import { PagedResponse } from '../models/paged-response';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class BioskopService {

  private baseUrl = 'http://localhost:8080/bioskop';

  constructor(private httpClient: HttpClient) {}

  //Get all bioskopi sa paginacijom i opcionalnim sortiranjem
  public getAll(page: number, size: number, sort?:string):Observable<PagedResponse<Bioskop>> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);
    
    if(sort){
      params = params.set('sort', sort);
    }

    return this.httpClient.get<PagedResponse<Bioskop>>(this.baseUrl, { params });

  }

  //Napredna pretraga za bioskop
  public naprednaPretraga(
    naziv: string | null,
    adresa: string | null,
    page: number, 
    size: number, 
    sort?:string
  ):Observable<PagedResponse<Bioskop>>{
     let params = new HttpParams()
      .set('page', page)
      .set('size', size);

    if (sort) {
      params = params.set('sort', sort);
    }
    if (naziv !== null) {
      params = params.set('naziv', naziv);
    }
    if (adresa !== null) {
      params = params.set('adresa', adresa);
    }
    
    return this.httpClient.get<PagedResponse<Bioskop>>(`${this.baseUrl}/pretraga`, { params })
  }

  //Create a novi bioskop
  public createBioskop(bioskop: Bioskop): Observable<Bioskop> {
    return this.httpClient.post<Bioskop>(this.baseUrl, bioskop);
  }

  //Update bioskop
  public updateBioskop(bioskop: Bioskop):Observable<Bioskop> {
    return this.httpClient.put<Bioskop>(`${this.baseUrl}/${bioskop.id}`, bioskop);
  }

  public deleteBioskop(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseUrl}/${id}`);
  }

}
