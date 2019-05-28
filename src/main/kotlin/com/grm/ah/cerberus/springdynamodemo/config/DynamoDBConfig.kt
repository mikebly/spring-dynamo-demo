package com.grm.ah.cerberus.springdynamodemo.config

import com.amazonaws.AmazonClientException
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverterFactory
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Profile("!local")
@Configuration
@EnableDynamoDBRepositories(basePackages = ["com.grm.ah.cerberus.springdynamodemo.repositories"])
class DynamoDBConfig (
    @Value("\${aws.dynamodb.endpoint}")
    private val dynamoEndpoint:String,

    @Value("\${cloud.aws.region.static}")
    private val region: String,

    @Value("\${cloud.aws.credentials.access-key}")
    private val accessKey: String,

    @Value("\${cloud.aws.credentials.secret-key}")
    private val secretKey: String
){
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Bean("amazonDynamoDB")
    fun amazonDynamoDb():AmazonDynamoDB{
        val client: AmazonDynamoDB
        val credentials = ProfileCredentialsProvider("personal")
        try{
            client = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentials)
                .withRegion(Regions.US_EAST_1)
                .build()
        }catch (err: Exception){
            logger.error("Error: [$err]")
            throw AmazonClientException("Cannot load credentials from ~/.aws/credentials, [$err]")
        }
        return client
    }

    @Primary
    @Bean
    fun mapper(dynamoDb:AmazonDynamoDB,mapperConfig: DynamoDBMapperConfig): DynamoDBMapper {
        return DynamoDBMapper(dynamoDb,mapperConfig)
    }

    @Primary
    @Bean
    fun mapperConfig(): DynamoDBMapperConfig {
        val builder = DynamoDBMapperConfig.Builder()
        builder.paginationLoadingStrategy = DynamoDBMapperConfig.PaginationLoadingStrategy.LAZY_LOADING
        builder.typeConverterFactory = DynamoDBTypeConverterFactory.standard()
        return builder.build()
    }

    @Bean
    fun dynamoDb(amazonDynamoDB: AmazonDynamoDB): DynamoDB {
        return DynamoDB(amazonDynamoDB)
    }
}