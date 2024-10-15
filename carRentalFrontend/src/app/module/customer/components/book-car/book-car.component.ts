import { Component } from '@angular/core';
import { CustomerService } from '../../services/customer.service';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CommonModule, NgIf } from '@angular/common';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzDatePickerModule } from 'ng-zorro-antd/date-picker';
import { StorageService } from '../../../../services/storage/storage.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { Router } from '@angular/router';


@Component({
  selector: 'app-book-car',
  standalone: true,
  imports: [NzDatePickerModule,NzFormModule,ReactiveFormsModule,NzSpinModule,RouterLink,CommonModule,NgIf,NzButtonModule],
  templateUrl: './book-car.component.html',
  styleUrl: './book-car.component.css'
})
export class BookCarComponent {
  isSpinning:boolean=false;
  carId:number=0;
  car:any;
  processedImage:any;
  validateForm!:FormGroup;
  dateFormat="dd_MM_YYYY";

  constructor(private router: Router,private message: NzMessageService,private fb: FormBuilder,private customerService: CustomerService,private activatedRoute: ActivatedRoute){}

  ngOnInit(){
    this.carId=this.activatedRoute.snapshot.params["id"];
    this.getCarById();
    this.validateForm=this.fb.group({
      toDate:[null,Validators.required],
      fromDate:[null,Validators.required],
    })
  }

  getCarById(){
    this.customerService.getCarById(this.carId).subscribe((res)=>{
      console.log(res);
      this.processedImage='data:image/jpeg;base64,'+res.returnedImage;
      this.car=res;
    })
  }

  bookACar(data: any){
    console.log("data ",data);
    this.isSpinning=true;
    let bookACarDto={
      toDate:data.toDate,
      fromDate: data.fromDate,
      userId:StorageService.getUserId(),
      carId: this.carId
    }
    console.log("bookACarDto  ",bookACarDto);
    
    this.customerService.bookACar(bookACarDto).subscribe((res)=>{
      console.log("res ",res);
      this.message.success("Booking request submitted successfully",{nzDuration: 3000});
      this.router.navigateByUrl("/customer/dashboard");
    },error=>{
      this.message.error("Something went wrong",{nzDuration:3000});
    })
    this.isSpinning=false;
  }
}
