Creating Key Store (paste in cmd in path C:\Program Files\Java\jdk1.8.0_211\bin>):

keytool -genseckey -alias myDesKey -keypass someKeyPassword -keystore myDesKey.jceks -storepass someKeystorePassword -v -storetype JCEKS -keyalg DES

Adding dependency for crypto-starter:

		<dependency>
			<groupId>org.apache.camel.springboot</groupId>
			<artifactId>camel-crypto-starter</artifactId>
			<version>${camel.version}</version>
		</dependency>

Method to read from Key Store (paste in sender and receiver routers and in pipeline add marshal(createEncryptor()) in sender and unmarshal(createEncryptor()) in receiver):

private CryptoDataFormat createEncryptor() throws KeyStoreException, IOException, NoSuchAlgorithmException,
CertificateException, UnrecoverableKeyException {
KeyStore keyStore = KeyStore.getInstance("JCEKS");
ClassLoader classLoader = getClass().getClassLoader();
keyStore.load(classLoader.getResourceAsStream("myDesKey.jceks"), "someKeystorePassword".toCharArray());
Key sharedKey = keyStore.getKey("myDesKey", "someKeyPassword".toCharArray());

	CryptoDataFormat sharedKeyCrypto = new CryptoDataFormat("DES", sharedKey);
	return sharedKeyCrypto;
}