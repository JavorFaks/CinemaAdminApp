import { Film } from "./film";
import { Sala } from "./sala";

export class Rezervacija {
    id!:number;
    brojOsoba!:number;
    cenaKarte!:number;
    placeno!:boolean;
    datum!:string;
    sala!:Sala;
    film!:Film;
}