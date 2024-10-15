import { Injectable} from '@angular/core';


const TOKEN="token";
const USER="user";

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  constructor() { }

  
  
  static saveToken(token: string):void{
    if (typeof window !== "undefined") {
      window.localStorage.removeItem(TOKEN);
      window.localStorage.setItem(TOKEN,token);
    }
    
  }

  static getUserId(): string{
    const user=this.getUser();
    if(user==null) return "";
    return user.id;
  }

  static saveUser(user: any):void{
    if (typeof window !== "undefined") {
    window.localStorage.removeItem(USER);
    window.localStorage.setItem(USER,JSON.stringify(user));
    }
  }

  static getToken(){
    return (typeof window !== "undefined")?window.localStorage.getItem(TOKEN):null;
  }

  static getUser(){
    const user=localStorage.getItem(USER)
    return (user)?JSON.parse(user):null;
  }

  static getUserRole(){
    const user=this.getUser();
    if(user==null) return "";
    return user.role;
  }

  static isAdminLoggedIn():boolean{
    if(this.getToken()==null) return false;
    const role:string=this.getUserRole();
    return role=="ADMIN";
  }

  static isCustomerLoggedIn():boolean{
    if(this.getToken()==null) return false;
    const role:string=this.getUserRole();
    return role=="CUSTOMER";
  }

  static logout():void{
    if (typeof window !== "undefined") {
    window.localStorage.removeItem(TOKEN);
    window.localStorage.removeItem(USER);
    }
  }

}
