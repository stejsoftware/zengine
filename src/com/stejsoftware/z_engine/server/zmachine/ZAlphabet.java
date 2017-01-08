/**
 * 
 */
package com.stejsoftware.z_engine.server.zmachine;

enum ALPHABET
{
    L,
    U,
    P
}

/**
 * @author jon
 * 
 */
public class ZAlphabet
{
    private int      alphabetL  = 0;
    private int      alphabetU  = 1;
    private int      alphabetP  = 2; // Set to 3 in V1
    
    private char[][] alphabets = {
        {' ','\0','\0','\0','\0','\0','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'},
        {' ','\0','\0','\0','\0','\0','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'},
        {' ','\0','\0','\0','\0','\0','\0','\n','0','1','2','3','4','5','6','7','8','9','.',',','!','?','_','#','\'','\"','/','\\','-',':','(',')'},
        {' ','\0','\0','\0','\0','\0','\0','0','1','2','3','4','5','6','7','8','9','.',',','!','?','_','#','\'','\"','/','\\','<','-',':','(',')'}
    };

    private ZAlphabet()
    {
    }

    static private ZAlphabet alphabet = null;

    static public ZAlphabet Get(int version)
    {        
        if( alphabet == null )
        {
            alphabet = new ZAlphabet();
        }
        
        if( version == 3 )
        {
            alphabet.alphabetP = 3;
        }
        else
        {
            alphabet.alphabetP = 2;
        }
            
        return alphabet;
    }

    public char getChar(ALPHABET a, int c)
    {
        switch( a )
        {
        case P:
            return alphabets[alphabetP][c];
        case L:
            return alphabets[alphabetL][c];
        case U:
            return alphabets[alphabetU][c];
        default:
            return 0;
        }
    }

    public char L(int c)
    {
        return alphabets[alphabetL][c];
    }

    public char U(int c)
    {
        return alphabets[alphabetU][c];
    }

    public char P(int c)
    {
        return alphabets[alphabetP][c];
    }
    
    public ALPHABET shiftUp(ALPHABET a)
    {
        if( a == ALPHABET.P )
        {
            return ALPHABET.L;
        }
        else if( a == ALPHABET.L )
        {
            return ALPHABET.U;
        }
        else 
        {
            return ALPHABET.L;
        }
    }

    public ALPHABET shiftDown(ALPHABET a)
    {
        if( a == ALPHABET.L )
        {
            return ALPHABET.P;
        }
        else if( a == ALPHABET.P )
        {
            return ALPHABET.U;
        }
        else 
        {
            return ALPHABET.L;
        }
    }
}
