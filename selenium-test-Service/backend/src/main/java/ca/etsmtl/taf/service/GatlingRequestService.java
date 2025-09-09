package ca.etsmtl.taf.service;


import ca.etsmtl.taf.entity.GatlingRequest;
import ca.etsmtl.taf.repository.GatlingRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class GatlingRequestService {

    @Autowired
    private GatlingRequestRepository gatlingRequestRepository;

    public boolean validateGatlingRequest(GatlingRequest request) {
        if (request == null) {
            return false;
        }
        // Vérifier les champs obligatoires (sans inclure testUri et testRequestBody)
        return StringUtils.hasText(request.getTestBaseUrl()) &&
                StringUtils.hasText(request.getTestScenarioName()) &&
                StringUtils.hasText(request.getTestRequestName()) &&
                StringUtils.hasText(request.getTestMethodType()) &&
                request.getTestUsersNumber() > 0; // Assurer que le nombre d'utilisateurs est positif
    }

    public void saveGatlingRequest(GatlingRequest request) {
        if (validateGatlingRequest(request)) {
            gatlingRequestRepository.save(request);
            System.out.println("✅ Gatling request saved to MongoDB!");
        } else {
            throw new IllegalArgumentException("Invalid Gatling request: Missing or incorrect fields.");
        }
    }
    
    public List<GatlingRequest> getAllRequests() {
        return gatlingRequestRepository.findAll();
    }

}
