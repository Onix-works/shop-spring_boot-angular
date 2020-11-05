import { Component, OnInit, OnDestroy } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import { ProductService } from '../product.service';
import { Product } from '../models/product.model';
import { CartService } from '../cart.service';

@Component({
  selector: 'app-product-view',
  templateUrl: './product-view.component.html',
  styleUrls: ['./product-view.component.scss']
})
export class ProductViewComponent implements OnInit, OnDestroy {

  product: Product;

  constructor(private products: ProductService, private cart: CartService) {
    console.log('product constr');
  }

  ngOnInit(): void {
    console.log('product init');
    this.products.selectedProduct
    .subscribe((data: Product)=>{
      this.product = data;
      console.log('product view ' + this.product.name.toString());
    })
  }

  plusItem(name: string, product: Product){
     this.cart.plusInCartProduct(name, product);
     this.products.fetchByName(this.product.name)
     .subscribe((data: Product)=>{
      this.products.selectedProduct.next(data);
      console.log('product view ' + this.product.name.toString());
    });
  }

  ngOnDestroy() {
    console.log('product destroy');
  }

}
