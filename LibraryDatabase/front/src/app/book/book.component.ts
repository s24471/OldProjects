import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { Book } from '../book';
import { AuthService } from '../auth.service';
import { BookService } from '../book.service';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html'
})
export class BookComponent implements OnInit{
  @Input() book!: Book;
  @Output() edit = new EventEmitter<Book>();
  @Output() delete = new EventEmitter<Book>();
  @Output() favorite = new EventEmitter<Book>();
  @Output() details = new EventEmitter<Book>();
  @Output() removeFromFavorites = new EventEmitter<Book>();
  
  public isFavorite: boolean = false;

  constructor(public authService: AuthService, private bookService: BookService){}

  ngOnInit(): void {
    this.checkIfFavorite();
  }

  onEdit(): void {
    this.edit.emit(this.book);
  }

  
  onDelete(): void {
    this.delete.emit(this.book);
  }

  onFavorite(): void{
    this.favorite.emit(this.book);
  }

  onDetails(): void{
    this.details.emit(this.book);
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
    }
  }
  onRemoveFromFavorites(): void{
    this.removeFromFavorites.emit(this.book);
  }
}
