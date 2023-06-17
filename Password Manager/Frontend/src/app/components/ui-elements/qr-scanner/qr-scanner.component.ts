import {
  AfterViewInit, Component, EventEmitter, Input,
  Output,
  ViewChild
} from '@angular/core';
import {
  NgxScannerQrcodeComponent,
  ScannerQRCodeConfig, ScannerQRCodeDevice, ScannerQRCodeResult
} from "ngx-scanner-qrcode";
import {delay} from "rxjs";
import {MessageService} from "primeng/api";


@Component({
  selector: 'app-qr-scanner',
  templateUrl: './qr-scanner.component.html',
  styleUrls: ['./qr-scanner.component.scss']
})
export class QrScannerComponent implements AfterViewInit{

  @ViewChild('action') action: NgxScannerQrcodeComponent;

  @Input('uploading') uploading: boolean;
  @Input('QRStartScan') qrStartEvent: EventEmitter<any>;

  @Output('QRCancel') qrCancelEvent: EventEmitter<void> = new EventEmitter<void>;
  @Output('QRUpload') qrUploadEvent: EventEmitter<string> = new EventEmitter<string>;

  qrValue: string = '';
  selectedDeviceId: string;
  devices: any[];

  public config: ScannerQRCodeConfig = {
    constraints: {
      video: {
        width: window.innerWidth
      }
    },

    canvasStyles: {
      lineWidth: 2,
      fillStyle: '#ff001854',
      strokeStyle: '#ff0018c7',
    } as any
  };

  constructor(private messageService: MessageService) {}

  ngAfterViewInit() {
    if(!this.action) {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'The camera is not connected or it is allocated to another app.',
        sticky: true
      });
    }

    this.action.isReady.pipe(delay(1000)).subscribe(() => {
      this.qrStartEvent.subscribe(() => this.handleQRCommand(this.action, 'start'));
    });
  }

  public onQRDetection(e: ScannerQRCodeResult[], action?: any): void {
    e?.length && action && action.pause();
    this.qrValue = e[0].value;
  }

  public handleQRCommand(action: any, fn: string): void {
    const playDeviceFacingBack = (devices: ScannerQRCodeDevice[]) => {
      this.extractDevices(devices);

      const device = devices.find(
        f => (/back|rear|environment/gi.test(f.label))
      );

      action.playDevice(device ? device.deviceId : devices[0].deviceId);
    }

    fn === 'start' ?
      action[fn](playDeviceFacingBack).subscribe() :
      action[fn]().subscribe();
  }

  extractDevices(devices: ScannerQRCodeDevice[]){
    this.devices = [];

    devices.forEach(device => {
      this.devices.push({label: device.label, value: device.deviceId})
    });
  }

  onQRStop() {
    this.handleQRCommand(this.action, 'stop');
    this.qrCancelEvent.emit();
  }

  onQRUpload() {
    this.handleQRCommand(this.action, 'stop');
    this.qrUploadEvent.emit(this.qrValue);
  }

  onQRRescan() {
    this.handleQRCommand(this.action, 'play');
    this.qrValue = '';
  }
}
