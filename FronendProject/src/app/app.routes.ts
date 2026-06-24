import { Routes } from '@angular/router';
import { BioskopComponent } from './main/bioskop/bioskop.component';
import { FilmComponent } from './main/film/film.component';
import { SalaComponent } from './main/sala/sala.component';

export const routes: Routes = [

    { path: 'bioskop', component: BioskopComponent },
    { path: 'film', component: FilmComponent },
    { path: 'sala', component: SalaComponent },
];
