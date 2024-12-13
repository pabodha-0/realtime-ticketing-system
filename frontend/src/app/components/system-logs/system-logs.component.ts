import { Component, OnDestroy, OnInit, NgZone } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { interval, Subscription, BehaviorSubject } from 'rxjs';
import { switchMap, tap, shareReplay } from 'rxjs/operators';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-system-logs',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './system-logs.component.html',
  styleUrls: ['./system-logs.component.css'],
})
export class SystemLogsComponent implements OnInit, OnDestroy {
  private pollingSubscription?: Subscription;
  private pollingActive = new BehaviorSubject<boolean>(false);
  public logs: {
    logId: number;
    severity: string;
    timestamp: string;
    message: string;
  }[] = []; // List to store the logs

  constructor(private http: HttpClient, private ngZone: NgZone) {}

  ngOnInit(): void {
    console.log('Component initialized. Starting polling...');

    // Prevent repeated subscriptions
    if (!this.pollingActive.value) {
      this.pollingActive.next(true);

      this.ngZone.runOutsideAngular(() => {
        this.pollingSubscription = interval(1000)
          .pipe(
            tap(() => console.log('Polling triggered')),
            switchMap(() =>
              this.http.get<
                {
                  logId: number;
                  severity: string;
                  timestamp: string;
                  message: string;
                }[]
              >('http://localhost:8080/logs')
            ),
            shareReplay(1) // Cache the result for repeated subscribers
          )
          .subscribe(
            (response) => {
              console.log('API Response:', response);
              this.ngZone.run(() => {
                // Append only new logs to the logs array
                const existingTimestamps = new Set(
                  this.logs.map((log) => log.logId)
                );
                const newLogs = response.filter(
                  (log) => !existingTimestamps.has(log.logId)
                );
                this.logs.push(...newLogs);
              });
            },
            (error) => {
              console.error('API Error:', error);
            }
          );
      });
    }
  }

  ngOnDestroy(): void {
    console.log('Component destroyed. Stopping polling...');
    this.pollingSubscription?.unsubscribe();
    this.pollingActive.next(false);
  }
}
