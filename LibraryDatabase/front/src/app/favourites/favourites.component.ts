import { BookService } from './../book.service';
import { Component, OnInit } from '@angular/core';
import { Book } from '../book';

@Component({
  selector: 'app-favourites',
  templateUrl: './favourites.component.html'
})
export class FavouritesComponent implements OnInit{
  
  books: Book[] = [];

  constructor(private bookService: BookService){}

  ngOnInit(): void {
      // this.bookService.getFavorites().subscribe(books => {
      //   this.books = books;
      // });
  }

  onRefreshRequired(): void {
    // this.bookService.getFavorites().subscribe(books => {
    //   this.books = books;
    // });
  }
}
