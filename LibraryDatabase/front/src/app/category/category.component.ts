import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Kategoria } from '../Kategoria';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html'
})
export class CategoryComponent {
  @Input() kategoria!: Kategoria;
  @Output() edit = new EventEmitter<Kategoria>();
  @Output() delete = new EventEmitter<Kategoria>();
  @Output() details = new EventEmitter<Kategoria>();
  constructor(public authService: AuthService){}
  onEdit(): void {
    this.edit.emit(this.kategoria);
  }

  onDelete(): void {
    this.delete.emit(this.kategoria);
  }

  onDetails(): void{
    this.details.emit(this.kategoria);
  }
}
