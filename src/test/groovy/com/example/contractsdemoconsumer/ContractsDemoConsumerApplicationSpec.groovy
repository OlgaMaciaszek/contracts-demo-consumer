package com.example.contractsdemoconsumer

import com.example.contractsdemoconsumer.model.Client
import com.example.contractsdemoconsumer.model.LoanApplication
import com.example.contractsdemoconsumer.model.LoanApplicationResult
import com.example.contractsdemoconsumer.model.LoanApplicationStatus
import spock.lang.Specification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = ['com.example:contracts-demo:+:stubs:8080'], stubsMode = StubRunnerProperties.StubsMode.LOCAL)
class ContractsDemoConsumerApplicationSpec extends Specification {

	@Autowired
	LoanApplicationService loanApplicationService

	def 'should successfully apply for loan'() {
		given:
			LoanApplication application = new LoanApplication(client: new Client(clientId: '12345678902'), amount: 123.123)
		when:
			LoanApplicationResult loanApplication = loanApplicationService.loanApplication(application)
		then:
			loanApplication.loanApplicationStatus == LoanApplicationStatus.LOAN_APPLIED
			loanApplication.rejectionReason == null
	}

	def 'should be rejected due to abnormal loan amount'() {
		given:
			LoanApplication application = new LoanApplication(client: new Client(clientId: '12345678902'), amount: 99_999)
		when:
			LoanApplicationResult loanApplication = loanApplicationService.loanApplication(application)
		then:
			loanApplication.loanApplicationStatus == LoanApplicationStatus.LOAN_APPLICATION_REJECTED
			loanApplication.rejectionReason == 'Amount too high'
	}
}
