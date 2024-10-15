import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzTableModule } from 'ng-zorro-antd/table';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzMessageService } from 'ng-zorro-antd/message';

@Component({
  selector: 'app-get-bookings',
  standalone: true,
  imports: [NgFor,NgIf,NzButtonModule,NzSpinModule,NzTableModule,CommonModule],
  templateUrl: './get-bookings.component.html',
  styleUrl: './get-bookings.component.css'
})
export class GetBookingsComponent {
  bookings:any;
  isSpinning:boolean=false;

  constructor(private service:AdminService,private message:NzMessageService){
    this.getBookings();
  }

  getBookings(){
    this.isSpinning=true;
    this.service.getCarBookings().subscribe((res)=>{
      console.log(res);
      this.bookings=res;
    })
    this.isSpinning=false;
  }

  changeBookingStatus(id:number,status:string){
    this.isSpinning=true;
    this.service.changeBookingStatus(id,status).subscribe((res)=>{
      this.isSpinning=false;
      this.getBookings();
      this.message.success("Booking Status Changed successfully!",{nzDuration:3000});

    },error=>{
      this.message.error("Something went Wrong",{nzDuration:3000});
    })
  }
}
