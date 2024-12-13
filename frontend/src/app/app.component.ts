import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './components/header/header.component';
import { SystemLogsComponent } from './components/system-logs/system-logs.component';
import { ManageSystemComponent } from './components/manage-system/manage-system.component';
import { CreateCustomerComponent } from './components/create-customer/create-customer.component';
import { CreateVendorComponent } from './components/create-vendor/create-vendor.component';
import { RemainingTicketsComponent } from './components/remaining-tickets/remaining-tickets.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    HeaderComponent,
    ManageSystemComponent,
    CreateCustomerComponent,
    CreateVendorComponent,
    SystemLogsComponent,
    RemainingTicketsComponent,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'frontend';
}
