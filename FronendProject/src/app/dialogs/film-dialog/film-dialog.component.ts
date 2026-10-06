import { Component, Inject } from '@angular/core';

import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';

import { Film } from '../../models/film';
import { FilmService } from '../../services/film.service';


@Component({
  selector: 'app-film-dialog',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, MatInputModule, FormsModule, MatFormFieldModule],
  templateUrl: './film-dialog.component.html',
  styleUrl: './film-dialog.component.css',
})
export class FilmDialogComponent {

  flag!: number;

  constructor(  
    private service: FilmService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<FilmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Film
  ){}

  //Dodavanje filma
  public add(): void {
    this.service.createFilm(this.data).subscribe({
      next: () => {
        this.dialogRef.close(1);
        this.snackBar.open('Movie added successfully!', 'OK', { duration: 2500 });
      },
      error: (err) => {
        console.log(err);
        this.snackBar.open('An error occurred while adding the movie.', 'OK', { duration: 2500 });
      }
    });
  }

  //Izmjena filma
  public update(): void {
    this.service.updateFilm(this.data).subscribe({
      next: () =>{
        this.dialogRef.close(1);
        this.snackBar.open('Movie updated successfully!', 'OK', { duration: 2500 });
      },
      error: (err) => {
        console.log(err);
        this.snackBar.open('An error occurred while updating the movie.', 'OK', { duration: 2500 });
      }
    });
  }

   //Brisanje filma
  public delete(): void {
    this.service.deleteFilm(this.data.id).subscribe({
      next: () =>{
        this.dialogRef.close(1);
        this.snackBar.open('Movie deleted successfully!', 'OK', { duration: 2500 });
      },
      error: (err) => {
        console.log(err);
        this.snackBar.open('An error occurred while deleting the movie.', 'OK', { duration: 2500 });
      }
    });
  }

  // Izlazak iz dijaloga
  public cancel(): void {
    this.dialogRef.close();
  }
}
