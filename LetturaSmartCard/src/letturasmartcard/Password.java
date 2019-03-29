/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package letturasmartcard;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateException;
import sun.security.pkcs11.SunPKCS11;

/**
 *
 * @author Lucida
 */
public class Password {
    char[] pin;
    public Password() {
    }
    
    public void getPass () throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException{
        try{
            
        
        String configName = "name=SunPKCS11-Open\r\nlibrary=C:\\Windows\\System32\\asepkcs.dll";
         
        KeyStore keyStore =null;
        
        byte[] configSettingsBytes = configName.getBytes();

        ByteArrayInputStream configStream = new ByteArrayInputStream(configSettingsBytes);

        Provider provider = new SunPKCS11(configStream);

        Security.addProvider(provider);

        keyStore = KeyStore.getInstance("PKCS11", provider);

        keyStore.load(null, pin);
            System.out.println("Entre a la tarjeta");
        }catch (KeyStoreException ksex){
            //Add log
            System.err.println("An KeyStoreException was caught :"+ksex.getMessage());
        }catch (IOException ioex){
            System.err.println("An IOException was caught :"+ioex.getMessage());
        }catch (NoSuchAlgorithmException nsaex){
            System.err.println("An NoSuchAlgorithmException was caught :"+nsaex.getMessage());
        }catch (CertificateException cex){
            System.err.println("An CertificateException was caught :"+cex.getMessage());
        }
        
    }
}
