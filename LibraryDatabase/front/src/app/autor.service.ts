import { Autor } from './Autor';
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AutorService {
  private apiUrl = 'http://localhost:3000/api/autors';
  private apiUrl2 = 'http://localhost:3000/api/autorID/';

  constructor(private http: HttpClient, private authService: AuthService) { }

  getAutors(page: number, limit: number): Observable<{autors: Autor[], total: number}> {
    const params = new HttpParams()
    .set('page', page.toString())
    .set('limit', limit.toString());
    return this.http.get<{autors: Autor[], total:number}>(this.apiUrl, {params});
  }
  getAutor(id: string): Observable<Autor>{
    return this.http.get<Autor>(this.apiUrl2+id)
  }
  updateAutor(id: string, autor: Autor): Observable<Autor>{
    return this.http.put<Autor>(this.apiUrl2 + id, autor, this.authService.generateHeader());
  }
  deleteAutor(id: string): Observable<any>{
    return this.http.delete(`http://localhost:3000/api/deleteAutor/${id}`, this.authService.generateHeader());
  }
  
  addAutor(autor: Autor): Observable<Autor> {
    return this.http.post<Autor>('http://localhost:3000/api/addAutor/', autor, this.authService.generateHeader());
  }
}
