import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Product} from './models/product.model';
import { User } from './models/user.model';
import { Observable, Subject, BehaviorSubject } from 'rxjs';
import { Credentials } from './models/credentials.model';


@Injectable({
  providedIn: 'root'
})
export class AppService {

  authenticated: BehaviorSubject <boolean> = new BehaviorSubject(false);
  user: Subject<any> = new Subject();

  constructor(private http: HttpClient) {}

  authenticate(credentials: Credentials, callback) {

        const headers = new HttpHeaders(credentials ? {
            authorization : 'Basic ' + btoa(credentials.username + ':' + credentials.password)
        } : {});

        this.http.get('http://localhost:8080/api/user', {headers}).subscribe((response: any) => {
          if (response.hasOwnProperty('authorizedClientRegistrationId')) {            
                this.fetchUser(response.authorities[0].attributes.login);
                return callback && callback();
            }
            else{
          if (response.name){             
                this.fetchUser(response.name);
                return callback && callback();
              }

          this.authenticated.next(false);
          return callback && callback();
            }
        });
    }

 register(credentials, callback) {

    this.http.post('http://localhost:8080/api/register', {email: credentials.username, password: credentials.password})
        .subscribe(data => {
          return callback && callback();
        });

  }

  fetchUser(email: string){
    this.http.get('http://localhost:8080/api/user/' + email).subscribe((response: User) => {
      this.user.next(response);
      this.authenticated.next(true);
    });
  }

}

