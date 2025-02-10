import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html'
})
export class RegisterComponent {
  registerForm = new FormGroup({
    login: new FormControl(''),
    haslo: new FormControl(''),
    email: new FormControl(''),
    imie: new FormControl(''),
    nazwisko: new FormControl('')
  });

constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    const formValue = this.registerForm.value;
    const login = formValue.login || '';
    const haslo = formValue.haslo || '';
    const email = formValue.email || '';
    const imie = formValue.imie || '';
    const nazwisko = formValue.nazwisko || '';
    if(haslo.length<5){
      alert("Hasło musi być >= 5 ");
      return;
    }
    this.authService.register({login, haslo, email, imie, nazwisko}).subscribe(() => {
        alert("rejestracja zakonczona sukcesem");
        this.router.navigate(['/login']);
    });
  }
}
