import { HttpClient } from '@angular/common/http';
import { Component, NgZone, signal } from '@angular/core';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-remaining-tickets',
  standalone: true,
  imports: [],
  templateUrl: './remaining-tickets.component.html',
  styleUrl: './remaining-tickets.component.css',
})
export class RemainingTicketsComponent {
  remainingTickets = signal<number>(0); // Tracks whether the system is running
  private pollingInterval: any;
  private subscriptions: Subscription = new Subscription(); // To manage all subscriptions

  constructor(private http: HttpClient, private ngZone: NgZone) {}

  ngOnInit(): void {
    this.checkRemainingTickets(); // Check the status on page load
    this.startPolling(); // Start polling every 5 seconds
  }

  ngOnDestroy(): void {
    this.stopPolling(); // Stop polling when the component is destroyed
    this.subscriptions.unsubscribe(); // Unsubscribe from all subscriptions
  }

  private checkRemainingTickets(): void {
    const statusRequest = this.http
      .get<number>('http://127.0.0.1:8080/ticket-system/current-tickets')
      .subscribe({
        next: (response) => {
          this.remainingTickets.set(response); // Update the state based on the response
        },
        error: (err) => {
          console.error('Error checking system status:', err);
        },
      });

    this.subscriptions.add(statusRequest); // Add to the subscription management
  }

  private startPolling(): void {
    this.ngZone.runOutsideAngular(() => {
      this.pollingInterval = setInterval(() => {
        this.ngZone.run(() => this.checkRemainingTickets()); // Ensure Angular's change detection works
      }, 1000);
    });
  }

  private stopPolling(): void {
    if (this.pollingInterval) {
      clearInterval(this.pollingInterval); // Clear the polling interval
      this.pollingInterval = null;
    }
  }
}
