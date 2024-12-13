import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CustomerData, CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-create-customer',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './create-customer.component.html',
  styleUrl: './create-customer.component.css',
})
export class CreateCustomerComponent {
  customerForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService
  ) {
    this.customerForm = this.fb.group({
      name: ['', [Validators.required]],
      retrievalInterval: ['', [Validators.required]],
    });
  }

  onSubmit() {
    if (this.customerForm.valid) {
      const customerData: CustomerData = {
        name: this.customerForm.get('name')?.value,
        retrievalInterval: this.customerForm.get('retrievalInterval')?.value,
      };

      this.customerService.createCustomer(customerData).subscribe({
        next: (response) => {
          console.log('Customer created successfully', response);
          // Optional: Reset form or show success message
          this.customerForm.reset();
        },
        error: (error) => {
          console.error('Error creating customer', error);
          // Optional: Show error message to user
        },
      });
    }
  }
}
