import { Component, ViewChild, OnInit, Input, EventEmitter, Output, HostListener } from '@angular/core';
import { AppService } from '../app.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { User } from '../models/user.model';
import { MatSidenav } from '@angular/material/sidenav';
import { Product } from '../models/product.model';
import { HypermediaResult } from '../models/hypermediaResult.model';
import { PageEvent } from '@angular/material/paginator';
import { ProductService } from '../product.service';
import { Router } from '@angular/router';
import { CartService } from '../cart.service';
import { ResizedEvent } from 'angular-resize-event';



@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  title = 'Demo';
  products: Array<Product>;
  length: number;
  pageSize: number;
  pageIndex: number;
  allCategories: Array<string> = ['PC', 'Laptops', 'Monitors' , 'Accessories'];
  category: string;
  
  cols: number = 3;

  constructor(private app: AppService, private http: HttpClient, 
    private product: ProductService, private cart: CartService, private router: Router) {
      console.log('home constr ');
    }

  ngOnInit(): void {
    console.log('home init');
    this.pageSize = 6;
    this.pageIndex = 0;
    this.fetch();

  }
  
  @HostListener('window:resize', ['$event'])
  onResized(event: ResizedEvent) {
    this.cols = 3;
    if (window.innerWidth <= 1050){
      this.cols = 2;
    }
    if (window.innerWidth <= 800){
      this.cols = 1;
    }


  }

   fetch(): void{
     console.log('fetch home');
     this.product.fetch(this.pageSize, this.pageIndex);
     this.product.productData.subscribe((data: HypermediaResult) => {
        this.products = data._embedded.products;
        this.length = data.page.totalElements;
        console.log('RESPONSE DATA home' + JSON.stringify(data));
      });
    }

    fetchByCategory(cat): void{
      if (this.allCategories.includes(cat)){
       this.product.fetchByCategory(this.pageSize, this.pageIndex, this.category);
       this.product.productData.subscribe((data: HypermediaResult) => {
        this.products = data._embedded.products;
        this.length = data.page.totalElements;
        });
      }
      else{
        this.fetch();
      }
    }



  authenticated(): boolean { return this.app.authenticated; }

  getServerData(event: PageEvent): void{
    this.pageIndex = event.pageIndex;
    this.fetch();

  }
  selectProduct(product: Product){
    console.log('pushing prod');
    
    this.router.navigate(['/product']);
    this.product.selectedProduct.next(product);
  }

  plusItem(product){
    if (this.app.getAuthenticated() === true) {
     this.cart.plusInCartProduct(product.name , product);
    }
    else{
      this.router.navigate(['/login']);
    }
  }
    
  
}
