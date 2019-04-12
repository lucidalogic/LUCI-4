/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Encryption;

import java.security.cert.X509Certificate;

/**
 *
 * @author Lucida
 */
public class RSARecipient extends Recipient {

    public RSARecipient(String cn, X509Certificate certificate, byte[] encryptedKey) {
        super(cn, certificate, encryptedKey);
    }
}
