import { Component, ViewChild, OnInit, signal, DestroyRef } from '@angular/core';

import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormsModule} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';


import { Film } from '../../models/film';
import { FilmService } from '../../services/film.service';
import { FilmDialogComponent } from '../../dialogs/film-dialog/film-dialog.component';

@Component({
  selector: 'app-film',
  standalone: true,
  imports: [
    CommonModule, FormsModule, MatTableModule, MatIconModule, MatButtonModule,
    MatFormFieldModule, MatInputModule, MatSelectModule,
    MatSortModule, MatPaginatorModule],
  templateUrl: './film.component.html',
  styleUrl: './film.component.css',
})
export class FilmComponent implements OnInit {
  displayedColumns = ['id', 'naziv', 'recenzija', 'trajanje', 'zanr', 'actions'];
  dataSource = new MatTableDataSource<Film>();

  totalElements = 0;
  pageIndex = 0;
  pageSize = 5;
  sortField = '';
  sortDirection = '';

  filterNaziv = '';
  filterZanr: string | null = null;
  filterMaxMinuta: number | null = null;

  //Get svi postojeci zanrovi u bazi
  zanrovi = signal<string[]>([]);
  
  private nazivSubject = new Subject<string>();

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private service: FilmService, 
    private dialog: MatDialog,
    private destroyRef: DestroyRef
  ) {}

  ngOnInit() {
    this.loadData();

    this.service.getZanrovi().subscribe({
      next: (zanrovi) => this.zanrovi.set(zanrovi),
      error: (err) => console.log(err)
    });

     this.nazivSubject.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(() => this.applyFilters());
  }

  public onNazivChange(value: string): void {
    this.nazivSubject.next(value);
  }


  // Load data for service
  public loadData(){
    const sort = this.sortField ? `${this.sortField},${this.sortDirection}` : undefined;
  
    this.service.naprednaPretraga(
      this.filterNaziv || null,
      this.filterZanr,
      this.filterMaxMinuta,
      this.pageIndex,
      this.pageSize,
      sort
    ).subscribe({
      next: (response) => {
        this.dataSource.data = response.content;
        this.totalElements = response.totalElements;
      },
      error: (err) => console.log(err)
    })
  }

  public applyFilters(): void {
    this.pageIndex = 0;
    this.loadData();
  }

  public resetFilters(): void {
    this.filterNaziv = '';
    this.filterZanr = null;
    this.filterMaxMinuta = null;
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

  public openDialog(flag: number, row?: Film): void {
    const ref = this.dialog.open(FilmDialogComponent, { data: row ? { ...row } : {} });

    ref.componentInstance.flag = flag;

    ref.afterClosed().subscribe((response) =>{
      if(response){ //ako je response 1 (add, update, delete) onda reload data
        this.loadData();
      }
    });
  }
 }
