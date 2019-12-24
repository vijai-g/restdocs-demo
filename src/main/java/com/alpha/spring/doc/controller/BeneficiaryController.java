package com.alpha.spring.doc.controller;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.spring.doc.dao.BeneficiaryRepository;
import com.alpha.spring.doc.model.Beneficiary;

@RestController
@RequestMapping("/beneficiary")
public class BeneficiaryController {
	
	BeneficiaryRepository beneficiaryRepository;
	
	public BeneficiaryController(BeneficiaryRepository br) {
		this.beneficiaryRepository= br;
	}

	/**
	 * @param id controller mela pota ID.
	 */
	@PostMapping("/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	Beneficiary addPayee(@RequestBody @Valid Beneficiary beneficiary, @PathVariable Long id ){
		//  Assert.isNull(beneficiary.getId(), "Id must be null daw");
		  
		return this.beneficiaryRepository.save(beneficiary);
	}
	
	
	@GetMapping("/{id}")
	Beneficiary getPayeeById(@PathVariable long id){
		Assert.notNull(id, "ID can not be null");
		return this.beneficiaryRepository.findById(id).orElse(null);	
	}
	
	@GetMapping("/getAllPayees")
    public Page<Beneficiary> getAllPayees(Pageable pageable) {
        return this.beneficiaryRepository.findAll(pageable);
    }
	
	
	@PutMapping("/update")
	public Beneficiary update(@RequestBody Beneficiary beneficiary) {
		return this.beneficiaryRepository.save(beneficiary) ;
		
	}
	
	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable long id) {
		this.beneficiaryRepository.deleteById(id);;
		return "Delete Successful";
		
	}
	
}

