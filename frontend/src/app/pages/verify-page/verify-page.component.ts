import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AsnService } from '../../services/asn.service';
import { Sku } from '../../models/sku.model';

@Component({
  selector: 'app-verify-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './verify-page.component.html',
  styleUrls: ['./verify-page.component.css']
})
export class VerifyPageComponent {

  verifyForm: FormGroup;

  constructor(private fb: FormBuilder, private asnService: AsnService) {
     this.verifyForm = this.fb.group({
      shipmentNumber: ['', Validators.required],
      skuId: ['', Validators.required],
      skuName: ['', Validators.required],
      batchNumber: ['', Validators.required],
      mrp: ['', Validators.required],
      expiry: ['', Validators.required]   // 👈 MUST be expiry, not expiryDate
    });
  }

 onVerify() {

    if (this.verifyForm.valid) {

      const shipmentNumber = this.verifyForm.value.shipmentNumber;

      const sku: Sku = {
        skuId: this.verifyForm.value.skuId,
        skuName: this.verifyForm.value.skuName,
        batchNumber: this.verifyForm.value.batchNumber,
        mrp: this.verifyForm.value.mrp,
        expiry: this.verifyForm.value.expiry
      };

      console.log('Shipment:', shipmentNumber);
      console.log('SKU:', sku);

      this.asnService.verifySku(shipmentNumber, sku).subscribe({
        next: (res) => {
          console.log('Verification Success', res);
          alert('Verified Successfully ✅');
          this.verifyForm.reset();
        },
        error: (err) => {
          console.error('Verification Failed', err);
          alert('Verification Failed ❌');
        }
      });

    }
  }
}
