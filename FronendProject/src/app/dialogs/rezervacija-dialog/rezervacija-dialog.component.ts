import { Component, Inject, OnInit } from '@angular/core';

import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';


import { Film } from '../../models/film';
import { FilmService } from '../../services/film.service';
import { RezervacijaService } from '../../services/rezervacija.service';
import { Rezervacija } from '../../models/rezervacija';


@Component({
  selector: 'app-rezervacija-dialog',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, MatInputModule, MatSelectModule, MatCheckboxModule, FormsModule, MatFormFieldModule],
  templateUrl: './rezervacija-dialog.component.html',
  styleUrl: './rezervacija-dialog.component.css',
})
export class RezervacijaDialogComponent implements OnInit {

  flag!: number;
  filmovi: Film[] = [];
  
  constructor(  
    private service: RezervacijaService,
    private filmService: FilmService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<RezervacijaDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Rezervacija
  ){}

  //Ocitavanje stranih kljuceva filma
  ngOnInit(): void {
    this.filmService.getAll(0, 100).subscribe({
      next: (response) => { this.filmovi = response.content; },
      error: (err) => {
        console.log(err);
        this.snackBar.open('Došlo je do greške prilikom učitavanja filmova.', 'OK', { duration: 2500 });
      }
    });
  }

  public compare(a: Film, b: Film): boolean {
    return a?.id === b?.id;
  }

  //Dodavanje filma
  public add(): void {
    this.service.createRezervacija(this.data).subscribe({
      next: () => {
        this.dialogRef.close(1);
        this.snackBar.open('Rezervacija je uspešno dodata!', 'OK', { duration: 2500 });
      },
      error: (err) => {
        console.log(err);
        this.snackBar.open('Došlo je do greške prilikom dodavanja rezervacije.', 'OK', { duration: 2500 });
      }
    });
  }

  //Izmjena filma
  public update(): void {
    this.service.updateRezervacija(this.data).subscribe({
      next: () =>{
        this.dialogRef.close(1);
        this.snackBar.open('Rezervacija je uspešno izmenjena!', 'OK', { duration: 2500 });
      },
      error: (err) => {
        console.log(err);
        this.snackBar.open('Došlo je do greške prilikom izmene rezervacije.', 'OK', { duration: 2500 });
      }
    });
  }

   //Brisanje filma
  public delete(): void {
    this.service.deleteRezervacija(this.data.id).subscribe({
      next: () =>{
        this.dialogRef.close(1);
        this.snackBar.open('Rezervacija je uspešno obrisana!', 'OK', { duration: 2500 });
      },
      error: (err) => {
        console.log(err);
        this.snackBar.open('Došlo je do greške prilikom brisanja rezervacije.', 'OK', { duration: 2500 });
      }
    });
  }

  // Izlazak iz dijaloga
  public cancel(): void {
    this.dialogRef.close();
  }
}
