/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bussiness;

import Encryption.ECRecipient;
import Encryption.RSARecipient;
import Exception.DecryptionException;
import java.security.cert.Certificate;

/**
 *
 * @author Lucida
 */
public interface Token {

    Certificate getCertificate();

    byte[] decrypt(RSARecipient recipient) throws DecryptionException;

    byte[] decrypt(ECRecipient recipient) throws DecryptionException;

}

