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
public abstract class Recipient {

    private String cn;
    private X509Certificate certificate;
    private byte[] encryptedKey;

    public Recipient(String cn, X509Certificate certificate, byte[] encryptedKey) {
        this.cn = cn;
        this.certificate = certificate;
        this.encryptedKey = encryptedKey;
    }

    public byte[] getEncryptedKey() {
        return encryptedKey;
    }

    public X509Certificate getCertificate() {
        return certificate;
    }

    public String getCN() {
        return cn;
    }
}
