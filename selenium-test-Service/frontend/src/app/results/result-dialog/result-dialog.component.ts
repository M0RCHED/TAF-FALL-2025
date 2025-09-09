import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { PerformanceTestApiService } from 'src/app/_services/performance-test-api.service';

@Component({
  selector: 'app-result-dialog',
  templateUrl: './result-dialog.component.html',
  styleUrls: ['./result-dialog.component.css']
})
export class ResultDialogComponent implements OnInit {
  result: any;

  constructor(
    public dialogRef: MatDialogRef<ResultDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { test: any, testType: string },
    private testApiService: PerformanceTestApiService
  ) {}


  ngOnInit(): void {
    this.loadResult();
  }

  loadResult(): void {
    switch (this.data.testType) {
        case 'gatling':
            const requestName = this.data.test.testRequestName;
            this.testApiService.getGatlingResult(requestName).subscribe(result => {
                this.result = result;
            });
            break;
        case 'jmeter':
            const id = this.data.test.id;
            this.testApiService.getJMeterResult(id).subscribe(result => {
                this.result = result;
            });
            break;
        case 'selenium':
            this.result = this.data.test.actionResults
            break;
        default:
            this.result = { message: 'Type de test non supporté' };
            break;
    }
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}
