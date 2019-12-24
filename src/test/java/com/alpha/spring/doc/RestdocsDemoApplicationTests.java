package com.alpha.spring.doc;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;



import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import com.alpha.spring.doc.controller.BeneficiaryController;
import com.alpha.spring.doc.dao.BeneficiaryRepository;
import com.alpha.spring.doc.model.Beneficiary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


//@SpringBootTest
@RunWith(SpringRunner.class)
@WebMvcTest(BeneficiaryController.class)
@AutoConfigureRestDocs
class RestdocsDemoApplicationTests {
	
	@MockBean
	BeneficiaryRepository beneficiaryRespository;
	
	@Autowired
	ObjectMapper objectMapper ;
	
	@Autowired
	MockMvc mockMvc;

	@Rule
	JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");
	
	@Test
	void addPayeeTest() throws Exception {
		
		Beneficiary beneficiary = new Beneficiary();
		// b.setId(1);
		beneficiary.setAccountId("sd");;
		beneficiary.setIfsc("SBI04085");
		beneficiary.setName("Vijai");
		beneficiary.setNickName("");
		
		when(this.beneficiaryRespository.save(any(Beneficiary.class))).thenReturn(beneficiary);
		FieldDescriptor[] beneficiaryFd= getFieldDescriptors();
		
		this.mockMvc.perform(post("/beneficiary/1").content(this.objectMapper.writeValueAsString(beneficiary))
				.contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        
        .andExpect(jsonPath("$.nickName").isEmpty())
        .andExpect(jsonPath("$.accountId").value("sd"))
        .andExpect(jsonPath("$.ifsc").value("SBI04085"))
        .andExpect(jsonPath("$.name").value("Vijai"))
        .andDo(document("{methodName}",
                requestFields(beneficiaryFd),
                responseFields(beneficiaryFd)));
		
	}
	
	
	@Test
	void getPayeeByIdTest()throws Exception {

		Beneficiary mock = new Beneficiary();
		mock.setId(1L);
		 when(this.beneficiaryRespository.findById(1L)).thenReturn(Optional.of(mock));
		 
		 FieldDescriptor[] beneficiaryFd= getFieldDescriptors();
		 
		 this.mockMvc.perform(RestDocumentationRequestBuilders.get("/beneficiary/{id}","1"))
		 .andExpect(status().isOk())
		 .andExpect(jsonPath("$.id").value(1))
		 .andDo(document("{methodName}",
				 pathParameters(
                         parameterWithName("id").description("The id of the beneficiary to retrieve")
                 ),
	                responseFields(beneficiaryFd)));
			
		}
	
	

	
	private FieldDescriptor[] getFieldDescriptors() {
		return new FieldDescriptor[] {
				fieldWithPath("id").description("ID of the payee").type(Long.class.getSimpleName()),
				fieldWithPath("name").description("ID of the payee").type(String.class.getSimpleName()),
				fieldWithPath("accountId").description("ID of the payee").type(String.class.getSimpleName()),
				fieldWithPath("ifsc").description("ID of the payee").type(String.class.getSimpleName()),
				fieldWithPath("nickName").description("ID of the payee").type(String.class.getSimpleName()),
				
				
		};
		
		
	}
	
}
