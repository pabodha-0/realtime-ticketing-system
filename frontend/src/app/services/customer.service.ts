import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CustomerData {
  name: string;
  retrievalInterval: string;
}

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private apiUrl = 'http://127.0.0.1:8080/customer';

  constructor(private http: HttpClient) {}

  createCustomer(customerData: CustomerData): Observable<any> {
    return this.http.post(this.apiUrl, customerData);
  }
}
