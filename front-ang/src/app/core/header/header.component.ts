import { Component, OnInit, HostListener, ChangeDetectorRef } from '@angular/core';
import { User } from '../../models/user.model';
import { AppService } from 'src/app/app.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { CartService } from 'src/app/cart.service';
import { ResizedEvent } from 'angular-resize-event';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  title: string;
  login: string;

  styleA: string = 'standard-size-a';
  styleImg: string = 'standard-size-img';

  authenticated: boolean;

constructor(private app: AppService,  private cart: CartService,
              private http: HttpClient, private router: Router, private ref: ChangeDetectorRef) {
    this.title = 'shop';
    this.login = 'login';
}

ngOnInit(): void {
	this.fetchAuthenticated();
}

@HostListener('window:resize', ['$event'])
onResized(event: ResizedEvent) {
    this.styleA = 'standard-size-a';
    this.styleImg = 'standard-size-img';

    if (window.innerWidth <= 800){
      this.styleA = 'low-size-a';
      this.styleImg = 'low-size-img';
    }
    if (window.innerWidth <= 600){
      this.styleA = 'lowest-size-a';
      this.styleImg = 'low-size-img';
    }
    console.log(window.innerWidth.toString());
  }

getUser(){
     return this.cart.user;
   }
getAuthenticated(){
    return this.authenticated;
}

getCartItemsAmount(){
      return this.getUser().inCartProducts.reduce(function(sum, current) {
      return sum + current.amount;
      }, 0);
   }

fetchAuthenticated() { 
	return this.app.authenticated.subscribe((data: boolean) => {
      this.authenticated = data;
      this.ref.detectChanges();
    });; 
	}

logout() {
      this.http.post('http://localhost:8080/api/logout', {}).pipe(finalize(() => {
          this.app.authenticated.next(false);
          this.ref.detectChanges();
          this.app.user.next(null);
          this.router.navigateByUrl('/login');
      })).subscribe();
    }

}
