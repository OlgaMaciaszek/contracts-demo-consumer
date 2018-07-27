package com.example.contractsdemoconsumer.model

class FraudServiceRequest {

	String clientId
	BigDecimal loanAmount

	FraudServiceRequest(LoanApplication loanApplication) {
		this.clientId = loanApplication.client.clientId
		this.loanAmount = loanApplication.amount
	}
}