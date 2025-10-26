import { Component, OnInit, OnDestroy } from '@angular/core';
import { BoardAdminService } from './board-admin.service';
import {
  RunCard,
  PassratePoint,
  CaseSearchResponse,
  ToolRate,
  TypeStat,
  NamedCount,
  CaseItem,
} from '../models/dashboard.model';

import { ChartConfiguration, ChartOptions } from 'chart.js';
import 'chart.js/auto';
import { Subject, takeUntil } from 'rxjs';

type StatusFilter = 'all' | 'passed' | 'failed';
type ToolFilter   = 'all' | 'gatling' | 'selenium' | 'restAssured' | 'other';

@Component({
  selector: 'app-board-admin',
  templateUrl: './board-admin.component.html',
  styleUrls: ['./board-admin.component.css']
})
export class BoardAdminComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  // ====== état & données ======
  loadingRuns = true;
  loadingRate = true;
  loadingCases = true;

  project = 'TAF';
  days: 7 | 14 | 30 | 60 = 30;

  // filtres
  fStatus: StatusFilter = 'all';
  fTool: ToolFilter = 'all';

  // pagination
  page = 0;
  size = 10;

  runs: RunCard[] = [];
  rate: PassratePoint[] = [];
  casesResp: CaseSearchResponse | null = null;

  // agrégats
  totalRuns = 0;
  totalPassed = 0;
  passPct = 0;

  // cartes additionnelles
  toolRates: ToolRate[] = [];
  typeStats: TypeStat[] = [];
  topFails: NamedCount[] = [];
  flaky: NamedCount[] = [];

  // ====== CHARTS ======
  // Donut (aliasé en pie pour le template)
  donutData: ChartConfiguration<'doughnut'>['data'] = {
    labels: ['Passés', 'Échoués'],
    datasets: [{ data: [0, 0], borderWidth: 0 }]
  };
  donutOptions: ChartOptions<'doughnut'> = {
    responsive: true,
    animation: { duration: 700, easing: 'easeOutQuart' },
    plugins: { legend: { position: 'bottom' }, tooltip: { enabled: true } },
    cutout: '65%'
  };
  // ALIAS demandés par le template
  pieData = this.donutData as unknown as ChartConfiguration<'pie'>['data'];
  pieOptions = this.donutOptions as ChartOptions<'pie'>;

  // Courbe passrate
  lineData: ChartConfiguration<'line'>['data'] = {
    labels: [],
    datasets: [{
      label: 'Passrate (%)',
      data: [],
      tension: 0.35,
      pointRadius: 3,
      pointHoverRadius: 6,
      fill: false
    }]
  };
  lineOptions: ChartOptions<'line'> = {
    responsive: true,
    interaction: { intersect: false, mode: 'index' },
    animation: { duration: 600, easing: 'easeOutCubic' },
    scales: { y: { beginAtZero: true, suggestedMax: 100, ticks: { stepSize: 20 } } },
    plugins: { legend: { display: true }, tooltip: { enabled: true } }
  };

  // % pass par run (barres)
  barsData: ChartConfiguration<'bar'>['data'] = {
    labels: [],
    datasets: [{ label: '% pass du run', data: [], borderWidth: 0 }]
  };
  barsOptions: ChartOptions<'bar'> = {
    responsive: true,
    animation: { duration: 600, easing: 'easeOutCubic' },
    scales: { y: { beginAtZero: true, suggestedMax: 100, ticks: { stepSize: 20 } } },
    plugins: { legend: { display: true }, tooltip: { enabled: true } }
  };

  // % pass par outil
  toolBarData: ChartConfiguration<'bar'>['data'] = { labels: [], datasets: [{ label: '% pass', data: [] }] };
  toolBarOptions: ChartOptions<'bar'> = {
    responsive: true,
    scales: { y: { beginAtZero: true, suggestedMax: 100 } },
    plugins: { legend: { display: true } }
  };

  // Empilé par type
  byTypeData: ChartConfiguration<'bar'>['data'] = {
    labels: [],
    datasets: [
      { label: 'Passés', data: [] },
      { label: 'Échoués', data: [] }
    ]
  };
  byTypeOptions: ChartOptions<'bar'> = {
    responsive: true,
    scales: {
      x: { stacked: true },
      y: { stacked: true, beginAtZero: true }
    },
    plugins: { legend: { position: 'bottom' } }
  };
  // ALIAS demandés par le template
  stackedTypeData = this.byTypeData;
  stackedTypeOptions = this.byTypeOptions;

  constructor(private api: BoardAdminService) {}

  ngOnInit(): void {
    this.loadAll();
  }
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // ========= actions / filtres =========
  loadAll(): void {
    // appelé par (change) sur les selects et sur les boutons de fenêtre
    this.applyFilters();
  }

  applyFilters(): void {
    this.page = 0;
    this.fetchRuns();
    this.fetchRate();
    this.fetchCases();
    this.fetchToolRates();
    this.fetchByType();
    this.fetchTopFails();
    this.fetchFlaky();
  }

  changeDays(d: 7 | 14 | 30 | 60): void {
    this.days = d;
    this.applyFilters();
  }

  changePage(delta: number): void {
    if (!this.casesResp) return;
    const maxPage = Math.max(0, Math.ceil(this.casesResp.total / this.size) - 1);
    this.page = Math.min(maxPage, Math.max(0, this.page + delta));
    this.fetchCases();
  }

  // ========= fetchers =========
  private fetchRuns(): void {
    this.loadingRuns = true;
    const statusParam = this.fStatus === 'all' ? undefined : this.fStatus;
    this.api.getLatestRuns(this.project, 5, statusParam)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (r: RunCard[]) => {
          this.runs = r || [];
          const labels = this.runs.map(run => run.runId ?? '—');
          const data = this.runs.map(run => {
            const tot = run.total ?? 0;
            const pas = run.passed ?? 0;
            return tot ? Math.round((pas / tot) * 100) : 0;
          });
          this.barsData = { labels, datasets: [{ label: '% pass du run', data, borderWidth: 0 }] };
          this.loadingRuns = false;
        },
        error: () => {
          this.runs = [];
          this.barsData = { labels: [], datasets: [{ label: '% pass du run', data: [], borderWidth: 0 }] };
          this.loadingRuns = false;
        }
      });
  }

  private fetchRate(): void {
    this.loadingRate = true;
    this.api.getPassrate(this.project, this.days)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (pts: PassratePoint[]) => {
          this.rate = pts || [];
          this.totalRuns = this.rate.reduce((s, p) => s + (p?.total || 0), 0);
          this.totalPassed = this.rate.reduce((s, p) => s + (p?.passed || 0), 0);
          const totalFailed = this.totalRuns - this.totalPassed;
          this.passPct = this.totalRuns ? Math.round((this.totalPassed / this.totalRuns) * 100) : 0;

          this.donutData = {
            labels: ['Passés', 'Échoués'],
            datasets: [{ data: [this.totalPassed, totalFailed], borderWidth: 0 }]
          };
          // ré-aliaser pour le pie (au cas où l’instance change)
          this.pieData = this.donutData as unknown as ChartConfiguration<'pie'>['data'];

          const labels = this.rate.map(p => p.day);
          const data = this.rate.map(p => p.total ? Math.round((p.passed / p.total) * 100) : 0);
          this.lineData = {
            labels,
            datasets: [{
              label: 'Passrate (%)',
              data,
              tension: 0.35,
              pointRadius: 3,
              pointHoverRadius: 6,
              fill: false
            }]
          };

          this.loadingRate = false;
        },
        error: () => {
          this.rate = [];
          this.totalRuns = this.totalPassed = this.passPct = 0;
          this.donutData = { labels: ['Passés', 'Échoués'], datasets: [{ data: [0, 0], borderWidth: 0 }] };
          this.pieData = this.donutData as unknown as ChartConfiguration<'pie'>['data'];
          this.lineData = { labels: [], datasets: [{ label: 'Passrate (%)', data: [], tension: 0.35, pointRadius: 3, pointHoverRadius: 6, fill: false }] };
          this.loadingRate = false;
        }
      });
  }

  private fetchCases(): void {
    this.loadingCases = true;
    const tool = this.fTool === 'all' ? undefined : (this.fTool === 'restAssured' ? 'restAssured' : this.fTool);
    const status = this.fStatus === 'all' ? undefined : this.fStatus;

    this.api.searchCases(this.project, this.page, this.size, { tool, status })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (res: CaseSearchResponse) => { this.casesResp = res; this.loadingCases = false; },
        error: () => {
          this.casesResp = { page: 0, size: this.size, total: 0, items: [] };
          this.loadingCases = false;
        }
      });
  }

  private fetchToolRates(): void {
    this.api.getToolRates(this.project, this.days)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (rows: ToolRate[]) => {
          this.toolRates = rows || [];
          const labels = this.toolRates.map(r => r.tool || '—');
          const data = this.toolRates.map(r => r.total ? Math.round(((r.passed ?? 0) / r.total) * 100) : 0);
          this.toolBarData = { labels, datasets: [{ label: '% pass', data }] };
        },
        error: () => {
          this.toolRates = [];
          this.toolBarData = { labels: [], datasets: [{ label: '% pass', data: [] }] };
        }
      });
  }

  private fetchByType(): void {
    this.api.getByType(this.project, this.days)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (rows: TypeStat[]) => {
          this.typeStats = rows || [];
          const labels = this.typeStats.map(r => r.type);
          const passed = this.typeStats.map(r => r.passed ?? 0);
          const failed = this.typeStats.map(r => r.failed ?? 0);
          this.byTypeData = {
            labels,
            datasets: [
              { label: 'Passés', data: passed },
              { label: 'Échoués', data: failed }
            ]
          };
          // alias pour le template
          this.stackedTypeData = this.byTypeData;
        },
        error: () => {
          this.typeStats = [];
          this.byTypeData = { labels: [], datasets: [{ label: 'Passés', data: [] }, { label: 'Échoués', data: [] }] };
          this.stackedTypeData = this.byTypeData;
        }
      });
  }

  private fetchTopFails(): void {
    this.api.getTopFails(this.project, this.days, 5)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (rows: NamedCount[]) => this.topFails = rows || [],
        error: () => this.topFails = []
      });
  }

  private fetchFlaky(): void {
    this.api.getFlaky(this.project, this.days, 5)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (rows: NamedCount[]) => this.flaky = rows || [],
        error: () => this.flaky = []
      });
  }

  // ====== helpers ======
  asDate(iso?: string | null): string {
    if (!iso) return '-';
    const d = new Date(iso);
    if (isNaN(d.getTime())) return '-';
    return d.toLocaleString();
  }

  statusClass(s?: string | null): string {
    if (s === 'passed') return 'badge pass';
    if (s === 'failed') return 'badge fail';
    return 'badge other';
  }

  // clic sur une ligne de case -> on branchera un MatDialog ensuite
  openCaseDetail(c: CaseItem): void {
    // TODO: ouvrir un MatDialog avec les détails; pour l’instant on trace
    console.log('Case detail:', c);
  }

  Math = Math; // pour le template
}
