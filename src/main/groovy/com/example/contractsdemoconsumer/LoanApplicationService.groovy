package com.example.contractsdemoconsumer

import com.example.contractsdemoconsumer.model.FraudCheckStatus
import com.example.contractsdemoconsumer.model.FraudServiceRequest
import com.example.contractsdemoconsumer.model.FraudServiceResponse
import com.example.contractsdemoconsumer.model.LoanApplication
import com.example.contractsdemoconsumer.model.LoanApplicationResult
import com.example.contractsdemoconsumer.model.LoanApplicationStatus

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class LoanApplicationService {

	private static final String FRAUD_SERVICE_JSON_VERSION_1 =
			'application/vnd.fraud.v1+json;charset=UTF-8'

	private final RestTemplate restTemplate

	LoanApplicationService() {
		this.restTemplate = new RestTemplate()
	}

	LoanApplicationResult loanApplication(LoanApplication loanApplication) {
		FraudServiceRequest request = new FraudServiceRequest(loanApplication)
		FraudServiceResponse response = sendRequestToFraudDetectionService(request)
		return buildResponseFromFraudResult(response)
	}

	private FraudServiceResponse sendRequestToFraudDetectionService(FraudServiceRequest request) {
		HttpHeaders httpHeaders = new HttpHeaders()
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, FRAUD_SERVICE_JSON_VERSION_1)
		ResponseEntity<FraudServiceResponse> response =
				restTemplate.exchange("http://localhost:8080/fraudcheck", HttpMethod.PUT,
						new HttpEntity<>(request, httpHeaders),
						FraudServiceResponse.class)
		return response.body
	}

	private LoanApplicationResult buildResponseFromFraudResult(FraudServiceResponse response) {
		LoanApplicationStatus applicationStatus = null
		if (FraudCheckStatus.OK == response.fraudCheckStatus) {
			applicationStatus = LoanApplicationStatus.LOAN_APPLIED
		} else if (FraudCheckStatus.FRAUD == response.fraudCheckStatus) {
			applicationStatus = LoanApplicationStatus.LOAN_APPLICATION_REJECTED
		}
		return new LoanApplicationResult(applicationStatus, response.rejectionReason)
	}

}