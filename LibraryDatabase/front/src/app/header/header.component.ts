import { AuthService } from './../auth.service';
import { Kategoria } from '../Kategoria';
import { Component, OnInit } from '@angular/core';
import { CategoriesService } from '../categories.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {
  langNapis = "English";
  categories: Kategoria[] = [];
  
  constructor(private categoriesService: CategoriesService, public authService: AuthService) {}

  ngOnInit(): void {
    this.categoriesService.getCategories(-1,-1).subscribe(data => {
      this.categories = data.kategorie;
    });
  }

  logout(): void{
    this.authService.logout();
    alert("Wylogowano");
  }
}
