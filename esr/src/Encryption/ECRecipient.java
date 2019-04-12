/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Encryption;

import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;

/**
 *
 * @author Lucida
 */
public class ECRecipient extends Recipient {

    private final ECPublicKey ephemeralPublicKey;
    private final byte[] algorithmId;
    private final byte[] partyUInfo;
    private final byte[] partyVInfo;

    public ECRecipient(String cn, X509Certificate certificate, byte[] encryptedKey, ECPublicKey ephemeralPublicKey, byte[] algorithmId, byte[] partyUInfo, byte[] partyVInfo) {
        super(cn, certificate, encryptedKey);
        this.ephemeralPublicKey = ephemeralPublicKey;
        this.algorithmId = algorithmId;
        this.partyUInfo = partyUInfo;
        this.partyVInfo = partyVInfo;
    }

    public ECPublicKey getEphemeralPublicKey() {
        return ephemeralPublicKey;
    }

    public byte[] getAlgorithmId() {
        return algorithmId;
    }

    public byte[] getPartyUInfo() {
        return partyUInfo;
    }

    public byte[] getPartyVInfo() {
        return partyVInfo;
    }
    
}
