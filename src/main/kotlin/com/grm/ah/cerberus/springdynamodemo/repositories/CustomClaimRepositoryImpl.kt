package com.grm.ah.cerberus.springdynamodemo.repositories

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.model.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.stereotype.Component

@Component
@EnableScan
class CustomClaimRepositoryImpl(
    private val dynamoDB: DynamoDB
): CustomClaimRepository {

    private val logger: Logger = LoggerFactory.getLogger(CustomClaimRepositoryImpl::class.java)
    private val tableName = "claims2"


    override fun createTable(){
        val schema = mutableListOf(
            KeySchemaElement("id",KeyType.HASH),
            KeySchemaElement("claimNumber",KeyType.RANGE)
        )
        val attributeDefinition = mutableListOf(
            AttributeDefinition("id",ScalarAttributeType.S),
            AttributeDefinition("claimNumber",ScalarAttributeType.S)
        )
        try{
            val table = dynamoDB.createTable(tableName,schema,attributeDefinition, ProvisionedThroughput(10L,10L))
            table.waitForActive()
            logger.info("Table created: ${table.description.tableStatus}")
        }catch (err: ResourceInUseException){
            logger.error("Table already exists: ${err.message}")
            throw ResourceInUseException(err.message)
        }catch (err: Exception){
            logger.error("Unable to create table: ${err.message}")
            throw AmazonDynamoDBException(err.message)
        }
    }
}