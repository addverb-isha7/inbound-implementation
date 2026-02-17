import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-verify-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './verify-page.component.html',
  styleUrls: ['./verify-page.component.css']
})
export class VerifyPageComponent {

  verifyForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.verifyForm = this.fb.group({
      shipmentNumber: [''],
      skuId: [''],
      mrp: [''],
      batchNumber: [''],
      expiryDate: ['']
    });
  }

  onVerify() {
    console.log(this.verifyForm.value);
  }
}
