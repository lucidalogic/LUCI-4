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
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
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
    
      
    private Provider getProvider() throws PKCS11Exception {
        try {
            if (pkcs11Provider == null) {
                // check if the provider already exists
                Provider[] providers = Security.getProviders();
                if (providers != null) {
                    for (Provider provider : providers) {
                        String providerInfo = provider.getInfo();
                        if (providerInfo.contains(params.getPkcs11Path())) {
                            pkcs11Provider = provider;
                            return provider;
                        }
                    }
                }
                // provider not already installed

                installProvider();
            }
            return pkcs11Provider;
        } catch (ProviderException e) {
            String message = "Not a PKCS#11 library";
            //LOGGER.error(message, e);
            throw new PKCS11Exception(message, e);
        }
    }
    
    public void getPass () throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException{
        try{
            //System.out.println(pin);
            
            String configName = "name=SunPKCS11-Open\\r\\nlibrary=C:\\Windows\\System32\\asepkcs.dll";

            KeyStore keyStore =null;

            byte[] configSettingsBytes = configName.getBytes();
            
            ByteArrayInputStream configStream = new ByteArrayInputStream(configSettingsBytes);
             //System.out.println(configStream );
            Provider provider = new SunPKCS11("SunPKCS11");
            System.out.println(provider);
            Security.addProvider(provider);

            keyStore = KeyStore.getInstance("PKCS11", provider);

            keyStore.load(null, pin);
            System.out.println("Entre a la tarjeta");
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
        }
        
        
    }
     private void installProvider() throws PKCS11Exception {
        String config = buildConfig();
        //LOGGER.debug("PKCS11 Config : \n{}", config);

        Provider provider;
        if (isJavaGreaterOrEquals9()) {
            provider = getProviderJavaGreaterOrEquals9(config);
        } else {
            provider = getProviderJavaLowerThan9(config);
        }

        pkcs11Provider = provider;
        Security.addProvider(pkcs11Provider);
    }
       private String buildConfig() {
        /*
         * The smartCardNameIndex int is added at the end of the smartCard name in order to enable the successive
         * loading of multiple pkcs11 libraries.
         *
         * CKA_TOKEN attribute setting is added in order for ECDH key agreement to work with the (OpenSC) driver.
         */
        String aPKCS11LibraryFileName = params.getPkcs11Path();
        aPKCS11LibraryFileName = escapePath(aPKCS11LibraryFileName);

        String pkcs11Config = "name = SmartCard" + UUID.randomUUID().toString() + "\n"
                + "library = \"" + aPKCS11LibraryFileName + "\"\n"
                + "slotListIndex = " + params.getSlot() + "\n"
                + "attributes(*,CKO_SECRET_KEY,*) = {\n" + "  CKA_TOKEN = false\n" + "}" ;

        return pkcs11Config;
    }

    private boolean isJavaGreaterOrEquals9() {
        try {
            Provider provider = Security.getProvider(SUN_PKCS11_PROVIDERNAME);
            if (provider != null) {
                Method configureMethod = provider.getClass().getMethod("configure", String.class);
                return configureMethod != null;
            }
        } catch (NoSuchMethodException e) {
            // ignore
        }
        return false;
    }

    private Provider getProviderJavaGreaterOrEquals9(String configString) throws PKCS11Exception {
        try {
            Provider provider = Security.getProvider(SUN_PKCS11_PROVIDERNAME);
            Method configureMethod = provider.getClass().getMethod("configure", String.class);
            // "--" is permitted in the constructor sun.security.pkcs11.Config
            return (Provider) configureMethod.invoke(provider, "--" + configString);
        } catch (Exception e) {
            throw new PKCS11Exception("Unable to instantiate PKCS11 for JDK >= 9", e);
            //System.out.println("Unable to instantiate PKCS11 for JDK >= 9");
            
        }
    }

    private Provider getProviderJavaLowerThan9(String config) throws PKCS11Exception {
        try (ByteArrayInputStream configStream = new ByteArrayInputStream(config.getBytes())) {
            Class<?> sunPkcs11ProviderClass = Class.forName(SUN_PKCS11_CLASSNAME);
            Constructor<?> constructor = sunPkcs11ProviderClass.getConstructor(InputStream.class);
            return (Provider) constructor.newInstance(configStream);
        } catch (Exception e) {
            throw new PKCS11Exception("Unable to instantiate PKCS11 for JDK < 9 ", e);
        }
    }
     private String escapePath(String pathToEscape) {
        if (pathToEscape != null) {
            return pathToEscape.replace("\\", "\\\\");
        } else {
            return "";
        }
    }
}
