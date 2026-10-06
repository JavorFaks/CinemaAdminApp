import { Component, Inject, OnInit, signal } from '@angular/core';

import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';

import { Film } from '../../models/film';
import { FilmService } from '../../services/film.service';
import { RezervacijaService } from '../../services/rezervacija.service';
import { Rezervacija } from '../../models/rezervacija';
import { SalaService } from '../../services/sala.service';
import { Sala } from '../../models/sala';


@Component({
  selector: 'app-rezervacija-dialog',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, MatInputModule, MatSelectModule, MatCheckboxModule, FormsModule, MatFormFieldModule, MatDatepickerModule],
  templateUrl: './rezervacija-dialog.component.html',
  styleUrl: './rezervacija-dialog.component.css',
})
export class RezervacijaDialogComponent implements OnInit {
  showSalaSelect = false;

  datumValue: Date | null = null;
  flag!: number;
  filmovi = signal<Film[]>([]);
  sale = signal<Sala[]>([]);


  constructor(  
    private service: RezervacijaService,
    private filmService: FilmService,
    private salaService: SalaService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<RezervacijaDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Rezervacija
  ){}

  //Ocitavanje stranih kljuceva filma
  ngOnInit(): void {
    this.showSalaSelect = !this.data.sala;
    this.datumValue = this.data.datum ? this.parseDate(this.data.datum) : null;

    this.filmService.getAll(0, 100).subscribe({
      next: (response) => this.filmovi.set(response.content),
      error: (err) => {
        console.log(err);
        this.snackBar.open('An error occurred while loading movies.', 'OK', { duration: 2500 });
      }
    });

    this.salaService.getAll(0, 100).subscribe({
    next: (response) => this.sale.set(response.content),
    error: (err) => {
      console.log(err)
      this.snackBar.open('An error occurred while loading halls.', 'OK', { duration: 2500 });
    }
  });
  }

  public compare(a: any, b: any): boolean {
    return a?.id === b?.id;
  }

  //Dodavanje filma
  public add(): void {
    this.data.datum = this.formatDate(this.datumValue);
    this.service.createRezervacija(this.data).subscribe({
      next: () => {
        this.dialogRef.close(1);
        this.snackBar.open('Reservation added successfully!', 'OK', { duration: 2500 });
      },
      error: (err) => {
        console.log(err);
        this.snackBar.open('An error occurred while adding the reservation.', 'OK', { duration: 2500 });
      }
    });
  }

  //Izmjena filma
  public update(): void {
    this.data.datum = this.formatDate(this.datumValue);
    this.service.updateRezervacija(this.data).subscribe({
      next: () =>{
        this.dialogRef.close(1);
        this.snackBar.open('Reservation updated successfully!', 'OK', { duration: 2500 });
      },
      error: (err) => {
        console.log(err);
        this.snackBar.open('An error occurred while updating the reservation.', 'OK', { duration: 2500 });
      }
    });
  }

   //Brisanje filma
  public delete(): void {
    this.service.deleteRezervacija(this.data.id).subscribe({
      next: () =>{
        this.dialogRef.close(1);
        this.snackBar.open('Reservation deleted successfully!', 'OK', { duration: 2500 });
      },
      error: (err) => {
        console.log(err);
        this.snackBar.open('An error occurred while deleting the reservation.', 'OK', { duration: 2500 });
      }
    });
  }

  // Izlazak iz dijaloga
  public cancel(): void {
    this.dialogRef.close();
  }

  private parseDate(value: string):Date{
    const [year, month, day] = value.split('-').map(Number);
    return new Date(year, month - 1, day);
  }

  private formatDate(date: Date | null):string{
    if (!date) { return ''; }
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
}
