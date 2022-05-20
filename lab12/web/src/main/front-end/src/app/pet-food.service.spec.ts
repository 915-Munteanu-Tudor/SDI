import { TestBed } from '@angular/core/testing';

import { PetFoodService } from './pet-food.service';

describe('PetFoodService', () => {
  let service: PetFoodService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PetFoodService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
