import { Component } from '@angular/core';
import { CustomerService } from '../../services/customer.service';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import {NzTableModule} from 'ng-zorro-antd/table';
import { CommonModule, NgFor } from '@angular/common';

@Component({
  selector: 'app-my-bookings',
  standalone: true,
  imports: [ NgFor,NzSpinModule,NzTableModule,CommonModule],
  templateUrl: './my-bookings.component.html',
  styleUrl: './my-bookings.component.css'
})
export class MyBookingsComponent {
  bookings: any;
  isSpinning:boolean=false;

  constructor(private service: CustomerService){
    this.getMyBookings();
  }

  getMyBookings(){
    this.isSpinning=true;
    this.service.getBookingsByUserId().subscribe((res)=>{
      this.isSpinning=false;
      console.log(res);
      this.bookings=res;
    })
  }

}
