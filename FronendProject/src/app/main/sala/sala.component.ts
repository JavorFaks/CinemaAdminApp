import { Component, OnInit, ViewChild, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { FormsModule, NgForm } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

import { Sala } from '../../models/sala';
import { SalaService } from '../../services/sala.service';
import { SalaDialogComponent } from '../../dialogs/sala-dialog/sala-dialog.component';

import { RezervacijaComponent } from '../rezervacija/rezervacija.component';
import { Bioskop } from '../../models/bioskop';
import { BioskopService } from '../../services/bioskop.service';

@Component({
  selector: 'app-sala',
  standalone: true,
  imports: [
    CommonModule, FormsModule, MatTableModule, MatIconModule, MatButtonModule,
    MatFormFieldModule, MatInputModule, MatSelectModule,
    MatSortModule, MatPaginatorModule, RezervacijaComponent
  ],
  templateUrl: './sala.component.html',
  styleUrl: './sala.component.css',
})
export class SalaComponent implements OnInit {
  displayedColumns = ['id', 'kapacitet', 'brojRedova', 'bioskop', 'actions'];
  dataSource = new MatTableDataSource<Sala>();

  totalElements = 0;
  pageIndex = 0;
  pageSize = 5;
  sortField = '';
  sortDirection = '';

  parentSelectedSala!: Sala; // Selected sala za dijete

  filterKapacitet: number | null = null;
  filterBrojRedova: number | null = null;
  filterBioskopId: number | null = null;

  bioskopi = signal<Bioskop[]>([]);

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private service: SalaService, 
    private dialog: MatDialog,
    private bioskopService: BioskopService
  ) {}

  ngOnInit() {
    this.loadData();

    this.bioskopService.getAll(0, 100).subscribe({
      next: (response) => this.bioskopi.set(response.content),
      error: (err) => console.log(err)
    });

  }

  // Load data for service
  public loadData(){
    const sort = this.sortField ? `${this.sortField},${this.sortDirection}` : undefined;
    
    this.service.naprednaPretraga(
      this.filterKapacitet,
      this.filterBrojRedova,
      this.filterBioskopId,
      this.pageIndex,
      this.pageSize,
      sort
    ).subscribe({
      next: (response) => {
        this.dataSource.data = response.content;
        this.totalElements = response.totalElements;
      },
      error: (err) => console.log(err)  
    });

  }

  //Potvrda i trazenje sa filterima
  public applyFilters(): void {
    this.pageIndex = 0;
    if(this.paginator){
      this.paginator.pageIndex = 0;
    }
    this.loadData();
  }

  //Resetovanje svih filtera
  public resetFilters() : void {
    this.filterKapacitet = null;
    this.filterBrojRedova = null;
    this.filterBioskopId = null;
    this.applyFilters();
  }

  // Handle page change event
  public onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadData();
  }

  // Sort change event
  public onSortChange(sort: Sort) {
    this.sortField = sort.direction ? sort.active : '';
    this.sortDirection = sort.direction;
    this.pageIndex = 0;  // Reset na prvu stranicu kada se promeni sortiranje

    if(this.paginator){
      this.paginator.pageIndex = 0;
    }

    this.loadData();
  }

  public openDialog(flag: number, row?: Sala): void {
    const ref = this.dialog.open(SalaDialogComponent, { data: row ? { ...row } : {} });

    ref.componentInstance.flag = flag;

    ref.afterClosed().subscribe((response) =>{
      if(response){ //ako je response 1 (add, update, delete) onda reload data
        this.loadData();
      }
    });
  }

    public selectRow  (row: Sala) {
    this.parentSelectedSala = row;
  }

 }
