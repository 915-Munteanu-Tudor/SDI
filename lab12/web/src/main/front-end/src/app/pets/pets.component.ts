import {Component, OnInit} from '@angular/core';
import {Pet} from "../pet.model";
import {PetsService} from "../pets.service";
import {Router} from "@angular/router";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-pets',
  templateUrl: './pets.component.html',
  styleUrls: ['./pets.component.css']
})
export class PetsComponent implements OnInit {
  pets: Array<Pet>;
  selectedPet: Pet;

  addPetForm = new FormGroup({
    petName: new FormControl('', Validators.required),
    petBreed: new FormControl('', Validators.required),
    petYears: new FormControl('', Validators.required)
  })

  addPetFormMethod(data: any) {
    let name = data.petName.trim();
    let breed = data.petBreed.trim();
    let age = data.petYears.trim();

    if (!name || !breed || !age) {
      alert("Please fill all fields");
      return;
    }
    this.petService.add({
      name: name,
      breed: breed,
      age: Number(age)
    } as Pet)
      .subscribe(
        _ => this.getPets()
      )
  }

  onSubmit() {
    this.addPetFormMethod(this.addPetForm.value)
  }

  constructor(private petService: PetsService, private router: Router) {
  }

  onSelect(pet: Pet): void {
    this.selectedPet = pet;
  }

  goToDetail(): void {
    this.router.navigate(['/pet/detail', this.selectedPet.id]);
  }

  add(name: string, breed: string, age: string): void {
    name = name.trim();
    breed = breed.trim();
    age = age.trim();
    if (!name || !breed || !age) {
      return;
    }
    const petYears = Number(age.trim());
    this.petService.add({name, breed, age: petYears} as Pet)
      .subscribe(_ => this.getPets());

  }

  getPets(): void {
    this.petService.getPets()
      .subscribe(pets => this.pets = pets.pets);
    console.log(this.pets);
  }

  delete(pet: Pet): void {
    this.pets = this.pets.filter(p => p !== pet);
    this.petService.delete(pet.id).subscribe();
  }

  save(): void {
    this.petService.update(this.selectedPet).subscribe();
  }

  orderPetsByAge(): void {
    this.pets.sort((a: Pet, b: Pet) => a.age - b.age)
  }

  ngOnInit(): void {
    this.getPets();
  }

}
