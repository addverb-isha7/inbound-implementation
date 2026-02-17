import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';

@Component({
  selector: 'app-asn-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './asn-page.component.html',
  styleUrls: ['./asn-page.component.css']
})
export class AsnPageComponent {

  asnForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.asnForm = this.fb.group({
      shipmentNumber: ['', Validators.required],
      supplierName: ['', Validators.required],
      skus: this.fb.array([this.createSku()])
    });
  }

  createSku(): FormGroup {
    return this.fb.group({
      skuId: ['', Validators.required],
      skuName: ['', Validators.required],
      mrp: ['', Validators.required],
      batchNumber: ['', Validators.required],
      expiryDate: ['', Validators.required],
      quantity: ['', Validators.required]
    });
  }

  get skus(): FormArray {
    return this.asnForm.get('skus') as FormArray;
  }

  addSku() {
    this.skus.push(this.createSku());
  }

  removeSku(index: number) {
    this.skus.removeAt(index);
  }

  onSubmit() {
    if (this.asnForm.valid) {
      console.log(this.asnForm.value);
    }
  }
}
