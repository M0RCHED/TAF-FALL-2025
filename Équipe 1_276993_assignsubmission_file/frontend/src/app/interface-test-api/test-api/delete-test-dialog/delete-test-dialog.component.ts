import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { TestApiService } from 'src/app/_services/test-api.service';

@Component({
  selector: 'app-delete-test-dialog',
  templateUrl: './delete-test-dialog.component.html',
  styleUrls: ['./delete-test-dialog.component.css']
})
export class DeleteTestDialogComponent {
    availableModels: any[] = [];
    selectedType: string = '';
    selectedId: string = '';

    constructor(
        public dialogRef: MatDialogRef<DeleteTestDialogComponent>,
        private testApiService: TestApiService
    ) {}

    confirmDelete(): void {
        if (!this.selectedType || !this.selectedId) {
            return;
        }

        this.testApiService.deleteElement(this.selectedType, this.selectedId).subscribe(() => {
        this.dialogRef.close(true);
        });
    }

    close(): void {
        this.dialogRef.close(false);
    }

    ngOnInit() {
        this.testApiService.getAvailableModels().subscribe(models => {
        this.availableModels = models;
        });
    }
}
