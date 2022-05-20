import {Component, Input, OnInit} from '@angular/core';
import {PetsService} from "../pets.service";
import {ActivatedRoute, Params} from "@angular/router";
import {switchMap} from "rxjs/operators";
import {Pet} from "../pet.model";
import {Location} from "@angular/common";

@Component({
  selector: 'app-cat-detail',
  templateUrl: './pet-detail.component.html',
  styleUrls: ['./pet-detail.component.css']
})
export class PetDetailComponent implements OnInit {
  @Input() pet: Pet;
  constructor(private petsService: PetsService, private route: ActivatedRoute, private location: Location) { }

  ngOnInit(): void {
    this.route.params.pipe(
      switchMap(
        (params: Params) => this.petsService.getPet(+params['id'])
      )
    ).subscribe(pet => this.pet = pet);
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    this.petsService.update(this.pet).subscribe(_ => this.goBack());
  }
}
