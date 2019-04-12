/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exception;

/**
 *
 * @author Lucida
 */
public class CDOCException extends Exception {

    public CDOCException(String message) {
        super(message);
    }

    public CDOCException(String message, Exception exception) {
        super(message, exception);
    }
}
