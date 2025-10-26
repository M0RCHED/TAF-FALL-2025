import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  RunCard,
  RunDetail,
  CaseSearchResponse,
  PassratePoint,
  ToolRate,
  TypeStat,
  NamedCount,
} from '../models/dashboard.model';

// Option A: parler direct au backend
const API = 'http://localhost:8083/dashboard';
// Option B (proxy Angular): const API = '/dashboard';

@Injectable({ providedIn: 'root' })
export class BoardAdminService {
  constructor(private http: HttpClient) {}

  getLatestRuns(project: string, limit = 5, status?: string): Observable<RunCard[]> {
    let p = new HttpParams().set('project', project).set('limit', limit);
    if (status) p = p.set('status', status);
    return this.http.get<RunCard[]>(`${API}/report`, { params: p });
  }

  getRunById(runId: string): Observable<RunDetail> {
    return this.http.get<RunDetail>(`${API}/report/run/${encodeURIComponent(runId)}`);
  }

  searchCases(
    project: string,
    page = 0,
    size = 20,
    filters?: { type?: string; tool?: string; status?: string; from?: string; to?: string }
  ): Observable<CaseSearchResponse> {
    let p = new HttpParams().set('project', project).set('page', page).set('size', size);
    if (filters?.type)   p = p.set('type', filters.type);
    if (filters?.tool)   p = p.set('tool', filters.tool);
    if (filters?.status) p = p.set('status', filters.status);
    if (filters?.from)   p = p.set('from', filters.from);
    if (filters?.to)     p = p.set('to', filters.to);
    return this.http.get<CaseSearchResponse>(`${API}/cases`, { params: p });
  }

  getPassrate(project: string, days = 30): Observable<PassratePoint[]> {
    const p = new HttpParams().set('project', project).set('days', days);
    return this.http.get<PassratePoint[]>(`${API}/summary/passrate`, { params: p });
  }

  // === méthodes ajoutées ===

  getToolRates(project: string, days = 30): Observable<ToolRate[]> {
    const p = new HttpParams().set('project', project).set('days', days);
    return this.http.get<ToolRate[]>(`${API}/summary/tool-rates`, { params: p });
  }

  getByType(project: string, days = 30): Observable<TypeStat[]> {
    const p = new HttpParams().set('project', project).set('days', days);
    return this.http.get<TypeStat[]>(`${API}/summary/by-type`, { params: p });
  }

  getTopFails(project: string, days = 30, limit = 5): Observable<NamedCount[]> {
    const p = new HttpParams().set('project', project).set('days', days).set('limit', limit);
    return this.http.get<NamedCount[]>(`${API}/summary/top-fails`, { params: p });
  }

  getFlaky(project: string, days = 30, limit = 5): Observable<NamedCount[]> {
    const p = new HttpParams().set('project', project).set('days', days).set('limit', limit);
    return this.http.get<NamedCount[]>(`${API}/summary/flaky`, { params: p });
  }
}
