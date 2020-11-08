import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import { HypermediaResult } from '../models/hypermediaResult.model';
import { ProductService } from '../product.service';
import { Product } from '../models/product.model';
import { CartService } from '../cart.service';
import { HttpClient, HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-product-view',
  templateUrl: './product-view.component.html',
  styleUrls: ['./product-view.component.scss']
})
export class ProductViewComponent implements OnInit, OnDestroy {

  product: Product;

  constructor(private products: ProductService, private cart: CartService,
              private ref: ChangeDetectorRef, private http: HttpClient) {}

  ngOnInit(): void {
    var selectedProductName = sessionStorage.getItem('selected_product_name');
    if (selectedProductName != null){
	let params = new HttpParams();
       params = params
       .append('name', selectedProductName);
      this.http.get('http://localhost:8080/products/search/findByName', {params: params})
              .subscribe((data: Product) => {
              this.product = data;
              this.ref.detectChanges();
           });
     }
  }

  plusItem(name: string, product: Product){
     this.cart.plusInCartProduct(name, product);
     this.products.fetchByName(this.product.name)
     .subscribe((data: Product)=>{
      this.product = data;
    });
  }

  ngOnDestroy() {
	sessionStorage.removeItem('selected_product_name');
  }

}
