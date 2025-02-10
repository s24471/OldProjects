import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { User } from './user';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private currentUserSubject: BehaviorSubject<User | null>;

    constructor(private http: HttpClient) {
       const storedUser = localStorage.getItem('currentUser');
       const user = storedUser ? JSON.parse(storedUser) : null;
       this.currentUserSubject = new BehaviorSubject<User | null>(user);
    }

    public get currentUserValue(): User | null {
        return this.currentUserSubject.value;
    }

    login(credentials: { login: string, password: string }): Observable<User> {
        return this.http.post<User>(`/api/login`, credentials)
            .pipe(map(user => {
                console.log("user : " + user);
                console.log(JSON.stringify(user));

                localStorage.setItem('currentUser', JSON.stringify(user));
                this.currentUserSubject.next(user);
                return user;
            }));
    }

    register(user: { login: string, haslo: string, email: string, imie: string, nazwisko: string }): Observable<any> {
        return this.http.post(`/api/register`, user);
    }

    logout(): void {
        localStorage.removeItem('currentUser');
        this.currentUserSubject.next(null);
    }

    isLoggedIn(): boolean {
        const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
        return !!currentUser && !!currentUser.token;
    }
    isAdmin(): boolean {
        const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
        console.log(currentUser);
        return !!currentUser && !!currentUser.token && currentUser.isAdmin;
    }

    generateHeader(): {} {
        const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
        const token = currentUser && currentUser.token ? currentUser.token : '';
    
        const headers = new HttpHeaders({
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        });
        return {headers: headers};
    }
}
