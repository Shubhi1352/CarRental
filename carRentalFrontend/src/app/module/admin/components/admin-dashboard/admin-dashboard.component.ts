import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { CommonModule, NgFor } from '@angular/common';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzMessageService } from 'ng-zorro-antd/message';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [RouterLink,NgFor,CommonModule,NzFormModule,NzButtonModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent {

  cars: any=[];

  constructor(private adminService: AdminService,private message: NzMessageService){}

  ngOnInit(){
    this.getAllCars();
  }

  getAllCars(){
    this.adminService.getAllCars().subscribe((res)=>{
      this.cars=[];
      for(let element of res){
        element.processedImage='data:image/jpg;base64,'+ element.returnedImage;
        this.cars.push(element)
      };
    })
  }

  deleteCar(id: number){
    this.adminService.deleteCar(id).subscribe((res)=>{
      this.getAllCars();
      this.message.success("Car Deleted Successfully!!",{nzDuration: 3000});
    })
  }
}
