package tg.jwt;

import java.nio.charset.Charset;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptResponse;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

public class OnlineEncyrption {

	/**
	 * Instead of Create a Local Key And Having the risk of sharing or losing it Why
	 * can i not use a key service a) The key storead shared because its a file b)
	 * Revoking a key store is difficult because i might used mulitple locatoin
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		SdkBytes inputBytes = SdkBytes.fromString("nilesh", Charset.forName("UTF-8"));
		/** fetches keys from a Service **/
		/**
		 * If key expires i dont have to relace keys in mulitple location
		 */
		KmsClient client = KmsClient.builder().build();
		/*******************************************
		 * ENCRYPT
		 ***************************************/
		EncryptRequest encryptRequest = EncryptRequest.builder().encryptionAlgorithm("RSAES_OAEP_SHA_256")
				.keyId("arn:aws:kms:us-east-1:457362930747:key/5d75d899-0bac-42f8-b110-5d941485d739")
				.plaintext(inputBytes).build();
		EncryptResponse encryptResponse = client.encrypt(encryptRequest);
		SdkBytes b1 = encryptResponse.ciphertextBlob();
		System.out.println(new String(b1.asByteArray()));
		/*******************************************
		 * ENCRYPT
		 ***************************************/

		/*******************************************
		 * ENCRYPT
		 ***************************************/

		DecryptRequest decryptRequest = DecryptRequest.builder().encryptionAlgorithm("RSAES_OAEP_SHA_256")
				.keyId("arn:aws:kms:us-east-1:457362930747:key/5d75d899-0bac-42f8-b110-5d941485d739").ciphertextBlob(b1)
				.build();
		System.out.println(client.decrypt(decryptRequest).plaintext().asUtf8String());

		/*******************************************
		 * ENCRYPT
		 ***************************************/
		test();
	}

	static void test() {
		// any password in my code
		// hardcoding any secrets
		// nor in my property file
		// not anywhere

		String secretName = "appleKey";
		Region region = Region.of("us-east-1");
		// Create a Secrets Manager client
		SecretsManagerClient client = SecretsManagerClient.builder().region(region).build();
		GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder().secretId(secretName).build();
		GetSecretValueResponse getSecretValueResponse;
		try {
			getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
		} catch (Exception e) {
			throw e;
		}

		String secret = getSecretValueResponse.secretString();

		// Your code goes here.
		System.out.println(secret);
	}
}
