import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface VendorData {
  name: string;
  ticketsPerRelease: number;
  releaseInterval: number;
}

@Injectable({
  providedIn: 'root',
})
export class VendorService {
  private apiUrl = 'http://127.0.0.1:8080/vendor';

  constructor(private http: HttpClient) {}

  createVendor(vendorData: VendorData): Observable<any> {
    return this.http.post(this.apiUrl, vendorData);
  }
}
