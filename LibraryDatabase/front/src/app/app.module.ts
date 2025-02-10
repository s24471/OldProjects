import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { BookListComponent } from './book-list/book-list.component';
import { BookComponent } from './book/book.component';
import { AutorListComponent } from './autor-list/autor-list.component';
import { AutorComponent } from './autor/autor.component';
import { CategoryListComponent } from './category-list/category-list.component';
import { CategoryComponent } from './category/category.component';
import { UserComponent } from './user/user.component';
import { UserListComponent } from './user-list/user-list.component';
import { BookDetailsComponent } from './book-details/book-details.component';
import { AutorDetailsComponent } from './autor-details/autor-details.component';
import { CategoryDetailsComponent } from './category-details/category-details.component';
import { FormsModule } from '@angular/forms';
import { BookAddComponent } from './book-add/book-add.component';
import { AutorAddComponent } from './autor-add/autor-add.component';
import { CategoryAddComponent } from './category-add/category-add.component';
import { UserDetailsComponent } from './user-details/user-details.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { ReactiveFormsModule } from '@angular/forms';
import { FavouritesComponent } from './favourites/favourites.component';
import { CeilPipe } from './ceil.pipe';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    BookListComponent,
    BookComponent,
    AutorListComponent,
    AutorComponent,
    CategoryListComponent,
    CategoryComponent,
    UserComponent,
    UserListComponent,
    BookDetailsComponent,
    AutorDetailsComponent,
    CategoryDetailsComponent,
    BookAddComponent,
    AutorAddComponent,
    CategoryAddComponent,
    UserDetailsComponent,
    LoginComponent,
    RegisterComponent,
    FavouritesComponent,
    CeilPipe,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
