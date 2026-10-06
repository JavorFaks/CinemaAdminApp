import { Routes } from '@angular/router';
import { BioskopComponent } from './main/bioskop/bioskop.component';
import { FilmComponent } from './main/film/film.component';
import { SalaComponent } from './main/sala/sala.component';
import { HomeComponent } from './utility/home/home.component';
import { AboutComponent } from './utility/about/about.component';
import { AuthorComponent } from './utility/author/author.component';
import { RezervacijaComponent } from './main/rezervacija/rezervacija.component';

export const routes: Routes = [

    { path: 'bioskop', component: BioskopComponent },
    { path: 'film', component: FilmComponent },
    { path: 'sala', component: SalaComponent },
    { path: 'rezervacija', component: RezervacijaComponent },

    { path: '', component: HomeComponent, pathMatch: 'full' },
    { path: 'about', component: AboutComponent },
    { path: 'author', component: AuthorComponent },
];
