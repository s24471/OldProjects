import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from './book';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private apiUrl = 'http://localhost:3000/api/books';
  private apiUrl2 = 'http://localhost:3000/api/bookID/';

  constructor(private http: HttpClient, private authService: AuthService) { }

  getBooks(page: number, limit: number): Observable<{books: Book[], total: number}> {
    const params = new HttpParams()
        .set('page', page.toString())
        .set('limit', limit.toString());
    return this.http.get<{books: Book[], total: number}>(this.apiUrl, { params});
  }

  getBook(id: string): Observable<Book>{
    return this.http.get<Book>(this.apiUrl2+id);
  }
  updateBook(id: string, book: Book): Observable<Book> {
    return this.http.put<Book>(`${this.apiUrl2}${id}`, book, this.authService.generateHeader());
  }
  deleteBook(id: string): Observable<any> {
    return this.http.delete(`http://localhost:3000/api/deleteBook/${id}`, this.authService.generateHeader());
  }
  addBook(book: Book): Observable<Book> {
    return this.http.post<Book>('http://localhost:3000/api/addBook', book, this.authService.generateHeader());
  }
  getBooksByAutor(autorId: string, page: number, limit: number): Observable<{books: Book[], total: number}> {
    const params = new HttpParams()
    .set('page', page.toString())
    .set('limit', limit.toString());
    return this.http.get<{books: Book[], total: number}>(`http://localhost:3000/api/booksByAutor/${autorId}`, { params});
  }
  
  getBooksByCategory(categoryID: string, page: number, limit: number): Observable<{books: Book[], total: number}> {
    const params = new HttpParams()
    .set('page', page.toString())
    .set('limit', limit.toString());
    return this.http.get<{books: Book[], total: number}>(`http://localhost:3000/api/booksByCategory/${categoryID}`, {params});
  }

  addToFavorites(bookId: string) {
    return this.http.post(`http://localhost:3000/api/addToFavorites/${bookId}`, "", this.authService.generateHeader());
  }
  getFavorites(page: number, limit: number): Observable<{books: Book[], total: number}> {
    const params = new HttpParams()
    .set('page', page.toString())
    .set('limit', limit.toString());
    const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
        const token = currentUser && currentUser.token ? currentUser.token : '';
    
        const headers = new HttpHeaders({
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        });

    return this.http.get<{books: Book[], total: number}>(`http://localhost:3000/api/favorites`, {params:params, headers: headers} );
  }
  isFavorite(bookId: string): Observable<{ isFavorite: boolean }> {
    return this.http.get<{ isFavorite: boolean }>(`http://localhost:3000/api/isFavorite/${bookId}`, this.authService.generateHeader());
  }
  removeFromFavorites(bookId: string): Observable<any> {
    return this.http.delete(`http://localhost:3000/api/removeFavorite/${bookId}`, this.authService.generateHeader());
  }

}
