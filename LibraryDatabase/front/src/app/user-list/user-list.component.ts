import { UsersService } from './../user.service';
import { Component } from '@angular/core';
import { User } from '../user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html'
})
export class UserListComponent {
  users: User[] = [];
  currentPage: number = 1;
  itemsPerPage: number = 5;
  totalItems: number = 0;

  constructor(private router: Router, private usersService: UsersService) { }

  ngOnInit(): void {
    this.usersService.getUsers(this.currentPage, this.itemsPerPage).subscribe(data => {
      this.users = data.users;
      this.totalItems = data.total;
    });
  }

  onEdit(user: User): void {
    
  }
  onPageSizeChange(event: Event): void {
    const element = event.target as HTMLSelectElement;
    const newSize = parseInt(element.value, 10);
    this.itemsPerPage = newSize;
    this.currentPage = 1;
    this.usersService.getUsers(this.currentPage, this.itemsPerPage).subscribe(data => {
      this.users = data.users;
      this.totalItems = data.total;
    });
  }
  
  onPageChange(newPage: number): void {
    this.currentPage = newPage;
    this.usersService.getUsers(this.currentPage, this.itemsPerPage).subscribe(data => {
      this.users = data.users;
      this.totalItems = data.total;
    });
  }
  onDelete(user: User): void {
    if(confirm('Czy na pewno chcesz usunąć tego użytkownika?')) {
      this.usersService.deleteUser(user.uzytkownikID).subscribe({
        next: () => {
          alert('Użytkownik został usunięty.');
          this.usersService.getUsers(this.currentPage, this.itemsPerPage).subscribe(data => {
            this.users = data.users;
            this.totalItems = data.total;
          });
          this.router.navigate(['user-list']);
        },
        error: err => {
          console.error('Error deleting user:', err);
          alert('Wystąpił błąd podczas usuwania użytkownika.');
        }
      });
    }
  }

  onDetails(user: User): void {
    this.router.navigate(['user-details', user.uzytkownikID]);
  }
}
