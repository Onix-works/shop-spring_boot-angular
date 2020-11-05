import { BrowserModule } from '@angular/platform-browser';
import { NgModule, Injectable } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HttpInterceptor, HttpRequest, HttpHandler,
   HTTP_INTERCEPTORS,  HttpXsrfTokenExtractor, HttpEvent } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';
import { Observable } from 'rxjs';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { AngularResizedEventModule } from 'angular-resize-event';

import { AppService } from './app.service';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { CoreModule } from './core/core.module';
import { MyMaterialModule } from './my-material.module';
import { CartComponent } from './cart/cart.component';
import { CartService } from './cart.service';
import { ProductViewComponent } from './product-view/product-view.component';
import { ProductService } from './product.service';


const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'home'},
  { path: 'home', component: HomeComponent},
  { path: 'login', component: LoginComponent},
  { path: 'cart', component: CartComponent},
  { path: 'product', component: ProductViewComponent},
];

@Injectable()
export class HttpXsrfInterceptor implements HttpInterceptor {

  constructor(private tokenExtractor: HttpXsrfTokenExtractor) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const headerName = 'X-XSRF-TOKEN';
    const token = this.tokenExtractor.getToken() as string;

    if (token !== null && !req.headers.has(headerName)) {
      req = req.clone({ headers: req.headers.set(headerName, token) });
    }
    return next.handle(req);
  }
}

@Injectable()
export class XhrInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const xhr = req.clone({
      headers: req.headers.set('X-Requested-With', 'XMLHttpRequest'),
      withCredentials: true
    });
    return next.handle(xhr);
  }
}

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    CartComponent,
    ProductViewComponent,
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    CoreModule,
    BrowserAnimationsModule,
    MyMaterialModule,
    AngularResizedEventModule
  ],
  providers: [AppService,
    CartService,
    ProductService,
   { provide: HTTP_INTERCEPTORS, useClass: XhrInterceptor, multi: true },
   {provide: HTTP_INTERCEPTORS, useClass: HttpXsrfInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
