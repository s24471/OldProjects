import { AuthService } from './auth.service';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  constructor(private http: HttpClient, private authService: AuthService) {}

  getUsers(page: number, limit: number): Observable<{users: User[], total: number}> {
    const params = new HttpParams()
        .set('page', page.toString())
        .set('limit', limit.toString());
        const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
        const token = currentUser && currentUser.token ? currentUser.token : '';
    
        const headers = new HttpHeaders({
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        });

    return this.http.get<{users: User[], total: number}>('/api/Users',  {params:params, headers: headers});
  }
  getUser(id: string): Observable<User>{
    return this.http.get<User>('http://localhost:3000/api/userID/' + id, this.authService.generateHeader());
  }
  updateUser(id: string, user: User): Observable<User> {
    return this.http.put<User>(`http://localhost:3000/api/userID/` + id, user, this.authService.generateHeader());
  }
  deleteUser(id: string): Observable<any> {
    return this.http.delete(`http://localhost:3000/api/deleteUser/${id}`, this.authService.generateHeader());
  }
  addUser(user: User): Observable<User> {
    return this.http.post<User>('http://localhost:3000/api/addUser', user, this.authService.generateHeader());
  }
}