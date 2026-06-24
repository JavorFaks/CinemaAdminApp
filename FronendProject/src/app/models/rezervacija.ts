import { Bioskop } from "./bioskop";
import { Film } from "./film";

export class Rezervacija {
    id!:number;
    brojOsoba!:number;
    cenaKarte!:number;
    placeno!:boolean;
    datum!:Date;
    bioskop!:Bioskop;
    film!:Film;
}