import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-json-editor',
  templateUrl: './json-editor.component.html',
  styleUrls: ['./json-editor.component.css']
})
export class JsonEditorComponent {
  @Input() data: any = {};
  @Output() dataChange = new EventEmitter<any>();

  onJsonChange(event: any) {
    try {
      this.dataChange.emit(JSON.parse(event.target.value));
    } catch (e) {
      console.error('Invalid JSON:', e);
    }
  }
}
