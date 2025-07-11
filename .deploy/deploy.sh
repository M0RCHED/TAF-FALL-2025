docker compose -f docker-compose.db.yml \
               -f docker-compose.selenium.yml \
               -f docker-compose.testapi.yml \
               -f docker-compose.registry.yml \
               -f docker-compose.gateway.yml \
               -f docker-compose.auth.yml \
               -f docker-compose.team1.yml \
               -f docker-compose.team2.yml \
               -f docker-compose.team3.yml \
               up -d