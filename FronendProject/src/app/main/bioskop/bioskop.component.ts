import { Component, ViewChild, OnInit } from '@angular/core';
import { Bioskop } from '../../models/bioskop';
import { BioskopService } from '../../services/bioskop.service';

import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { BioskopDialogComponent } from '../../dialogs/bioskop-dialog/bioskop-dialog.component';
@Component({
  selector: 'app-bioskop',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatToolbarModule, MatIconModule, MatSortModule, MatPaginatorModule],
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

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private service: BioskopService, private dialog: MatDialog) {}

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
