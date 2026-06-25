import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { PagedResponse } from '../models/paged-response';
import { Observable } from 'rxjs';
import { Sala } from '../models/sala';

@Injectable({
  providedIn: 'root',
})
export class SalaService {

  private baseUrl = 'http://localhost:8080/sala';

  constructor(private httpClient: HttpClient) {}

  //Get all sale sa paginacijom i opcionalnim sortiranjem
  public getAll(page: number, size: number, sort?:string):Observable<PagedResponse<Sala>> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);
    
    if(sort){
      params = params.set('sort', sort);
    }

    return this.httpClient.get<PagedResponse<Sala>>(this.baseUrl, { params });

  }

  
  public naprednaPretraga(
      kapacitet: number | null,
      brojRedova: number | null,
      bioskopId: number | null,
      page: number,
      size: number,
      sort?: string
  ):Observable<PagedResponse<Sala>>{
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);
    
    if (sort) {
      params = params.set('sort', sort);
    }
    if (kapacitet !== null) {
      params = params.set('kapacitet', kapacitet);
    }
    if (brojRedova !== null) {
      params = params.set('brojRedova', brojRedova);
    }
    if (bioskopId !== null) {
      params = params.set('bioskopId', bioskopId);
    } 

    return this.httpClient.get<PagedResponse<Sala>>(`${this.baseUrl}/pretraga`, { params });
 }

  //Create a novi sala
  public createSala(sala: Sala): Observable<Sala> {
    return this.httpClient.post<Sala>(this.baseUrl, sala);
  }

  //Update sala
  public updateSala(sala: Sala):Observable<Sala> {
    return this.httpClient.put<Sala>(`${this.baseUrl}/${sala.id}`, sala);
  }

  public deleteSala(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseUrl}/${id}`);
  }

}
