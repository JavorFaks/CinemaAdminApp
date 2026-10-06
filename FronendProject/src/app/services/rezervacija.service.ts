import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { PagedResponse } from '../models/paged-response';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Rezervacija } from '../models/rezervacija';

@Injectable({
  providedIn: 'root',
})
export class RezervacijaService {

  private baseUrl = `${environment.apiUrl}/rezervacija`;

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

  public naprednaPretraga(
    filmId: number | null,
    salaId: number | null,
    placeno: boolean | null,
    datumOd: string | null,
    datumDo: string | null,
    page: number,
    size: number,
    sort?: string
  ):Observable<PagedResponse<Rezervacija>>{
    let params = new HttpParams()
      .set('page',page)
      .set('size',size);

    if(sort){
      params = params.set('sort', sort);
    }

    if(filmId!=null){ params = params.set('filmId', filmId); } 
    if(salaId!=null){ params = params.set('salaId', salaId); } 
    if(placeno!=null){ params = params.set('placeno', placeno); } 
    if (datumOd) { params = params.set('datumOd', datumOd); }
    if (datumDo) { params = params.set('datumDo', datumDo); }

    return this.httpClient.get<PagedResponse<Rezervacija>>(`${this.baseUrl}/pretraga`, { params });
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
