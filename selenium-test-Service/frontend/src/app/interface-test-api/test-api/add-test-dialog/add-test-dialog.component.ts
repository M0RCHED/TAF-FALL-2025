import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { TestApiService } from 'src/app/_services/test-api.service';

@Component({
  selector: 'app-add-test-dialog',
  templateUrl: './add-test-dialog.component.html',
  styleUrls: ['./add-test-dialog.component.css']
})
export class AddTestDialogComponent {
  availableModels: any[] = [];
  selectedType: string = '';
  template: any = null;

  constructor(public dialogRef: MatDialogRef<AddTestDialogComponent>, private testApiService: TestApiService) {}

  ngOnInit() {
    this.testApiService.getAvailableModels().subscribe(models => {
      this.availableModels = models;
    });
  }

  onTypeChange(): void {
    if (this.selectedType) {
      this.testApiService.getTemplate(this.selectedType).subscribe(template => {
        this.template = template;
      });
    }
  }

  saveElement() {
    if (this.template) {
      this.testApiService.addTest(this.selectedType, this.template).subscribe(() => {
          this.dialogRef.close(true);
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
