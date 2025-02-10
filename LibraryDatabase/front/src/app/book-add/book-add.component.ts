import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { BookService } from '../book.service';
import { Book } from '../book';
import { AutorService } from '../autor.service';
import { Autor } from '../Autor';
import { Kategoria } from '../Kategoria';
import { CategoriesService } from '../categories.service';

@Component({
  selector: 'book-add',
  templateUrl: './book-add.component.html'
})
export class BookAddComponent implements OnInit {
  newBook: Book = {
    autorID: -1,
    ksiazkaID: '',
    kategoriaID: '',
    tytul: '',
    rokWydania: -1,
    nazwaKategorii: '',
    imie: '',
    nazwisko: '',
    rokUrodzenia: 0
  }; 
  autors: Autor[] = [];
  kategorie!: Kategoria[];

  constructor(private categoriesService: CategoriesService, private router: Router, private bookService: BookService, private autorService: AutorService) { }

  ngOnInit(): void {
    this.autorService.getAutors(-1, -1).subscribe(data => {
      this.autors = data.autors;
    });
    
    this.categoriesService.getCategories(-1,-1).subscribe(kategorie => {
      this.kategorie = kategorie.kategorie;
    });
  }

  addBook(): void {
    this.bookService.addBook(this.newBook).subscribe({
      next: () => {
        alert('Książka została dodana.');
        this.router.navigate(['book-list']);
      },
      error: err => {
        console.error('Error adding book:', err);
        alert('Wystąpił błąd podczas dodawania książki.');
      }
    });
  }
}
