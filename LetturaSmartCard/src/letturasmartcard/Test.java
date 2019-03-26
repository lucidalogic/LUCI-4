/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package letturasmartcard;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class Test {

    public static void main(String[] args) throws Exception{
                Provider pkcs11Provider =new sun.security.pkcs11.SunPKCS11("c:\\dev\\pkcs11.cfg");
                char [] pin = {'1', '2', '3', '4'};
                KeyStore smartCardKeyStore = KeyStore.getInstance("PKCS11",pkcs11Provider);
                smartCardKeyStore.load(null, null);
                Enumeration aliasesEnum = smartCardKeyStore.aliases();
                while (aliasesEnum.hasMoreElements()) {
                   String alias = (String)aliasesEnum.nextElement();
                   System.out.println("Alias: " + alias);
                   X509Certificate cert =
                   (X509Certificate) smartCardKeyStore.getCertificate(alias);
                   System.out.println("Certificate: " + cert);
                   PrivateKey privateKey =
                      (PrivateKey) smartCardKeyStore.getKey(alias, null);
                   System.out.println("Private key: " + privateKey);
                }

    }

}