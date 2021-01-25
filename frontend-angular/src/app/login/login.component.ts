import { Component } from '@angular/core';
import { AppService } from '../app.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import {Credentials} from '../models/credentials.model'
import { environment } from 'src/environments/environment';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent {

  private baseUrl = environment.baseUrl;
  credentials: Credentials = {username: null, password: null};

  constructor(private app: AppService, 
              private router: Router) {
  
  }

  login() {
    this.app.authenticate(this.credentials, () => {
        this.router.navigateByUrl('/');
    });
    return false;
  }

  register(){
    this.app.register(this.credentials, () => {
        this.router.navigateByUrl('/');
    });
    return false;
  }

  requestAuth(){
    window.location.href = (this.baseUrl +'oauth2/authorization/github');
  }

}
