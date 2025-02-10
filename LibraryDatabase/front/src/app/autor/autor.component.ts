import { AuthService } from './../auth.service';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Autor } from '../Autor'; 

@Component({
  selector: 'app-autor',
  templateUrl: './autor.component.html'
})
export class AutorComponent {
  @Input() autor!: Autor;
  @Output() edit = new EventEmitter<Autor>();
  @Output() delete = new EventEmitter<Autor>();
  @Output() details = new EventEmitter<Autor>();

  constructor(public authService: AuthService){}
  
  onEdit(): void {
    this.edit.emit(this.autor);
  }

  onDelete(): void {
    this.delete.emit(this.autor);
  }

  onDetails(): void{
    this.details.emit(this.autor);
  }
}
