import { AutorService } from './../autor.service';
import { Component, OnInit } from '@angular/core';
import { Autor } from '../Autor';
import { Router } from '@angular/router';

@Component({
  selector: 'app-autor-add',
  templateUrl: './autor-add.component.html'
})
export class AutorAddComponent {
  newAutor: Autor = {
    autorID: '',
    imie: '',
    nazwisko: '',
    rokUrodzenia: 0
  }

  constructor(private autorService: AutorService, private router: Router){}
  
  addAutor(): void {
      this.autorService.addAutor(this.newAutor).subscribe({
        next: () => {
          alert('Autor został dodany.');
          this.router.navigate(['autor-list']);
        },
        error: err => {
          console.error('Error adding autor: ', err);
          alert('Wystąpił błąd podczas dodawania autora.');
        }
      });
  }
}
