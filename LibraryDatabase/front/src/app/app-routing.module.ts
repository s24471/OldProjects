import { BookAddComponent } from './book-add/book-add.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AutorListComponent } from './autor-list/autor-list.component';
import { BookListComponent } from './book-list/book-list.component';
import { CategoryListComponent } from './category-list/category-list.component';
import { UserListComponent } from './user-list/user-list.component';
import { BookDetailsComponent } from './book-details/book-details.component';
import { AutorDetailsComponent } from './autor-details/autor-details.component';
import { CategoryDetailsComponent } from './category-details/category-details.component';
import { AutorAddComponent } from './autor-add/autor-add.component';
import { CategoryAddComponent } from './category-add/category-add.component';
import { UserDetailsComponent } from './user-details/user-details.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { FavouritesComponent } from './favourites/favourites.component';

const routes: Routes = [
  { path: '', component: BookListComponent},
  { path: 'home', component: BookListComponent},
  { path: 'book-list', component: BookListComponent},
  { path: 'category-list', component: CategoryListComponent},
  { path: 'book-details/:id', component: BookDetailsComponent },
  { path: 'autor-details/:id', component: AutorDetailsComponent },
  { path: 'category-details/:id', component: CategoryDetailsComponent },
  { path: 'user-details/:id', component: UserDetailsComponent },
  { path: 'user-list', component: UserListComponent},
  { path: 'book-add', component: BookAddComponent },
  { path: 'autor-add', component: AutorAddComponent },
  { path: 'category-add', component: CategoryAddComponent },
  { path: 'login', component: LoginComponent},
  { path: 'register', component: RegisterComponent},
  { path: 'favourites', component: FavouritesComponent},
  { path: 'autor-list', component: AutorListComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
