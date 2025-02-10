import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Kategoria } from './Kategoria';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class CategoriesService {
  
  private apiUrl2 = 'http://localhost:3000/api/categoryID/';

  constructor(private http: HttpClient, private authService: AuthService) {}
  getCategories(page: number, limit: number): Observable<{kategorie: Kategoria[], total: number}> {
    const params = new HttpParams()
        .set('page', page.toString())
        .set('limit', limit.toString());
    return this.http.get<{kategorie: Kategoria[], total: number}>('/api/categories', { params});
  }
 
  
  getCategory(id: string): Observable<Kategoria>{
    return this.http.get<Kategoria>(this.apiUrl2+id);
  }
  updateCategory(id: string, kategoria: Kategoria): Observable<Kategoria> {
    return this.http.put<Kategoria>(`${this.apiUrl2}${id}`, kategoria, this.authService.generateHeader());
  }
  deleteCategory(id: string): Observable<any> {
    return this.http.delete(`http://localhost:3000/api/deleteCategory/${id}`, this.authService.generateHeader());
  }
  addCategory(kategoria: Kategoria): Observable<Kategoria> {
    return this.http.post<Kategoria>('http://localhost:3000/api/addCategory', kategoria, this.authService.generateHeader());
  }
}