import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import {NzSelectModule} from 'ng-zorro-antd/select';
import {NzDatePickerModule} from 'ng-zorro-antd/date-picker';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AdminService } from '../../../services/admin.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { error } from 'console';
import { Router } from '@angular/router';


@Component({
  selector: 'app-post-car',
  standalone: true,
  imports: [NzSpinModule,NzFormModule,NzButtonModule,NzInputModule,NzLayoutModule,CommonModule,NzSelectModule,NzDatePickerModule,ReactiveFormsModule],
  templateUrl: './post-car.component.html',
  styleUrl: './post-car.component.css'
})
export class PostCarComponent {

  postCarForm!:FormGroup;

  isSpinning:boolean=false;
  selectedFile!: File|null;
  imagePreview!:string|ArrayBuffer|null;
  listOfOption:Array<{label: string;value: string}>=[];
  listOfBrands=["koenigsegg","Bugatti","BMW","AUDI","FERRARI","TESLA","VOLVO","TOYOTA","HONDA","FORD","NISSAN","HYUNDAI","LEXUS","KIA"];
  listOfType=["Petrol","Hybrid","Diesel","Electric","CNG"];
  listOfColor=["Red","White","Blue","Orange","Grey","Silver"];
  listOfTransmission=["Manual","Automatic"];

  constructor(private fb:FormBuilder,private adminService:AdminService,private message: NzMessageService
    ,private router:Router
  ){

  }

  ngOnInit(){
    this.postCarForm=this.fb.group({
      name:[null,Validators.required],
      brand:[null,Validators.required],
      type:[null,Validators.required],
      color:[null,Validators.required],
      transmission:[null,Validators.required],
      price:[null,Validators.required],
      description:[null,Validators.required],
      year:[null,Validators.required],
    })
  }

  postCar(){
    if(this.postCarForm.invalid){
      this.showNotificationMessage('Invalid Car Post :(', 3000); 
      return;
    }
    this.isSpinning=true;
    console.log(this.postCarForm.value)
    let formData=new FormData();
    if (this.selectedFile) {
      formData.set('image', this.selectedFile);
    }

    const year=this.postCarForm.get('year')?.value;
    const convertedYear:Date=new Date(year);
    const formattedYear = `${convertedYear.getFullYear()}-${(convertedYear.getMonth() + 1).toString().padStart(2, '0')}-${convertedYear.getDate().toString().padStart(2, '0')} ${convertedYear.getHours().toString().padStart(2, '0')}:${convertedYear.getMinutes().toString().padStart(2, '0')}:${convertedYear.getSeconds().toString().padStart(2, '0')}.000000`;

    formData.set("brand",this.postCarForm.get('brand')?.value);
    formData.set("name",this.postCarForm.get('name')?.value);
    formData.set("type",this.postCarForm.get('type')?.value);
    formData.set("color",this.postCarForm.get('color')?.value);
    formData.set("year",formattedYear);
    formData.set("transmission",this.postCarForm.get('transmission')?.value);
    formData.set("description",this.postCarForm.get('description')?.value);
    formData.set("price",this.postCarForm.get('price')?.value);
    
    this.adminService.postCar(formData).subscribe({
      next:(res)=>{
      this.isSpinning=false;
      this.message.success("Car posted Successfully",{nzDuration: 3000});
      console.log(res);
      this.router.navigateByUrl("/admin/dashboard");
    },error:(error)=>{
      this.isSpinning=false;
      this.message.error("Error while posting Car",{nzDuration:3000})
    },
    complete:()=>{
      console.log("Car post operation completed.");
      
    }
    });
  }

  onFileSelected(event:any) {
    this.selectedFile=event.target.files[0];
    this.previewImage();
  }

  previewImage(){
    if(this.selectedFile){
      const reader=new FileReader();
    reader.onload=()=>{
      this.imagePreview=reader.result;
    }
    reader.readAsDataURL(this.selectedFile);
    }  
  }

  showNotification = false;
  notificationMessage = '';

  showNotificationMessage(message: string,duration: number) {
    this.notificationMessage = message;
    this.showNotification = true;
    setTimeout(() => this.showNotification = false, duration);
  }


}
