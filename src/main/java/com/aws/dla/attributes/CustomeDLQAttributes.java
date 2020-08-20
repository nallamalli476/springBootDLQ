package com.aws.dla.attributes;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.QueueAttributeName;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;

public class CustomeDLQAttributes {

	public static void main(String[] args) {

		  final String sourceQueueName ="mq-dsqs"; 
		  final String deadLetterQueueName = "mq-dsqs";
		  final String visibility_timeout="1";
		  final String delay_seconds="0";
		  final String receivemsgtimeout_seconds="3";
		  final String maxreceive_count="100";
		  
		  String awsAccessKey="XXXXXXXXXXXXXXXXXXX";
		  String awsSecretKey="XXXXXXXXXXXXXXXXXXXXX";
		  final AmazonSQS sqs = AmazonSQSAsyncClientBuilder.standard().withRegion(Regions.US_EAST_2)
					           .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
					           .build();
		  
      try {
		   
    	  // Get the dead-letter queue ARN.
		  final String deadLetterQueueUrl = sqs.getQueueUrl(deadLetterQueueName).getQueueUrl();
		  final GetQueueAttributesResult deadLetterQueueAttributes = sqs.getQueueAttributes(new GetQueueAttributesRequest(deadLetterQueueUrl).withAttributeNames("QueueArn"));
		  final String deadLetterQueueArn = deadLetterQueueAttributes.getAttributes().get("QueueArn");
		  final String sourceQueueUrl = sqs.getQueueUrl(sourceQueueName) .getQueueUrl();
		  final SetQueueAttributesRequest request = new SetQueueAttributesRequest()
		                               .withQueueUrl(sourceQueueUrl)
                                       . addAttributesEntry(QueueAttributeName.VisibilityTimeout.toString(), visibility_timeout)
		          	                   .addAttributesEntry(QueueAttributeName.DelaySeconds.toString(), delay_seconds)
		          	                   .addAttributesEntry(QueueAttributeName.ReceiveMessageWaitTimeSeconds.toString (), receivemsgtimeout_seconds);
		          	                   //.addAttributesEntry(QueueAttributeName.RedrivePolicy.toString(), "{\"maxReceiveCount\":\"" + maxreceive_count + "\", \"deadLetterTargetArn\":\"" + deadLetterQueueArn + "\"}");
		   sqs.setQueueAttributes(request);
           System.out.println("Set queue " + sourceQueueName + " as source queue " +"for dead-letter queue " + deadLetterQueueName + ".");
		    } catch (final AmazonServiceException ase) {
		        System.out.println("Caught an AmazonServiceException, which means " +
		                "your request made it to Amazon SQS, but was " +
		                "rejected with an error response for some reason.");
		        System.out.println("Error Message:    " + ase.getMessage());
		        System.out.println("HTTP Status Code: " + ase.getStatusCode());
		        System.out.println("AWS Error Code:   " + ase.getErrorCode());
		        System.out.println("Error Type:       " + ase.getErrorType());
		        System.out.println("Request ID:       " + ase.getRequestId());
		    } catch (final AmazonClientException ace) {
		        System.out.println("Caught an AmazonClientException, which means " +
		                "the client encountered a serious internal problem while " +
		                "trying to communicate with Amazon SQS, such as not " +
		                "being able to access the network.");
		        System.out.println("Error Message: " + ace.getMessage());
		    }
			

		

	}

}
