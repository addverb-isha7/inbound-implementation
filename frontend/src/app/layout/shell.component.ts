import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from '../components/sidebar/sidebar.component';
import { HeaderComponent } from '../components/header/header.component';

@Component({
  standalone: true,
  selector: 'app-shell',
  imports: [CommonModule, RouterOutlet, SidebarComponent, HeaderComponent],
  templateUrl: './shell.component.html'
})
export class ShellComponent {
  isSidebarOpen = false;

  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }
}
