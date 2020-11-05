import { Component } from '@angular/core';
import { AppService } from './app.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { User } from './models/user.model';



@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent {
  title: string;



  constructor(private app: AppService,
              private http: HttpClient, private router: Router) {
      this.app.authenticate(undefined, undefined);
          }




}
