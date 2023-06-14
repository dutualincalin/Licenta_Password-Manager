import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {HttpClient, HttpParams, HttpResponse} from "@angular/common/http";
import {PasswordMetadata} from "../objects/passwordMetadata";


@Injectable({providedIn: 'root'})
export class QrService{
  private resourceUrl = 'http://localhost:8080' + '/qr';

  constructor(protected http: HttpClient) {
  }

  readQR(QRPath: string): Observable<HttpResponse<void>> {
    QRPath = QRPath.trim();
    let options = new HttpParams().set('path', QRPath);
    return this.http.get<void>(`${this.resourceUrl}/readQR`, {params: options, observe: 'response'});
  }

  exportQR(passwordMetadataList: PasswordMetadata[]): Observable<HttpResponse<{[index: string]: string}>> {
    return this.http.post<{}>(
      `${this.resourceUrl}/exportQR`,
      passwordMetadataList,
      {observe: 'response', withCredentials: true}
    );
  }
}
