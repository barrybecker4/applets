package com.becker.game.twoplayer.go;


/**
 * This interface must be implemented by any class which becomes GoSet members (or members of derived classes) .
 *
 * @see GoSet
 *
 * @author Barry Becker
 */
public interface GoMember extends Cloneable
{
    Object clone() throws CloneNotSupportedException;

}
