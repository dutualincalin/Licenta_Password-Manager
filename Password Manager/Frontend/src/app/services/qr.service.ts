import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {PasswordConfiguration} from "../objects/passwordConfiguration";


@Injectable({providedIn: 'root'})
export class QrService{
  private resourceUrl = 'http://localhost:8080' + '/qr';

  constructor(protected http: HttpClient) {
  }

  /**
   ** QR methods
   ************************************************************************************/

  readQR(qrPayload: string): Observable<HttpResponse<void>> {
    return this.http.post<void>(
      `${this.resourceUrl}/readQR`,
      qrPayload,
      {observe: 'response', withCredentials: true}
    );
  }

  exportQR(passwordMetadataList: PasswordConfiguration[]): Observable<Blob> {
    return this.http.post(
      `${this.resourceUrl}/exportQR`,
      passwordMetadataList,
      {responseType: 'blob', withCredentials: true}
    );
  }
}
