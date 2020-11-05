import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';
import { RouterModule } from '@angular/router';
import { MyMaterialModule } from '../my-material.module';



@NgModule({
  imports: [
    RouterModule,
    CommonModule,
    MyMaterialModule
  ],
  exports: [
    HeaderComponent,
    FooterComponent
  ],
  declarations: [
    HeaderComponent,
    FooterComponent
  ]
})
export class CoreModule { }
