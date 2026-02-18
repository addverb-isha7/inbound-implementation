import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { AsnService } from '../../services/asn.service';
import {Asn} from '../../models/asn.model';

@Component({
  selector: 'app-asn-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './asn-page.component.html',
  styleUrls: ['./asn-page.component.css']
})
export class AsnPageComponent {

  asnForm: FormGroup;

  constructor(private fb: FormBuilder,private asnService: AsnService) {
    this.asnForm = this.fb.group({
      shipmentNumber: ['', Validators.required],
      supplier: ['', Validators.required],
      skus: this.fb.array([this.createSku()])
    });
  }

  createSku(): FormGroup {
    return this.fb.group({
      skuId: ['', Validators.required],
      skuName: ['', Validators.required],
      mrp: ['', Validators.required],
      batchNumber: ['', Validators.required],
      expiry: ['', Validators.required],
      expectedQuantity: ['', Validators.required]
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

    const asnData: Asn = this.asnForm.value;

    this.asnService.createAsn(asnData).subscribe({
      next: (response) => {
        console.log('ASN Created:', response);
        alert('ASN Saved Successfully ✅');

        this.asnForm.reset();
        this.skus.clear();
        this.addSku();
          console.log(JSON.stringify(this.asnForm.value));
      },
      error: (error) => {
        console.error('Error creating ASN:', error);
        alert('Error saving ASN ❌');
      }
    });

  }
}


}
