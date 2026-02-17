export interface Sku {
  id?: number;
  skuId: string;
  skuName: string;
  mrp: number;
  batchNumber: string;
  expiry: string;
  expectedQuantity?: number;
  status?: string;
}
