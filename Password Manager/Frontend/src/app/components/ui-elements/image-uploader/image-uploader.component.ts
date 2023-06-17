import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-image-uploader',
  templateUrl: './image-uploader.component.html',
  styleUrls: ['./image-uploader.component.scss']
})
export class ImageUploaderComponent {
  @Output('imgSelectEvent') imageSelectEvent: EventEmitter<any> = new EventEmitter<any>();
  @Output('imgUploadEvent') imageUploadEvent: EventEmitter<void> = new EventEmitter<void>();
  @Output('imgCancelEvent') imageCancelEvent: EventEmitter<void> = new EventEmitter<void>();
  @Input('imgFileURL') imgFileURL: string;

  uploadLoading = false;
}
