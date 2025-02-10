import { Component, OnInit } from '@angular/core';
import { Kategoria } from '../Kategoria';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoriesService } from '../categories.service';
import { Book } from '../book';
import { BookService } from '../book.service';
import { AuthService } from '../auth.service';

@Component({
  selector: 'category-details',
  templateUrl: './category-details.component.html'
})
export class CategoryDetailsComponent implements OnInit {
  kategoria!: Kategoria;
  books: Book[] = [];
  isEditMode = false;
  constructor(private router: Router, private route: ActivatedRoute,private bookService: BookService, private categoriesService: CategoriesService, public authService: AuthService) { }

  ngOnInit(): void {
    const kategoriaId =  this.route.snapshot.paramMap.get('id');
    if(kategoriaId){
      this.categoriesService.getCategory(kategoriaId).subscribe(kategoria => {
        this.kategoria = kategoria;
      });
      // this.bookService.getBooksByCategory(kategoriaId).subscribe(books => {
      //   this.books = books;
      // });
    }
  }


  toggleEditMode(): void {
    this.isEditMode = !this.isEditMode;
  }
  saveChanges(): void {
    if (this.kategoria && this.kategoria.kategoriaID) {
      this.categoriesService.updateCategory(this.kategoria.kategoriaID, this.kategoria).subscribe({
        next: (updatedCategory) => {
          this.kategoria = updatedCategory;
          this.isEditMode = false;
          alert('Kategoria została zaktualizowana.');
        },
        error: (err) => {
          console.error('Error updating category:', err);
          alert('Wystąpił błąd podczas aktualizacji kategorii.');
        }
      });
    }
  }

  cancelEdit(): void {
    const kategoriaId = this.route.snapshot.paramMap.get('id');
    if (kategoriaId) {
      this.categoriesService.getCategory(kategoriaId).subscribe({
        next: (originalCategory) => {
          this.kategoria = originalCategory;
          this.isEditMode = false;
          
          console.error('anulowano') ;
        },
        error: (err) => {
          console.error('Error fetching category:', err); 
          alert('Wystąpił błąd podczas ponownego ładowania danych kategorii.');
       
        }
      });
    } else {
      this.isEditMode = false;
    }
  }

  deleteCategory(id: string): void {
    if(confirm('Czy na pewno chcesz usunąć tę Kategorie i wszystkie jej książki?')) {
      this.categoriesService.deleteCategory(id).subscribe({
        next: () => {
          alert('Kategoria została usunięta.');
          this.router.navigate(['category-list']);
        },
        error: err => {
          console.error('Error deleting category:', err);
          alert('Wystąpił błąd podczas usuwania kategorii.');
        }
      });
    }
  }
}
