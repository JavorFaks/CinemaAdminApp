import { Component, Inject, OnInit } from '@angular/core';

import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';

import { Sala } from '../../models/sala';
import { SalaService } from '../../services/sala.service';
import { BioskopService } from '../../services/bioskop.service';
import { Bioskop } from '../../models/bioskop';

@Component({
  selector: 'app-sala-dialog',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, MatInputModule, MatSelectModule, FormsModule, MatFormFieldModule],
  templateUrl: './sala-dialog.component.html',
  styleUrl: './sala-dialog.component.css',
})
export class SalaDialogComponent implements OnInit {

  flag!: number;
  bioskopi: Bioskop[] = [];

  constructor(  
    private service: SalaService,
    private bioskopService: BioskopService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<SalaDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Sala
  ){}

  ngOnInit(): void {
    this.bioskopService.getAll(0, 100).subscribe({
      next: (response) => {
        this.bioskopi = response.content;
      }, error: (err) => {
        console.log(err);
        this.snackBar.open('Došlo je do greške prilikom učitavanja bioskopa.', 'OK', { duration: 2500 });
      }
    });
  }

  public compare(a: Bioskop, b: Bioskop): boolean {
    return a?.id === b?.id;
  }
  //Dodavanje sale
  public add(): void {
    this.service.createSala(this.data).subscribe({
      next: () => {
        this.dialogRef.close(1);
        this.snackBar.open('Sala je uspešno dodata!', 'OK', { duration: 2500 });
      },
      error: (err) => {
        console.log(err);
        this.snackBar.open('Došlo je do greške prilikom dodavanja sale.', 'OK', { duration: 2500 });
      }
    });
  }

  //Izmjena sile
  public update(): void {
    this.service.updateSala(this.data).subscribe({
      next: () =>{
        this.dialogRef.close(1);
        this.snackBar.open('Sala je uspešno izmenjena!', 'OK', { duration: 2500 });
      },
      error: (err) => {
        console.log(err);
        this.snackBar.open('Došlo je do greške prilikom izmene sale.', 'OK', { duration: 2500 });
      }
    });
  }

   //Brisanje sile
  public delete(): void {
    this.service.deleteSala(this.data.id).subscribe({
      next: () =>{
        this.dialogRef.close(1);
        this.snackBar.open('Sala je uspešno obrisana!', 'OK', { duration: 2500 });
      },
      error: (err) => {
        console.log(err);
        this.snackBar.open('Došlo je do greške prilikom brisanja sale.', 'OK', { duration: 2500 });
      }
    });
  }

  // Izlazak iz dijaloga
  public cancel(): void {
    this.dialogRef.close();
  }
}
