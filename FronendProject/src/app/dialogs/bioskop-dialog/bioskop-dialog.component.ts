import { Component, Inject } from '@angular/core';
import { Bioskop } from '../../models/bioskop';
import { BioskopService } from '../../services/bioskop.service';


import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';

@Component({
  selector: 'app-bioskop-dialog',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, MatInputModule, FormsModule, MatFormFieldModule],
  templateUrl: './bioskop-dialog.component.html',
  styleUrl: './bioskop-dialog.component.css',
})
export class BioskopDialogComponent {

  flag!: number;

  constructor(  
    private service: BioskopService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<BioskopDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Bioskop
  ){}

  //Dodavanje bioskopa
  public add(): void {
    this.service.createBioskop(this.data).subscribe({
      next: () => {
        this.dialogRef.close(1);
        this.snackBar.open('Bioskop je uspešno dodat!', 'OK', { duration: 2500 });
      },
      error: (err) => {
        console.log(err);
        this.snackBar.open('Došlo je do greške prilikom dodavanja bioskopa.', 'OK', { duration: 2500 });
      }
    });
  }

  //Izmjena bioskopa
  public update(): void {
    this.service.updateBioskop(this.data).subscribe({
      next: () =>{
        this.dialogRef.close(1);
        this.snackBar.open('Bioskop je uspešno izmenjen!', 'OK', { duration: 2500 });
      },
      error: (err) => {
        console.log(err);
        this.snackBar.open('Došlo je do greške prilikom izmene bioskopa.', 'OK', { duration: 2500 });
      }
    });
  }

   //Brisanje bioskopa
  public delete(): void {
    this.service.deleteBioskop(this.data.id).subscribe({
      next: () =>{
        this.dialogRef.close(1);
        this.snackBar.open('Bioskop je uspešno obrisan!', 'OK', { duration: 2500 });
      },
      error: (err) => {
        console.log(err);
        this.snackBar.open('Došlo je do greške prilikom brisanja bioskopa.', 'OK', { duration: 2500 });
      }
    });
  }

  // Izlazak iz dijaloga
  public cancel(): void {
    this.dialogRef.close();
  }
}
