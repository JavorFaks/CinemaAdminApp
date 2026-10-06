import { Component, Input, OnChanges, OnInit, Output, EventEmitter, ViewChild, signal,SimpleChanges } from '@angular/core';
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

import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';

import { Film } from '../../models/film';
import { FilmService } from '../../services/film.service';
import { SalaService } from '../../services/sala.service';
@Component({
  selector: 'app-rezervacija',
  standalone: true,
  imports: [
    CommonModule, FormsModule, MatTableModule, MatIconModule, MatButtonModule,
    MatFormFieldModule, MatInputModule, MatSelectModule, MatSortModule, MatPaginatorModule, MatDatepickerModule
  ],
  templateUrl: './rezervacija.component.html',
  styleUrl: './rezervacija.component.css',
})
export class RezervacijaComponent implements OnChanges, OnInit{
  @Input() childSelectedSala?: Sala; //Dobijanje sale iz roditelj komponente
  @Output() closed = new EventEmitter<void>();
  
  dataSource = new MatTableDataSource<Rezervacija>();

  totalElements = 0;
  pageIndex = 0;
  pageSize = 5;
  sortField = '';
  sortDirection = '';

  filterFilmId: number | null = null;
  filterSalaId: number | null = null;
  filterPlaceno: boolean | null = null;
  filterDatumOdValue: Date | null = null;
  filterDatumDoValue: Date | null = null;

  filmovi = signal<Film[]>([]);
  sale = signal<Sala[]>([]);

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private service: RezervacijaService, 
    private filmService: FilmService,
    private salaService: SalaService,
    private dialog: MatDialog
  ) {}

  public get isNested(): boolean {
    return !!this.childSelectedSala;
  }

  public get displayedColumns(): string[] {
    return this.isNested
      ? ['id', 'datum', 'brojOsoba', 'cenaKarte', 'placeno', 'film', 'actions']
      : ['id', 'datum', 'brojOsoba', 'cenaKarte', 'placeno', 'film', 'sala', 'actions'];
  }

  //Validacija unosa datuma
  public get datumRangeInvalid(): boolean {
    if(!this.filterDatumOdValue || !this.filterDatumDoValue){
      return false;
    }

    return this.filterDatumOdValue.getTime() > this.filterDatumDoValue.getTime();
  }

  ngOnInit(): void {
    this.filmService.getAll(0, 100).subscribe({
      next: (response) => this.filmovi.set(response.content),
      error: (err) => console.log(err)
    });

    if (!this.isNested) {
      this.salaService.getAll(0, 100).subscribe({
        next: (response) => this.sale.set(response.content),
        error: (err) => console.log(err)
      });
      this.loadData();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['childSelectedSala']) {
      this.pageIndex = 0;
      this.loadData();
    }
  }


  // Load data for service
  public loadData(){
    const sort = this.sortField ? `${this.sortField},${this.sortDirection}` : undefined;
    const salaId = this.isNested ? this.childSelectedSala!.id : this.filterSalaId;
    const datumOd = this.formatDate(this.filterDatumOdValue);
    const datumDo = this.formatDate(this.filterDatumDoValue);

    this.service.naprednaPretraga(
      this.filterFilmId,
      salaId,
      this.filterPlaceno,
      datumOd || null,
      datumDo || null,
      this.pageIndex,
      this.pageSize,
      sort
    ).subscribe({
      next: (response) =>{
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
    this.filterFilmId = null;
    this.filterSalaId = null;
    this.filterPlaceno = null;
    this.filterDatumOdValue = null;
    this.filterDatumDoValue = null;
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

  public openDialog(flag: number, row?: Rezervacija): void {
    const ref = this.dialog.open(RezervacijaDialogComponent, { data: row ? { ...row } : {} });

    ref.componentInstance.flag = flag;

    if(this.isNested){
      ref.componentInstance.data.sala = this.childSelectedSala!;
    }
    

    ref.afterClosed().subscribe((response) =>{
      if(response){ //ako je response 1 (add, update, delete) onda reload data
        this.loadData();
      }
    });
  }


  private formatDate(date: Date | null): string {
    if (!date) { return ''; }
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }


 }
