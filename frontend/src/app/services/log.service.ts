import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LogService {
  private logsUrl = 'http://localhost:8080/logs';

  constructor(private http: HttpClient) {}

  getLogs(): Observable<string[]> {
    return this.http.get<string[]>(this.logsUrl);
  }
}
