package com.example.contractsdemoconsumer.model

import groovy.transform.Canonical

@Canonical
class LoanApplicationResult {

	LoanApplicationStatus loanApplicationStatus
	String rejectionReason
}