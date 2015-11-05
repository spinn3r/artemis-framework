package com.spinn3r.artemis.init.terminal;

import com.spinn3r.artemis.init.BaseService;

import java.io.PrintStream;

/**
 * A service that captures standard input and output and then restored it
 * when the service is returned.
 */
public class CapturedTerminalService extends BaseService {

    protected PrintStream stdout;
    protected PrintStream stderr;

    private final CapturedTerminal capturedTerminal = new CapturedTerminal();

    @Override
    public void init() {
        advertise( CapturedTerminal.class, capturedTerminal );
    }

    @Override
    public void start() throws Exception {

        stdout = System.out;
        stderr = System.err;

        System.setOut( new PrintStream( capturedTerminal.out ) );
        System.setErr( new PrintStream( capturedTerminal.err ) );

    }

    @Override
    public void stop() throws Exception {
        System.setOut( stdout );
        System.setErr( stderr );
    }

}

