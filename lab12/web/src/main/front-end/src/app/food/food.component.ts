import {Component, OnInit} from '@angular/core';
import {FoodService} from "../food.service";
import {Router} from "@angular/router";
import {Food} from "../food.model";
import {Pet} from "../pet.model";
import {PetFoodService} from "../pet-food.service";

@Component({
  selector: 'app-food',
  templateUrl: './food.component.html',
  styleUrls: ['./food.component.css']
})
export class FoodComponent implements OnInit {

  selectedFood: Food;
  foodList: Food[];
  petsEatingSelectedFood: Pet[];

  constructor(private foodService: FoodService, private router: Router, private petFoodService: PetFoodService) {
  }

  onSelect(food: Food) {
    this.selectedFood = food;
    this.petFoodService.filterPetsThatEatCertainFood(food.id).subscribe(
      petFood => {
        console.log(petFood);
        this.petsEatingSelectedFood = petFood.petFoods.map(
            (pf: { pet: any; }) => pf.pet
        );
      }
    );

  }

  goToDetail() {
    this.router.navigate(['/food/detail', this.selectedFood.id])
  }

  getFood() {
    this.foodService.getFood().subscribe(
      food => {
        this.foodList = food.foods.map(
            (f: Food) => f = {
            id: f.id,
            name: f.name,
            producer: f.producer,
            expirationDate: new Date(f.expirationDate)
          } as Food
        )
        console.log(this.foodList)
      }
    );
  }

  getValidFood() {
    this.foodService.getValidFood().subscribe(
      food => {
        this.foodList = food.foods.map(
            (f: Food) => f = {
            id: f.id,
            name: f.name,
            producer: f.producer,
            expirationDate: new Date(f.expirationDate)
          } as Food
        )
      }
    )
  }

  add(name: string, producer: string, expirationDate: Date) {
    name = name.trim();
    producer = producer.trim();
    if (!name || !producer || !expirationDate) {
      alert("Please enter all the info for the food");
      return;
    }
    this.foodService.add({name, producer, expirationDate} as Food)
      .subscribe(_ => this.getFood())
  }

  delete(food: Food) {
    this.foodService.delete(food.id).subscribe(_ => this.getFood())
  }

  save() {
    this.foodService.update(this.selectedFood).subscribe()
  }

  ngOnInit(): void {
    this.getFood();
  }

}
