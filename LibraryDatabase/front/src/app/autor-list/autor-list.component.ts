import { AutorService } from './../autor.service';
import { Component, OnInit } from '@angular/core';
import { Autor } from '../Autor';
import { Router } from '@angular/router';
import { AutorComponent } from '../autor/autor.component';
@Component({
  selector: 'autor-list',
  templateUrl: './autor-list.component.html' 
})
export class AutorListComponent implements OnInit{
    autors: Autor[] = [];
    currentPage: number = 1;
    itemsPerPage: number = 5;
    totalItems: number = 0;

    constructor(private router: Router, private autorService: AutorService) { }
  
    ngOnInit(): void {
      this.autorService.getAutors(this.currentPage, this.itemsPerPage).subscribe(data => {
        this.autors = data.autors;
        this.totalItems = data.total
      });
    }
  
    onEdit(autor: Autor): void {
      
    }
    onPageSizeChange(event: Event): void {
      const element = event.target as HTMLSelectElement;
      const newSize = parseInt(element.value, 10);
      this.itemsPerPage = newSize;
      this.currentPage = 1;
      this.autorService.getAutors(this.currentPage, this.itemsPerPage).subscribe(data => {
        this.autors = data.autors;
        this.totalItems = data.total
      });
    }
    onPageChange(newPage: number): void {
      this.currentPage = newPage;
      this.autorService.getAutors(this.currentPage, this.itemsPerPage).subscribe(data => {
        this.autors = data.autors;
        this.totalItems = data.total
      });
    }
    onDelete(autor: Autor): void {
        if(confirm('Czy na pewno chcesz usunąć tego autora i wszystkie jego ksiazki?')) {
          this.autorService.deleteAutor(autor.autorID).subscribe({
            next: () => {
              alert('autor został usunięty.');
              this.autorService.getAutors(this.currentPage, this.itemsPerPage).subscribe(data => {
                this.autors = data.autors;
                this.totalItems = data.total
              });
        
              this.router.navigate(['autor-list']);
            },
            error: err => {
              console.error('Error deleting autor:', err);
              alert('Wystąpił błąd podczas usuwania autora.');
              this.router.navigate(['autor-list']);
            }
          });
        }
      
    }
  
    onDetails(autor: Autor): void {
      this.router.navigate(['autor-details', autor.autorID]);
    }
}
