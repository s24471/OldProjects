import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { BookService } from '../book.service';
import { Book } from '../book';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'book-list',
  templateUrl: './book-list.component.html'
})
export class BookListComponent implements OnInit, OnChanges{
  @Input() mode: 'all' | 'category' | 'autor' | 'fav' = 'all';
  @Input() booksInput?: Book[];
  @Input() id?: string;
  @Output() refreshRequired = new EventEmitter<void>();
  books: Book[] = [];
  currentPage: number = 1;
  itemsPerPage: number = 5;
  totalItems: number = 0;

  constructor(private router: Router, private bookService: BookService, private activatedRoute: ActivatedRoute) { }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['booksInput'] && changes['booksInput'].currentValue) {
      this.books = changes['booksInput'].currentValue;
    }
  }
  onPageSizeChange(event: Event): void {
    const element = event.target as HTMLSelectElement;
    const newSize = parseInt(element.value, 10);
    this.itemsPerPage = newSize;
    this.currentPage = 1;
    this.getBooks();
  }
  
  onPageChange(newPage: number): void {
    this.currentPage = newPage;
    this.getBooks();
  }
  
  getBooks(): void {
    switch (this.mode) {
      case 'all':
        this.bookService.getBooks(this.currentPage, this.itemsPerPage).subscribe(response => {
          this.books = response.books;
          this.totalItems = response.total;
        });
        break;
      case 'category':
        if (this.id) {
          this.bookService.getBooksByCategory(this.id, this.currentPage, this.itemsPerPage)
            .subscribe(response => {
              this.books = response.books;
              console.error(response.total);
              this.totalItems = response.total;
            });
        }
        break;
      case 'autor':
        if (this.id) {
          this.bookService.getBooksByAutor(this.id, this.currentPage, this.itemsPerPage)
          .subscribe(response => {
            this.books = response.books;
            this.totalItems = response.total;
          });
        }
        break;
      case 'fav':
          this.bookService.getFavorites(this.currentPage, this.itemsPerPage)
          .subscribe(response => {
            this.books = response.books;
            this.totalItems = response.total;
          });
        
        break;
    }
  }

 
  ngOnInit(): void {
    this.getBooks();
  }
  
  onEdit(book: Book): void {
    
  }

  onDelete(book: Book): void {
    if(confirm('Czy na pewno chcesz usunąć tę książkę?')) {
      this.bookService.deleteBook(book.ksiazkaID).subscribe({
        next: () => {
          this.refreshRequired.emit();
          

          let str = this.router.url;
          console.log(this.router.getCurrentNavigation()?.extractedUrl);
          this.router.navigateByUrl('/book-list', { skipLocationChange: true }).then(() => {
            this.router.navigate([str]);
          }); 
          alert('Książka została usunięta.');
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
          this.router.navigateByUrl('/autor-list', { skipLocationChange: true }).then(() => {
            this.router.navigate([str]);
          }); 
      },
      error: (error) => {
        console.error('Błąd podczas dodawania do ulubionych', error);
        alert('Błąd podczas dodawania do ulubionych');
      }
    });
  }
  

  onDetails(book: Book): void {
    this.router.navigate(['book-details', book.ksiazkaID]);
  }

  
  onRemoveFromFavorites(book: Book): void {
    if(confirm('Czy na pewno chcesz usunąć tę książkę z ulubionych?')) {
      this.bookService.removeFromFavorites(book.ksiazkaID).subscribe({
        next: () => {
          this.refreshRequired.emit();
          let str = this.router.url;
          this.router.navigateByUrl('/autor-list', { skipLocationChange: true }).then(() => {
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
