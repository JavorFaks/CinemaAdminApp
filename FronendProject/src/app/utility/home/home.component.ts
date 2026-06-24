import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { BioskopService } from '../../services/bioskop.service';
import { SalaService } from '../../services/sala.service';
import { FilmService } from '../../services/film.service';
import { RezervacijaService } from '../../services/rezervacija.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, MatIconModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  brojBioskopa = signal(0);
  brojSala = signal(0);
  brojFilmova = signal(0);
  brojRezervacija = signal(0);

  constructor(
    private bioskopService: BioskopService,
    private salaService: SalaService,
    private filmService: FilmService,
    private rezervacijaService: RezervacijaService
  ) {}

  ngOnInit(): void {
    this.bioskopService.getAll(0, 1).subscribe({
      next: (response) => this.brojBioskopa.set(response.totalElements),
      error: (err) => console.log(err)
    });

    this.salaService.getAll(0, 1).subscribe({
      next: (response) => this.brojSala.set(response.totalElements),
      error: (err) => console.log(err)
    });

    this.filmService.getAll(0, 1).subscribe({
      next: (response) => this.brojFilmova.set(response.totalElements),
      error: (err) => console.log(err)
    });

    this.rezervacijaService.getAll(0, 1).subscribe({
      next: (response) => this.brojRezervacija.set(response.totalElements),
      error: (err) => console.log(err)
    });
  }
}