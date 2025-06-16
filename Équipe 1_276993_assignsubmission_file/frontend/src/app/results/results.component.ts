import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PerformanceTestApiService } from 'src/app/_services/performance-test-api.service';
import { ResultDialogComponent } from './result-dialog/result-dialog.component';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnInit {
  testTypes: string[] = [];
  selectedTestType: string = "";
  tests: any[] = [];

  constructor(private testApiService: PerformanceTestApiService, public dialog: MatDialog) {}

  ngOnInit(): void {
    this.testTypes = this.testApiService.getAvailibleName();
    this.selectedTestType = this.testTypes[0]
    this.loadTests();
  }

  loadTests(): void {
    this.tests = [];
    this.testApiService.getTestsByType(this.selectedTestType).subscribe(tests => {
      this.tests = tests;
    });
  }

  openResultDialog(test: any): void {
    const testType = this.selectedTestType;
    const dialogRef = this.dialog.open(ResultDialogComponent, {
      width: '80vw',
      height: '85vh',
      data: { test, testType }
    });

    dialogRef.afterClosed().subscribe(result => {});
  }
}
