import { Component, OnInit } from '@angular/core';
import { TestApiService } from 'src/app/_services/test-api.service';
import { MatDialog } from '@angular/material/dialog';
import { AddTestDialogComponent } from './add-test-dialog/add-test-dialog.component';
import { EditTestDialogComponent } from './edit-test-dialog/edit-test-dialog.component';
import { DeleteTestDialogComponent } from './delete-test-dialog/delete-test-dialog.component';

@Component({
  selector: 'app-test-api',
  templateUrl: './test-api.component.html',
  styleUrls: ['./test-api.component.css']
})
export class TestApiComponent implements OnInit {
  testPlans: any[] = [];
  newTestType = 'TestPlan';
  testTemplate: any = null;

  constructor(private testApiService: TestApiService, public dialog: MatDialog) {}

  ngOnInit(): void {
    this.loadTestPlans();
  }

  loadTestPlans(): void {
    this.testApiService.getTestPlans().subscribe(plans => {
      this.testPlans = plans;
    });
  }

  fetchTemplate(): void {
    this.testApiService.getTemplate(this.newTestType).subscribe(template => {
      this.testTemplate = template;
    });
  }

  saveTest(): void {
    if (!this.testTemplate) return;
    this.testApiService.addTest(this.newTestType, this.testTemplate).subscribe(() => {
      this.loadTestPlans();
      this.testTemplate = null;
    });
  }

  // HTML Utilities
  getObjectKeys(obj: any): string[] {
    return obj ? Object.keys(obj) : [];
  }

  openAddDialog() {
    const dialogRef = this.dialog.open(AddTestDialogComponent, {
      width: '80vw',
      height: '85vh',
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadTestPlans();
      }
    });
  }

  openEditDialog() {
    const dialogRef = this.dialog.open(EditTestDialogComponent, {
      width: '80vw',
      height: '85vh',
    });
  
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadTestPlans();
      }
    });
  }

  openDeleteDialog() {
    const dialogRef = this.dialog.open(DeleteTestDialogComponent, {
      width: '40vw',
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadTestPlans();
      }
    });
  }
}
