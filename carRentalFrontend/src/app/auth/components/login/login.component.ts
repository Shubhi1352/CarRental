import { Component } from '@angular/core';
import { RouterModule,Router,RouterLink } from '@angular/router';
import { SignupComponent } from '../signup/signup.component';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzInputModule } from 'ng-zorro-antd/input';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { AuthService } from '../../../services/auth/auth.service';
import { StorageService } from '../../../services/storage/storage.service';
import { NzMessageService } from 'ng-zorro-antd/message';



@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterLink,NzButtonModule,NzSpinModule,ReactiveFormsModule,NzFormModule,NzInputModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  constructor(private fb: FormBuilder,private authService:AuthService,private router:Router,private message:NzMessageService){}
  isSpinning: boolean=false;
  loginForm!: FormGroup;

  ngOnInit(){
    this.loginForm=this.fb.group({
      email:[null,[Validators.email,Validators.required]],
      password:[null,[Validators.required]]
    })
  }

  login(){
    console.log(this.loginForm.value);
    this.authService.login(this.loginForm.value).subscribe((res)=>{
      console.log(res);
      if(res.userId!=null){
        const user={
          id:res.userId,
          role:res.userRole
        }
        StorageService.saveUser(user);
        StorageService.saveToken(res.jwt);
        if(StorageService.isAdminLoggedIn()){
          this.router.navigateByUrl("/admin/dashboard");
        }else if(StorageService.isCustomerLoggedIn()){
          this.router.navigateByUrl("/customer/dashboard");
        }else{
          this.message.error("Bad Credentials :( ",{nzDuration: 5000});
        }
      }
    })
  }
}
