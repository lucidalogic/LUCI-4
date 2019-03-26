/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package letturasmartcard;

/**
 *
 * @author Lucida
 */
import java.io.*;
import java.security.*;
import java.security.spec.*;

class VerSig {

    public static void main(String[] args) {

        /* Verify a DSA signature */

        if (args.length != 3) {
            System.out.println("Usage: VerSig " +
                "publickeyfile signaturefile " + "datafile");
        }
        else try {

        // the rest of the code goes here
            /*
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance ("SHA1PRNG", "SUN"); 
            keyGen.initialize (1024, random);
            KeyPair pair = keyGen.generateKeyPair (); 
            PrivateKey priv = pair.getPrivate (); 
            PublicKey pub = pair.getPublic ();
                    */
            
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }

}