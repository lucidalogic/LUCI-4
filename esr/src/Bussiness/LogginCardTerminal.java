/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bussiness;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

/**
 *
 * @author Lucida
 */
public class LogginCardTerminal extends CardTerminal{

    protected CardTerminal terminal = null;
    //private Object storePassword;
    @Override
    public String getName() {
        return terminal.getName(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Card connect(String string) throws CardException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCardPresent()  throws CardException {
        TerminalFactory factory = TerminalFactory.getDefault();
        //System.out.println("entre");
        List<CardTerminal> terminals = factory.terminals().list();
        
        System.out.println("Smart card reader list: " + terminals);

        // prendo il primo lettore
         terminal = terminals.get(0);
        //System.out.println(terminal);
        return terminal.isCardPresent();//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean waitForCardPresent(long l) throws CardException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
   /*public CardChannel openLogicalChannel() throws CardException{
        return 
    }*/

    @Override
    public boolean waitForCardAbsent(long l) throws CardException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String getOperativeSystem(){
        String sSistemaOperativo = System.getProperty("os.name");
        //System.out.println(sSistemaOperativo);
        return sSistemaOperativo;
    }
        public boolean getPassword(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException{
        FileInputStream keyfis = new FileInputStream (args [0]);
        byte [] encKey = new byte [keyfis.available ()];  
        keyfis.read (encKey);

        keyfis.close ();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec (encKey);
        KeyFactory keyFactory = KeyFactory.getInstance ("DSA", "SUN");
        PublicKey pubKey = keyFactory.generatePublic (pubKeySpec);
        
        
        return false;
    
        
    }
  
}
    

