import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { TestApiService } from 'src/app/_services/test-api.service';

@Component({
  selector: 'app-edit-test-dialog',
  templateUrl: './edit-test-dialog.component.html',
  styleUrls: ['./edit-test-dialog.component.css']
})
export class EditTestDialogComponent {
    availableModels: any[] = [];
    selectedType: string = '';
    selectedId: string = '';
    template: any = null;

    constructor(
        public dialogRef: MatDialogRef<EditTestDialogComponent>,
        private testApiService: TestApiService
    ) {}

    resetTemplate() {
        this.template = null;
        this.selectedId = '';
    }

    loadElement() {
        if (!this.selectedType || !this.selectedId) {
        alert("Veuillez sélectionner un type et entrer un ID.");
        return;
        }

        this.testApiService.getElement(this.selectedType, this.selectedId).subscribe(
        data => {
            this.template = data;
        },
        error => {
            alert("Élément introuvable !");
        }
        );
    }

    saveElement() {
        if (this.template) {
        this.testApiService.updateElement(this.selectedType, this.selectedId, this.template).subscribe(() => {
            this.dialogRef.close(true);
        });
        }
    }

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
}
