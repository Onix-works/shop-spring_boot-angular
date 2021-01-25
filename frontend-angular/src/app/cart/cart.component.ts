import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import { HttpClient } from '@angular/common/http';
import { User } from '../models/user.model';
import { Router } from '@angular/router';
import { CartService } from '../cart.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {


  constructor(private app: AppService, private cart: CartService,
              private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
  }

  getUser(){
     return this.cart.user;
   }

  plusItem(name){
     this.cart.plusInCartProduct(name);
  }

  minusItem(name){
     this.cart.minusInCartProduct(name);
  }

  getPrice(): number{
    return this.getUser().inCartProducts.reduce(function(sum, current) {
    return sum + current.product.price*current.amount;
    }, 0);

  }

  makeOrder(){
    this.router.navigate(['/home']);
  }

}
