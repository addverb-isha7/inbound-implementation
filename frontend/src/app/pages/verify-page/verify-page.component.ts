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
skuList: any[] = [];
  message: string | null = null;
  messageType: 'success' | 'error' | null = null;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private asnService: AsnService
  ) {}

loadSkus() {
  const shipment = this.verifyForm.get('shipmentNumber')?.value;

  if (!shipment) return;

  this.asnService.getAsnByShipment(shipment)
    .subscribe({
      next: (asn: any) => {
        this.skuList = asn.skus || [];
      },
      error: () => {
        this.showMessage('ASN not found', 'error');
        this.skuList = [];
      }
    });
}

onSkuChange() {
  const selectedSkuId = this.verifyForm.get('skuId')?.value;

  const selectedSku = this.skuList.find(
    sku => sku.skuId === selectedSkuId
  );

  if (selectedSku) {
    this.verifyForm.patchValue({
      skuName: selectedSku.skuName
    });
  }
}


  ngOnInit(): void {
    this.verifyForm = this.fb.group({
      shipmentNumber: ['', Validators.required],
      skuId: ['', Validators.required],
      skuName: ['', Validators.required],
      mrp: [null, Validators.required],
      batchNumber: ['', Validators.required],
      expiry: ['', Validators.required],
     // quantity: [null, Validators.required]
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
    next: (response: string) => {
      this.loading = false;

      // response example: "Final Status: GOOD"
      const status = response.split(': ')[1]?.trim();

      if (status === 'GOOD') {
        this.showMessage('Verification Successful - Status: GOOD ✅', 'success');
      } 
      else if (status === 'DAMAGED') {
        this.showMessage('Verification Successful - Status: DAMAGED ⚠️', 'error');
      } 
      else if (status === 'PENDING') {
        this.showMessage('Verification Pending ⏳', 'error');
      } 
      else {
        this.showMessage('Unexpected response from server.', 'error');
      }

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
