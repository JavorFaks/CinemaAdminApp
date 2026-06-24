import { Component, ViewChild, OnInit } from '@angular/core';

import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

import { Film } from '../../models/film';
import { FilmService } from '../../services/film.service';
import { FilmDialogComponent } from '../../dialogs/film-dialog/film-dialog.component';

@Component({
  selector: 'app-film',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatIconModule, MatSortModule, MatPaginatorModule, MatButtonModule],
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

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private service: FilmService, private dialog: MatDialog) {}

  ngOnInit() {
    this.loadData();
  }

  // Load data for service
  public loadData(){
    const sort = this.sortField ? `${this.sortField},${this.sortDirection}` : undefined;

    this.service.getAll(this.pageIndex, this.pageSize, sort).subscribe({
      next: (response) => {
        this.dataSource.data = response.content;
        this.totalElements = response.totalElements;
      },
      error: (err) => console.log(err)
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

  public openDialog(flag: number, id?: number, naziv?: string, recenzija?: string, trajanje?: number, zanr?: string): void {
    const ref = this.dialog.open(FilmDialogComponent, { data: {id, naziv, recenzija, trajanje, zanr}});

    ref.componentInstance.flag = flag;

    ref.afterClosed().subscribe((response) =>{
      if(response){ //ako je response 1 (add, update, delete) onda reload data
        this.loadData();
      }
    });
  }
 }
