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

  createAsn(asn: Asn): Observable<Asn> {
    return this.http.post<Asn>(this.baseUrl, asn);
  }

  getAllAsn(): Observable<Asn[]> {
    return this.http.get<Asn[]>(this.baseUrl);
  }

  verifySku(shipmentNumber: string, sku: Sku): Observable<any> {
    return this.http.post(`${this.baseUrl}/${shipmentNumber}/verify`, 
      sku,
     { responseType: 'text' }
    );
  }
}
