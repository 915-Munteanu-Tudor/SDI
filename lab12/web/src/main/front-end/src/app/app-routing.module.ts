import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PetsComponent} from "./pets/pets.component";
import {PetDetailComponent} from "./pet-detail/pet-detail.component";
import {CustomerComponent} from "./customer/customer.component";
import {FoodComponent} from "./food/food.component";
import {PurchaseComponent} from "./purchase/purchase.component";

const routes: Routes = [
  {path: 'pet', component: PetsComponent},
  {path: 'pet/detail/:id', component: PetDetailComponent},
  {path: 'food', component: FoodComponent},
  {path: 'customer', component: CustomerComponent},
  {path: 'purchase', component: PurchaseComponent},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
