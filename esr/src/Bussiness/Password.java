 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bussiness;

import Exception.PKCS11Exception;
import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
import javax.crypto.KeyAgreement;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import static java.util.Collections.singletonList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import static javax.xml.crypto.dsig.CanonicalizationMethod.EXCLUSIVE;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import static javax.xml.crypto.dsig.Transform.ENVELOPED;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.keyinfo.X509IssuerSerial;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import static jdk.nashorn.tools.ShellFunctions.input;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import sun.security.pkcs11.SunPKCS11;

/**
 *
 * @author Lucida
 */
public class Password {
    public char[] pin;
    private Provider pkcs11Provider;
    private PKCS11TokenParams params;
    private static final String SUN_PKCS11_PROVIDERNAME = "SunPKCS11";
    private static final String SUN_PKCS11_CLASSNAME = "sun.security.pkcs11.SunPKCS11";
    public Password() {
    }
    
      
    
    public void getPass () throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, PKCS11Exception, InvalidAlgorithmParameterException, UnrecoverableEntryException, SAXException, MarshalException, TransformerConfigurationException, KeyException, ParserConfigurationException, XMLSignatureException{
        try{
            //System.out.println(pin);
            
            String configName = "name=SunPKCS11-Open\r\nlibrary=C:\\Windows\\System32\\asepkcs.dll";

            KeyStore keyStore =null;

            byte[] configSettingsBytes = configName.getBytes();
            
            ByteArrayInputStream configStream = new ByteArrayInputStream(configSettingsBytes);
             //System.out.println(configStream );
            Provider provider = new SunPKCS11(configStream);
            System.out.println(provider);
            Security.addProvider(provider);

            keyStore = KeyStore.getInstance("PKCS11", provider);

            keyStore.load(null, pin);
          //  System.out.println(keyStore.getCertificate(configName).hashCode());
            System.out.println("Entre a la tarjeta");
            Enumeration aliases = keyStore.aliases();
            System.out.println(aliases);
            String alias = null;
            while (aliases.hasMoreElements()) {             
                alias = aliases.nextElement().toString();
                System.out.println(alias);
            }  
            PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, null); 
            System.out.println(privKey);
            //KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
            //System.out.println("keyEntry: "+ keyEntry);
            
            
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
 
            /*sign documen*/
          //  X509Certificate cert = (X509Certificate) keyEntry.getCertificate();
           // Path x509cert = dataDir.resolve("cert.pem");
           // System.out.println("Cert: "+ cert);
            

           
      //  XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM", (Provider) Class.forName(providerName).newInstance());
        System.out.println("Entre");
        /*DigestMethod digestMethod = fac.newDigestMethod(DigestMethod.SHA256, null);
        Transform transform = fac.newTransform(ENVELOPED, (TransformParameterSpec) null);
        Reference reference = (Reference) fac.newReference("", digestMethod, singletonList(transform), null, null);
        SignatureMethod signatureMethod = fac.newSignatureMethod("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256", null);
        CanonicalizationMethod canonicalizationMethod = fac.newCanonicalizationMethod(EXCLUSIVE, (C14NMethodParameterSpec) null);
        
        // Create the SignedInfo
        SignedInfo si = fac.newSignedInfo(canonicalizationMethod, signatureMethod, singletonList(reference));


        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);

        KeyPair kp = kpg.generateKeyPair();

        KeyInfoFactory kif = fac.getKeyInfoFactory();
        KeyValue kv = kif.newKeyValue(kp.getPublic());
        
        // Create a KeyInfo and add the KeyValue to it
        KeyInfo ki = kif.newKeyInfo(Collections.singletonList(kv));
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        String document = "C:\\Users\\Lucida\\Desktop\\Factura.xml";
        Document doc = dbf.newDocumentBuilder().parse(new FileInputStream(document));
        
        DOMSignContext dsc = new DOMSignContext(kp.getPrivate(), doc.getDocumentElement());

        XMLSignature signature = fac.newXMLSignature(si, ki);
        signature.sign(dsc);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();

        // output the resulting document
        OutputStream os;

        os = new FileOutputStream("xmlOut.xml");

        trans.transform(new DOMSource(doc), new StreamResult(os));
            */
        }catch (KeyStoreException ksex){    
            //Add log
            System.out.println("An KeyStoreException was caught :"+ksex.getMessage());
        }catch (IOException ioex){
            System.out.println("IO exception ");
            System.out.println("An IOException was caught :"+ioex.getMessage());
        }catch (NoSuchAlgorithmException nsaex){
            System.out.println("An NoSuchAlgorithmException was caught :"+nsaex.getMessage());
        }catch (CertificateException cex){
            System.out.println("An CertificateException was caught :"+cex.getMessage());
        } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
        } //catch (TransformerException ex) {
            //Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
         /*catch (ParserConfigurationException ex) {
            Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLSignatureException ex) {
            Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        catch (Exception e){
            System.out.println("Error: "+e);
        }
    
    }
    
  
}
