import { Component, OnInit } from '@angular/core';
import { BoardAdminService, TestResult } from './board-admin.service';
import { Chart } from 'chart.js/auto';

@Component({
  selector: 'app-board-admin',
  templateUrl: './board-admin.component.html',
  styleUrls: ['./board-admin.component.css']
})
export class BoardAdminComponent implements OnInit {
  // Propriétés pour les filtres
  selectedTool: string = 'Tous';
  selectedStatus: string = 'Tous';
  selectedDate: string = '';
  selectedExecutedBy: string = 'Tous';

  // Listes pour les options de filtre
  tools: string[] = ['Tous', 'Selenium', 'Postman', 'Gatling', 'JMeter'];
  statuses: string[] = ['Tous', 'passed', 'failed'];
  executedBys: string[] = ['Tous', 'Thread Group 1-1']; // Ajustez selon vos données

  // Données récupérées et filtrées
  allTestResults: TestResult[] = [];
  filteredTestResults: TestResult[] = [];

  // Variables pour les KPI
  totalTests: number = 0;
  passedPercentage: number = 0;
  avgExecutionTime: string = '';

  // Graphiques
  public doughnutChart: any;
  public barChart: any;

  constructor(private boardAdminService: BoardAdminService) { }

  ngOnInit(): void {
    // Récupération des données de l'API
    this.boardAdminService.getResults().subscribe(data => {
      console.log(data);
      this.allTestResults = data; // Assigner directement `data` car c'est déjà un tableau
      this.filteredTestResults = [...this.allTestResults];

      // Extraire les valeurs uniques de `threadName` pour `executedBys`
      this.executedBys = ['Tous', ...new Set(this.allTestResults.map(test => test.threadName))];

      this.calculateKPI();
      this.createDoughnutChart();
      this.createBarChart();
    });
  }



  // Méthode pour filtrer les résultats
  // Méthode pour filtrer les résultats
  // Méthode pour filtrer les résultats
  filterResults() {
    this.filteredTestResults = this.allTestResults.filter(test => {
      const matchesTool = this.selectedTool === 'Tous' || test.tool === this.selectedTool;
      const matchesStatus = this.selectedStatus === 'Tous' ||
        (test.success ? 'passed' : 'failed') === this.selectedStatus;
      const matchesExecutedBy = this.selectedExecutedBy === 'Tous' || test.threadName === this.selectedExecutedBy;
      const matchesDate = !this.selectedDate || test.date.startsWith(this.selectedDate);

      return matchesTool && matchesStatus && matchesExecutedBy && matchesDate;
    });

    console.log('Données filtrées :', this.filteredTestResults); // Vérifie les données filtrées ici
    this.calculateKPI();
    this.updateCharts();
  }


  // Méthode pour calculer les KPI
  // Méthode pour calculer les KPI
  calculateKPI() {
    this.totalTests = this.filteredTestResults.length;

    // Utiliser `test.success` directement comme booléen, sans comparer à une chaîne
    const passedTests = this.filteredTestResults.filter(test => test.success).length;

    // Calcul du pourcentage
    this.passedPercentage = this.totalTests > 0
      ? parseFloat(((passedTests / this.totalTests) * 100).toFixed(2))
      : 0;

    // Calcul du temps d'exécution moyen
    const totalExecutionTime = this.filteredTestResults.reduce((acc, test) => acc + parseInt(test.elapsed), 0);
    const avgTime = this.totalTests > 0 ? totalExecutionTime / this.totalTests : 0;
    this.avgExecutionTime = avgTime >= 1000 ? `${(avgTime / 1000).toFixed(2)}s` : `${avgTime.toFixed(2)}ms`;
  }


  // Méthode pour créer le graphique Doughnut
  createDoughnutChart() {
    const passed = this.filteredTestResults.filter(test => test.success).length;
    const failed = this.filteredTestResults.length - passed;

    this.doughnutChart = new Chart("MyDoughnutChart", {
      type: 'doughnut',
      data: {
        labels: ['Passed', 'Failed'],
        datasets: [{
          data: [passed, failed],
          backgroundColor: ['green', 'red']
        }]
      },
      options: {
        plugins: {
          title: {
            display: true,
            text: 'Test Status Distribution'
          }
        }
      }
    });
  }


  // Méthode pour créer le graphique Bar
  createBarChart() {
    const selenium = this.filteredTestResults.filter(test => test.tool === 'Selenium').length;
    const gatling = this.filteredTestResults.filter(test => test.tool === 'Gatling').length;
    const jmeter = this.filteredTestResults.filter(test => test.tool === 'JMeter').length;
    const postman = this.filteredTestResults.filter(test => test.tool === 'Postman').length;

    this.barChart = new Chart("MyBarChart", {
      type: 'bar',
      data: {
        labels: ['Selenium', 'Gatling', 'JMeter', 'Postman'],
        datasets: [{
          data: [selenium, gatling, jmeter, postman],
          backgroundColor: 'rgba(54, 162, 235, 0.2)',
          borderColor: 'rgb(54, 162, 235)',
          borderWidth: 1
        }]
      },
      options: {
        plugins: {
          title: {
            display: true,
            text: 'Tool Usage Distribution'
          }
        },
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  }



  // Méthode pour mettre à jour les graphiques
  updateCharts() {
    if (this.doughnutChart) this.doughnutChart.destroy();
    if (this.barChart) this.barChart.destroy();
    this.createDoughnutChart();
    this.createBarChart();
  }

}
