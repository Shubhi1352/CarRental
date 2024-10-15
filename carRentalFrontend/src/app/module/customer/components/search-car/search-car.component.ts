import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { CommonModule, NgFor } from '@angular/common';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { CustomerService } from '../../services/customer.service';


@Component({
  selector: 'app-search-car',
  standalone: true,
  imports: [CommonModule,NgFor,NzSpinModule,NzFormModule,ReactiveFormsModule,NzSelectModule,NzButtonModule],
  templateUrl: './search-car.component.html',
  styleUrl: './search-car.component.css'
})
export class SearchCarComponent {
  searchCarForm!:FormGroup;
  isSpinning:boolean=false;
  cars:any;

  listOfBrands=["koenigsegg","Bugatti","BMW","AUDI","FERRARI","TESLA","VOLVO","TOYOTA","HONDA","FORD","NISSAN","HYUNDAI","LEXUS","KIA"];
  listOfType=["Petrol","Hybrid","Diesel","Electric","CNG"];
  listOfColor=["Red","White","Blue","Orange","Grey","Silver"];
  listOfTransmission=["Manual","Automatic"];

  constructor(private service:CustomerService,private fb: FormBuilder){
    this.searchCarForm=this.fb.group({
      brand:[null],
      type:[null],
      transmission:[null],
      color:[null],
    })
  }

  searchCar(){
    this.isSpinning=true;
    console.log(this.searchCarForm.value);
    this.service.searchCar(this.searchCarForm.value).subscribe((res)=>{
      console.log(res);
      for(let element of res.carDtoList){
        element.processedImage='data:image/jpeg;base64,'+element.returnedImage;
      };
      this.cars=res.carDtoList;
      this.isSpinning=false;

    })
  }

}
