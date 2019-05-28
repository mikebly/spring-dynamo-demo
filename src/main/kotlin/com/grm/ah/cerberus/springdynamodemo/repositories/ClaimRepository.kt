package com.grm.ah.cerberus.springdynamodemo.repositories

import com.grm.ah.cerberus.springdynamodemo.entities.Claim
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository

@EnableScan
interface ClaimRepository: CrudRepository<Claim,String>, CustomClaimRepository{
//    fun findByClaimNumber(number: String):Claim
    fun findClaimByClaimNumber(number: String):List<Claim>
    fun deleteClaimByClaimNumber(number: String)

}