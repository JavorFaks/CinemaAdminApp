import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { PagedResponse } from '../models/paged-response';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Film } from '../models/film';

@Injectable({
  providedIn: 'root',
})
export class FilmService {

  private baseUrl = `${environment.apiUrl}/film`;

  constructor(private httpClient: HttpClient) {}

  //Get all filmovi sa paginacijom i opcionalnim sortiranjem
  public getAll(page: number, size: number, sort?:string):Observable<PagedResponse<Film>> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);
    
    if(sort){
      params = params.set('sort', sort);
    }

    return this.httpClient.get<PagedResponse<Film>>(this.baseUrl, { params });

  }


  public naprednaPretraga(
      naziv: string | null,
      zanr: string | null,
      maxMinuta: number | null,
      page: number,
      size: number,
      sort?: string
  ):Observable<PagedResponse<Film>>{
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);
    
    if (sort) {
      params = params.set('sort', sort);
    }
    if (naziv !== null) {
      params = params.set('naziv', naziv);
    }
    if (zanr !== null) {
      params = params.set('zanr', zanr);
    }
    if (maxMinuta !== null) {
      params = params.set('maxMinuta', maxMinuta);
    } 

    return this.httpClient.get<PagedResponse<Film>>(`${this.baseUrl}/pretraga`, { params });
 }

  public getZanrovi(): Observable<string[]>{
    return this.httpClient.get<string[]>(`${this.baseUrl}/zanrovi`);
  }

  //Create a novi film
  public createFilm(film: Film): Observable<Film> {
    return this.httpClient.post<Film>(this.baseUrl, film);
  }

  //Update film
  public updateFilm(film: Film):Observable<Film> {
    return this.httpClient.put<Film>(`${this.baseUrl}/${film.id}`, film);
  }

  public deleteFilm(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseUrl}/${id}`);
  }

}
