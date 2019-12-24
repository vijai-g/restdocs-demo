package com.alpha.spring.doc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.cli.CliDocumentation;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alpha.spring.doc.controller.BeneficiaryController;
import com.alpha.spring.doc.dao.BeneficiaryRepository;
import com.alpha.spring.doc.model.Beneficiary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import capital.scalable.restdocs.AutoDocumentation;
import capital.scalable.restdocs.jackson.JacksonResultHandlers;

@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@WebMvcTest(BeneficiaryController.class)
@EnableSpringDataWebSupport
public class RestAutoDocTests {

	 @MockBean
	    private BeneficiaryRepository beneficiaryRepository;

	    @Autowired
	    private ObjectMapper objectMapper;
	    @Autowired
	    private WebApplicationContext context;
	    
	    @Autowired
	    private MockMvc mockMvc;
	    
	    @BeforeEach
	    void setUp(RestDocumentationContextProvider restDocumentation) {
	        this.mockMvc = MockMvcBuilders
	                .webAppContextSetup(context)
	                .alwaysDo(JacksonResultHandlers.prepareJackson(objectMapper))
	                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
	                        .uris()
	                        .withScheme("http")
	                        .withHost("localhost")
	                        .withPort(8080)
	                        .and().snippets()
	                        .withDefaults(CliDocumentation.curlRequest(),
	                                HttpDocumentation.httpRequest(),
	                                HttpDocumentation.httpResponse(),
	                                AutoDocumentation.requestFields(),
	                                AutoDocumentation.responseFields(),
	                                AutoDocumentation.pathParameters(),
	                                AutoDocumentation.requestParameters(),
	                                AutoDocumentation.description(),
	                                AutoDocumentation.methodAndPath(),
	                                AutoDocumentation.section()))
	                .alwaysDo(document("{methodName}",
	                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
	                .build();
	    }

	    
	    @Test
	    void addPayeeTest() throws Exception {
			
			Beneficiary beneficiary = new Beneficiary();
			//beneficiary.setId(1);
			beneficiary.setAccountId("sd");
			beneficiary.setIfsc("SBI04085");
			beneficiary.setName("Vijai");
			beneficiary.setNickName("");
			
			when(this.beneficiaryRepository.save(any(Beneficiary.class))).thenReturn(beneficiary);
		
			
			this.mockMvc.perform(post("/beneficiary/1").content(this.objectMapper.writeValueAsString(beneficiary))
					.contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isCreated())
	       // .andExpect(jsonPath("$.id").value(1))
	        .andExpect(jsonPath("$.nickName").isEmpty())
	        .andExpect(jsonPath("$.accountId").value("sd"))
	        .andExpect(jsonPath("$.ifsc").value("SBI04085"))
	        .andExpect(jsonPath("$.name").value("Vijai"));
	        
	    }
	
	    @Test
	    void getPayeeByIdTest() throws Exception 
	    {
	    	Beneficiary mock = new Beneficiary();
			mock.setId(1L);
			 when(this.beneficiaryRepository.findById(1L)).thenReturn(Optional.of(mock));
			 
		
			 
			 this.mockMvc.perform(RestDocumentationRequestBuilders.get("/beneficiary/{id}","1"))
			 .andExpect(status().isOk());
			// .andExpect(jsonPath("$.id").value(1));
	    }
	    
	    @Test
	   void  getAllPayeesTest() throws Exception{

	        when(this.beneficiaryRepository.findAll(PageRequest.of(1, 15))).thenReturn(new PageImpl<>(Collections.singletonList(new Beneficiary())));

	        this.mockMvc.perform(get("/beneficiary/getAllPayees?page=1&size=15"))
	                .andExpect(status().isOk());
	    }
	    
	    @Test
	    void updateTest() throws JsonProcessingException, Exception {
	    	 Beneficiary beneficiary = new Beneficiary();
	         beneficiary.setId(1L);;
	         beneficiary.setAccountId("asfas");
	         beneficiary.setIfsc("SBI6584");
	         beneficiary.setName("Vijai");

	         this.mockMvc.perform(put("/beneficiary/update")
	                 .content(this.objectMapper.writeValueAsString(beneficiary))
	                 .contentType(MediaType.APPLICATION_JSON))
	                 .andExpect(status().isOk());
	    }
}
