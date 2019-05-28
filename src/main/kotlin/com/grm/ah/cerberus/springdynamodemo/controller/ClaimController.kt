package com.grm.ah.cerberus.springdynamodemo.controller

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.grm.ah.cerberus.springdynamodemo.entities.Claim
import com.grm.ah.cerberus.springdynamodemo.repositories.ClaimRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/claims")
class ClaimController(private val claimRepository: ClaimRepository) {
    companion object{
        val logger: Logger = LoggerFactory.getLogger(ClaimController::class.java)
    }

    @Autowired
    lateinit var mapper: DynamoDBMapper

    @GetMapping("/table")
    fun createTable(){
        logger.info("Creating table")
        claimRepository.createTable()
    }

    @GetMapping("")
    fun findAll(
        @RequestParam(required = false) number: String? = null
    ):List<Claim>{
        logger.info("Listing all claims")
//        Use mapper or repository to retrieve data
//        val claims = mapper.scan(Claim::class.java, DynamoDBScanExpression())

        val claims = if(number != null) {
            claimRepository.findClaimByClaimNumber(number)
        }else{
            claimRepository.findAll().filterNotNull()
        }
        printClaimsInfo(claims)
        return claims
    }

    @PostMapping
    fun createClaim(
        @RequestParam id: String,
        @RequestParam number: String,
        @RequestParam lastName: String,
        @RequestParam firstName: String,
        @RequestParam amount: String
    ):Claim{
        logger.info("Creating new claim")
        val newClaim = Claim(
            id = id,
            claimNumber = number,
            claimAmount = amount,
            claimantLastName = lastName,
            claimantFirstName = firstName
        )
        logger.info("Created [$newClaim]")
//        Can use DynamoDBMapper to save items, or leverage Spring Data to save with your repository
//        mapper.save(newClaim)
//        return newClaim
        return claimRepository.save(newClaim)
    }

    @PutMapping("/{number}")
    fun updateClaim(
        @PathVariable number: String,
        @RequestBody claim: Claim
    ):Claim{
        val claimById = claimRepository.findClaimByClaimNumber(number)
        claimById[0].id = claim.id
        claimById[0].claimAmount = claim.claimAmount
        claimById[0].claimNumber = claim.claimNumber
        claimById[0].claimantLastName = claim.claimantLastName
        claimById[0].claimantFirstName = claim.claimantFirstName
        claimRepository.save(claimById[0])
        return claimById[0]
    }

    @DeleteMapping("/remove/{number}")
    fun removeClaim(@PathVariable number: String){
        claimRepository.deleteClaimByClaimNumber(number)
    }

    private fun printClaimsInfo(claims: List<Claim>){
        claims.forEach {
            logger.info("**************************")
            logger.info("Retrieved id: [${it.id}]")
            logger.info("Claim Number [${it.claimNumber}]")
            logger.info("Claim Amount [${it.claimAmount}]")
            logger.info("Name: [${it.claimantFirstName} ${it.claimantLastName}]")
            logger.info("**************************")
        }
    }

}