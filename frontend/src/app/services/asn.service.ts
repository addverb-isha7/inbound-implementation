import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Asn } from '../models/asn.model';
import { Sku } from '../models/sku.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AsnService {

  private baseUrl = environment.apiUrl + '/asn';

  constructor(private http: HttpClient) {}

  getAllAsns(): Observable<Asn[]> {
    return this.http.get<Asn[]>(this.baseUrl);
  }

  // GET ASN by shipment number (includes SKUs)
  getAsnByShipment(shipmentNumber: string): Observable<Asn> {
    return this.http.get<Asn>(`${this.baseUrl}/${shipmentNumber}`);
  }

  verifySku(shipmentNumber: string, sku: any) {
  return this.http.post(
    `${this.baseUrl}/${shipmentNumber}/verify`,
    sku
  );
}

}
