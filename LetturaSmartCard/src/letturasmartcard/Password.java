/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package letturasmartcard;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;

/**
 *
 * @author Lucida
 */
public class Password {
    char[] pin;
    public Password() {
    }
    
    private void getPass () throws KeyStoreException{
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try{
            java.io.FileInputStream fis = null;
            try {
            fis = new java.io.FileInputStream("keyStoreName");
            keyStore.load(fis, pin);
            } finally {
                if (fis != null) {
                        fis.close();
                }
            }
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(pin);
        // get my private key
        KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry)
        keyStore.getEntry("privateKeyAlias", protParam);
        PrivateKey myPrivateKey = pkEntry.getPrivateKey(); 
        }
        catch (Exception ex) {
            String errorMessage = " - The PIN for the smart card is incorrect.\n" +
            "Problem details: " + ex.getMessage();
        }
        
         
    }
}
