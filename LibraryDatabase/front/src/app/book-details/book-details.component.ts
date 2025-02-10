import { CategoriesService } from './../categories.service';
import { Component, OnInit } from '@angular/core';
import { BookService } from '../book.service';
import { Book } from '../book';
import { ActivatedRoute, Router } from '@angular/router';
import { Autor } from '../Autor';
import { AutorService } from '../autor.service';
import { Kategoria } from '../Kategoria';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html'
})
export class BookDetailsComponent implements OnInit {
  book!: Book;
  isEditMode = false;
  autors!: Autor[];
  kategorie!: Kategoria[];
   
  public isFavorite: boolean = false;

  constructor(private categoriesService: CategoriesService, private router: Router, private route: ActivatedRoute, private autorService: AutorService, private bookService: BookService, public authService: AuthService) { }

  ngOnInit(): void {
    const bookId =  this.route.snapshot.paramMap.get('id');
    if(bookId){
      this.bookService.getBook(bookId).subscribe(book => {
        this.book = book;
        this.checkIfFavorite();
      });
    }
    this.autorService.getAutors(-1, -1).subscribe(autors => {
      this.autors = autors.autors;
    });
    this.categoriesService.getCategories(-1,-1).subscribe(kategorie => {
      this.kategorie = kategorie.kategorie;
    });
  }
  
  checkIfFavorite() {
    if (this.book && this.book.ksiazkaID) {
      this.bookService.isFavorite(this.book.ksiazkaID).subscribe({
        next: (response) => {
          this.isFavorite = response.isFavorite;
        },
        error: (err) => {
          console.error('Błąd podczas sprawdzania ulubionych', err);
        }
      });
    }else{
      console.error(this.book);
    }
  }
  toggleEditMode(): void {
    this.isEditMode = !this.isEditMode;
  }

  saveChanges(): void {
    if (this.book && this.book.ksiazkaID) {
      this.bookService.updateBook(this.book.ksiazkaID, this.book).subscribe({
        next: (updatedBook) => {
          this.book = updatedBook;
          this.isEditMode = false;
          alert('Książka została zaktualizowana pomyślnie.');
        },
        error: (err) => {
          console.error('Error updating book:', err);
          alert('Wystąpił błąd podczas aktualizacji książki.');
        }
      });
    }
  }

  cancelEdit(): void {
    const bookId = this.route.snapshot.paramMap.get('id');
    if (bookId) {
      this.bookService.getBook(bookId).subscribe({
        next: (originalBook) => {
          this.book = originalBook;
          this.isEditMode = false;
          
          console.error('anulowano') ;
        },
        error: (err) => {
          console.error('Error fetching book:', err); 
          alert('Wystąpił błąd podczas ponownego ładowania danych książki.');
       
        }
      });
    } else {
      this.isEditMode = false;
    }
  }
  deleteBook(id: string): void {
    if(confirm('Czy na pewno chcesz usunąć tę książkę?')) {
      this.bookService.deleteBook(id).subscribe({
        next: () => {
          alert('Książka została usunięta.');
          this.router.navigate(['book-list']);
        },
        error: err => {
          console.error('Error deleting book:', err);
          alert('Wystąpił błąd podczas usuwania książki.');
        }
      });
    }
  }
  onFavorite(book: Book): void {
    this.bookService.addToFavorites(book.ksiazkaID).subscribe({ next: () => {
        console.log('Książka dodana do ulubionych');
        alert('Książka dodana do ulubionych.');
        let str = this.router.url;
        this.router.navigateByUrl('/book-list', { skipLocationChange: true }).then(() => {
          this.router.navigate([str]);
        }); 
      },
      error: (error) => {
        console.error('Błąd podczas dodawania do ulubionych', error);
        alert('Błąd podczas dodawania do ulubionych');
      }
    });
  }

  
  onRemoveFromFavorites(book: Book): void {
    if(confirm('Czy na pewno chcesz usunąć tę książkę z ulubionych?')) {
      this.bookService.removeFromFavorites(book.ksiazkaID).subscribe({
        next: () => {
          let str = this.router.url;
          this.router.navigateByUrl('/book-list', { skipLocationChange: true }).then(() => {
            this.router.navigate([str]);
          }); 
          alert('Książka została usunięta z ulubionych.');
        },
        error: err => {
          console.error('Błąd podczas usuwania książki z ulubionych:', err);
          alert('Wystąpił błąd podczas usuwania książki z ulubionych.');
        }
      });
    }
  }
}
