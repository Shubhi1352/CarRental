import { Component } from '@angular/core';
import { CustomerService } from '../../services/customer.service';
import { CommonModule, NgFor } from '@angular/common';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-customer-dashboard',
  standalone: true,
  imports: [NgFor,CommonModule,NzButtonModule,RouterLink],
  templateUrl: './customer-dashboard.component.html',
  styleUrl: './customer-dashboard.component.css'
})
export class CustomerDashboardComponent {
  constructor(private service: CustomerService){}

  cars:any=[]

  ngOnInit(){
    this.getAllCars();
  }

  getAllCars(){
    this.service.getAllCars().subscribe((res)=>{
      this.cars=[];
      for(let element of res){
        element.processedImage='data:image/jpg;base64,'+ element.returnedImage;
        this.cars.push(element)
      };
      
    })
  }
}
