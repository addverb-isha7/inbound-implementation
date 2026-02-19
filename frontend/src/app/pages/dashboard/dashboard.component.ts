import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Asn } from '../../models/asn.model';
import { AsnService } from '../../services/asn.service';


@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {


  asns: Asn[] = [];
  expandedShipment: string | null = null;
  searchShipment: string = '';

  constructor(private asnService: AsnService) {}

  ngOnInit(): void {
    console.log('Dashboard loaded');
    this.loadAllAsns();
  }

  loadAllAsns() {
    this.asnService.getAllAsns().subscribe({
      next: (data) => {
        console.log('ASN Response:', data);
        this.asns = data || [];
      },
      error: (err) => {
        console.error('Error loading ASNs', err);
      }
    });
  }

  toggleExpand(shipmentNumber: string) {
    this.expandedShipment =
      this.expandedShipment === shipmentNumber ? null : shipmentNumber;
  }

  search() {
    const shipment = this.searchShipment?.trim();

    if (!shipment) {
      this.loadAllAsns();
      return;
    }

    this.asns = this.asns.filter(a =>
      a.shipmentNumber.toLowerCase().includes(shipment.toLowerCase())
    );

    this.expandedShipment = this.asns.length
      ? this.asns[0].shipmentNumber
      : null;
  }

  get totalAsns(): number {
    return this.asns.length;
  }

  get totalSkus(): number {
    return this.asns.reduce((count, asn) =>
      count + (asn.skus?.length || 0), 0);
  }

  get verifiedCount(): number {
    return this.countByStatus('GOOD');
  }

  get pendingCount(): number {
    return this.countByStatus('PENDING');
  }

  get damagedCount(): number {
    return this.countByStatus('BAD');
  }

  private countByStatus(status: string): number {
    return this.asns.reduce((total, asn) =>
      total + (asn.skus?.filter(s => s.status === status).length || 0),
      0
    );
  }
}



