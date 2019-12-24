package com.alpha.spring.doc.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Beneficiary {

	/**
	 * Idhu dhan ID .
	 */
	
	@Id
	private long id;

	/**
	 * Account No. badhila ID nu potutan
	 */
	@Size(max=20)
	@NotNull
	private String accountId;

	/**
	 * Name of a beneficiary. max value set pani irukan
	 */
	@Size(max=20)
	@NotNull
	private String name;

	@Size(max=10)
	@NotNull
	private String ifsc;

	@Min(5)
	@Max(10)
	private String nickName;

}
