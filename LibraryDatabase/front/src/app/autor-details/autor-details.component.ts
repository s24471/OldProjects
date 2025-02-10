import { AuthService } from './../auth.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Autor } from '../Autor';
import { AutorService } from '../autor.service';
import { BookService } from '../book.service';
import { Book } from '../book';

@Component({
  selector: 'app-autor-details',
  templateUrl: './autor-details.component.html'
})
export class AutorDetailsComponent implements OnInit{
  autor!: Autor;
  books: Book[] = [];
  isEditMode = false;

  constructor(private autorService: AutorService, private bookService: BookService, private router: Router, private route: ActivatedRoute, public authService: AuthService) { }

  ngOnInit(): void {
    const autorId =  this.route.snapshot.paramMap.get('id');
    if(autorId){
      this.autorService.getAutor(autorId).subscribe(autor => {
        this.autor = autor;
        // this.bookService.getBooksByAutor(autorId).subscribe(books => {
        //   this.books = books;
        // });
      });
    }
  }

  toggleEditMode(): void {
    this.isEditMode = !this.isEditMode;
  }

  saveChanges(): void {
    if (this.autor && this.autor.autorID) {
      this.autorService.updateAutor(this.autor.autorID, this.autor).subscribe({
        next: (updatedAutor) => {
          this.autor = updatedAutor;
          this.isEditMode = false;
          alert('Autor został zaktualizowany pomyślnie.');
        },
        error: (err) => {
          console.error('Error updating autor:', err);
          alert('Wystąpił błąd podczas aktualizacji autora.');
        }
      });
    }
  }

  cancelEdit(): void {
    const autorID = this.route.snapshot.paramMap.get('id');
    if (autorID) {
      this.autorService.getAutor(autorID).subscribe({
        next: (originalAutor) => {
          this.autor = originalAutor;
          this.isEditMode = false;
          
          console.error('anulowano') ;
        },
        error: (err) => {
          console.error('Error fetching autor:', err); 
          alert('Wystąpił błąd podczas ponownego ładowania danych autora.');
       
        }
      });
    } else {
      this.isEditMode = false;
    }
  }
  deleteAutor(id: string): void {
    if(confirm('Czy na pewno chcesz usunąć tego autora i wszystkie jego ksiazki?')) {
      this.autorService.deleteAutor(id).subscribe({
        next: () => {
          alert('autor został usunięty.');
          this.router.navigate(['autor-list']);
        },
        error: err => {
          console.error('Error deleting autor:', err);
          alert('Wystąpił błąd podczas usuwania autora.');
        }
      });
    }
  }
}


