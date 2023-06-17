import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from "@angular/common/http";
import {PasswordConfiguration} from "../objects/passwordConfiguration";
import {Observable} from "rxjs";

@Injectable({providedIn: "root"})
export class PasswordService {
  public resourceUrl = 'http://localhost:8080' + '/password';

  constructor(protected http: HttpClient) {}

  /**
   ** Password Configuration administration methods
   ************************************************************************************/

  create(passwordForm: {}): Observable<HttpResponse<void>> {
    return this.http.post<void>(
      `${this.resourceUrl}/addPassMeta`,
      passwordForm,
      {observe: 'response', withCredentials: true}
    );
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      params: new HttpParams().set("id", id),
      withCredentials: true
    };

    return this.http.delete<HttpResponse<{}>>(`${this.resourceUrl}/delPassMeta`, options);
  }

  fetch():Observable<HttpResponse<PasswordConfiguration[]>> {
    return this.http.get<PasswordConfiguration[]>(
      `${this.resourceUrl}/getPassMetaList`,
      {observe: 'response', withCredentials: true}
    );
  }


  /**
   ** Password generation method
   ************************************************************************************/

  generate(masterPass: string, id: string): Observable<HttpResponse<{[string: string]: any}>> {
    masterPass = masterPass.trim();

    return this.http.get(`${this.resourceUrl}/generatePassword`, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      params: new HttpParams()
        .set('masterPass',masterPass)
        .set('id', id),
      observe: 'response',
      withCredentials: true
    });
  }
}
