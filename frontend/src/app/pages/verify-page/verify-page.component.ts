import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AsnService } from '../../services/asn.service';

@Component({
  selector: 'app-verify-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './verify-page.component.html'
})
export class VerifyPageComponent implements OnInit {

  verifyForm!: FormGroup;

  message: string | null = null;
  messageType: 'success' | 'error' | null = null;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private asnService: AsnService
  ) {}

  ngOnInit(): void {
    this.verifyForm = this.fb.group({
      shipmentNumber: ['', Validators.required],
      skuId: ['', Validators.required],
      skuName: ['', Validators.required],
      mrp: [null, Validators.required],
      batchNumber: ['', Validators.required],
      expiry: ['', Validators.required],
      quantity: [null, Validators.required]
    });
  }

  onVerify() {

    if (this.verifyForm.invalid) {
      this.showMessage('Please fill all fields properly.', 'error');
      return;
    }

    this.loading = true;
    this.message = null;

    const { shipmentNumber, ...sku } = this.verifyForm.value;

    this.asnService.verifySku(shipmentNumber, sku)
      .subscribe({
        next: () => {
          this.loading = false;
          this.showMessage('Verification successful!', 'success');
          this.verifyForm.reset();
        },
        error: (err) => {
          this.loading = false;
          const backendMessage =
            err?.error?.message ||
            'Verification failed. Please check details.';
          this.showMessage(backendMessage, 'error');
        }
      });
  }

  private showMessage(text: string, type: 'success' | 'error') {
    this.message = text;
    this.messageType = type;

    setTimeout(() => {
      this.message = null;
      this.messageType = null;
    }, 4000);
  }
}
