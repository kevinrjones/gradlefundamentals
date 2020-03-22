/*
 * Created on 17-Aug-2004
 *
 */
package com.pluralsight.security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;


/**
 * @author kevinj
 *  
 */
public class Sign extends SecurityBase
{

    static Logger logger = Logger.getLogger(Hash.class);

    byte[] bytesToSign;

    private boolean verify = false;
    private KeyStore keystore;
    private char[] keyStorePass;
    private char[] keyPassword;
    private String keystoreType;
    private String keyAlias;

    private String keyStoreFilename;
    private String signatureFileName;

    private boolean decode = false;

    public static void main(String[] args)
    {
        URL url = Hash.class.getResource("/log4j.properties");
        if(url != null)
        {
            PropertyConfigurator.configure(url);
        }

        if ((args.length == 1 && args[0].equals("--help")) || args.length < 6)
        {
            usage();
            System.exit(-1);
        }

        Sign h = new Sign();
        try
        {
            h.parseArgs(args);
            h.checkArgs();
            h.run();
        }
        catch (Exception e)
        {
            logger.info(e, e);
        }
    }

    public Sign()
    {
        setAlgorithm("DSA");
        setKeystoreType("JKS");
    }
    
    /**
     *  
     */
    private void checkArgs()
    {
        if (getKeyStoreFilename() == null || getKeyPassword() == null
                || getKeyAlias() == null)
        {
            usage();
            System.exit(-1);
        }

        if(isVerify() && getSignatureFileName() == null)
        {
            usage();
            System.exit(-1);
        }
    }

    /**
     *  
     */
    private static void usage()
    {
        System.err.println("signing: java Sign "
                        + "-s [-f filename] [-d signaturefile] [-p provider] [-a algorithm]");
        System.err.println("\t\t[-o] [-encode] [-keystoretype keystoretype] [-storepass storepassword]");
        System.err.println("\t\t-keystore storename -keypass keypassword -alias alias");
        System.err.println();
        System.err.println("verifying: java Sign "
                + "-v [-f filename] [-p provider] [-a algorithm] [-decode]");
        System.err.println("\t\t[-keystoretype keystoretype] [-sigfilename signaturefilename]");
        System.err.println("\t\t[-storepass storepassword]");
        System.err.println();
        System.err.println("\t\t-keystore storename -keypass keypassword -alias alias");
        System.err.println("\tf filename\t: read input data from filename");
        System.err.println("\td signaturefile\t: write output signature to signaturefile");
        System.err.println("\tp provider\t: use specific provider");
        System.err.println("\ta algorithm\t: algorithm to use for digest");
        System.err.println("\to\t\t: overwrite destfilename file");
        System.err.println("\tencode\t\t: BASE64 encode output");
        System.err.println("\tdecode\t\t: BASE64 decode intput");
        System.err.println("\ts\t\t: sign data");
        System.err.println("\tv\t\t: verify signature");
        System.err.println("\tsigfilename\t: name of file containing signature (if verifying)");
        System.err.println("\tkeystoretype\t: type of keystore in use");
        System.err.println("\tstorepasst\t: password to keystore");
        System.err.println("\tkeystore\t\t: keystore");
        System.err.println("\tkeypass\t\t: password for key");
        System.err.println("\talias\t\t: alias for key");
    }

    public void run() throws SignatureException, InvalidKeyException, NoSuchProviderException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException
    {
        Signature signature = createSignatureInstance();

        InputStream isDataToSignOrVerify = createInputStream(getFileName());
        OutputStream os = createOutputStream(getDestFileName());

        KeyStore ks = loadKeyStore();
        setKeystore(ks);
        if (isVerify())
        {
            InputStream isSig = createInputStream(getSignatureFileName());
            if(verifyData(signature, readBytes(isDataToSignOrVerify), readBytes(isSig)))
            {
                System.out.println("Data verified");
            }
            else
            {
                System.out.println("Data not verified");
            }
        }
        else
        {
            byte[] bytesToSign = readBytes(isDataToSignOrVerify);
            byte[] signedBytes = signData(signature, bytesToSign);
            writeBytes(os, signedBytes);
            System.out.print("Data signed");
            if(getDestFileName() != null)
            {
              System.out.println(" to file: " + getDestFileName());   
            }                
            else
                System.out.println();
        }
        
        os.close();
    }

    /**
     * @return
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     */
    final protected PrivateKey loadPrivateKey() throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException 
    {
        String alias = getKeyAlias();
        char[] password = getKeyPassword();
        
        if(alias == null || password == null)
            throw new IllegalArgumentException("Either alias or password is null");
        PrivateKey key = (PrivateKey)keystore.getKey(alias, password);
        return key;
    }

    final protected KeyStore loadKeyStore() throws KeyStoreException, NoSuchAlgorithmException,
            CertificateException, FileNotFoundException, IOException
    {
        if(getKeystoreType() == null
            || getKeyStoreFilename() == null)
            throw new IllegalArgumentException("Either keystore type or keystore name is null");
        KeyStore keystore = KeyStore.getInstance(getKeystoreType());
        keystore.load(new FileInputStream(getKeyStoreFilename()), getKeyStorePass());
        return keystore;
    }

    /**
     * @return 
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     */
    final protected Signature createSignatureInstance()
            throws NoSuchAlgorithmException, NoSuchProviderException
    {
        Signature sign;
        if (getProvider() == null)
            sign = Signature.getInstance(getAlgorithm());
        else
            sign = Signature.getInstance(getAlgorithm(), getProvider());

        return sign;
    }

    /**
     * @param signature
     * @param bytesToSign
     * @throws IOException
     * @throws SignatureException
     * @throws InvalidKeyException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     */
    public byte[] signData(Signature signature, byte[] bytesToSign)
            throws IOException, SignatureException, InvalidKeyException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException
    {
        PrivateKey pk = loadPrivateKey();
        signature.initSign(pk);
        signature.update(bytesToSign);
        byte[] signedBytes = signature.sign();
        return signedBytes;
    }

    /**
     * @return
     * @throws KeyStoreException
     */
    final protected PublicKey loadPublicKey() throws KeyStoreException
    {
        String alias = getKeyAlias();
        if(alias == null)
            throw new IllegalArgumentException("Alias is null");
        return keystore.getCertificate(alias).getPublicKey();
    }

    /**
     * @param signature
     * @param bytesToVerify
     * @param bytesSignature
     * @throws InvalidKeyException
     * @throws IOException
     * @throws SignatureException
     * @throws KeyStoreException
     */
//  public boolean verifyData(Signature signature, InputStream isBytesToVerify, InputStream isSignature) throws InvalidKeyException, IOException, SignatureException, KeyStoreException
    public boolean verifyData(Signature signature, byte[] bytesToVerify, byte[] bytesSignature) throws InvalidKeyException, IOException, SignatureException, KeyStoreException
    {
        PublicKey key = loadPublicKey();
        signature.initVerify(key);
//        byte[] bytesToVerify = readBytes(isBytesToVerify);        
//        byte[] bytesSignature = readBytes(isSignature);
        if(isDecode())
            bytesSignature =  decodeData(bytesSignature);
        signature.update(bytesToVerify);
        return signature.verify(bytesSignature);
    }

    /**
     * @param bytesSignature
     * @return
     * @throws IOException
     */
    private byte[] decodeData(byte[] bytesSignature) throws IOException
    {
       return parseBase64Binary(String.valueOf(bytesSignature));
    }

    /**
     * @return
     */
    public String getKeystoreType()
    {
        return keystoreType;
    }

    /**
     * @return
     */
    public KeyStore getKeystore()
    {
        return keystore;
    }

    /**
     * @return
     */
    public char[] getKeyPassword()
    {
        return keyPassword;
    }

    /**
     * @return
     */
    public String getKeyAlias()
    {
        return keyAlias;
    }

    public void setKeyAlias(String keyAlias)
    {
        this.keyAlias = keyAlias;
    }

    /**
     * @return Returns the bytesToHash.
     */
    public byte[] getBytesToSign()
    {
        return bytesToSign;
    }

    /**
     * @param bytesToSign
     *            The bytesToHash to set.
     */
    public void setBytesToSign(byte[] bytesToSign)
    {
        this.bytesToSign = bytesToSign;
    }

    /**
     * @return Returns the verify.
     */
    public boolean isVerify()
    {
        return verify;
    }

    /**
     * @param verify
     *            The verify to set.
     */
    public void setVerify(boolean verify)
    {
        this.verify = verify;
    }

    /**
     * @param keyPassword
     *            The keyPass to set.
     */
    public void setKeyPassword(char[] keyPassword)
    {
        this.keyPassword = keyPassword;
    }

    /**
     * @return Returns the keyStorePass.
     */
    public char[] getKeyStorePass()
    {
        return keyStorePass;
    }

    /**
     * @param keyStorePass
     *            The keyStorePass to set.
     */
    public void setKeyStorePass(char[] keyStorePass)
    {
        this.keyStorePass = keyStorePass;
    }

    /**
     * @param keystore
     *            The keystore to set.
     */
    public void setKeystore(KeyStore keystore)
    {
        this.keystore = keystore;
    }

    /**
     * @param keyStoreFilename The keyStoreFilename to set.
     */
    public void setKeyStoreFilename(String keyStoreFilename)
    {
        this.keyStoreFilename = keyStoreFilename;
    }
    
    
    /**
     * @param keystoreType
     *            The keystoreType to set.
     */
    public void setKeystoreType(String keystoreType)
    {
        this.keystoreType = keystoreType;
    }

    protected final void parseArgs(String[] args)
    {
        super.parseArgs(args);
        for (int i = 0; i < args.length; i++)
        {

            if (args[i].equals("-s"))
                setVerify(false);
            else if (args[i].equals("-v"))
                setVerify(true);
            else if (args[i].equals("-keystore"))
                setKeystoreFileName(args[++i]);
            else if (args[i].equals("-keystoretype"))
                setKeystoreType(args[++i]);
            else if (args[i].equals("-keypass"))
                setKeyPassword(args[++i].toCharArray());
            else if (args[i].equals("-storepass"))
                setKeyStorePass(args[++i].toCharArray());
            else if (args[i].equals("-alias")) 
                setKeyAlias(args[++i]);
            else if (args[i].equals("-sigfilename")) 
                setSignatureFileName(args[++i]);
            else if (args[i].equals("-decode")) 
                setDecode(true);
        }
    }

    /**
     * @param decode
     * */
    public void setDecode(boolean decode)
    {
        this.decode = decode;        
    }
    
    /**
     * @return Returns the decode.
     */
    public boolean isDecode()
    {
        return decode;
    }
    /**
     * @param keyStoreFilename
     */
    public void setKeystoreFileName(String keyStoreFilename)
    {
        this.keyStoreFilename = keyStoreFilename;
    }

    /**
     * @return Returns the keyStoreFilename.
     */
    public String getKeyStoreFilename()
    {
        return keyStoreFilename;
    }
        
    /**
     * @return Returns the signatureFileName.
     */
    public String getSignatureFileName()
    {
        return signatureFileName;
    }
    
    /**
     * @param signatureFileName The signatureFileName to set.
     */
    public void setSignatureFileName(String signatureFileName)
    {
        this.signatureFileName = signatureFileName;
    }
}