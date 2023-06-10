import {Injectable} from "@angular/core";
import {HttpClient, HttpParams, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {PasswordMetadata} from "../objects/passwordMetadata";


@Injectable({providedIn: "root"})
export class ConfigurationService {
  public resourceUrl = 'http://localhost:8080' + '/config';

  constructor(protected http: HttpClient) {
  }

  gather(): Observable<HttpResponse<{}>> {
    return this.http.get(`${this.resourceUrl}/configGather`, {observe: 'response'});
  }

  save(): Observable<HttpResponse<{}>> {
    return this.http.get(`${this.resourceUrl}/configSave`, {observe: 'response'});
  }

  readQR(QRPath: string): Observable<HttpResponse<void>> {
    QRPath = QRPath.trim();
    let options = new HttpParams().set('path', QRPath);
    return this.http.get<void>(`${this.resourceUrl}/readQR`, {params: options, observe: 'response'});
  }

  exportQR(passwordMetadataList: PasswordMetadata[]): Observable<HttpResponse<string>> {
    return this.http.post<string>(`${this.resourceUrl}/exportQR`, passwordMetadataList, {observe: 'response'});
  }

  setImg(imgPath: string): Observable<HttpResponse<void>> {
    imgPath = imgPath.trim();
    let options = new HttpParams().set('imgPath', imgPath);
    return this.http.get<void>(`${this.resourceUrl}/imgConfig`, {params: options, observe: 'response'});
  }
}
