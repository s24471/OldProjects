import { CategoriesService } from './../categories.service';
import { Component } from '@angular/core';
import { Kategoria } from '../Kategoria';
import { Router } from '@angular/router';

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html'
})
export class CategoryListComponent {
  kategorie: Kategoria[] = [];
  
  currentPage: number = 1;
  itemsPerPage: number = 5;
  totalItems: number = 10;
    constructor(private router: Router, private categoriesService: CategoriesService) { }
  
    ngOnInit(): void {
      this.categoriesService.getCategories(this.currentPage, this.itemsPerPage).subscribe(data => {
        this.kategorie = data.kategorie;
        this.totalItems = this.totalItems;
      });
    }
  
    onEdit(kategoria: Kategoria): void {
      
    }
    onPageSizeChange(event: Event): void {
      const element = event.target as HTMLSelectElement;
      const newSize = parseInt(element.value, 10);
      this.itemsPerPage = newSize;
      this.currentPage = 1;
      this.categoriesService.getCategories(this.currentPage, this.itemsPerPage).subscribe(data => {
        this.kategorie = data.kategorie;
        this.totalItems = this.totalItems;
      });
    }
    
    onPageChange(newPage: number): void {
      this.currentPage = newPage;
      this.categoriesService.getCategories(this.currentPage, this.itemsPerPage).subscribe(data => {
        this.kategorie = data.kategorie;
        this.totalItems = this.totalItems;
      });
    }
    onDelete(kategoria: Kategoria): void {
      if(confirm('Czy na pewno chcesz usunąć tę Kategorie i wszystkie jej książki?')) {
        this.categoriesService.deleteCategory(kategoria.kategoriaID).subscribe({
          next: () => {
            alert('Kategoria została usunięta.');
            this.categoriesService.getCategories(this.currentPage, this.itemsPerPage).subscribe(data => {
              this.kategorie = data.kategorie;
              this.totalItems = this.totalItems;
            });
            this.router.navigate(['category-list']);
          },
          error: err => {
            console.error('Error deleting category:', err);
            alert('Wystąpił błąd podczas usuwania kategorii.');
          }
        });
      }
    }
  
    onDetails(kategoria: Kategoria): void {
      console.log("A: ", kategoria.kategoriaID);
      this.router.navigate(['category-details', kategoria.kategoriaID]);
    }
}
