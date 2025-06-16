import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from "../../environments/environment";

const API_URL = `${environment.apiUrl}/api`;

@Injectable({
  providedIn: 'root'
})
export class TestApiService {
    constructor(private http: HttpClient) {}

    getTemplate(template: string): Observable<any> {
        return this.http.get(`${API_URL}/templates/${template}`);
    }

    getTestPlanTemplate(): Observable<any> {
        return this.getTemplate("plan");
    }

    getTestSuiteTemplate(): Observable<any> {
        return this.getTemplate("suite");
    }

    getTestCaseTemplate(): Observable<any> {
        return this.getTemplate("case");
    }

    getTestPlans(): Observable<any> {
        return this.http.get(`${API_URL}/testapi/testPlans`);
    }

    addTest(type: string, testObject: any): Observable<any> {
        return this.http.post<any>(`${API_URL}/testapi/${type}`, testObject);
    }

    getElement(type: string, id: string): Observable<any> {
        return this.http.get<any>(`${API_URL}/testapi/${type}/${id}`);
    }
    
    updateElement(type: string, id: string, data: any): Observable<any> {
        return this.http.put<any>(`${API_URL}/testapi/${type}/${id}`, data);
    }

    deleteElement(type: string, id: string): Observable<void> {
        return this.http.delete<void>(`${API_URL}/testapi/${type}/${id}`);
    }

    getAvailableModels(): Observable<any[]> {
        return this.http.get<any[]>(`${API_URL}/models`);
    }
}
