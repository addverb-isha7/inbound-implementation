import { Routes } from '@angular/router';
import { AsnPageComponent } from './pages/asn-page/asn-page.component';
import { VerifyPageComponent } from './pages/verify-page/verify-page.component';

export const routes: Routes = [
  { path: '', redirectTo: 'asn', pathMatch: 'full' },
  { path: 'asn', component: AsnPageComponent },
  { path: 'verify', component: VerifyPageComponent }
];
