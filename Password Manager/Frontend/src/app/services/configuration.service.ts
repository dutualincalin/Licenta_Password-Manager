import {Injectable} from "@angular/core";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";


@Injectable({providedIn: "root"})
export class ConfigurationService {
  private resourceUrl = 'https://localhost:8443' + '/config';

  constructor(protected http: HttpClient) {
  }

  /**
   ** CSRF protocol method
   ************************************************************************************/

  init(): Observable<HttpResponse<{}>> {
    return this.http.get(`${this.resourceUrl}/getCSRF`, {observe: 'response', withCredentials:true});
  }


  /**
   ** App Configuration methods
   ************************************************************************************/

  setImg(imgPath: {}): Observable<HttpResponse<void>> {
    return this.http.post<void>(`${this.resourceUrl}/imgConfig`, imgPath, {observe: 'response',withCredentials:true});
  }

  gather(): Observable<HttpResponse<{}>> {
    return this.http.get(`${this.resourceUrl}/configGather`, {observe: 'response', withCredentials: true});
  }

  save(): Observable<HttpResponse<{}>> {
    return this.http.get(`${this.resourceUrl}/configSave`, {observe: 'response', withCredentials: true});
  }
}
