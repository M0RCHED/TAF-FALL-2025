import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

export interface TestResult {
  timeStamp: string;
  date: string;
  elapsed: string;
  label: string;
  responseCode: string;
  responseMessage: string;
  threadName: string;
  dataType: string;
  success: string;
  failureMessage: string;
  bytes: string;
  tool?: string; // Défini comme optionnel
  sentBytes: string;
  grpThreads: string;
  allThreads: string;
  URL: string;
  Latency: string;
  IdleTime: string;
  Connect: string;
}

@Injectable({
  providedIn: 'root'
})
export class BoardAdminService {
  private apiUrl = 'http://localhost:8085/api/results/jmeter';

  constructor(private http: HttpClient) { }

  getResults(): Observable<TestResult[]> {
    return this.http.get<TestResult[]>(this.apiUrl).pipe(
      map(results => results.map(result => ({ 
        ...result, 
        tool: 'JMeter' // Ajoute la propriété `tool` avec la valeur par défaut `JMeter`
      })))
    );
  }
}
