import { NgIf } from '@angular/common';
import { Component, NgModule } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink, RouterModule} from '@angular/router';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { AuthService } from '../../../services/auth/auth.service';
import { NzMessageService } from 'ng-zorro-antd/message';


@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [NzButtonModule,NgIf,NzFormModule,NzSpinModule,NzInputModule,RouterModule,RouterLink,ReactiveFormsModule,FormsModule],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
  constructor(private router: Router,private message: NzMessageService,private fb: FormBuilder, private authService: AuthService){}
  signupForm!: FormGroup;
  isSpinning: boolean=false;
  ngOnInit(){
    this.signupForm=this.fb.group({
      name: [null,[Validators.required]],
      email: [null,[Validators.required,Validators.email]],
      password: [null,[Validators.required]],
      checkPassword: [null,[Validators.required,this.confirmationValidate]],
    })
  }

  confirmationValidate=(control: FormControl):{[s: string]:boolean}=>{
    if(!control.value){
      return {required: true};
    }else if(control.value !== this.signupForm.controls['password'].value){
      return {confirm: true,error: true};
    }
    return {};
  };

  register(){
    console.log("Signup form obj ",this.signupForm.value);
    
    this.authService.register(this.signupForm.value).subscribe((res)=>{
      console.log("Res:::  ",res);  
      if(res.id !=null){
        this.message.success("SignUp Successful",{nzDuration: 3000});
        this.router.navigateByUrl("/login");
      }else{
        this.message.error("Something went wrong",{nzDuration: 3000});
      }
    });    
  }
  
}
