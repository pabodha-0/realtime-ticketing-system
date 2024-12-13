import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SystemConfigData {
  totalTickets: number;
  ticketReleaseRate: number;
  customerRetrievalRate: number;
  maxTicketCapacity: number;
}

@Injectable({
  providedIn: 'root',
})
export class SystemConfigurationService {
  private apiUrl = 'http://127.0.0.1:8080/ticket-system/configure';

  constructor(private http: HttpClient) {}

  saveSystemConfiguration(configData: SystemConfigData): Observable<any> {
    return this.http.post(this.apiUrl, configData);
  }
}
