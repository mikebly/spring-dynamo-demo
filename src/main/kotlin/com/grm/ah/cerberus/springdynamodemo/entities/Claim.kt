package com.grm.ah.cerberus.springdynamodemo.entities

import com.amazonaws.services.dynamodbv2.datamodeling.*

@DynamoDBTable(tableName = "claims")
data class Claim @JvmOverloads constructor(
    @DynamoDBHashKey(attributeName = "id")
    var id: String = "",

    @DynamoDBAttribute(attributeName = "claimantLastName")
    var claimantLastName: String? = "",

    @DynamoDBAttribute(attributeName = "claimantFirstName")
    var claimantFirstName: String? = "",

    @DynamoDBAttribute(attributeName = "claimNumber")
    var claimNumber: String? = "",

    @DynamoDBAttribute(attributeName = "claimAmount")
    var claimAmount: String? = ""
)