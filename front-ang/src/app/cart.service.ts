import { Injectable } from '@angular/core';
import { AppService } from './app.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { User } from './models/user.model';
import { InCartProduct } from './models/inCartProduct.model';
import { ProductService } from './product.service';
import { Product } from './models/product.model';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  user: User;

  constructor(private app: AppService, private http: HttpClient, private products: ProductService) {
    this.fetchUser();
   }


  fetchUser(){
     console.log('fetchuser cart');
     this.app.user.subscribe(response => {
       if (this.app.authenticated) {
       this.user = response;
       console.log('cartservice USER: auth ' + JSON.stringify(this.user));
       }
       else{
         this.user = null;
         console.log('carservicet USER: noauth ' + JSON.stringify(this.user));
       }
     });
   }

   plusInCartProduct(name: string, product: Product = undefined){

    function predicate(element: InCartProduct, index: number, array: InCartProduct[]){
       if (element.product.name === name) {
        return true;
       }
    }
    let inCartProducts = this.user.inCartProducts;
    let selectedProduct = inCartProducts.find(predicate);
    console.log('inCartProducts ' + JSON.stringify(selectedProduct));
    if (selectedProduct===undefined){
      let selectedProduct = new InCartProduct();
      inCartProducts[inCartProducts.length] = selectedProduct;
      selectedProduct.product = product;
      selectedProduct.amount = 1;
    }
    
    selectedProduct.amount++;
    this.http.post('http://localhost:8080/api/user/' + this.user.id.toString(), this.user)
    .subscribe(
      data => this.app.user.next(data)
    );

    }
   
    minusInCartProduct(name: string){

    function predicate(element: InCartProduct, index: number, array: InCartProduct[]){
       if (element.product.name === name) {
        return true;
       }
    }
    let selectedProduct = this.user.inCartProducts.find(predicate).amount--;
    this.user.inCartProducts = this.user.inCartProducts.filter((value, index, arr)=>{ return value.amount>0;})
    
    this.http.post('http://localhost:8080/api/user/' + this.user.id.toString(), this.user)
    .subscribe(
      data => this.app.user.next(data)
    );

    }
  }
  
