import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzFormModule } from 'ng-zorro-antd/form';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgFor, NgIf } from '@angular/common';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzDatePickerModule } from 'ng-zorro-antd/date-picker';
import { NzMessageService } from 'ng-zorro-antd/message';

@Component({
  selector: 'app-update-car',
  standalone: true,
  imports: [NzDatePickerModule,NzButtonModule,NzSelectModule,ReactiveFormsModule,NzInputModule,NzSpinModule,NzFormModule,NgFor,NgIf],
  templateUrl: './update-car.component.html',
  styleUrl: './update-car.component.css'
})
export class UpdateCarComponent {
  isSpinning:boolean=false;
  carId:number=0;
  existingImage:string|null=null;
  updateForm!:FormGroup;
  imgChanged:boolean=false;
  selectedFile:any;
  imagePreview:string|ArrayBuffer|null=null;
  
  


  listOfOption:Array<{label: string;value: string}>=[];
  listOfBrands=["koenigsegg","Bugatti","BMW","AUDI","FERRARI","TESLA","VOLVO","TOYOTA","HONDA","FORD","NISSAN","HYUNDAI","LEXUS","KIA"];
  listOfType=["Petrol","Hybrid","Diesel","Electric","CNG"];
  listOfColor=["Red","White","Blue","Orange","Grey","Silver"];
  listOfTransmission=["Manual","Automatic"];


  constructor(private router: Router,private adminService: AdminService,private activatedRoute:ActivatedRoute,private fb:FormBuilder,private message: NzMessageService){
  }

  ngOnInit(){
    this.updateForm=this.fb.group({
      name:[null,Validators.required],
      brand:[null,Validators.required],
      type:[null,Validators.required],
      color:[null,Validators.required],
      transmission:[null,Validators.required],
      price:[null,Validators.required],
      description:[null,Validators.required],
      year:[null,Validators.required],
      image:[null]
    });
  this.carId=this.activatedRoute.snapshot.params["id"];
  this.getCarById();
  }

  getCarById(){
    this.isSpinning=true;
    this.adminService.getCarById(this.carId).subscribe((res)=>{
      this.isSpinning=false;
      const carDto=res;
      this.existingImage='data:image/jpeg;base64,'+res.returnedImage;
      
      console.log(carDto);
      this.updateForm.patchValue(carDto);
    }) 
  }

  updateCar(){
    this.isSpinning=true;
    let formData:FormData=new FormData();
    if (this.selectedFile && this.imgChanged ) {
      formData.set('image', this.selectedFile);
      console.log('Image added to FormData:', this.selectedFile);
    }
    else if(this.existingImage) {
      formData.append('image',this.base64ToFile(this.existingImage, 'existing_image.png'))
    }
    const year=this.updateForm.get('year')?.value;
    const convertedYear:Date=new Date(year);
    const formattedYear = `${convertedYear.getFullYear()}-${(convertedYear.getMonth() + 1).toString().padStart(2, '0')}-${convertedYear.getDate().toString().padStart(2, '0')} ${convertedYear.getHours().toString().padStart(2, '0')}:${convertedYear.getMinutes().toString().padStart(2, '0')}:${convertedYear.getSeconds().toString().padStart(2, '0')}.000000`;
   
    formData.set("brand",this.updateForm.get('brand')?.value);
    formData.set("name",this.updateForm.get('name')?.value);
    formData.set("type",this.updateForm.get('type')?.value);
    formData.set("color",this.updateForm.get('color')?.value);
    formData.set("year", formattedYear);
    formData.set("transmission",this.updateForm.get('transmission')?.value);
    formData.set("description",this.updateForm.get('description')?.value);
    formData.set("price",this.updateForm.get('price')?.value);

    
    const entries = (formData as any).entries();  // Bypass TypeScript type checking

    for (const [key, value] of entries) {
      console.log(`${key}: ${value}`);
    }


    this.adminService.updateCar(this.carId,formData).subscribe({
      next:(res)=>{
      this.isSpinning=false;
      this.message.success("Car updated Successfully",{nzDuration: 3000});
      console.log(res);
      this.router.navigateByUrl("/admin/dashboard");
    },error:(error)=>{
      this.isSpinning=false;
      this.message.error("Error while updating Car",{nzDuration:3000});
      console.log(error);
    },
    complete:()=>{
      console.log("Car update operation completed.");
      
    }
    });
  }

  onFileSelected(event:any){
    if(event.target.files && event.target.files.length>0){
    this.selectedFile=event.target.files[0];
    this.imgChanged=true;
    this.existingImage=null;
    console.log('Selected file:', this.selectedFile);
    this.previewImage();
    }else{
      console.log('No file Selected');
      
    }
  }

  previewImage(){
    if(this.selectedFile){
    const reader=new FileReader();
    reader.onload =()=>{
      this.imagePreview=reader.result;
    };
    reader.readAsDataURL(this.selectedFile);
    }
  }


  base64ToFile(base64Image: string, fileName: string): File {
    const byteString = atob(base64Image.split(',')[1]); // Decode Base64 string
    const mimeString = base64Image.split(',')[0].split(':')[1].split(';')[0]; // Get MIME type
  
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }
  
    const blob = new Blob([ab], { type: mimeString }); // Create Blob
    return new File([blob], fileName, { type: mimeString }); // Convert Blob to File
  }
}
