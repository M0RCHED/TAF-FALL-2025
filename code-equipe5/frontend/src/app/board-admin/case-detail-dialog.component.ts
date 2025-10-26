import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-case-detail-dialog',
  templateUrl: './case-detail-dialog.component.html',
  styleUrls: ['./case-detail-dialog.component.css']
})
export class CaseDetailDialogComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private ref: MatDialogRef<CaseDetailDialogComponent>
  ) {}

  close(): void { this.ref.close(); }

  pretty(obj: any): string {
    try { return JSON.stringify(obj, null, 2); } catch { return String(obj ?? ''); }
  }
}
