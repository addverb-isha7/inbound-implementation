import { Routes } from '@angular/router';
import { ShellComponent } from './layout/shell.component';

export const routes: Routes = [
  {
    path: '',
    component: ShellComponent,
    children: [
      {
        path: '',
        loadComponent: () =>
          import('./pages/dashboard/dashboard.component')
            .then(m => m.DashboardComponent)
      },
      {
        path: 'verify',
        loadComponent: () =>
          import('./pages/verify-page/verify-page.component')
            .then(m => m.VerifyPageComponent)
      }
    ]
  }
];
