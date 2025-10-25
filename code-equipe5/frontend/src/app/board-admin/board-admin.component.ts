import { Component, OnInit, ViewChild } from '@angular/core';
import { ReportService } from '../_services/report.service';
import { Report, TestItem, TestStatus } from '../models/report';
import { ChartConfiguration, ChartType, ChartData } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';

type ToolFilter = 'Tous' | 'jmeter' | 'selenium' | 'gatling' | 'unknown';
type StatusFilter = 'Tous' | 'passed' | 'failed' | 'skipped';

@Component({
  selector: 'app-admin-board',
  templateUrl: './board-admin.component.html',
  styleUrls: ['./board-admin.component.css']
})
export class AdminBoardComponent implements OnInit {
  @ViewChild(BaseChartDirective) chart?: BaseChartDirective;

  pieChartType: 'doughnut' = 'doughnut';
  pieChartData: ChartData<'doughnut'> = {
    labels: ['Passed', 'Failed', 'Skipped'],
    datasets: [{
      data: [0, 0, 0],
      backgroundColor: ['#28a745', '#dc3545', '#6c757d']
    }],
  };
  pieChartOptions: ChartConfiguration<'doughnut'>['options'] = {
    responsive: true,
    plugins: { legend: { position: 'bottom' } },
  };

  loading = true;
  error: string | null = null;

  report: Report | null = null;
  visible: TestItem[] = [];

  // filtres UI
  tool: ToolFilter = 'Tous';
  status: StatusFilter = 'Tous';
  date?: string;       // format "yyyy-MM-dd" depuis <input type="date">
  userQuery = '';      // recherche sur executedBy + scenario

  tools: ToolFilter[] = ['Tous', 'jmeter', 'selenium', 'gatling', 'unknown'];
  statuses: StatusFilter[] = ['Tous', 'passed', 'failed', 'skipped'];

  total = 0;
  passed = 0;
  failed = 0;
  skipped = 0;
  successPct = 0;
  avgMs = 0;

  constructor(private api: ReportService) {}

  ngOnInit(): void {
    this.api.getReport().subscribe({
      next: (rep: any) => {
        this.report = rep;
        this.applyFilters();
        this.loading = false;
      },
      error: (err: any) => {
        this.error = 'Erreur de chargement du rapport';
        console.error(err);
        this.loading = false;
      }
    });
  }

  applyFilters(): void {
    if (!this.report) return;

    let data = [...this.report.tests];

    // filtre outil
    if (this.tool !== 'Tous') {
      data = data.filter(t => t.tool === this.tool);
    }

    // filtre statut
    if (this.status !== 'Tous') {
      data = data.filter(t => t.status === this.status);
    }

    // filtre utilisateur + recherche textuelle (executedBy OU scenario)
    if (this.userQuery.trim()) {
      const q = this.userQuery.trim().toLowerCase();
      data = data.filter(t =>
        (t.executedBy || '').toLowerCase().includes(q) ||
        (t.scenario   || '').toLowerCase().includes(q)
      );
    }

    // filtre date (match sur le début de l'ISO ex: "2025-10-12")
    if (this.date) {
      const day = this.date; // "yyyy-MM-dd"
      data = data.filter(t => (t.executedAt ? t.executedAt.startsWith(day) : false));
    }

    this.visible = data;

    // --- KPIs ---
    this.total   = data.length;
    this.passed  = data.filter(t => t.status === 'passed').length;
    this.failed  = data.filter(t => t.status === 'failed').length;
    this.skipped = data.filter(t => t.status === 'skipped').length;
    this.successPct = this.total ? Math.round((this.passed / this.total) * 100) : 0;

    const totalDur = data.reduce((s, t) => s + (t.durationMs || 0), 0);
    this.avgMs = this.total ? Math.round(totalDur / this.total) : 0;

    // --- MAJ graphique ---
    this.pieChartData = {
      labels: ['Passed', 'Failed', 'Skipped'],
      datasets: [{
        data: [this.passed, this.failed, this.skipped],
        backgroundColor: ['#28a745', '#dc3545', '#6c757d']
      }],
    };

    this.chart?.update();
  }

  badgeClass(s: string) {
    return {
      'passed': 'badge bg-success',
      'failed': 'badge bg-danger',
      'skipped': 'badge bg-secondary'
    }[s] || 'badge bg-secondary';
  }
}
