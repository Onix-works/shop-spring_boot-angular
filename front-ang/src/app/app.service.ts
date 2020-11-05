import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Product} from './models/product.model';
import { User } from './models/user.model';
import { Observable, Subject } from 'rxjs';
import { Credentials } from './models/credentials.model';

/*interface Response{
    name;
  }*/


@Injectable({
  providedIn: 'root'
})
export class AppService {


  authenticated = false;
  user: Subject<any> = new Subject();




  constructor(private http: HttpClient) {

    console.log('app constr ');

  }
  getAuthenticated(): boolean{
    return this.authenticated;
  }

  authenticate(credentials: Credentials, callback) {

        const headers = new HttpHeaders(credentials ? {
            authorization : 'Basic ' + btoa(credentials.username + ':' + credentials.password)
        } : {});


        this.http.get('http://localhost:8080/api/user', {headers}).subscribe((response: any) => {
          if (response.hasOwnProperty('authorizedClientRegistrationId')) {
                console.log('oauth');
                this.authenticated = true;
                this.fetchUser(response.authorities[0].attributes.login);
                return callback && callback();
            }
            else{
          if (response.name){
                console.log('basicauth');
                this.authenticated = true;
                this.fetchUser(response.name);
                return callback && callback();
              }


          console.log('noauth');
          this.authenticated = false;
          return callback && callback();
            }



        });
    }

 register(credentials, callback) {


    this.http.post('http://localhost:8080/api/register', {email: credentials.username, password: credentials.password})

        .subscribe(data => {
          console.log('RESPONSE DATA ' + JSON.stringify(data));
          return callback && callback();
        });

  }

  fetchUser(email: string){
    this.http.get('http://localhost:8080/api/user/' + email).subscribe((response: User) => {
      console.log('fetchuser service ' );
      this.user.next(response);
    });
  }

}

