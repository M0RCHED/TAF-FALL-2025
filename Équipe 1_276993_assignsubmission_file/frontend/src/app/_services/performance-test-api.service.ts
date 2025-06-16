import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { environment } from '../../environments/environment';
import { JMeterHttpRequest } from '../performance-test-api/jmeter-api/jmeter-http-request';
import { JMeterFTPRequest } from '../performance-test-api/jmeter-api/jmeter-ftp-request';

import { GatlingRequest } from '../performance-test-api/gatling-api/gatling-request';

const GATLING_API = `${environment.apiUrl}/api/gatling`;
const JMETER_API = `${environment.apiUrl}/api/jmeter`;
const JMeter_HttpRequest_API = `${JMETER_API}/http`;
const JMeter_FtpRequest_API = `${JMETER_API}/ftp`;
// TODO: Move Selenium to separated service
const SELENIUM_API = `${environment.apiUrl}/api/selenium`;

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};

@Injectable({
  providedIn: 'root',
})
export class PerformanceTestApiService {
  constructor(private http: HttpClient) { }

  sendGatlingRequest(request: GatlingRequest): Observable<any> {
    const url = `${GATLING_API}/runSimulation`;
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    const response = this.http.post(url, request, httpOptions);
    return response;
  }
  sendHttpJMeterRequest(
    jmeter_http_request: JMeterHttpRequest
  ): Observable<any> {
    const url = `${JMeter_HttpRequest_API}`;
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.post(url, jmeter_http_request, httpOptions);
  }

  sendFtpJMeterRequest(jmeter_ftp_request: JMeterFTPRequest): Observable<any> {
    const url = `${JMeter_FtpRequest_API}`;
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.post(url, jmeter_ftp_request, httpOptions);
  }

  getAvailibleName(): string[] {
    return ["gatling", "jmeter", "selenium"]
  }

  getTestsByType(type: string): Observable<any[]> {
    if (type == "gatling") {
      return this.http.get<any[]>(`${GATLING_API}/requests`);
    } else if (type == "jmeter") {
      return this.http.get<any[]>(`${JMETER_API}/requests`);
    } else if (type == "selenium") {
      return this.http.get<any[]>(`${SELENIUM_API}/requests`);
    }
    return of([{ message: 'Aucun résultat disponible' }]);
  }

  getGatlingResult(requestName: string): Observable<any> {
    return this.http.get<any>(`${GATLING_API}/results?requestName=${requestName}`);
  }

  getJMeterResult(testPlanId: string): Observable<any> {
    return this.http.get<any>(`${JMETER_API}/results?testPlanId=${testPlanId}`);
  }
}
