import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { PagedResponse } from '../models/paged-response';
import { Observable } from 'rxjs';
import { Film } from '../models/film';

@Injectable({
  providedIn: 'root',
})
export class FilmService {

  private baseUrl = 'http://localhost:8080/film';

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
