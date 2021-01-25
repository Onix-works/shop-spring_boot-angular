import { Injectable, OnDestroy } from '@angular/core';
import { HypermediaResult } from './models/hypermediaResult.model';
import { HttpParams, HttpClient } from '@angular/common/http';
import { Product } from './models/product.model';
import { Subject, BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  productData: Subject<HypermediaResult> = new Subject();
  currentPage: number;
  pageSize: number;

  constructor(private http: HttpClient) {}

  fetch(pageSize: number, pageIndex: number): void{
      this.http.get('http://localhost:8080/products?size=' + pageSize + '&page=' + pageIndex)
      .subscribe((data: HypermediaResult) =>
      {
        this.productData.next(data);
      });
    }

    fetchByCategory(pageSize: number, pageIndex: number, category: string): void{
       let params = new HttpParams();
       params = params
       .append('size', pageSize.toString())
       .append('page', pageIndex.toString())
       .append('ctgr', category);

       this.http.get('http://localhost:8080/products/search/findByCategories', {params: params})
      .subscribe((data: HypermediaResult) =>
      {
        this.productData.next(data);
      });
    }

    fetchByName(name: string){
      let params = new HttpParams();
       params = params
       .append('name', name);
      return this.http.get('http://localhost:8080/products/search/findByName', {params: params})
    }
 
}
