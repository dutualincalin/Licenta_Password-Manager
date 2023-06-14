import {Injectable} from "@angular/core";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";


@Injectable({providedIn: "root"})
export class ConfigurationService {
  private resourceUrl = 'http://localhost:8080' + '/config';

  constructor(protected http: HttpClient) {
  }

  init(): Observable<HttpResponse<{}>> {
    return this.http.get(`${this.resourceUrl}/getCSRF`, {observe: 'response', withCredentials:true});
  }

  gather(): Observable<HttpResponse<{}>> {
    return this.http.get(`${this.resourceUrl}/configGather`, {observe: 'response', withCredentials: true});
  }

  save(): Observable<HttpResponse<{}>> {
    return this.http.get(`${this.resourceUrl}/configSave`, {observe: 'response', withCredentials: true});
  }

  setImg(imgPath: {}): Observable<HttpResponse<void>> {
    return this.http.post<void>(`${this.resourceUrl}/imgConfig`, imgPath, {observe: 'response',withCredentials:true});
  }

  shutdownSignal(): Observable<void> {
    return this.http.get<void>(`${this.resourceUrl}/shutdown`,{withCredentials: true});
  }
}
