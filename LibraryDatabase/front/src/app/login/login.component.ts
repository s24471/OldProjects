import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent {
  loginForm = new FormGroup({
      login: new FormControl(''),
      password: new FormControl('')
  });

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    const formValue = this.loginForm.value;
    const login = formValue.login || '';
    const password = formValue.password || '';

    this.authService.login({ login, password }).subscribe(() => {
      
        this.router.navigate(['/book-list']);
    });
  }
}