package com.example.contractsdemoconsumer.model

import groovy.transform.Canonical

@Canonical
class FraudServiceResponse {

	FraudCheckStatus fraudCheckStatus
	String rejectionReason
}
