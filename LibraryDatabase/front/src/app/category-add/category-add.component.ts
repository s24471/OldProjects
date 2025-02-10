import { Router } from '@angular/router';
import { CategoriesService } from '../categories.service';
import { Kategoria } from './../Kategoria';
import { Component } from '@angular/core';

@Component({
  selector: 'app-category-add',
  templateUrl: './category-add.component.html'
})
export class CategoryAddComponent {
  newKategoria: Kategoria ={
    kategoriaID: '',
    nazwaKategorii: '',
    rokUtworzenia: 0
  }

  constructor(private categoriesService: CategoriesService, private router: Router) { }

  addCategory(): void {
    this.categoriesService.addCategory(this.newKategoria).subscribe({
      next: () => {
        alert('Kategoria została dodana.');
        this.router.navigate(['category-list']);
      },
      error: err => {
        console.error('Error adding category:', err);
        alert('Wystąpił błąd podczas dodawania kategorii.');
      }
    });
  }

}
