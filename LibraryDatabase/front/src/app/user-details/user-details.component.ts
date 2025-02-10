import { UsersService } from './../user.service';
import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html'
})
export class UserDetailsComponent implements OnInit{
  user!: User;
  isEditMode = false;

  constructor(private usersService: UsersService, private router: Router, private route: ActivatedRoute){}

  ngOnInit(): void {
    const userID =  this.route.snapshot.paramMap.get('id');
    console.log(userID);
    if(userID){
      this.usersService.getUser(userID).subscribe(user => {
        this.user = user;
        console.log(this.user);
        console.log(this.user.login);
      });
    }
  }

  toggleEditMode(): void {
    this.isEditMode = !this.isEditMode;
  }

  saveChanges(): void {
    if (this.user && this.user.uzytkownikID) {
      this.usersService.updateUser(this.user.uzytkownikID, this.user).subscribe({
        next: (updatedUser) => {
          this.user = updatedUser;
          this.isEditMode = false;
          alert('Uzytkownik został zaktualizowany pomyślnie.');
        },
        error: (err) => {
          console.error('Error updating user:', err);
          alert('Wystąpił błąd podczas aktualizacji uzytkownika.');
        }
      });
    }
  }

  cancelEdit(): void {
    const userId = this.route.snapshot.paramMap.get('id');
    if (userId) {
      this.usersService.getUser(userId).subscribe({
        next: (originalUser) => {
          this.user = originalUser;
          this.isEditMode = false;
          
          console.error('anulowano') ;
        },
        error: (err) => {
          console.error('Error fetching user:', err); 
          alert('Wystąpił błąd podczas ponownego ładowania danych User.');
       
        }
      });
    } else {
      this.isEditMode = false;
    }
  }
  deleteUser(id: string): void {
    if(confirm('Czy na pewno chcesz usunąć tego użytkownika?')) {
      this.usersService.deleteUser(id).subscribe({
        next: () => {
          alert('Użytkownik został usunięty.');
          this.router.navigate(['user-list']);
        },
        error: err => {
          console.error('Error deleting user:', err);
          alert('Wystąpił błąd podczas usuwania użytkownika.');
        }
      });
    }
  }
}
