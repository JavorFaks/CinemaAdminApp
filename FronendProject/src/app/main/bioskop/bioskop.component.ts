import { Component, ViewChild, OnInit } from '@angular/core';
import { Bioskop } from '../../models/bioskop';
import { BioskopService } from '../../services/bioskop.service';

import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { BioskopDialogComponent } from '../../dialogs/bioskop-dialog/bioskop-dialog.component';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { Subject } from 'rxjs';
import { DestroyRef } from '@angular/core';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
@Component({
  selector: 'app-bioskop',
  standalone: true,
  imports: [
    CommonModule, FormsModule, MatTableModule, MatIconModule, MatButtonModule,
    MatFormFieldModule, MatInputModule, MatSortModule, MatPaginatorModule
  ],
  templateUrl: './bioskop.component.html',
  styleUrl: './bioskop.component.css',
})
export class BioskopComponent implements OnInit {
  displayedColumns = ['id', 'naziv', 'adresa', 'actions'];
  dataSource = new MatTableDataSource<Bioskop>();

  totalElements = 0;
  pageIndex = 0;
  pageSize = 5;
  sortField = '';
  sortDirection = '';

  filterNaziv = '';
  filterAdresa = '';

  private nazivSubject = new Subject<string>();


  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private service: BioskopService, 
    private dialog: MatDialog,
    private destroyRef: DestroyRef) {}

  ngOnInit():void {
    this.loadData();

    this.nazivSubject.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(() => this.applyFilters());
  }

  // Load data for service
  public loadData(){
    const sort = this.sortField ? `${this.sortField},${this.sortDirection}` : undefined;

    this.service.naprednaPretraga(
      this.filterNaziv || null,
      this.filterAdresa || null,
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

  public onNazivChange(value: string): void {
    this.nazivSubject.next(value);
  }

 public applyFilters(): void {
    this.pageIndex = 0;
    this.loadData();
  }

  public resetFilters(): void {
    this.filterNaziv = '';
    this.filterAdresa = '';
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

  public openDialog(flag: number, id?: number, naziv?: string, adresa?: string): void {
    const ref = this.dialog.open(BioskopDialogComponent, { data: {id, naziv, adresa}});

    ref.componentInstance.flag = flag;

    ref.afterClosed().subscribe((response) =>{
      if(response){ //ako je response 1 (add, update, delete) onda reload data
        this.loadData();
      }
    });
  }
 }
