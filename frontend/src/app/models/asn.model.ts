import { Sku } from './sku.model';

export interface Asn {
  asnId?: number;
  shipmentNumber: string;
  supplier: string;
  skus?: Sku[];
}
