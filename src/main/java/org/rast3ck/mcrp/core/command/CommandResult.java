package org.rast3ck.mcrp.core.command;

public enum CommandResult {

    /**
     * El comando se ejecutó correctamente.
     */
    SUCCESS,

    /**
     * Ocurrió un error interno.
     */
    ERROR,

    /**
     * El comando no existe.
     */
    NOT_FOUND,

    /**
     * El remitente no tiene permisos.
     */
    NO_PERMISSION,

    /**
     * El comando solo puede ejecutarlo un jugador.
     */
    PLAYER_ONLY,

    /**
     * El comando solo puede ejecutarlo la consola.
     */
    CONSOLE_ONLY,

    /**
     * Los argumentos son inválidos o insuficientes.
     */
    INVALID_USAGE,

    /**
     * El remitente es inválido.
     */
    INVALID_SENDER,

    /**
     * El comando está deshabilitado.
     */
    DISABLED

}