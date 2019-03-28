/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package letturasmartcard;

import java.security.PrivateKey;

/**
 *
 * @author Lucida
 */
public class SmartCardSignerApplet {
     private static final String FILE_NAME_FIELD_PARAM =
      "fileNameField";
   private static final String CERT_CHAIN_FIELD_PARAM =
      "certificationChainField";
   private static final String SIGNATURE_FIELD_PARAM =
      "signatureField";
   private static final String SIGN_BUTTON_CAPTION_PARAM =
      "signButtonCaption";

   private static final String PKCS11_KEYSTORE_TYPE =
      "PKCS11";
   private static final String X509_CERTIFICATE_TYPE =
      "X.509";
   private static final String CERTIFICATION_CHAIN_ENCODING =
      "PkiPath";
   private static final String DIGITAL_SIGNATURE_ALGORITHM_NAME =
      "SHA1withRSA";
   private static final String SUN_PKCS11_PROVIDER_CLASS =
      "sun.security.pkcs11.SunPKCS11";

   private Button mSignButton;

   /**
    * Initializes the applet - creates and initializes its graphical
    * user interface. Actually the applet consists of a single
    * button, that fills its all surface. The button's caption is
    * taken from the applet parameter SIGN_BUTTON_CAPTION_PARAM.
    */

   public void init() {
      String signButtonCaption =
         this.getParameter(SIGN_BUTTON_CAPTION_PARAM);
      mSignButton = new Button(signButtonCaption);
      mSignButton.setLocation(0, 0);
      Dimension appletSize = this.getSize();
      mSignButton.setSize(appletSize);
      mSignButton.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e) {
            signSelectedFile();
         }
      });
      this.setLayout(null);
      this.add(mSignButton);
   }

   /**
    * Signs the selected file. The file name comes from a field
    * in the HTML document. The result consists of the calculated
    * digital signature and certification chain, both placed in
    * fields in the HTML document, encoded in Base64 format.
    * The HTML document should contain only one HTML form.
    * The name of the field, that contains the name of the file
    * to be signed is obtained from FILE_NAME_FIELD_PARAM applet
    * parameter. The names of the output fields for the signature
    * and the certification chain are obtained from the parameters
    * CERT_CHAIN_FIELD_PARAM and SIGNATURE_FIELD_PARAM. The user
    * is asked to choose a PKCS#11 implementation library and a
    * PIN code for accessing the smart card.
    */

   private void signSelectedFile() {
      try {
         // Get the file name to be signed from the form in the
         // HTML document
         JSObject browserWindow = JSObject.getWindow(this);
         JSObject mainForm =
            (JSObject) browserWindow.eval("document.forms[0]");
         String fileNameFieldName =
            this.getParameter(FILE_NAME_FIELD_PARAM);
         JSObject fileNameField =
           (JSObject) mainForm.getMember(fileNameFieldName);
         String fileName = (String) fileNameField.getMember("value");

         // Perform the actual file signing
         CertificationChainAndSignatureBase64 signingResult =
            signFile(fileName);
         if null) {
            // Document signed. Fill the certificate and
            // signature fields
            String certChainFieldName =
               this.getParameter(CERT_CHAIN_FIELD_PARAM);
            JSObject certChainField =
               (JSObject) mainForm.getMember(certChainFieldName);
            certChainField.setMember("value",
               signingResult.mCertificationChain);
            String signatureFieldName =
               this.getParameter(SIGNATURE_FIELD_PARAM);
            JSObject signatureField =
               (JSObject) mainForm.getMember(signatureFieldName);
            signatureField.setMember("value",
                signingResult.mSignature);
         else {
            // User canceled signing
         }
      }
      catch (DocumentSignException dse) {
         // Document signing failed. Display error message
         String errorMessage = dse.getMessage();
         JOptionPane.showMessageDialog(this, errorMessage);
      }
      catch (SecurityException se) {
         se.printStackTrace();
         JOptionPane.showMessageDialog(this,
            "Unable to access the local file system.\n" +
            "This applet should be started with full security permissions.\n" +
            "Please accept to trust this applet when the Java Plug-In asks you.");
      }
      catch (JSException jse) {
         jse.printStackTrace();
         JOptionPane.showMessageDialog(this,
            "Unable to access some of the fields of the\n" +
            "HTML form. Please check the applet parameters.");
      }
      catch (Exception e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(this,
            "Unexpected error: " + e.getMessage());
      }
   }

   /**
    * Signs given local file. The certificate and private key to
    * be used for signing come from the locally attached smart
    * card. The user is requested to provide a PKCS#11
    * implementation library and the PIN code for accessing the
    * smart card. @param aFileName the name of the file to be
    * signed.
    * @return the digital signature of the given file and the
    * certification chain of the certificatie used for signing the
    * file, both Base64-encoded or null if the signing process is
    * canceled by the user.
    * @throws DocumentSignException when a problem arised during
    * the singing process (e.g. smart card access problem,
    * invalid certificate, invalid PIN code, etc.)
    */

   private CertificationChainAndSignatureBase64 signFile(String
      aFileName)
   throws DocumentSignException {

      // Load the file for signing
      byte[] documentToSign = null;
      try {
         documentToSign = readFileInByteArray(aFileName);
      } catch (IOException ioex) {
         String errorMsg = "Cannot read the file for signing " +
            aFileName + ".";
         throw new DocumentSignException(errorMsg, ioex);
      }

      // Show a dialog for choosing PKCS#11 implementation library
      // and smart card PIN
      PKCS11LibraryFileAndPINCodeDialog pkcs11Dialog =
         new PKCS11LibraryFileAndPINCodeDialog();
      boolean dialogConfirmed;
      try {
         dialogConfirmed = pkcs11Dialog.run();
      } finally {
         pkcs11Dialog.dispose();
      }

      if (dialogConfirmed) {
         String oldButtonLabel = mSignButton.getLabel();
         mSignButton.setLabel("Working...");
         mSignButton.setEnabled(false);
         try {
            String pkcs11LibraryFileName =
               pkcs11Dialog.getLibraryFileName();
            String pinCode = pkcs11Dialog.getSmartCardPINCode();

            // Do the actual signing of the document with the
            // smart card
            CertificationChainAndSignatureBase64 signingResult =
               signDocument(documentToSign, pkcs11LibraryFileName,
                  pinCode);
            return signingResult;
         finally {
            mSignButton.setLabel(oldButtonLabel);
            mSignButton.setEnabled(true);
         }
      }
      else {
         return null;
      }
   }

   private C signDocument(
      byte[] aDocumentToSign, String aPkcs11LibraryFileName,
         String aPinCode)
   throws DocumentSignException {
      if (aPkcs11LibraryFileName.length() == 0) {
         String errorMessage = "It is mandatory to choose " +
            "a PCKS#11 native implementation library for " + 
            "smart card (.dll or .so file)!";
         throw new DocumentSignException(errorMessage);
      }

      // Load the keystore from the smart card using the specified
      // PIN code
      KeyStore userKeyStore = null;
      try{
         userKeyStore = loadKeyStoreFromSmartCard(
             aPkcs11LibraryFileName, aPinCode);
      }catch (Exception ex) {
         String errorMessage = "Cannot read the keystore from the smart card.\n" +
            "Possible reasons:\n" +
            " - The smart card reader in not connected.\n" +
            " - The smart card is not inserted.\n" +
            " - The PKCS#11 implementation library is invalid.\n" +
            " - The PIN for the smart card is incorrect.\n" +
            "Problem details: " + ex.getMessage();
         throw new DocumentSignException(errorMessage, ex);
      }

      // Get the private key and its certification chain from the
      // keystore
      PrivateKeyAndCertChain privateKeyAndCertChain = null;
      try {
         privateKeyAndCertChain =
            getPrivateKeyAndCertChain(userKeyStore);
      } catch (GeneralSecurityException gsex) {
         String errorMessage = "Cannot extract the private key " +
            "and certificate from the smart card. Reason: " +
            gsex.getMessage();
         throw new DocumentSignException(errorMessage, gsex);
      }

      // Check if the private key is available
      PrivateKey privateKey = privateKeyAndCertChain.mPrivateKey;
      if (privateKey == null) {
         String errorMessage = "Cannot find the private key on " +
            "the smart card.";
         throw new DocumentSignException(errorMessage);
      }

      // Check if X.509 certification chain is available
      Certificate[] certChain =
         privateKeyAndCertChain.mCertificationChain;
      if (certChain == null) {
         String errorMessage = "Cannot find the certificate on " +
            "the smart card.";
         throw new DocumentSignException(errorMessage);
      }

      // Create the result object
      CertificationChainAndSignatureBase64 signingResult =
         new CertificationChainAndSignatureBase64();

      // Save X.509 certification chain in the result encoded in
      // Base64
      try {
         signingResult.mCertificationChain =
            encodeX509CertChainToBase64(certChain);
      }
      catch (CertificateException cee) {
         String errorMessage = "Invalid certificate on the "
            "smart card.";
         throw new DocumentSignException(errorMessage);
      }

      // Calculate the digital signature of the file,
      // encode it in Base64 and save it in the result
      try {
         byte[] digitalSignature =
            signDocument(aDocumentToSign, privateKey);
         signingResult.mSignature =
            Base64Utils.base64Encode(digitalSignature);
      catch (GeneralSecurityException gsex) {
         String errorMessage = "File signing failed.\n" +
            "Problem details: " + gsex.getMessage();
         throw new DocumentSignException(errorMessage, gsex);
      }

      return signingResult;
   }

   /**
    * Loads the keystore from the smart card using its PKCS#11
    * implementation library and the Sun PKCS#11 security provider.
    * The PIN code for accessing the smart card is required.
    */

   private KeyStore loadKeyStoreFromSmartCard(String
      aPKCS11LibraryFileName, String aSmartCardPIN)
   throws GeneralSecurityException, IOException {
      // First configure the Sun PKCS#11 provider. It requires a
      // stream (or file) containing the configuration parameters -
      // "name" and "library".
      String pkcs11ConfigSettings =
         "name = SmartCard\n" + "library = " + aPKCS11LibraryFileName;
      byte[] pkcs11ConfigBytes = pkcs11ConfigSettings.getBytes();
      ByteArrayInputStream confStream =
         new ByteArrayInputStream(pkcs11ConfigBytes);

      // Instantiate the provider dynamically with Java reflection
      try {
         Class sunPkcs11Class =
            Class.forName(SUN_PKCS11_PROVIDER_CLASS);
         Constructor pkcs11Constr = sunPkcs11Class.getConstructor(
            java.io.InputStream.class);
         Provider pkcs11Provider =
           (Provider) pkcs11Constr.newInstance(confStream);
         Security.addProvider(pkcs11Provider);
      catch (Exception e) {
         throw new KeyStoreException("Can initialize " +
            "Sun PKCS#11 security provider. Reason: " + 
            e.getCause().getMessage());
      }

      // Read the keystore form the smart card
      char[] pin = aSmartCardPIN.toCharArray();
      KeyStore keyStore = KeyStore.getInstance(PKCS11_KEYSTORE_TYPE);
      keyStore.load(null, pin);
      return keyStore;
   }

   /**
    * @return private key and certification chain corresponding
    * to it, extracted from the given keystore. The keystore is
    * considered to have only one entry that contains both
    * certification chain and its corresponding private key.
    * If the keystore has no entries, an exception is thrown.
    */

   private PrivateKeyAndCertChain getPrivateKeyAndCertChain(
      KeyStore aKeyStore)
   throws GeneralSecurityException {
      Enumeration aliasesEnum = aKeyStore.aliases();
      if (aliasesEnum.hasMoreElements()) {
         String alias = (String)aliasesEnum.nextElement();
         Certificate[] certificationChain =
            aKeyStore.getCertificateChain(alias);
         PrivateKey privateKey =
            (PrivateKey) aKeyStore.getKey(alias, null);
         PrivateKeyAndCertChain result = new PrivateKeyAndCertChain();
         result.mPrivateKey = privateKey;
         result.mCertificationChain = certificationChain;
         return result;
      else {
         throw new KeyStoreException("The keystore is empty!");
      }
   }

   /**
    * @return Base64-encoded ASN.1 DER representation of given
    * X.509 certification chain.
    */

   private String encodeX509CertChainToBase64
      (Certificate[] aCertificationChain)
   throws CertificateException {
      List certList = Arrays.asList(aCertificationChain);
      CertificateFactory certFactory =
         CertificateFactory.getInstance(X509_CERTIFICATE_TYPE);
      CertPath certPath = certFactory.generateCertPath(certList);
      byte[] certPathEncoded =
         certPath.getEncoded(CERTIFICATION_CHAIN_ENCODING);
      String base64encodedCertChain =
         Base64Utils.base64Encode(certPathEncoded);
      return base64encodedCertChain;
   }

   /**
    * Reads the specified file into a byte array.
    */

   private byte[] readFileInByteArray(String aFileName)
   throws IOException {
      File file = new File(aFileName);
      FileInputStream fileStream = new FileInputStream(file);
      try {
         int fileSize = (int) file.length();
         byte[] data = new byte[fileSize];
         int bytesRead = 0;
         while (bytesRead < fileSize) {
            bytesRead += fileStream.read(
               data, bytesRead, fileSize-bytesRead);
         }
         return data;
      }
      finally {
         fileStream.close();
      }
   }

   /**
    * Signs given document with a given private key.
    */

   private byte[] signDocument(byte[] aDocument,
                               PrivateKey aPrivateKey)
   throws GeneralSecurityException {
      Signature signatureAlgorithm =
         Signature.getInstance(DIGITAL_SIGNATURE_ALGORITHM_NAME);
      signatureAlgorithm.initSign(aPrivateKey);
      signatureAlgorithm.update(aDocument);
      byte[] digitalSignature = signatureAlgorithm.sign();
      return digitalSignature;
   }

   /**
    * Data structure that holds a pair of private key and
    * certification chain corresponding to this private key.
    */

   static class PrivateKeyAndCertChain {
      public PrivateKey mPrivateKey;
      public Certificate[] mCertificationChain;
   }

   /**
    * Data structure that holds a pair of Base64-encoded
    * certification chain and digital signature.
    */

   static class CertificationChainAndSignatureBase64 {
      public String mCertificationChain = null;
      public String mSignature = null;
   }

   /**
    * Exception class used for document signing errors.
    */

   static class DocumentSignException extends Exception {
      public DocumentSignException(String aMessage) {
         super(aMessage);
      }

      public DocumentSignException(String aMessage,
            Throwable aCause) {
         super(aMessage, aCause);
      }
   }
       
}
