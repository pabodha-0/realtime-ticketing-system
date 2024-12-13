// import { Component } from '@angular/core';
// import {
//   FormBuilder,
//   FormGroup,
//   Validators,
//   ReactiveFormsModule,
// } from '@angular/forms';
// import { HttpClient } from '@angular/common/http';
// import { CommonModule } from '@angular/common';

// @Component({
//   selector: 'app-create-vendor',
//   standalone: true,
//   imports: [ReactiveFormsModule, CommonModule],
//   templateUrl: './create-vendor.component.html',
//   styleUrls: ['./create-vendor.component.css'],
// })
// export class CreateVendorComponent {
//   vendorForm: FormGroup;

//   constructor(private fb: FormBuilder, private http: HttpClient) {
//     // this.vendorForm = this.fb.group({
//     //   name: ['', Validators.required],
//     //   ticketsPerRelease: ['', Validators.required],
//     //   releaseInterval: ['', Validators.required],
//     // });
//   }

//   onSubmit(): void {
//     if (this.vendorForm.valid) {
//       const formData = this.vendorForm.value;
//       this.http.post('http://127.0.0.1:8080/vendor', formData).subscribe({
//         next: (response) => {
//           console.log('Vendor created successfully', response);
//         },
//         error: (error) => {
//           console.error('Error creating vendor', error);
//         },
//       });
//     } else {
//       console.error('Form is invalid');
//     }
//   }
// }

import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { VendorData, VendorService } from '../../services/vendor.service';

@Component({
  selector: 'app-create-vendor',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './create-vendor.component.html',
  styleUrls: ['./create-vendor.component.css'],
})
export class CreateVendorComponent {
  vendorForm: FormGroup;

  constructor(private fb: FormBuilder, private vendorService: VendorService) {
    this.vendorForm = this.fb.group({
      name: ['', [Validators.required]],
      ticketsPerRelease: ['', [Validators.required, Validators.min(1)]],
      releaseInterval: ['', [Validators.required, Validators.min(1)]],
    });
  }

  onSubmit() {
    if (this.vendorForm.valid) {
      const vendorData: VendorData = {
        name: this.vendorForm.get('name')?.value,
        ticketsPerRelease: +this.vendorForm.get('ticketsPerRelease')?.value,
        releaseInterval: +this.vendorForm.get('releaseInterval')?.value,
      };

      this.vendorService.createVendor(vendorData).subscribe({
        next: (response) => {
          console.log('Vendor created successfully', response);
          // Optional: Reset form or show success message
          this.vendorForm.reset();
        },
        error: (error) => {
          console.error('Error creating vendor', error);
          // Optional: Show error message to user
        },
      });
    }
  }
}
