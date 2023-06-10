import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from "@angular/common/http";
import {PasswordMetadata} from "../objects/passwordMetadata";
import {Observable} from "rxjs";

@Injectable({providedIn: "root"})
export class PasswordService {
  public resourceUrl = 'http://localhost:8080' + '/password';

  constructor(protected http: HttpClient) {
  }

  create(passwordMetadata: PasswordMetadata): Observable<HttpResponse<void>> {
    return this.http.post<void>(
      `${this.resourceUrl}/addPassMeta`,
      passwordMetadata,
      {observe: 'response'}
    );
  }

  delete(passwordMetadata: PasswordMetadata): Observable<HttpResponse<{}>> {
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      body: {passwordMetadata: JSON.stringify(passwordMetadata)},
    };

    return this.http.delete<HttpResponse<{}>>(`${this.resourceUrl}/delPassMeta`, options);
  }

  fetch():Observable<HttpResponse<PasswordMetadata[]>> {
    return this.http.get<PasswordMetadata[]>(`${this.resourceUrl}/getPassMetaList`, {observe: 'response'});
  }

  // TODO: Solve this issue
  generate(passwordMetadata: PasswordMetadata, masterPass: string): Observable<HttpResponse<string>> {
    masterPass = masterPass.trim();

    return this.http.get<string>(`${this.resourceUrl}/generatePassword`, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      // body: JSON.parse(JSON.stringify(passwordMetadata)),
      params: new HttpParams().append('masterPass', masterPass),
      observe: 'response'
    });
  }
}
