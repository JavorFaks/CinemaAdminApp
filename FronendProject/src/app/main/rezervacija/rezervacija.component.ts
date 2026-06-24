import { Component, Input, OnChanges, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { Sala } from '../../models/sala';
import { Rezervacija } from '../../models/rezervacija';
import { RezervacijaService } from '../../services/rezervacija.service';
import { RezervacijaDialogComponent } from '../../dialogs/rezervacija-dialog/rezervacija-dialog.component';

@Component({
  selector: 'app-rezervacija',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatIconModule, MatButtonModule, MatSortModule, MatPaginatorModule],
  templateUrl: './rezervacija.component.html',
  styleUrl: './rezervacija.component.css',
})
export class RezervacijaComponent implements OnChanges{
  @Input() childSelectedSala!: Sala; //Dobijanje sale iz roditelj komponente

  displayedColumns = ['id', 'brojOsoba', 'cenaKarte', 'datum', 'placeno', 'film', 'actions'];
  dataSource = new MatTableDataSource<Rezervacija>();

  totalElements = 0;
  pageIndex = 0;
  pageSize = 5;
  sortField = '';
  sortDirection = '';

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private service: RezervacijaService, private dialog: MatDialog) {}

  ngOnChanges() {
    this.pageIndex = 0;
    this.loadData();
  }


  // Load data for service
  public loadData(){
    const sort = this.sortField ? `${this.sortField},${this.sortDirection}` : undefined;

    /*
    this.service.getAll(this.pageIndex, this.pageSize, sort).subscribe({
      next: (response) => {
        this.dataSource.data = response.content;
        this.totalElements = response.totalElements;
      },
      error: (err) => console.log(err)
    });
    */

    this.service.getRezervacijaBySala(this.childSelectedSala.id, this.pageIndex, this.pageSize, sort).subscribe({
      next: (response) => {
        this.dataSource.data = response.content;
        this.totalElements = response.totalElements;
      },
      error: (err) => {
        if(err.status === 404){
          this.dataSource.data = [];
          this.totalElements = 0;
        } else {
          console.log(err);
        }
      }
    });
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

  public openDialog(flag: number, row?: Rezervacija): void {
    const ref = this.dialog.open(RezervacijaDialogComponent, { data: row ? { ...row } : {} });

    ref.componentInstance.flag = flag;
    ref.componentInstance.data.sala = this.childSelectedSala;

    ref.afterClosed().subscribe((response) =>{
      if(response){ //ako je response 1 (add, update, delete) onda reload data
        this.loadData();
      }
    });
  }
 }
