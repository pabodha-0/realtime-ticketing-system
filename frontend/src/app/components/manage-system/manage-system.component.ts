import { HttpClient } from '@angular/common/http';
import { Component, OnInit, OnDestroy, NgZone, signal } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Subscription } from 'rxjs';
import {
  SystemConfigData,
  SystemConfigurationService,
} from '../../services/system-configuration.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-manage-system',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './manage-system.component.html',
  styleUrl: './manage-system.component.css',
})
export class ManageSystemComponent implements OnInit, OnDestroy {
  isConfigPanelOpen = signal(false); // Tracks whether the configuration panel is open
  systemConfigForm: FormGroup;

  isSystemRunning = signal<boolean>(false); // Tracks whether the system is running
  private pollingInterval: any;
  private subscriptions: Subscription = new Subscription(); // To manage all subscriptions

  constructor(
    private http: HttpClient,
    private ngZone: NgZone,
    private fb: FormBuilder,
    private systemConfigService: SystemConfigurationService
  ) {
    this.systemConfigForm = this.fb.group({
      totalTickets: ['', [Validators.required, Validators.min(1)]],
      ticketReleaseRate: ['', [Validators.required, Validators.min(1)]],
      customerRetrievalRate: ['', [Validators.required, Validators.min(1)]],
      maxTicketCapacity: ['', [Validators.required, Validators.min(1)]],
    });
  }

  ngOnInit(): void {
    this.checkSystemStatus(); // Check the status on page load
    this.startPolling(); // Start polling every 5 seconds
  }

  ngOnDestroy(): void {
    this.stopPolling(); // Stop polling when the component is destroyed
    this.subscriptions.unsubscribe(); // Unsubscribe from all subscriptions
  }

  closeConfigPanel() {
    this.isConfigPanelOpen.set(false);
  }

  onSubmit() {
    if (this.systemConfigForm.valid) {
      const systemConfigData: SystemConfigData = {
        totalTickets: +this.systemConfigForm.get('totalTickets')?.value,
        ticketReleaseRate:
          +this.systemConfigForm.get('ticketReleaseRate')?.value,
        customerRetrievalRate: +this.systemConfigForm.get(
          'customerRetrievalRate'
        )?.value,
        maxTicketCapacity:
          +this.systemConfigForm.get('maxTicketCapacity')?.value,
      };

      this.systemConfigService
        .saveSystemConfiguration(systemConfigData)
        .subscribe({
          next: (response) => {
            console.log('System configuration saved successfully', response);
            // Optional: Show success message or perform additional actions
            this.systemConfigForm.reset();
          },
          error: (error) => {
            console.error('Error saving system configuration', error);
            // Optional: Show error message to user
          },
        });
    }
  }

  toggleSystem() {
    const url = this.isSystemRunning()
      ? 'http://localhost:8080/ticket-system/stop'
      : 'http://localhost:8080/ticket-system/start';

    const request = this.http.get(url).subscribe({
      next: () => {
        this.isSystemRunning.set(!this.isSystemRunning()); // Toggle the state
      },
      error: (err) => {
        console.error('Error toggling the system:', err);
      },
    });

    this.subscriptions.add(request); // Add to the subscription management
  }

  private checkSystemStatus(): void {
    const statusRequest = this.http
      .get<boolean>('http://127.0.0.1:8080/ticket-system/status')
      .subscribe({
        next: (response) => {
          this.isSystemRunning.set(response); // Update the state based on the response
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
        this.ngZone.run(() => this.checkSystemStatus()); // Ensure Angular's change detection works
      }, 5000);
    });
  }

  private stopPolling(): void {
    if (this.pollingInterval) {
      clearInterval(this.pollingInterval); // Clear the polling interval
      this.pollingInterval = null;
    }
  }
}
