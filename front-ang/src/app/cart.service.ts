import { Injectable } from '@angular/core';
import { AppService } from './app.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { User } from './models/user.model';
import { InCartProduct } from './models/inCartProduct.model';
import { ProductService } from './product.service';
import { Product } from './models/product.model';
import { map } from 'rxjs/operators';

@Injectable({
	providedIn: 'root'
})
export class CartService {

	user: User;
	authenticated = false;

	constructor(private app: AppService, private http: HttpClient, private products: ProductService) {
		this.fetchUser();
	}

	fetchUser() {

		this.app.user.subscribe(response => {
			if (this.app.authenticated) {
				this.user = response;
			}
			else {
				this.user = null;
			}
		});
	}

	plusInCartProduct(name: string, product: Product = undefined) {

		function predicate(element: InCartProduct, index: number, array: InCartProduct[]) {
			if (element.product.name === name) {
				return true;
			}
		}
		var inCartProducts = this.user.inCartProducts;
		var selectedProduct = inCartProducts.find(predicate);
		if (selectedProduct === undefined) {
			var selectedProduct = new InCartProduct();
			inCartProducts[inCartProducts.length] = selectedProduct;
			selectedProduct.product = product;
			selectedProduct.amount = 0;
		}
		selectedProduct.amount++;

		return this.http.post('http://localhost:8080/api/user/' + this.user.id.toString(), this.user)
			.pipe(map(
				data => this.app.user.next(data)
			));

	}

	minusInCartProduct(name: string) {

		function predicate(element: InCartProduct, index: number, array: InCartProduct[]) {
			if (element.product.name === name) {
				return true;
			}
		}
		var selectedProduct = this.user.inCartProducts.find(predicate);
		if (selectedProduct.amount > 0) {
			console.log("MOOOORE")
			selectedProduct.amount--;
			this.user.inCartProducts = this.user.inCartProducts.filter(
				(value, index, arr) => { return value.amount > 0; }
			)

			this.http.post('http://localhost:8080/api/user/' + this.user.id.toString(), this.user)
				.subscribe(
					data => this.app.user.next(data)
				);
		} else {
			console.log("LEEEESS")
			return;
		}

	}
}

