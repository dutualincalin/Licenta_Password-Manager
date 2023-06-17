import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable, switchMap} from "rxjs";
import { CookieService } from 'ngx-cookie-service';
import {ConfigurationService} from "./configuration.service";


@Injectable()
export class InterceptorService implements HttpInterceptor {
  constructor(private cookieService: CookieService, private configurationService: ConfigurationService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (request.method === 'GET') {
      return next.handle(request);
    }

    this.cookieService.deleteAll();

    return this.configurationService.init().pipe(
      switchMap(() => {
        const csrfToken = this.cookieService.get('XSRF-TOKEN') || '';

        if (csrfToken !== '') {
          const modifiedRequest = request.clone({
            setHeaders: {
              'X-XSRF-TOKEN': csrfToken
            }
          });

          this.cookieService.set("X-XSRF-TOKEN", csrfToken);
          return next.handle(modifiedRequest);
        } else {
          return next.handle(request);
        }
      })
    );
  }
}
